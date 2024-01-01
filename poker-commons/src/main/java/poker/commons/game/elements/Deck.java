package poker.commons.game.elements;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Deck {
    private final ArrayList<Card> freeCards = new ArrayList<>();
    private final SecureRandom random = new SecureRandom();

    public Deck() {
        for (Rank rank : Rank.values()) {
            if(rank == Rank.ONE) continue;
            for (Suit suit : Suit.values()) {
                freeCards.add(new Card(rank, suit));
            }
        }
    }

    public Card getRandomCard(){
        return freeCards.remove(random.nextInt(freeCards.size()));
    }
}
