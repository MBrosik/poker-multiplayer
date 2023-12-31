package poker.server.game;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import poker.commons.game.elements.Card;
import poker.server.socket.SessionData;

import java.util.ArrayList;


//@Data
public class Player {
    @Getter
    @Setter
    public static final int startMoney = 200;

    @Getter
    @Setter
    private SessionData sessionData;

    @Getter
    @Setter
    private  ArrayList<Card> cardsInHand = new ArrayList<>();

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

//    @Override
//    public int hashCode() {
//        return this.hashCode();
//    }
}
