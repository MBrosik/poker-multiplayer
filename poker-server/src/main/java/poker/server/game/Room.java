package poker.server.game;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;


public class Room {

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
}
