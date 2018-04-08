package com.phayao.thinkdast;

import java.util.*;

public class Card implements Comparable<Card> {
    // string representations of ranks
    public static final String[] RANKS = {null, "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    // string representations of suits
    public static final String[] SUITS = {"Clubs", "Diamonds", "Hearts", "Spades"};

    // rank and suit are instance variables
    private final int rank;
    private final int suit;

    /**
     * Constructs and card of the given rank and suit
     * @param rank
     * @param suit
     */
    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
    }

    /**
     * Get the card's rank
     * @return
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Get the card's suit
     * @return
     */
    public int getSuit() {
        return this.suit;
    }

    /**
     * Returns a string representation of the card
     * @return
     */
    public String toString() {
        return RANKS[this.rank] + " of " + SUITS[this.suit];
    }

    /**
     * Returns a negative integer if this card comes before
     * the given card, zero if the two cards are equal, or
     * a positive integer if this card come after the card.
     * @param
     * @return
     */
    @Override
    public int compareTo(Card that) {
        if(this.suit < that.suit) {
            return -1;
        }
        if(this.suit > that.suit) {
            return 1;
        }
        if(this.rank < that.rank) {
            return -1;
        }
        if(this.rank > that.rank) {
            return 1;
        }
        return 0;
    }

    /**
     * Returns true if the given card has the same
     * rank AND some suit; otherwise return false.
     * @param that
     * @return
     */
    public boolean equals(Card that) {
        return this.rank == that.rank && this.suit == that.suit;
    }

    /**
     * Make a List of 52 cards.
     * @return
     */
    public static List<Card> makeDeck() {
        List<Card> cards = new ArrayList<Card>();
        for(int suit = 0; suit <= 3; suit++) {
            for(int rank = 0; rank <= 13; rank++) {
                Card card = new Card(rank, suit);
                cards.add(card);
            }
        }
        return cards;
    }

    public static void main(String[] args) {
        // sort the cards using the natural methods.
        List<Card> cards = makeDeck();
        Collections.sort(cards);
        System.out.println(cards.get(0));
        System.out.println(cards.get(51));

        Comparator<Card> comparator = new Comparator<Card>() {
            @Override
            public int compare(Card card1, Card card2) {
                if(card1.getSuit() < card2.getSuit()) {
                    return -1;
                }
                if(card1.getSuit() > card2.getSuit()) {
                    return 1;
                }
                int rank1 = getRankAceHigh(card1);
                int rank2 = getRankAceHigh(card2);

                if(rank1 < rank2) {
                    return -1;
                }
                if(rank1 > rank2) {
                    return 1;
                }
                return 0;
            }

            private int getRankAceHigh(Card card) {
                int rank = card.getRank();
                if(rank == 1) {
                    return 14;
                }
                else {
                    return rank;
                }
            }
        };
        Collections.sort(cards, comparator);
        System.out.println(cards.get(0));
        System.out.println(cards.get(51));
    }
}
