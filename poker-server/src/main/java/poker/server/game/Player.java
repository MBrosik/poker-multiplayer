package poker.server.game;

import lombok.Setter;
import poker.server.socket.SessionData;


public class Player {
    SessionData sessionData;

    @Setter
    private boolean readyToPlay = false;

    public Player(SessionData sessionData){
        sessionData = sessionData;
    }
}
