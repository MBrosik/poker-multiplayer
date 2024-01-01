package poker.commons.game.elements;

public enum Rank {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("Walet"),
    DAME("Dama"),
    KING("Król"),
    ACE("As");

    private final String name;

    Rank(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
