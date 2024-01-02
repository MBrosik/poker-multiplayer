package poker.commons.game.elements;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CardTest {
    @Test
    void testCardConstruction() {
        Rank rank = Rank.ACE;
        Suit suit = Suit.SPADES;
        Card card = new Card(rank, suit);

        assertEquals(rank, card.getRank());
        assertEquals(suit, card.getSuit());
    }

    @Test
    void testToString() {
        Rank rank = Rank.KING;
        Suit suit = Suit.HEARTS;
        Card card = new Card(rank, suit);

        String expectedString = suit.getName() + " - " + rank.getName();
        assertEquals(expectedString, card.toString());
    }

    @Test
    void testEquality() {
        Rank rank = Rank.TWO;
        Suit suit = Suit.DIAMONDS;
        Card card1 = new Card(rank, suit);
        Card card2 = new Card(rank, suit);

        assertEquals(card1, card2);
        assertEquals(card1.hashCode(), card2.hashCode());
    }

    @Test
    void testInequality() {
        Card card1 = new Card(Rank.THREE, Suit.CLUBS);
        Card card2 = new Card(Rank.FOUR, Suit.CLUBS);

        assertNotEquals(card1, card2);
        assertNotEquals(card1.hashCode(), card2.hashCode());
    }
}
