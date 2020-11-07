package game;

import ch.aplu.jcardgame.Card;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
public class HighestRankSelection implements SelectionStrategy{

    @Override
    public Card chooseCard(Round round, ArrayList<Card> cards, int playerNo) {

        Card selected = null;

        // Loop through the ranks in reverse order
        rankSearch:
        for(Rank rank : Rank.values()) {
            for(Card card : cards) {
                // When the card of the highest card is found, return
                if(card.getRank() == rank) {
                   selected = card;
                   break rankSearch;
                }
            }
        }

        return selected;
    }
}
