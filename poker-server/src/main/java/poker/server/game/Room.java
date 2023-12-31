package poker.server.game;

import jdk.jshell.spi.ExecutionControl;
import lombok.Getter;
import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.game.elements.Card;
import poker.commons.game.elements.Deck;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.whileGame.*;
import poker.server.socket.SessionData;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Room {
    public enum RoomState {
        PlayerGathering,
        Betting,
        EndGame
    }

    public static final int minimumBet = 5;
    public static final int minSize = 2;
    public static final int maxSize = 4;

    @Getter
    private long gameId;
    public RoomState roomState = RoomState.PlayerGathering;
    public ArrayList<Player> players = new ArrayList<>();

    public Deck deck = new Deck();

    public int smallBlindInd = -1;
    public int bigBlindInd = -1;
    public int currentBetInd = -1;
    public int countAfterLastRaise = 0;

    public int moneyOnTable = 0;
    public int currentBet = 0;

    public ArrayList<Card> cardsOnTable = new ArrayList<>();

    Room() {
        int min = 0;
        int max = 100;
        int randomInRange = (int) (Math.random() * (max - min + 1) + min);

        gameId = System.currentTimeMillis() * 1000 + randomInRange;
    }

    // region Before Start
    public void addPlayer(SessionData sessionData) {
        var player = new Player(sessionData);
        players.add(player);
        sessionData.setPlayer(player);
    }

    public boolean removePlayer(SessionData sessionData) {
        return false;
    }

    public boolean isFull() {
        return players.size() == maxSize;
    }

    public boolean areAllPlayersReady() {
        return players.stream().allMatch(Player::isReadyToPlay) && players.size() >= minSize;
    }

    // endregion


    public void startGame() throws IOException {
        if (roomState != RoomState.PlayerGathering) return;
        roomState = RoomState.Betting;

        choseBlinds();
        currentBet = minimumBet * 2;
        dealTheCards();
        sendInfoAboutBlinds();

        choseNextBetInd();
        sendInfoAboutBet();
    }

    public void nextBetting() throws IOException {
        if (roomState != RoomState.Betting) return;

        var playersInGame = players.stream().filter(player -> !player.isPassed() && !player.checkIfPossibleToBet()).toList();

        MyLogger.logln(String.valueOf(countAfterLastRaise));

        if(playersInGame.size() == 1){
            while(!dealCardsOnTheTableAndCheckIfCountOfCardsIs5()){}
            zeroValues();
            endOfTurn();
        } else if(countAfterLastRaise >= playersInGame.size()){
            if(dealCardsOnTheTableAndCheckIfCountOfCardsIs5()){
                zeroValues();
                endOfTurn();
            }
            else{
                prepareForNextRoundOfBetting();
            }
        } else{
            choseNextBetInd();
            sendInfoAboutBet();
        }
    }

    public void prepareForNextRoundOfBetting() throws IOException {
//        for (Player player: players) {
//            moneyOnTable += player.giveBetMoney();
//        }

        currentBetInd = smallBlindInd == 0 ? players.size() - 1 : smallBlindInd - 1;
//        choseNextBetInd();
//        currentBet = 0;
//        countAfterLastRaise = 0;

        zeroValues();

        sendInfoAboutNextRound();
        sendInfoAboutBet();
    }

    public void zeroValues(){
        for (Player player: players) {
            moneyOnTable += player.giveBetMoney();
        }

        choseNextBetInd();
        currentBet = 0;
        countAfterLastRaise = 0;
    }

    public boolean dealCardsOnTheTableAndCheckIfCountOfCardsIs5() {
        if(cardsOnTable.size() == 0){
            for (int i = 0; i < 3; i++) {
                cardsOnTable.add(deck.getRandomCard());
            }
            return false;
        } else if(cardsOnTable.size() == 5){
            return true;
        }
        else if(cardsOnTable.size() >= 3){
            cardsOnTable.add(deck.getRandomCard());
            return false;
        }
        return false;
    }

    private void choseBlinds() {
        int index = (int) (Math.random() * players.size());
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
                playerType = PlayerType.SmallBlind;
            } else if (i == bigBlindInd) {
                playerType = PlayerType.BigBlind;
            } else {
                playerType = PlayerType.NormalPlayer;
            }

            var startGameDataInfo = new StartGameDataInfo(playerType, minimumBet, minimumBet * 2, player.getMoney(), player.getCardsInHand());
            ReceiveData data = new ReceiveData(ActionType.StartGameInfo, startGameDataInfo);
            SocketManager.sendToClient(player.getSessionData().getKey(), data);
        }
    }

    private void sendInfoAboutBet() throws IOException {
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);
            boolean myBet = i == currentBetInd;

            var betInfo = new BetInfo(player.getMoney(), currentBet, myBet, player.isPassed(), player.checkIfPossibleToBet());

            ReceiveData data = new ReceiveData(ActionType.Bet, betInfo);
            SocketManager.sendToClient(player.getSessionData().getKey(), data);
        }
    }

    private void sendInfoAboutNextRound() throws IOException {
        for (Player player : players) {
            var nextRoundInfo = new NextRoundInfo(cardsOnTable);

            ReceiveData data = new ReceiveData(ActionType.NextRound, nextRoundInfo);
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
        if (roomState != RoomState.Betting) return;
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

    public void receivePassFromPlayer(Player player, ReceiveData receiveData) throws IOException{
        if (roomState != RoomState.Betting) return;
        if (players.indexOf(player) != currentBetInd) return;

        MyLogger.logln("passedddddddddd");
        player.setPassed(true);

        nextBetting();
    }

    public void endOfTurn() throws IOException {
        LinkedHashMap<Player, CardCheckerManager> pointMap = new LinkedHashMap<>();

        for (Player player: players) {
            pointMap.put(player, new CardCheckerManager(cardsOnTable, player.getCardsInHand()));
        }


        MyLogger.logln("Noice EndOfTurn");
        for (var xD: pointMap.entrySet()){
            MyLogger.logln(JSONManager.jsonStringify1(xD.getKey().getCardsInHand()));
            MyLogger.logln(String.valueOf(xD.getValue()));
        }

        CardCheckerManager maxValue = pointMap.values().stream().max(Comparator.comparingLong(el -> el.points)).orElseThrow(() -> {
            MyLogger.elog("Something wrong with maxValue");
            return new IllegalStateException("Something wrong with maxValue");
        });;


        List<Player> bestPlayers = pointMap.entrySet()
                .stream()
                .filter(el -> Objects.equals(el.getValue().points, maxValue.points))
                .map(Map.Entry::getKey)
                .toList();

        for (var player: bestPlayers) {
            player.setMoney(player.getMoney() + moneyOnTable / bestPlayers.size());
        }

        for (var entry: pointMap.entrySet()) {
            var player = entry.getKey();
            var value = entry.getValue();
            boolean won = bestPlayers.contains(entry.getKey());

            EndGameInfo endGameInfo = new EndGameInfo(
                    cardsOnTable,
                    player.getCardsInHand(),
                    won,
                    bestPlayers.size(),
                    value.variation,
                    won ? moneyOnTable / bestPlayers.size():0,
                    player.getMoney()
            );

            ReceiveData receiveData = new ReceiveData(ActionType.EndTurn, endGameInfo);
            SocketManager.sendToClient(player.getSessionData().getKey(), receiveData);
        }
    }
}
