package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
public class NoFilter implements FilterStrategy {


    @Override
    public ArrayList<Card> filterCards(Round round, Hand hand) {
        return hand.getCardList();
    }
}
