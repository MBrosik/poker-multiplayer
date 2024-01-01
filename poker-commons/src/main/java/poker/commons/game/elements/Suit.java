package poker.commons.game.elements;

public enum Suit {
    Diamonds("Dzwonek"),
    Clubs("Żołądź"),
    Hearths("Serce"),
    Spades("Wino");

    private final String name;
    Suit(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
