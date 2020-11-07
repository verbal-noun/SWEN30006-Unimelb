package game;

import ch.aplu.jcardgame.*;

import java.io.IOException;
import java.util.*;

//Team 1 Tuesday 5:15pm
@SuppressWarnings("serial")
public class Whist {
	private static final String FILE_TO_READ = "smart.properties";
	public Whist() {
		Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
		Round round = new Round(deck);
		Optional<Integer> winner;
		// While a winner emerges, keep playing the game
		do {
			round.initRound();
			winner = round.playRound();
		} while (winner.isEmpty());
		WhistUI ui = WhistUI.getInstance();
		ui.displayGameOver();
		ui.setStatusText("Game over. Winner is player: " + winner.get());
	}

  	public static void main(String[] args) throws IOException {
	    // Load settings from configuration file
		Setting.loadSetting(FILE_TO_READ);
		// Specify seed in args will override seed in properties
		if (args.length != 0){
			Setting.getInstance().setSeed(args[0]);
		}

		new Whist();
	}
}
