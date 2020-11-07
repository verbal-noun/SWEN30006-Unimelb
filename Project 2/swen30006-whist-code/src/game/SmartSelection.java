package game;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Team 1 Tuesday 5:15pm
public class SmartSelection implements SelectionStrategy {
    private int numberOfRanks = 12;

    private double getWinValue(Round round, int playerNo) {
        return 1.0;
    }

    // Assumes that the opponents are selecting cards at random
    private double getWinLikelihood(Round round, Card card) {
        Suit trump = round.getTrump();
        Card winningCard = round.getWinningCard();
         if (winningCard != null && !round.beats(card, winningCard)) {
             return 0;
         }
         int turnsLeft = round.getNumberOfPlayers() - round.getTurn() - 1;
         double turnWinLikelihood = (double) (numberOfRanks - card.getRankId()) / numberOfRanks;
         if (trump != card.getSuit())
             turnWinLikelihood = 0.75 * turnWinLikelihood;
         else
            turnWinLikelihood = 0.75 + 0.25 * turnWinLikelihood;

         return Math.pow(turnWinLikelihood, turnsLeft);
    }

    private double getCardValue(Round round, Card card) {
        int cardRank = card.getRankId();
        int isTrump = card.getSuit() == round.getTrump() ? 1 : 0;
        /* For example
        01/24: 2
        02/24: 3
        ...
        12/24: Ace
        13/24: 2 (Trump Suit)
        14/24: 3 (Trump Suit)
        ...
        24/24: Ace (Trump Suit)
         */
        return (isTrump + (double) (numberOfRanks - cardRank) / numberOfRanks) / 4;
    }

    public Card chooseCard(Round round, ArrayList<Card> hand, int playerNo) {
        double winValue = getWinValue(round, playerNo);
        List<Double> discountedValues = new ArrayList<Double>();
        for (Card card : hand) {
            double cardValue = getCardValue(round, card);
            double winLikelihood = getWinLikelihood(round, card);
            double discountedValue = winValue * winLikelihood - cardValue;
            discountedValues.add(discountedValue);
        }
        return hand.get(discountedValues.lastIndexOf(Collections.max(discountedValues)));
    }
}
