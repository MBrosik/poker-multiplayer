package poker.server.game;

import poker.commons.game.elements.Card;
import poker.commons.game.elements.Rank;
import poker.commons.game.elements.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class CardCheckerManager {
    public final static long multiplier = 100_00_00_00_00L;
//    public final static long multiplier = 10000000000L;

    public static long getPointsForCards(ArrayList<Card> cardsOnTable, ArrayList<Card> playerCards) {
        ArrayList<Card> allCards = new ArrayList<>();
        allCards.addAll(cardsOnTable);
        allCards.addAll(playerCards);

        long hasRoyalFlush = hasRoyalFlush(allCards);
        if (hasRoyalFlush != -1) return hasRoyalFlush;

        long hasStraightFlush = hasStraightFlush(allCards);
        if (hasStraightFlush != -1) return hasStraightFlush;


        long hasFourOfKind = hasFourOfKind(allCards);
        if (hasFourOfKind != -1) return hasFourOfKind;

        long hasFullHouse = hasFullHouse(allCards);
        if (hasFullHouse != -1) return hasFullHouse;

        long hasFlush = hasFlush(allCards);
        if (hasFlush != -1) return hasFlush;

        long hasStraight = hasStraight(allCards);
        if (hasStraight != -1) return hasStraight;

        long hasThreeOfAKind = hasThreeOfAKind(allCards);
        if (hasThreeOfAKind != -1) return hasThreeOfAKind;

        long hasTwoPairs = hasTwoPairs(allCards);
        if (hasTwoPairs != -1) return hasTwoPairs;

        long hasPair = hasPair(allCards);
        if (hasPair != -1) return hasPair;

        return checkHighCard(allCards);
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
                    if (straightRanks.get(lastIndex) == Rank.Ace &&
                            straightRanks.get(lastIndex - 4) == Rank.Ten) {
                        return 9L * multiplier + hasStraightFlush - 4L * multiplier;
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
                return 8L * multiplier + hasStraight((ArrayList<Card>) suitCards) - 4L * multiplier;
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

        return 7L * multiplier + fourths.get(0).ordinal() * 100L + notFourths.get(0).ordinal();
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

        return 6L * multiplier + threes.get(0).ordinal() * 100L + pairs.get(0).ordinal();
//        boolean hasThreeOfAKind = rankFrequency.containsValue(3L);
//        boolean hasPair = rankFrequency.containsValue(2L);

//        return hasThreeOfAKind && hasPair;
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

        return 5L * multiplier + sortedRanks.get(0).ordinal();
    }

    private static long hasStraight(ArrayList<Card> cards) {
        Set<Rank> uniqueRanks = cards.stream().map(Card::getRank).collect(Collectors.toSet());

        if (uniqueRanks.size() < 5) {
            return -1;
        }
        if (uniqueRanks.contains(Rank.Ace)) {
            uniqueRanks.add(Rank.One);
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

        return 4L * multiplier + bestRank.ordinal();
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

        return 3L * multiplier + threes.get(0).ordinal() * 100L + notThrees.get(0).ordinal();
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

        List<Rank> twoBestPairs = new ArrayList<>(List.of(new Rank[]{pairs.get(0), pairs.get(1)}));

        List<Rank> notPairs = rankFrequency.keySet().stream()
                .filter(aLong -> !twoBestPairs.contains(aLong))
                .sorted(Comparator.reverseOrder())
                .toList();

        return 2L * multiplier + pairs.get(0).ordinal() * 100_00L + pairs.get(1).ordinal() * 100L + notPairs.get(0).ordinal();
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


        return multiplier + (pairs.get(0).ordinal() * 100L) + notPairs.get(0).ordinal();

    }

    private static long checkHighCard(ArrayList<Card> cards) {
        List<Rank> sortedCards = cards.stream()
                .map(Card::getRank)
                .sorted(Comparator.reverseOrder())
                .toList();

        return sortedCards.get(0).ordinal() * 100_00_00_00L + sortedCards.get(1).ordinal() * 100_00_00L + sortedCards.get(2).ordinal() * 100_00L+ sortedCards.get(3).ordinal() * 100L + sortedCards.get(4).ordinal();
    }
}
