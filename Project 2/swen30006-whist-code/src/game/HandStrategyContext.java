package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

//Team 1 Tuesday 5:15pm
/**
 * Strategy context class that provides other classes and client access to the different
 * strategies.
 */
public class HandStrategyContext {

    // References of the filter and selection strategy
    private FilterStrategy filterStrategy;
    private SelectionStrategy selectionStrategy;

    // Set the strategy for filtering
    public void setFilterStrategy(FilterStrategy filterStrategy) {
        this.filterStrategy = filterStrategy;
    }

    // Set the strategy for card selection
    public void setSelectionStrategy(SelectionStrategy selectionStrategy) {
        this.selectionStrategy = selectionStrategy;
    }

    // Wrapper method to execute filter and selection strategy
    public Card playCard(Round round, Hand hand, int playerNo) {
        // Get filtered cards based on selected method
        ArrayList<Card> filteredCards= filterStrategy.filterCards(round, hand);
        // Choose final card to play
        return selectionStrategy.chooseCard(round, filteredCards, playerNo);
    }

}


