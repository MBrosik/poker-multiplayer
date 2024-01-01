package poker.server.game;

import lombok.Getter;
import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.game.elements.Card;
import poker.commons.game.elements.Deck;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.data_types.ActionType;
import poker.commons.socket.data_types.while_game.*;
import poker.server.socket.SessionData;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;


public class Room {
    public enum RoomState {
        PLAYER_GATHERING,
        BETTING,
        END_ROUND
    }

    public static final int MINIMUM_BET = 5;
    public static final int MIN_SIZE = 2;
    public static final int MAX_SIZE = 4;

    private final SecureRandom random = new SecureRandom();

    @Getter
    private long gameId;
    @Getter
    private RoomState roomState = RoomState.PLAYER_GATHERING;
    private final List<Player> players = new ArrayList<>();

    private Deck deck = new Deck();

    private int smallBlindInd = -1;
    private int bigBlindInd = -1;
    private int currentBetInd = -1;
    private int countAfterLastRaise = 0;

    private int moneyOnTable = 0;
    private int currentBet = 0;

    private List<Card> cardsOnTable = new ArrayList<>();

    Room() {
        int min = 0;
        int max = 100;

        int randomInRange = random.nextInt(min, max);

        gameId = System.currentTimeMillis() * 1000 + randomInRange;
    }

    // region Before Start
    public void addPlayer(SessionData sessionData) {
        var player = new Player(sessionData);
        players.add(player);
        sessionData.setPlayer(player);
    }

    public boolean isFull() {
        return players.size() == MAX_SIZE;
    }

    public boolean areAllPlayersReady() {
        return players.stream().allMatch(Player::isReadyToPlay) && players.size() >= MIN_SIZE;
    }

    // endregion


    public void startGame() throws IOException {
        if (
                roomState != RoomState.PLAYER_GATHERING
                        && roomState != RoomState.END_ROUND
        ) return;

        roomState = RoomState.BETTING;

        choseBlinds();
        currentBet = MINIMUM_BET * 2;
        dealTheCards();
        sendInfoAboutBlinds();

        choseNextBetInd();
        sendInfoAboutBet();
    }


    public void nextBetting() throws IOException {
        if (roomState != RoomState.BETTING) return;

        var playersInGame = players.stream().filter(player -> !player.isPassed() && !player.checkIfPossibleToBet()).toList();

        MyLogger.logln(String.valueOf(countAfterLastRaise));

        if (playersInGame.size() == 1) {
            for (int i = cardsOnTable.size(); i < 5; i++) {
                cardsOnTable.add(deck.getRandomCard());
            }
            zeroValues();
            endOfTurn();
        } else if (countAfterLastRaise >= playersInGame.size()) {
            if (dealCardsOnTheTableAndCheckIfCountOfCardsIs5()) {
                zeroValues();
                endOfTurn();
            } else {
                prepareForNextRoundOfBetting();
            }
        } else {
            choseNextBetInd();
            sendInfoAboutBet();
        }
    }

    public void prepareForNextRoundOfBetting() throws IOException {
        currentBetInd = smallBlindInd == 0 ? players.size() - 1 : smallBlindInd - 1;

        zeroValues();

        sendInfoAboutNextRound();
        sendInfoAboutBet();
    }

    public void zeroValues() {
        for (Player player : players) {
            moneyOnTable += player.giveBetMoney();
        }

        choseNextBetInd();
        currentBet = 0;
        countAfterLastRaise = 0;
    }

