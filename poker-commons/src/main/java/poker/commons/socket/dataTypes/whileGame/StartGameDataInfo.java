package poker.commons.socket.dataTypes.whileGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import poker.commons.game.elements.Card;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class StartGameDataInfo {
    private PlayerType playerType;
    private int smallBlindBet;
    private int bigBlindBet;
    private int money;
    private ArrayList<Card> cards;
}
