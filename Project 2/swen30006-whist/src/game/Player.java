package game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

//Team 1 Tuesday 5:15pm

// Base class for game player and NPC
public abstract class Player {

    private static int newPlayerNo = 0;
    protected int playerNo;
    protected int score;
    protected Hand hand;
    protected Card selected;

    public Player(){
        this.playerNo = newPlayerNo;
        newPlayerNo++;
        score = 0;
    }

    public int getPlayerNo() {
        return playerNo;
    }

    public int getScore() {
        return score;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        hand.sort(Hand.SortType.SUITPRIORITY, true);
        this.hand = hand;
    };

    // Incrementing player score
    public void addScore(){
        score++;
    }

    /**
     * Method to play card during each round of the game
     *
     * @param round - The current round that's being player
     */
    public abstract Card playTurn(Round round);
}
