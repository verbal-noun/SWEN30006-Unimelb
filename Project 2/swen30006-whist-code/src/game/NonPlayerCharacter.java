package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
/**
 * Class to represent the NPC
 */
public class NonPlayerCharacter extends Player{

    // Instance of the context class that provides access to the filter and
    // selection strategies
    private final HandStrategyContext strategyContext;

    public NonPlayerCharacter(HandStrategyContext strategyContext) {
        this.strategyContext = strategyContext;
    }

    @Override
    public void setHand(Hand hand) {
        hand.setVerso(Setting.getInstance().isHideCards());
        super.setHand(hand);
    }

    @Override
    public Card playTurn(Round round) {
        selected = null;
        WhistUI.getInstance().setStatusText("Player " + playerNo + " thinking...");
        GameGrid.delay(Setting.getInstance().getThinkingTime());

        // Select card to play based on filter and selection strategy
        selected = strategyContext.playCard(round, hand, playerNo);
        return selected;
    }

}
