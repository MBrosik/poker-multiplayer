package poker.server.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import poker.commons.game.elements.Card;
import poker.commons.game.elements.Rank;
import poker.commons.game.elements.Suit;
import poker.server.socket.SessionData;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        SessionData sessionData = new SessionData();
        player = new Player(sessionData);
        sessionData.setPlayer(player);
    }

    @Test
    void testInitialMoney() {
        assertEquals(200, Player.START_MONEY);
        assertEquals(Player.START_MONEY, player.getMoney());
    }

    @Test
    void testAddCard() {
        Card testCard = new Card(Rank.ACE, Suit.HEARTS);
        player.addCard(testCard);

        assertEquals(1, player.getCardsInHand().size());
        assertTrue(player.getCardsInHand().contains(testCard));
    }

    @Test
    void testCheckIfPossibleToBet() {
        player.setBet(50);
        player.setMoney(50);
        assertTrue(player.checkIfPossibleToBet());

        player.setMoney(40);
        assertFalse(player.checkIfPossibleToBet());
    }

    @Test
    void testGiveBetMoney() {
        int initialMoney = player.getMoney();
        player.setBet(30);
        int returnedMoney = player.giveBetMoney();

        assertEquals(30, returnedMoney);
        assertEquals(initialMoney - 30, player.getMoney());
        assertEquals(0, player.getBet());
    }

    @Test
    void testPlayerReadyToPlay() {
        assertFalse(player.isReadyToPlay());
        player.setReadyToPlay(true);
        assertTrue(player.isReadyToPlay());
    }

    @Test
    void testPlayerPassed() {
        assertFalse(player.isPassed());
        player.setPassed(true);
        assertTrue(player.isPassed());
    }

    @Test
    void testPlayerAttendingInNextRound() {
        assertFalse(player.isAttendingInNextRound());
        player.setAttendingInNextRound(true);
        assertTrue(player.isAttendingInNextRound());
    }
}
