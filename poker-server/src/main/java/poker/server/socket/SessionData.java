package poker.server.socket;

import lombok.Data;
import poker.server.game.Player;
import poker.server.game.Room;

import java.nio.channels.SelectionKey;

@Data
public class SessionData {
    private Room room = null;
    private SelectionKey key;
    private Player player;
}
