package poker.commons;

import org.junit.jupiter.api.Test;
import poker.commons.socket.ReceiveData;
import poker.commons.socket.data_types.ActionType;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JSONManagerTest {

    @Test
    void testJsonParse() {
        String jsonString = "{\"action\": \"CREATE_ROOM\", \"data\": {\"key\": \"Test key\"}}";
        ReceiveData receiveData = JSONManager.jsonParse(jsonString);

        assertNotNull(receiveData);
        assertEquals(ActionType.CREATE_ROOM, receiveData.getAction());
        assertNotNull(receiveData.getData());
    }

    @Test
    void testJsonParse1() {
        String jsonString = "{\"key\": \"TEST\", \"count\": 5}";
        SomeData someData = JSONManager.jsonParse1(jsonString, SomeData.class);

        assertNotNull(someData);
        assertEquals("TEST", someData.getKey());
        assertEquals(5, someData.getCount());
    }

    @Test
    void testJsonStringify() {
        ReceiveData receiveData = new ReceiveData(ActionType.READY_TO_PLAY, new SomeData("Test key", 5));
        ByteBuffer byteBuffer = JSONManager.jsonStringify(receiveData);

        assertNotNull(byteBuffer);
        assertEquals("{\"action\":\"READY_TO_PLAY\",\"data\":{\"key\":\"Test key\",\"count\":5}}", new String(byteBuffer.array()).trim());
    }

    @Test
    void testJsonStringify1() {
        SomeData someData = new SomeData("Test key", 5);
        String jsonString = JSONManager.jsonStringify1(someData);

        assertNotNull(jsonString);
        assertEquals("{\"key\":\"Test key\",\"count\":5}", jsonString);
    }

    @Test
    void testReparseJson() {
        SomeData someData = new SomeData("Test key", 5);
        SomeData reparsedData = JSONManager.reparseJson(someData, SomeData.class);

        assertNotNull(reparsedData);
        assertEquals("Test key", reparsedData.getKey());
        assertEquals(5, reparsedData.getCount());
    }


    static class SomeData {
        private String key;
        private int count;

        public SomeData(String key, int count) {
            this.key = key;
            this.count = count;
        }

        public String getKey() {
            return key;
        }

        public int getCount() {
            return count;
        }
    }
}
