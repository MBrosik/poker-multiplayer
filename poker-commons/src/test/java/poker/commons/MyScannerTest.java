package poker.commons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MyScannerTest {
    private final InputStream standardIn = System.in;
    private final String inputString = "test\n123\n456789\n";
    private ByteArrayInputStream inputStream;



    @AfterEach
    void tearDown() {
        System.setIn(standardIn);
    }

    @Test
    void testGetStreamString() {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        MyScanner.changeInput(inputStream);
        assertEquals("test", MyScanner.getStreamString());
    }

    @Test
    void testGetStreamStringWithMessage() {
        InputStream inputStream = new ByteArrayInputStream("123".getBytes());
        MyScanner.changeInput(inputStream);
        assertEquals("123", MyScanner.getStreamString("Enter a string: "));
    }

    @Test
    void testGetStreamInt() {
        InputStream inputStream = new ByteArrayInputStream("456789".getBytes());
        MyScanner.changeInput(inputStream);
        assertEquals(456789, MyScanner.getStreamInt());
    }

    @Test
    void testGetStreamIntWithMessage() {
        InputStream inputStream = new ByteArrayInputStream("123".getBytes());
        MyScanner.changeInput(inputStream);
        assertEquals(123, MyScanner.getStreamInt("Enter an integer: "));
    }

    @Test
    void testGetStreamLong() {
        InputStream inputStream = new ByteArrayInputStream("456789".getBytes());
        MyScanner.changeInput(inputStream);
        assertEquals(456789L, MyScanner.getStreamLong());
    }

    @Test
    void testGetStreamLongWithMessage() {
        InputStream inputStream = new ByteArrayInputStream("456789".getBytes());
        MyScanner.changeInput(inputStream);
        assertEquals(456789L, MyScanner.getStreamLong("Enter a long: "));
    }
}
