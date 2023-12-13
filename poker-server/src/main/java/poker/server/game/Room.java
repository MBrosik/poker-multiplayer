package poker.server.game;

import lombok.Getter;
import poker.commons.JSONManager;
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

    public void smallBroadCast(){

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

    public void startGame() throws IOException {
        roomState = RoomState.WaitingForBetFromSmallBlind;
        choseBlinds();
        notifyAboutBlinds(ActionType.SmallBlindBetTurn);
    }

    public void choseBlinds() {
        int index = (int) (Math.random() * players.size());
        smallBlindInd = index;
        if (players.size() > 2) {
            bigBlindInd = index == players.size() - 1 ? 0 : index + 1;
        }
    }

    public void notifyAboutBlinds(ActionType action) throws IOException {
        for (Player player : players) {
            PlayerType playerType = getPlayerBetType(player);
            boolean myBet = isMyBet(action, player);

            var gameData = new GameData(player.getMoney(), currentBet, playerType, myBet);

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

        } else if (roomState == RoomState.WaitingForBetFromSmallBlind) {
            smallBlindBet(player, receiveData);
        } else if (roomState == RoomState.WaitingForBetFromNormal) {

        }
    }

    public void smallBlindBet(Player player, ReceiveData data) throws IOException {
        int index = players.indexOf(player);
        int playerBet = JSONManager.reparseJson(data.getData(), Integer.class);

        System.out.println("00000");
        if (index != smallBlindInd) return;
        if (playerBet <= 0) return;
        if (playerBet > player.getMoney()) return;
        System.out.println("11111");
        currentBet = playerBet;

        if (bigBlindInd != -1) {
            roomState = RoomState.WaitingForBetFromBigBlind;
//            ReceiveData sendData = new ReceiveData(ActionType.BigBLindBetTurn, null);
            notifyAboutBlinds(ActionType.BigBlindBetTurn);
        } else {

        }
    }

    public void dealTheCards() {
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                player.addCard(deck.getRandomCard());
            }
        }
    }
    // endregion
}
