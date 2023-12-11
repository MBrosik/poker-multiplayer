package poker.commons.socket;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ReceiveData {
    private ActionType action;
    private Object data;
}
