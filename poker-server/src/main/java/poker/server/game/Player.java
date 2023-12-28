package poker.server.game;

import lombok.Data;
import poker.commons.game.elements.Card;
import poker.server.socket.SessionData;

import java.util.ArrayList;


@Data
public class Player {
    public static final int startMoney = 200;
    private SessionData sessionData;
    private  ArrayList<Card> cardsInHand = new ArrayList<>();

    private boolean readyToPlay = false;
    private int money = startMoney;
    private int bet = 0;

    private boolean passed = false;

//    String name;

    public Player(SessionData sessionData){
        this.sessionData = sessionData;
    }

    public void addCard(Card card){
        cardsInHand.add(card);
    }

    public boolean checkIfPossibleToBet(){
        return bet == money;
    }

    public int giveBetMoney(){
        int temp = bet;
        money-=temp;
        bet = 0;
        return temp;
    }
}
