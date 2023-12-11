package poker.server.socket;

import lombok.Data;
import poker.server.game.Room;

import java.nio.channels.SelectionKey;

@Data
public class SessionData {
    Room room = null;
    SelectionKey key;
}
