package poker.commons.socket.dataTypes.whileGame;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BetInfo {
    private int money;
    private int currentBet;
    private boolean myBet;
    private boolean passed;
    private boolean moneyAvailable;

}
