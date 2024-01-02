package poker.server.game;

import org.junit.jupiter.api.Test;
import poker.commons.game.elements.Card;
import poker.commons.game.elements.Rank;
import poker.commons.game.elements.Suit;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardCheckerManagerTest {

    @Test
    void testRoyalFlush() {
        List<Card> royalFlushCards = List.of(
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.ACE, Suit.SPADES)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), royalFlushCards);
        assertEquals("Poker królewski", checker.getVariation());
        assertEquals(90000000013L, checker.getPoints());
    }

    @Test
    void testHasStraightFlush() {
        List<Card> straightFlushCards = List.of(
                new Card(Rank.FIVE, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.SIX, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.TEN, Suit.HEARTS)
        );


        CardCheckerManager checker = new CardCheckerManager(List.of(), straightFlushCards);

        assertEquals("Poker", checker.getVariation());
        assertEquals(80000000009L, checker.getPoints());
    }

    @Test
    void testHasFourOfKind() {
        List<Card> fourOfAKindCards = List.of(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.TEN, Suit.HEARTS)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), fourOfAKindCards);

        assertEquals("Kareta", checker.getVariation());
        assertEquals(70000001209L, checker.getPoints());
    }

    @Test
    void testHasFullHouse() {
        List<Card> fullHouseCards = List.of(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), fullHouseCards);

        assertEquals("Full", checker.getVariation());
        assertEquals(60000001309L, checker.getPoints());
    }

    @Test
    void testHasFlush() {
        List<Card> flushCards = List.of(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.TEN, Suit.HEARTS),
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.TEN, Suit.SPADES)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), flushCards);


        assertEquals("Kolor", checker.getVariation());
        assertEquals(50000000013L, checker.getPoints());
    }

    @Test
    void testHasStraight() {
        List<Card> straightCards = List.of(
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.SIX, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.DIAMONDS),
                new Card(Rank.EIGHT, Suit.CLUBS),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.TEN, Suit.HEARTS)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), straightCards);

        assertEquals("Strit", checker.getVariation());
        assertEquals(40000000009L, checker.getPoints());
    }

    @Test
    void testHasThreeOfAKind() {
        List<Card> threeOfAKindCards = List.of(
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.SEVEN, Suit.SPADES),
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.TEN, Suit.HEARTS)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), threeOfAKindCards);

        assertEquals("Trójka", checker.getVariation());
        assertEquals(30000001009L, checker.getPoints());
    }

    @Test
    void testHasTwoPairs() {
        List<Card> twoPairsCards = List.of(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.NINE, Suit.SPADES)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), twoPairsCards);

        assertEquals("Dwie pary", checker.getVariation());
        assertEquals(20000130908L, checker.getPoints());
    }

    @Test
    void testHasPair() {
        List<Card> pairCards = List.of(
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.SEVEN, Suit.CLUBS),
                new Card(Rank.TEN, Suit.SPADES),
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.NINE, Suit.SPADES)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), pairCards);

        assertEquals("Para", checker.getVariation());
        assertEquals(10000001209L, checker.getPoints());
    }

    @Test
    void testCheckHighCard() {
        List<Card> highCardHand = List.of(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.HEARTS)
        );

        CardCheckerManager checker = new CardCheckerManager(List.of(), highCardHand);


        assertEquals("Wysoka karta", checker.getVariation());
        assertEquals(1312111008L, checker.getPoints());
    }
}
