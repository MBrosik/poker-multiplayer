package poker.commons.game.elements;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    @Test
    void testDeckCreation() {
        Deck deck = new Deck();

        assertEquals(52, deck.getFreeCards().size());
    }

    @Test
    void testGetRandomCard() {
        Deck deck = new Deck();
        Set<Card> selectedCards = new HashSet<>();

        // Sprawdź, czy można pobrać 51 unikalnych kart (bez karty o wartości ONE)
        for (int i = 0; i < 52; i++) {
            Card randomCard = deck.getRandomCard();
            assertNotNull(randomCard);
            assertFalse(selectedCards.contains(randomCard));
            selectedCards.add(randomCard);
        }

        assertNull(deck.getRandomCard());
    }
}
