package poker.commons.game.elements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuitTest {
    @Test
    void testSuitGetName() {
        assertEquals("Dzwonek", Suit.DIAMONDS.getName());
        assertEquals("Żołądź", Suit.CLUBS.getName());
        assertEquals("Serce", Suit.HEARTS.getName());
        assertEquals("Wino", Suit.SPADES.getName());
    }

    @Test
    void testSuitSize() {
        assertEquals(4, Suit.values().length);
    }

    @Test
    void testSuitOrdinal() {
        assertEquals(0, Suit.DIAMONDS.ordinal());
        assertEquals(1, Suit.CLUBS.ordinal());
        assertEquals(2, Suit.HEARTS.ordinal());
        assertEquals(3, Suit.SPADES.ordinal());
    }
}