    public boolean dealCardsOnTheTableAndCheckIfCountOfCardsIs5() {
        if (cardsOnTable.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                cardsOnTable.add(deck.getRandomCard());
            }
            return false;
        } else if (cardsOnTable.size() == 5) {
            return true;
        } else if (cardsOnTable.size() >= 3) {
            cardsOnTable.add(deck.getRandomCard());
            return false;
        }
        return false;
    }

    private void choseBlinds() {

        int index = random.nextInt(players.size());
        smallBlindInd = index;
        bigBlindInd = index == players.size() - 1 ? 0 : index + 1;
    }

    private void choseNextBetInd() {
        if (currentBetInd == -1) {
            currentBetInd = bigBlindInd == players.size() - 1 ? 0 : bigBlindInd + 1;
        } else {
            while (true) {
                currentBetInd = currentBetInd == players.size() - 1 ? 0 : currentBetInd + 1;
                var player = players.get(currentBetInd);

                if (!player.isPassed() && player.getBet() != player.getMoney()) break;
            }
        }
    }

    private void sendInfoAboutBlinds() throws IOException {
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);

            PlayerType playerType;

            if (i == smallBlindInd) {
                playerType = PlayerType.SMALL_BLIND;
            } else if (i == bigBlindInd) {
                playerType = PlayerType.BIG_BLIND;
            } else {
                playerType = PlayerType.NORMAL_PLAYER;
            }

            var startGameDataInfo = new StartGameDataInfo(playerType, MINIMUM_BET, MINIMUM_BET * 2, player.getMoney(), player.getCardsInHand());
            ReceiveData data = new ReceiveData(ActionType.START_GAME_INFO, startGameDataInfo);
            SocketManager.sendToClient(player.getSessionData().getKey(), data);
        }
    }

    private void sendInfoAboutBet() throws IOException {
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);
            boolean myBet = i == currentBetInd;

            var betInfo = new BetInfo(player.getMoney(), currentBet, myBet, player.isPassed(), player.checkIfPossibleToBet());

            ReceiveData data = new ReceiveData(ActionType.BET, betInfo);
            SocketManager.sendToClient(player.getSessionData().getKey(), data);
        }
    }

    private void sendInfoAboutNextRound() throws IOException {
        for (Player player : players) {
            var nextRoundInfo = new NextRoundInfo(cardsOnTable);

            ReceiveData data = new ReceiveData(ActionType.NEXT_ROUND, nextRoundInfo);
            SocketManager.sendToClient(player.getSessionData().getKey(), data);
        }
    }

    private void dealTheCards() {
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                player.addCard(deck.getRandomCard());
            }
        }
    }

    public void receiveBetFromPlayer(Player player, ReceiveData receiveData) throws IOException {
        if (roomState != RoomState.BETTING) return;
        if (players.indexOf(player) != currentBetInd) return;

        int bet = JSONManager.reparseJson(receiveData.getData(), Integer.class);
        if (currentBet <= bet && bet <= player.getMoney()) {
            if (bet == currentBet) countAfterLastRaise++;
            else countAfterLastRaise = 0;

            currentBet = bet;
            player.setBet(bet);
        } else if (bet == player.getMoney()) {
            player.setBet(bet);
        } else {
            return;
        }
        nextBetting();
    }

    public void receivePassFromPlayer(Player player) throws IOException {
        if (roomState != RoomState.BETTING) return;
        if (players.indexOf(player) != currentBetInd) return;

        MyLogger.logln("passed");
        player.setPassed(true);

        nextBetting();
    }

    public void endOfTurn() throws IOException {
        LinkedHashMap<Player, CardCheckerManager> pointMap = new LinkedHashMap<>();

        for (Player player : players) {
            pointMap.put(player, new CardCheckerManager(cardsOnTable, player.getCardsInHand()));
        }

        var maxValue = pointMap.entrySet().stream()
                .filter(el -> !el.getKey().isPassed())
                .max(Comparator.comparingLong(el-> el.getValue().getPoints())).orElseThrow(() -> {
                    MyLogger.elog("Something wrong with maxValue");
                    return new IllegalStateException("Something wrong with maxValue");
                }).getValue();


        List<Player> bestPlayers = pointMap.entrySet()
                .stream()
                .filter(el -> Objects.equals(el.getValue().getPoints(), maxValue.getPoints()) && !el.getKey().isPassed())
                .map(Map.Entry::getKey)
                .toList();

        for (var player : bestPlayers) {
            player.setMoney(player.getMoney() + moneyOnTable / bestPlayers.size());
        }

        for (var entry : pointMap.entrySet()) {
            var player = entry.getKey();
            var value = entry.getValue();
            boolean won = bestPlayers.contains(entry.getKey());

            EndGameInfo endGameInfo = new EndGameInfo(
                    cardsOnTable,
                    player.getCardsInHand(),
                    won,
                    bestPlayers.size(),
                    value.getVariation(),
                    won ? moneyOnTable / bestPlayers.size() : 0,
                    player.getMoney()
            );

            ReceiveData receiveData = new ReceiveData(ActionType.END_TURN, endGameInfo);
            SocketManager.sendToClient(player.getSessionData().getKey(), receiveData);
        }

        afterEndOfTurn();
    }

    private void afterEndOfTurn() {
        roomState = RoomState.END_ROUND;
        smallBlindInd = -1;
        bigBlindInd = -1;
        currentBetInd = -1;
        deck = new Deck();
        cardsOnTable = new ArrayList<>();

        players.removeIf(el -> el.getMoney() == 0);

        for (Player player : players) {
            player.setPassed(false);
            player.setCardsInHand(new ArrayList<>());
            player.setAttendingInNextRound(false);
        }
    }

    public void infoFromPlayerAboutNextTurn(SessionData session, ReceiveData receiveData) throws IOException {
        if (roomState != RoomState.END_ROUND) return;
        Player player = session.getPlayer();
        boolean ready = JSONManager.reparseJson(receiveData.getData(), Boolean.class);

        if (ready) {
            player.setAttendingInNextRound(true);
        } else {
            players.remove(player);
        }

        if (players.size() < MIN_SIZE) {
            sendInfoAboutEndOfGame();
            return;
        }

        boolean allAttending = players.stream().allMatch(Player::isAttendingInNextRound);


        if (allAttending) {
            startGame();
        }
    }

    public void sendInfoAboutEndOfGame() throws IOException {
        for (Player player : players) {
            ReceiveData receiveData = new ReceiveData(ActionType.END_GAME, true);
            SocketManager.sendToClient(player.getSessionData().getKey(), receiveData);
        }
    }
}
