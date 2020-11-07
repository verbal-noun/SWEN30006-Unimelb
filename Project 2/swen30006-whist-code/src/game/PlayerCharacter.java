package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

//Team 1 Tuesday 5:15pm
/**
 * Class to represent the human player
 */
public class PlayerCharacter extends Player {

    public PlayerCharacter(){
        super();
    }

    @Override
    public void setHand(Hand hand) {
        CardListener cardListener = new CardAdapter()  // Human Player plays card
        {
            public void leftDoubleClicked(Card card) { selected = card; hand.setTouchEnabled(false); }
        };

        hand.addCardListener(cardListener);
        super.setHand(hand);
    }

    /**
     * Implement a playTurn for the PC player
     */
    @Override
    public Card playTurn(Round round) {
        selected = null;
        hand.setTouchEnabled(true);
        WhistUI.getInstance().setStatusText("Player 0 double-click on card to follow.");
        while (null == selected) GameGrid.delay(100);
        return selected;
    }
}
