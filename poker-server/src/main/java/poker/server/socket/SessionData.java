package poker.server.socket;

import lombok.Data;
import poker.server.game.Room;

@Data
public class SessionData {
    Room room = null;

}
