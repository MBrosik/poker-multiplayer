package poker.client.game.while_game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerChoicesTest {
    @Test
    void etestLog() {
        PlayerChoices choice = PlayerChoices.BET;
        assertEquals(PlayerChoices.BET, choice);
    }
}
