package game;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
public class RandomSelection implements SelectionStrategy {

    @Override
    public Card chooseCard(Round round, ArrayList<Card> cards, int playerNo) {
        int x = Setting.getInstance().getRandom().nextInt(cards.size() );
        return cards.get(x);
    }
}
