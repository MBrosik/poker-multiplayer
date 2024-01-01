package poker.commons.socket.data_types.while_game;

import lombok.AllArgsConstructor;
import lombok.Data;
import poker.commons.game.elements.Card;

import java.util.List;

@Data
@AllArgsConstructor
public class EndGameInfo {
    private List<Card> cardsOnTable;
    private List<Card> cardsInHand;
    private boolean win;
    private int countOfWinner;
    private String variation;
    private int howMuchIWon;
    private int myMoney;
}
