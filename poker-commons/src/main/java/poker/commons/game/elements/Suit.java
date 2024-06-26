package poker.commons.game.elements;

public enum Suit {
    DIAMONDS("Dzwonek"),
    CLUBS("Żołądź"),
    HEARTS("Serce"),
    SPADES("Wino");

    private final String name;
    Suit(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
