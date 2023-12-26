package poker.server.game;

import lombok.Getter;
import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.game.elements.Card;
import poker.commons.game.elements.Deck;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.whileGame.BetInfo;
import poker.commons.socket.dataTypes.whileGame.StartGameDataInfo;
import poker.commons.socket.dataTypes.whileGame.PlayerType;
import poker.server.socket.SessionData;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.util.ArrayList;


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
        return players.stream().allMatch(Player::isReadyToPlay);
    }

    // endregion

    // region Start Game

    public void gameLoop() throws IOException {
        if (roomState == RoomState.PlayerGathering) {
            roomState = RoomState.Betting;

            choseBlinds();
            currentBet = minimumBet * 2;
            dealTheCards();
            sendInfoAboutBlinds();

            choseNextBetInd();
            sendInfoAboutBet();
        } else if (roomState == RoomState.Betting) {
            choseNextBetInd();
            sendInfoAboutBet();
        }
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

            var betInfo = new BetInfo(player.getMoney(), currentBet, myBet);

            ReceiveData data = new ReceiveData(ActionType.Bet, betInfo);
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
            if (bet > currentBet) countAfterLastRaise++;
            else countAfterLastRaise = 0;

            currentBet = bet;
            player.setBet(bet);
        } else if (bet == player.getMoney()) {
            player.setBet(bet);
        } else {
            return;
        }
        gameLoop();
    }

    public void receivePassFromPlayer(Player player, ReceiveData receiveData) throws IOException {
        if (roomState != RoomState.Betting) return;
        if (players.indexOf(player) != currentBetInd) return;

        MyLogger.logln("passedddddddddd");
        player.setPassed(true);

        gameLoop();
    }
}
