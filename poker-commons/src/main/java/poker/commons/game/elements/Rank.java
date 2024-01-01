package poker.commons.game.elements;

public enum Rank {
    One("1"),
    Two("2"),
    Three("3"),
    Four("4"),
    Five("5"),
    Six("6"),
    Seven("7"),
    Eight("8"),
    Nine("9"),
    Ten("10"),
    Jack("Walet"),
    Dame("Dama"),
    King("Król"),
    Ace("As");

    private final String name;

    Rank(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
