package poker.commons.game.elements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RankTest {

    @Test
    void testRankGetName() {
        assertEquals("1", Rank.ONE.getName());
        assertEquals("2", Rank.TWO.getName());
        assertEquals("3", Rank.THREE.getName());
        assertEquals("4", Rank.FOUR.getName());
        assertEquals("5", Rank.FIVE.getName());
        assertEquals("6", Rank.SIX.getName());
        assertEquals("7", Rank.SEVEN.getName());
        assertEquals("8", Rank.EIGHT.getName());
        assertEquals("9", Rank.NINE.getName());
        assertEquals("10", Rank.TEN.getName());
        assertEquals("Walet", Rank.JACK.getName());
        assertEquals("Dama", Rank.QUEEN.getName());
        assertEquals("Król", Rank.KING.getName());
        assertEquals("As", Rank.ACE.getName());
    }
}
