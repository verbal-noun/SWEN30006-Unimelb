package game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.GameGrid;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

//Team 1 Tuesday 5:15pm
/**
 * Round class that is responsible for the progression of game rounds
 */
public class Round {
    private Hand trick;
    private DeckDecorator deck;
    private Suit lead;
    private Suit trump;
    private ArrayList<Player> players;
    private Player winner;
    private Card winningCard;
    private Random random;
    private int turn;

    public Round(Deck deck){
        this.deck = new SeededDeck(deck);
        trick = new Hand(deck);
        this.players = Setting.getInstance().getPlayers();
        random = Setting.getInstance().getRandom();
        turn = 0;
    }

    /*
    * Start the Round
    * */
    public void initRound() {
        WhistUI ui = WhistUI.getInstance();
        // Clear trick
        ui.displayTrick(trick);

        // Last element of hands is leftover cards; these are ignored
        Hand[] hands = deck.dealingOut(players.size());

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            player.setHand(hands[i]);
        }

        ui.displayHands(hands);
    }

    /*
    * Play the round
    * */
    public Optional<Integer> playRound() {
        WhistUI ui = WhistUI.getInstance();
        // Select and display trump suit
        trump = deck.randomSuit();
        ui.displayTrumps(trump.ordinal());

        // randomly select player to lead for this round
        int nextPlayer = random.nextInt(players.size());

        boolean isLeadingPlayer;
        Card selected;

        for (int i = 0; i < Setting.getInstance().getNbStartCards(); i++) {
            trick.removeAll(true);

            for (turn = 0; turn < players.size(); turn++) {
                Player player = players.get(nextPlayer);
                // Round will be passed to NPC
                selected = player.playTurn(this);
                isLeadingPlayer = turn == 0;

                if (isLeadingPlayer) {
                    // No restrictions on the card being lead
                    lead = (Suit) selected.getSuit();
                    winner = player;
                    winningCard = selected;
                }

                // Check: Following card must follow suit if possible
                if (!isLegal(selected, nextPlayer)) {
                    // Rule violation
                    String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
                    //System.out.println(violation);
                    if (Setting.getInstance().isEnforceRules())
                        try {
                            throw(new BrokeRuleException(violation));
                        } catch (BrokeRuleException e) {
                            e.printStackTrace();
                            System.out.println("A cheating player spoiled the game!");
                            System.exit(0);
                        }
                }

                // In case it is upside down
                selected.setVerso(false);
                // transfer to trick (includes graphic effect)
                selected.transfer(trick, true);
                ui.displayTrick(trick);

                if (isLeadingPlayer) {
                    System.out.println("New trick: Lead Player = "+nextPlayer+", Lead suit = "+selected.getSuit()+", Trump suit = "+ trump);
                } else {
                    System.out.println("Winning card: "+winningCard.toString());
                }
                System.out.println("Player "+nextPlayer+" play: "+selected.toString()+" from ["+printHand(player.getHand().getCardList())+"]");
                // beat current winner with higher card
                if (beats(selected, winningCard)) {
                    winner = player;
                    winningCard = selected;
                }

                // From last back to first
                nextPlayer = (nextPlayer + 1) % players.size();
            }
            // End Follow
            GameGrid.delay(600);
            nextPlayer = players.indexOf(winner);
            System.out.println("Winner: "+ winner.getPlayerNo());
            ui.setStatusText("Player " + winner.getPlayerNo() + " wins trick.");
            winner.addScore();
            ui.displayScore(nextPlayer, winner.getScore());
            if (Setting.getInstance().getWinningScore() == winner.getScore()) return Optional.of(nextPlayer);
        }
        return Optional.empty();
    }

    private String printHand(ArrayList<Card> cards) {
        String out = "";
        for(int i = 0; i < cards.size(); i++) {
            out += cards.get(i).toString();
            if(i < cards.size()-1) out += ",";
        }
        return(out);
    }

    private boolean isLegal (Card card, int playerNo) {
        Hand hand = players.get(playerNo).getHand();
        if (card.getSuit() == trump || card.getSuit() == lead) {
            return true;
        } else {
            return ((hand.getNumberOfCardsWithSuit(lead) == 0) &&
                    (hand.getNumberOfCardsWithSuit(trump) == 0));
        }
    }

    public boolean beats (Card card1, Card card2) {
        return ((card1.getSuit() == card2.getSuit() && rankGreater(card1, card2)) ||
        // trumped when non-trump was winning
        (card1.getSuit() == trump && card2.getSuit() != trump));
    }

    private boolean rankGreater(Card card1, Card card2) {
        // Warning: Reverse rank order of cards (see comment on enum)
        return card1.getRankId() < card2.getRankId();
    }

    public Suit getTrump() {
        return trump;
    }

    public Hand getTrick() {
        return trick;
    }

    public Card getWinningCard() {
        return winningCard;
    }

    public int[] getScores() {
        return players.stream().mapToInt(Player::getScore).toArray();
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public int getTurn(){
        return turn;
    }

    public Suit getLead() {
        return lead;
    }
}
