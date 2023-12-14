package poker.server.game;

import lombok.Getter;
import poker.commons.JSONManager;
import poker.commons.MyLogger;
import poker.commons.game.elements.Deck;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.dataTypes.ActionType;
import poker.commons.socket.dataTypes.whileGame.GameData;
import poker.commons.socket.dataTypes.whileGame.PlayerType;
import poker.server.socket.SessionData;
import poker.server.socket.SocketManager;

import java.io.IOException;
import java.util.ArrayList;


public class Room {
    public enum RoomState {
        PlayerGathering,
        WaitingForBetFromBigBlind,
        WaitingForBetFromSmallBlind,
        WaitingForBetFromNormal,
        EndGame
    }

    public static final int minSize = 2;
    public static final int maxSize = 4;

    @Getter
    private long gameId;
    public RoomState roomState = RoomState.PlayerGathering;
    public ArrayList<Player> players = new ArrayList<>();

    public Deck deck = new Deck();

    public int smallBlindInd = -1;
    public int bigBlindInd = -1;
    public int normalBetInd = -1;
    public int currentBet = 0;

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
        if(roomState == RoomState.PlayerGathering) {
            roomState = RoomState.WaitingForBetFromSmallBlind;
            choseBlinds();
            sendInfoToPlayers(ActionType.SmallBlindBetTurn);
        }
    }

    public void choseBlinds() {
        int index = (int) (Math.random() * players.size());
        smallBlindInd = index;
        if (players.size() > 2) {
            bigBlindInd = index == players.size() - 1 ? 0 : index + 1;
        }
    }

    public void sendInfoToPlayers(ActionType action) throws IOException {
        for (Player player : players) {
            PlayerType playerType = getPlayerBetType(player);
            boolean myBet = isMyBet(action, player);

            var gameData = new GameData(player.getMoney(), currentBet, playerType, myBet, player.cardsInHand);

            ReceiveData data = new ReceiveData(action, gameData);
            SocketManager.sendToClient(player.sessionData.getKey(), data);
        }
    }
    private boolean isMyBet(ActionType action, Player player){
        int playerIndex = players.indexOf(player);

        if(action == ActionType.SmallBlindBetTurn){
            return playerIndex == smallBlindInd;
        }
        else if(action == ActionType.BigBlindBetTurn){
            return playerIndex == smallBlindInd;
        }
        else if(action == ActionType.NormalBetTurn){
            return playerIndex == normalBetInd;
        }
        return false;
    }

    private PlayerType getPlayerBetType(Player player) {
        PlayerType playerType;
        int playerIndex = players.indexOf(player);

        if (playerIndex == smallBlindInd) {
            playerType = PlayerType.SmallBlind;
        } else if (playerIndex == bigBlindInd) {
            playerType = PlayerType.BigBlind;
        } else {
            playerType = PlayerType.NormalPlayer;
        }
        return playerType;
    }

    public void getBet(Player player, ReceiveData receiveData) throws IOException {
        if (roomState == RoomState.WaitingForBetFromBigBlind) {
            MyLogger.logln("Big blind bet");
        } else if (roomState == RoomState.WaitingForBetFromSmallBlind) {
            smallBlindBet(player, receiveData);
        } else if (roomState == RoomState.WaitingForBetFromNormal) {
            MyLogger.logln("Normal bet");
        } else{
            MyLogger.logln("Bad case");
        }
    }

    public void smallBlindBet(Player player, ReceiveData data) throws IOException {
        int index = players.indexOf(player);
        int playerBet = JSONManager.reparseJson(data.getData(), Integer.class);


        if (index != smallBlindInd) return;
        if (playerBet <= 0) return;
        if (playerBet > player.getMoney()) return;
        currentBet = playerBet;
        player.setBet(playerBet);

        if (bigBlindInd != -1) {
            roomState = RoomState.WaitingForBetFromBigBlind;
            sendInfoToPlayers(ActionType.BigBlindBetTurn);
        } else {
            roomState = RoomState.WaitingForBetFromNormal;
            normalBetInd = smallBlindInd == players.size() - 1 ? 0 : smallBlindInd + 1;
            dealTheCards();
            sendInfoToPlayers(ActionType.NormalBetTurn);

        }
    }

    public void dealTheCards() {
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                player.addCard(deck.getRandomCard());
            }
        }
    }


//    public void sendInfoAboutCards() throws IOException {
//        for (Player player : players) {
//            PlayerType playerType = getPlayerBetType(player);
//            boolean myBet = isMyBet(ActionType.Bet, player);
//
//            var gameData = new GameData(player.getMoney(), currentBet, playerType, myBet, player.cardsInHand);
//            var receiveData = new ReceiveData(ActionType.Bet, gameData);
//
//            SocketManager.sendToClient(player.sessionData.getKey(), receiveData);
//        }
//    }
    // endregion
}
