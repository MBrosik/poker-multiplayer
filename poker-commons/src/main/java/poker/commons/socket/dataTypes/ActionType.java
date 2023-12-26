package poker.commons.socket.dataTypes;

public enum ActionType {
    CreateRoom,
    JoinRoom,
    ReadyToPlay,

    StartGameInfo,
    Bet,
    Check,
    Pass,
}
