package poker.commons.socket.dataTypes.whileGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import poker.commons.game.elements.Card;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class GameData {
    private int money;
    private int currentBet;
    private PlayerType playerType;
    private boolean myBet;
    private ArrayList<Card> cards;
}
