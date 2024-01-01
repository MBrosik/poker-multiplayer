package poker.commons.socket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import poker.commons.socket.data_types.ActionType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiveData {
    private ActionType action;
    private Object data;
}
