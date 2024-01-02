package poker.server.game;

import lombok.Getter;
import poker.commons.game.elements.Card;
import poker.commons.game.elements.Rank;
import poker.commons.game.elements.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class CardCheckerManager {
    public static final long MULTIPLIER = 100_00_00_00_00L;

    @Getter
    private long points;
    @Getter
    private String variation;

    public CardCheckerManager(List<Card> cardsOnTable, List<Card> playerCards) {
        getPointsForCards(cardsOnTable, playerCards);
    }


    public void getPointsForCards(List<Card> cardsOnTable, List<Card> playerCards) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(cardsOnTable);
        allCards.addAll(playerCards);

        long hasRoyalFlush = hasRoyalFlush(allCards);
        if (hasRoyalFlush != -1) {
            variation = "Poker królewski";
            points = hasRoyalFlush;
            return;
        }

        long hasStraightFlush = hasStraightFlush(allCards);
        if (hasStraightFlush != -1) {
            variation = "Poker";
            points = hasStraightFlush;
            return;
        }

        long hasFourOfKind = hasFourOfKind(allCards);
        if (hasFourOfKind != -1) {
            variation = "Kareta";
            points = hasFourOfKind;
            return;
        }

        long hasFullHouse = hasFullHouse(allCards);
        if (hasFullHouse != -1) {
            variation = "Full";
            points = hasFullHouse;
            return;
        }

        long hasFlush = hasFlush(allCards);
        if (hasFlush != -1) {
            variation = "Kolor";
            points = hasFlush;
            return;
        }

        long hasStraight = hasStraight(allCards);
        if (hasStraight != -1) {
            variation = "Strit";
            points = hasStraight;
            return;
        }

        long hasThreeOfAKind = hasThreeOfAKind(allCards);
        if (hasThreeOfAKind != -1) {
            variation = "Trójka";
            points = hasThreeOfAKind;
            return;
        }

        long hasTwoPairs = hasTwoPairs(allCards);
        if (hasTwoPairs != -1) {
            variation = "Dwie pary";
            points = hasTwoPairs;
            return;
        }

        long hasPair = hasPair(allCards);
        if (hasPair != -1) {
            variation = "Para";
            points = hasPair;
            return;
        }

        points = checkHighCard(allCards);
        variation = "Wysoka karta";
    }

    private static long hasRoyalFlush(ArrayList<Card> allCards) {
        Map<Suit, List<Card>> cardsBySuit = allCards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitCards : cardsBySuit.values()) {
            if (suitCards.size() >= 5) {
                long hasStraightFlush = hasStraight((ArrayList<Card>) suitCards);

                if (hasStraightFlush != -1) {
                    List<Rank> straightRanks = suitCards.stream()
                            .sorted(Comparator.comparing(Card::getRank))
                            .map(Card::getRank)
                            .toList();

                    int lastIndex = straightRanks.size() - 1;
                    if (straightRanks.get(lastIndex) == Rank.ACE &&
                            straightRanks.get(lastIndex - 4) == Rank.TEN) {
                        return 9L * MULTIPLIER + hasStraightFlush - 4L * MULTIPLIER;
                    }
                }
            }
        }

        return -1;
    }

    private static long hasStraightFlush(ArrayList<Card> allCards) {
        Map<Suit, List<Card>> cardsBySuit = allCards.stream()
                .collect(Collectors.groupingBy(Card::getSuit));

        for (List<Card> suitCards : cardsBySuit.values()) {
            if (suitCards.size() >= 5) {
                var hasStraight = hasStraight((ArrayList<Card>) suitCards);
                if(hasStraight != -1){
                    return 8L * MULTIPLIER + hasStraight - 4L * MULTIPLIER;
                }
            }
        }
        return -1;
    }

    private static long hasFourOfKind(ArrayList<Card> allCards) {
        Map<Rank, Long> rankFrequency = allCards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        List<Rank> fourths = rankFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == 4L)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (fourths.isEmpty()) return -1;

        List<Rank> notFourths = rankFrequency.keySet().stream()
                .filter(aLong -> aLong != fourths.get(0))
                .sorted(Comparator.reverseOrder())
                .toList();

        return 7L * MULTIPLIER + fourths.get(0).ordinal() * 100L + notFourths.get(0).ordinal();
    }

    private static long hasFullHouse(ArrayList<Card> allCards) {
        Map<Rank, Long> rankFrequency = allCards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        List<Rank> threes = rankFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == 3L)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (threes.isEmpty()) return -1;

        List<Rank> pairs = rankFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == 2L && entry.getKey() != threes.get(0))
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (pairs.isEmpty()) return -1;

        return 6L * MULTIPLIER + threes.get(0).ordinal() * 100L + pairs.get(0).ordinal();
    }

    private static long hasFlush(ArrayList<Card> allCards) {
        Map<Suit, Long> suitFrequency = allCards.stream()
                .collect(Collectors.groupingBy(Card::getSuit, Collectors.counting()));

        if (!suitFrequency.containsValue(5L)) return -1;

        Suit chosenSuit = suitFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == 5L)
                .map(Map.Entry::getKey)
                .toList()
                .get(0);

        List<Rank> sortedRanks = allCards.stream()
                .filter(card -> card.getSuit() == chosenSuit)
                .map(Card::getRank)
                .sorted(Comparator.reverseOrder())
                .toList();

        return 5L * MULTIPLIER + sortedRanks.get(0).ordinal();
    }

    private static long hasStraight(ArrayList<Card> cards) {
        Set<Rank> uniqueRanks = cards.stream().map(Card::getRank).collect(Collectors.toSet());

        if (uniqueRanks.size() < 5) {
            return -1;
        }
        if (uniqueRanks.contains(Rank.ACE)) {
            uniqueRanks.add(Rank.ONE);
        }

        List<Rank> sortedUniqueRanks = uniqueRanks.stream()
                .sorted()
                .toList();

        Rank bestRank = null;
        for (int i = 0; i < sortedUniqueRanks.size() - 4; i++) {
            if (sortedUniqueRanks.get(i).ordinal() + 4 == sortedUniqueRanks.get(i + 4).ordinal()) {
                bestRank = sortedUniqueRanks.get(i + 4);
            }
        }


        if (bestRank == null)
            return -1;

        return 4L * MULTIPLIER + bestRank.ordinal();
    }

    private static long hasThreeOfAKind(ArrayList<Card> cards) {
        Map<Rank, Long> rankFrequency = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        List<Rank> threes = rankFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == 3L)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (threes.isEmpty()) return -1;

        List<Rank> notThrees = rankFrequency.keySet().stream()
                .filter(aLong -> aLong != threes.get(0))
                .sorted(Comparator.reverseOrder())
                .toList();

        return 3L * MULTIPLIER + threes.get(0).ordinal() * 100L + notThrees.get(0).ordinal();
    }

    private static long hasTwoPairs(ArrayList<Card> cards) {
        Map<Rank, Long> rankFrequency = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));


        List<Rank> pairs = rankFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == 2L)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (pairs.size() != 2) return -1;

        List<Rank> twoBestPairs = List.of(pairs.get(0), pairs.get(1));

        List<Rank> notPairs = rankFrequency.keySet().stream()
                .filter(aLong -> !twoBestPairs.contains(aLong))
                .sorted(Comparator.reverseOrder())
                .toList();

        return 2L * MULTIPLIER + pairs.get(0).ordinal() * 100_00L + pairs.get(1).ordinal() * 100L + notPairs.get(0).ordinal();
    }

    private static long hasPair(ArrayList<Card> cards) {
        Map<Rank, Long> rankFrequency = cards.stream()
                .collect(Collectors.groupingBy(Card::getRank, Collectors.counting()));

        List<Rank> pairs = rankFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() == 2L)
                .map(Map.Entry::getKey)
                .sorted(Comparator.reverseOrder())
                .toList();

        if (pairs.isEmpty()) return -1;


        List<Rank> notPairs = rankFrequency.keySet().stream()
                .filter(aLong -> aLong != pairs.get(0))
                .sorted(Comparator.reverseOrder())
                .toList();


        return MULTIPLIER + (pairs.get(0).ordinal() * 100L) + notPairs.get(0).ordinal();

    }

    private static long checkHighCard(ArrayList<Card> cards) {
        List<Rank> sortedCards = cards.stream()
                .map(Card::getRank)
                .sorted(Comparator.reverseOrder())
                .toList();

        return sortedCards.get(0).ordinal() * 100_00_00_00L + sortedCards.get(1).ordinal() * 100_00_00L + sortedCards.get(2).ordinal() * 100_00L + sortedCards.get(3).ordinal() * 100L + sortedCards.get(4).ordinal();
    }
}
