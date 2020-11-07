package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
public interface SelectionStrategy {
    /*
     * Return a card that has been selected using the strategy
     * */
    public Card chooseCard(Round round, ArrayList<Card> cards, int playerNo);
}
