package poker.commons.socket.dataTypes.whileGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import poker.commons.game.elements.Card;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class NextRoundInfo {
    private ArrayList<Card> cardsOnTheTable;
}
