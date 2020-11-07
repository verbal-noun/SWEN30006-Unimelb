package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
public class TrumpSavingFilter implements FilterStrategy{

    @Override
    public ArrayList<Card> filterCards(Round round, Hand hand) {
        ArrayList<Card> validCards = new ArrayList<>();
        Suit trumpSuit = round.getTrump();
        Suit leadSuit = round.getLead();

        // Filter cards from lead suit first
        for(Card card : hand.getCardList()) {
            if(card.getSuit() == leadSuit) {
                validCards.add(card);
            }
        }

        if(validCards.size() > 0) {
            return validCards;
        }

        // Filter cards from trump or lead suit
        for(Card card : hand.getCardList()) {
            if(card.getSuit() == trumpSuit) {
                validCards.add(card);
            }
        }

        if(validCards.size() == 0) {
            return hand.getCardList();
        }

        return validCards;
    }
}
