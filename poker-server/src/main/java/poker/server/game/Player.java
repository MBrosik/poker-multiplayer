package poker.server.game;

import lombok.Getter;
import lombok.Setter;
import poker.server.socket.SessionData;


public class Player {
    SessionData sessionData;

    @Getter
    @Setter
    private boolean readyToPlay = false;

    public Player(SessionData sessionData){
        this.sessionData = sessionData;
    }
}
