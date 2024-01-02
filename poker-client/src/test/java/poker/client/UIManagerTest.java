package poker.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import poker.commons.game.elements.Card;
import poker.commons.game.elements.Rank;
import poker.commons.game.elements.Suit;
import poker.commons.socket.data_types.while_game.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UIManagerTest {
    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errorStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(errorStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    @Test
    void testShowHomeScreen() {
        UIManager.showHomeScreen();

        String testString = "==========================================================================\n" +
                "Wybierz opcje:\n" +
                "1. Stwórz pokój \n" +
                "2. Dołącz do istniejącego pokoju \n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowAddedRoomScreen() {
        double code = 1234.0;
        UIManager.showAddedRoomScreen(code);

        String testString = "==========================================================================\n" +
                "Twój pokój został stworzony\n" +
                "Numer pokoju: 1234\n" +
                "Wybierz opcje:\n" +
                "1. Oznacz, że jesteś gotowy do gry \n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowAddedToRoomScreen() {
        UIManager.showAddedToRoomScreen();

        String testString = "==========================================================================\n" +
                "Zostałeś dodany do pokoju\n" +
                "Wybierz opcje:\n" +
                "1. Oznacz, że jesteś gotowy do gry \n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowGameIsStartedScreen() {
        UIManager.showGameIsStartedScreen();

        String testString = "==========================================================================\n" +
                "Gra się rozpoczęła\n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowInfoAboutBlinds() {
        ArrayList<Card> cards = new ArrayList<>(); // populate with appropriate cards
        StartGameDataInfo startGameDataInfo = new StartGameDataInfo(PlayerType.SMALL_BLIND, 10, 20, 100, cards);

        UIManager.showInfoAboutBlinds(startGameDataInfo);

        String testString = "==========================================================================\n" +
                "Zostałeś SmallBlind\n" +
                "SmallBlind rzucił 10\n" +
                "BigBlind rzucił 20\n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowYourCards() {
        List<Card> cards = new ArrayList<>();

        cards.add(new Card(Rank.ACE, Suit.SPADES));
        cards.add(new Card(Rank.KING, Suit.HEARTS));

        UIManager.showYourCards(cards);

        String testString = "==========================================================================\n" +
                "O to Twoje karty:\n" +
                "\tWino - As\n" +
                "\tSerce - Król\n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowBetTurn() {
        BetInfo betInfo = new BetInfo(100, 50, true, false, true);

        UIManager.showBetTurn(betInfo);

        String testString = "==========================================================================\n" +
                "Twój stan konta: 100\n" +
                "Aktualna stawka na stole wynosi: 50\n" +
                "Teraz Twoja kolej\n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowNextRoundDealCards() {
        List<Card> cards = List.of(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS));

        NextRoundInfo nextRoundInfo = new NextRoundInfo(cards); // Utwórz obiekt NextRoundInfo z danymi testowymi

        UIManager.showNextRoundDealCards(nextRoundInfo);

        String testString = "==========================================================================\n" +
                "Rozpoczęła się następna runda\n" +
                "O to karty na stole:\n" +
                "\tWino - As\n" +
                "\tSerce - Król\n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowEndTurnScreen() {
        List<Card> cardsOnTable = List.of(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS));


        List<Card> cardsInHand = List.of(new Card(Rank.KING, Suit.SPADES), new Card(Rank.QUEEN, Suit.HEARTS));


        EndGameInfo endGameInfo = new EndGameInfo(cardsOnTable, cardsInHand, true, 1, "Variation", 100, 150); // Utwórz obiekt EndGameInfo z danymi testowymi

        UIManager.showEndTurnScreen(endGameInfo);

        String testString = "==========================================================================\n" +
                "Koniec rundy\n" +
                "Karty na stole:\n" +
                "\tWino - As\n" +
                "\tSerce - Król\n" +
                "Twoje karty:\n" +
                "\tWino - Król\n" +
                "\tSerce - Dama\n" +
                "Twój układ: Variation\n" +
                "Wygrałeś!\n" +
                "Zebrałeś: 100 \n" +
                "Twój stan konta: 150 \n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }

    @Test
    void testShowWaitForNewTurn() {
        UIManager.showWaitForNewTurn();

        String testString = "==========================================================================\n" +
                "Za chwilę rozpocznie się kolejna tura\n" +
                "Wybierz opcje:\n" +
                "1. Oznacz, że jesteś gotowy do następnej tury \n" +
                "2. Oznacz, że rezygnujesz \n" +
                "==========================================================================";

        assertEquals(testString, outputStreamCaptor.toString().trim().replaceAll("\r\n", "\n"));
    }
}
