package poker.commons.socket.data_types;

public enum ActionType {
    CREATE_ROOM,
    JOIN_ROOM,
    READY_TO_PLAY,

    START_GAME_INFO,
    BET,
    PASS,

    NEXT_ROUND,
    END_TURN,

    READY_FOR_NEXT_ROUND,
    END_GAME,
}
