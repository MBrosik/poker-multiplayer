package poker.server.game;

import lombok.Getter;
import lombok.Setter;
import poker.commons.game.elements.Card;
import poker.server.socket.SessionData;

import java.util.ArrayList;


public class Player {
    public static final int startMoney = 200;
    SessionData sessionData;
    public ArrayList<Card> cardsInHand = new ArrayList<>();

    @Getter
    @Setter
    private boolean readyToPlay = false;

    @Getter
    @Setter
    private int money = startMoney;

    @Getter
    @Setter
    private int bet = 0;

    @Getter
    @Setter
    private boolean passed = false;

    public Player(SessionData sessionData){
        this.sessionData = sessionData;
    }

    public void addCard(Card card){
        cardsInHand.add(card);
    }
}
