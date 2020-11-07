package game;

import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.Deck;

//Team 1 Tuesday 5:15pm

/**
 * Decorator abstract class for the SeededDeck
 *
 * To be utilized with Round class to instantiate a new deck with a seed
 */
public abstract class DeckDecorator {
    protected Deck deck;
    public DeckDecorator(Deck deck) {
        this.deck = deck;
    }

    public abstract Suit randomSuit();
    public abstract Hand[] dealingOut(int nbPlayers);
}
