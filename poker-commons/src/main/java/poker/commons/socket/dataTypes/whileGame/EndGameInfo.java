package poker.commons.socket.dataTypes.whileGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poker.commons.game.elements.Card;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class EndGameInfo {
    private ArrayList<Card> cardsOnTable;
    private ArrayList<Card> cardsInHand;
    private boolean win;
    private int countOfWinner;
    private String variation;
    private int howMuchIWon;
    private int myMoney;
}
