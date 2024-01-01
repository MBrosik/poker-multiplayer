package poker.commons.socket.data_types.while_game;

import lombok.AllArgsConstructor;
import lombok.Data;
import poker.commons.game.elements.Card;

import java.util.List;

@Data
@AllArgsConstructor
public class NextRoundInfo {
    private List<Card> cardsOnTheTable;
}
