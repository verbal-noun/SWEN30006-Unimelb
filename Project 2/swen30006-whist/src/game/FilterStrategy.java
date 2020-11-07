package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
public interface FilterStrategy {
    /*
     * Return an ArrayList of cards that have been filtered
     * */
    public ArrayList<Card> filterCards(Round round, Hand hand);
}
