package poker.commons.socket.dataTypes.whileGame;

import lombok.AllArgsConstructor;
import lombok.Data;
import poker.commons.game.elements.Card;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class BetInfo {
    private int money;
    private int currentBet;
    private boolean myBet;
}
