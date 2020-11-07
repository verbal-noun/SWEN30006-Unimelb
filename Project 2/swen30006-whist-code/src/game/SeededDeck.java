package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.Random;

//Team 1 Tuesday 5:15pm
/**
 * Singleton class to deal out cards randomly
 */
public class SeededDeck extends DeckDecorator {
    private Random random;

    public SeededDeck(Deck deck) {
        super(deck);
        this.random = Setting.getInstance().getRandom();
    }

    public Suit randomSuit() {
        int x = random.nextInt(Suit.class.getEnumConstants().length);
        return Suit.class.getEnumConstants()[x];
    }

    // return random Card from Hand
    private Card randomCard(Hand hand){
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    /**
     * A method to deal out cards randomly to the players
     * @param nbPlayers - the number of players playing the game
     * @return
     */
    public Hand[] dealingOut(int nbPlayers){
        Hand newDeck = deck.toHand(false);
        // last hand is the remaining undealt cards
        Hand[] playerHands = new Hand[nbPlayers + 1];
        for (int i = 0; i < nbPlayers; i++) {
            playerHands[i] = new Hand(deck);
        }
        int totalCardsToBeDealt = nbPlayers * Setting.getInstance().getNbStartCards();
        int handNo = 0;
        while (totalCardsToBeDealt > 0){
            Card dealt = randomCard(newDeck);

            dealt.removeFromHand(false);
            playerHands[handNo].insert(dealt, false);
            //Cycle through players
            handNo = (handNo + 1) % nbPlayers;
            totalCardsToBeDealt--;
        }
        playerHands[nbPlayers] = newDeck;
        return playerHands;
    }
}
