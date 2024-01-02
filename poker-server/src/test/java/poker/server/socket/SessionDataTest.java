package poker.server.socket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import poker.commons.MyLogger;
import poker.server.game.Player;
import poker.server.game.Room;

import java.nio.channels.SelectionKey;

import static org.junit.jupiter.api.Assertions.*;

class SessionDataTest {
    @Test
    void testRoomIsNullByDefault() {
        SessionData sessionData = new SessionData();
        assertNull(sessionData.getRoom());
    }

    @Test
    void testSetRoom() {
        Room room = new Room();
        SessionData sessionData = new SessionData();
        sessionData.setRoom(room);
        assertEquals(room, sessionData.getRoom());
    }

    @Test
    void testKeyIsNullByDefault() {
        SessionData sessionData = new SessionData();
        assertNull(sessionData.getKey());
    }

    @Test
    void testSetKey() {
        SelectionKey key = createSelectionKey();
        SessionData sessionData = new SessionData();
        sessionData.setKey(key);
        assertEquals(key, sessionData.getKey());
    }

    @Test
    void testPlayerIsNullByDefault() {
        SessionData sessionData = new SessionData();
        assertNull(sessionData.getPlayer());
    }

    @Test
    void testSetPlayer() {
        SessionData sessionData = new SessionData();
        Player player = new Player(sessionData);
        sessionData.setPlayer(player);
        assertEquals(player, sessionData.getPlayer());
    }
    @Test
    void testSessionDataEquals(){
        SessionData sessionData1 = new SessionData();
        SessionData sessionData2 = new SessionData();
        System.out.println(sessionData1.toString());

        Player player = new Player(sessionData1);

        sessionData1.setPlayer(player);
        sessionData2.setPlayer(player);
        assertEquals(sessionData1, sessionData2);
    }

    @Test
    void testSessionDataToString(){
        SessionData sessionData1 = new SessionData();
        MyLogger.logln(String.valueOf(sessionData1.hashCode()));
        assertEquals("SessionData(room=null, key=null, player=null)", sessionData1.toString());
    }

    @Test
    void testSessionDataHashCode(){
        SessionData sessionData1 = new SessionData();
        assertEquals(357642, sessionData1.hashCode());
    }

    private SelectionKey createSelectionKey() {
        return null;
    }
}
