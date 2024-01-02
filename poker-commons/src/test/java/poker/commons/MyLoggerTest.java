package poker.commons;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MyLoggerTest {
    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    @Test
    void testLog() {
        MyLogger.log("Test log message");
        assertEquals("Test log message", outputStreamCaptor.toString().trim());
    }

    @Test
    void testLogln() {
        MyLogger.logln("Test log message with new line");
        assertEquals("Test log message with new line", outputStreamCaptor.toString().trim());
    }

    @Test
    void testLogf() {
        MyLogger.logf("Test log message with formatted %s", "output");
        assertEquals("Test log message with formatted output", outputStreamCaptor.toString().trim());
    }

    @Test
    void testLogLineSep() {
        MyLogger.logLineSep();
        assertEquals("==========================================================================", outputStreamCaptor.toString().trim());
    }

    @Test
    void etestLog() {
        MyLogger.elog("Test err message");
        assertEquals("Test err message", errorStreamCaptor.toString().trim());
    }

    @Test
    void etestLogln() {
        MyLogger.elogln("Test err message with new line");
        assertEquals("Test err message with new line", errorStreamCaptor.toString().trim());
    }

    @Test
    void etestLogf() {
        MyLogger.elogf("Test err message with formatted %s", "output");
        assertEquals("Test err message with formatted output", errorStreamCaptor.toString().trim());
    }

    @Test
    void etestLogLineSep() {
        MyLogger.elogLineSep();
        assertEquals("==========================================================================", errorStreamCaptor.toString().trim());
    }
}
