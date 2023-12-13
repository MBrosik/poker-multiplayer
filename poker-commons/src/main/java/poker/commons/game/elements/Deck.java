package poker.commons.game.elements;

import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> freeCards = new ArrayList<>();

    public Deck() {
        for (Rank rank : Rank.values()) {
            for (Suit suit : Suit.values()) {
                freeCards.add(new Card(rank, suit));
            }
        }
    }

    public Card getRandomCard(){
        return freeCards.remove((int) (Math.random() * freeCards.size()));
    }
}
