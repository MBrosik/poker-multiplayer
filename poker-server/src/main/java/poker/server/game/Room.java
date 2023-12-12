package poker.server.game;

import lombok.Getter;
import poker.server.socket.SessionData;

import java.util.ArrayList;


public class Room {
    public static final int minSize = 2;
    public static final int maxSize = 4;

    public enum RoomState {
        PlayerGathering,
        DuringGame,
        EndGame
    }

    @Getter
    private long gameId;
    public RoomState roomState = RoomState.PlayerGathering;
    public ArrayList<Player> players = new ArrayList<>();

    Room() {
        int min = 0;
        int max = 100;
        int randomInRange = (int) (Math.random() * (max - min + 1) + min);

        gameId = System.currentTimeMillis() * 1000 + randomInRange;
    }

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

    public boolean areAllPlayersReady(){
        return players.stream().allMatch(Player::isReadyToPlay);
    }
}
