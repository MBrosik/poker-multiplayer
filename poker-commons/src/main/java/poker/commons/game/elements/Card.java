package poker.commons.game.elements;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Card {
    private Rank rank;
    private Suit suit;

    public String toString() {
        return suit.getName() + " - " + rank.getName();
    }
}
