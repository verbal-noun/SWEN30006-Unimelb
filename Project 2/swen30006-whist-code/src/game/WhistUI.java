package game;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;

import java.awt.*;

//Team 1 Tuesday 5:15pm
@SuppressWarnings("serial")
public class WhistUI extends CardGame {
	static private final WhistUI instance = new WhistUI();

	public static WhistUI getInstance() {
		return instance;
	}

	final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

	private final String version = "1.0";
	private final int handWidth = 400;
	private final int trickWidth = 40;
	private final Location[] handLocations = {
			  new Location(350, 625),
			  new Location(75, 350),
			  new Location(350, 75),
			  new Location(625, 350)
	  };
	private final Location[] scoreLocations = {
			  new Location(575, 675),
			  new Location(25, 575),
			  new Location(575, 25),
			  new Location(650, 575)
	  };
	private int nbPlayers = Setting.getInstance().getNbPlayers();
	private Actor[] scoreActors = new Actor[nbPlayers];
	private Actor trumpsActor = null;
	private final Location trickLocation = new Location(350, 350);
	private final Location textLocation = new Location(350, 450);
	private Location trumpsActorLocation = new Location(50, 50);

	Font bigFont = new Font("Serif", Font.BOLD, 36);


	public void displayGameOver() {
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		refresh();
	}

	public void displayTrumps(int trumps) {
	    if (trumpsActor != null) {
	    	removeActor(trumpsActor);
		}
		trumpsActor = new Actor("sprites/"+trumpImage[trumps]);
		addActor(trumpsActor, trumpsActorLocation);
	}

	public void displayScore() {
		for (int i = 0; i < nbPlayers; i++) {
		 scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
		}
	}

	public void displayScore(int player, int score) {
		removeActor(scoreActors[player]);
		scoreActors[player] = new TextActor(String.valueOf(score), Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[player], scoreLocations[player]);
	}

	public void displayHands(Hand[] hands) {
		RowLayout[] layouts = new RowLayout[nbPlayers];
		for (int i = 0; i < nbPlayers; i++) {
			  layouts[i] = new RowLayout(handLocations[i], handWidth);
			  layouts[i].setRotationAngle(90 * i);
			  hands[i].setView(this, layouts[i]);
			  hands[i].setTargetArea(new TargetArea(trickLocation));
			  hands[i].draw();
		}
	}

	public void displayTrick(Hand trick) {
		trick.setView(this, new RowLayout(trickLocation, (trick.getNumberOfCards()+2)*trickWidth));
		trick.draw();
	}

	public WhistUI() {
		super(700, 700, 30);
		setTitle("WhistGame (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		displayScore();
	}
}
