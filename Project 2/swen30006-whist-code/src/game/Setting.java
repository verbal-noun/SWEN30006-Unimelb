package game;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

//Team 1 Tuesday 5:15pm
/*

 * Singleton class to instantiate the game and players according to configuration
 */
public class Setting {
    private static Setting instance;
    private int nbPlayers;
    private int nbStartCards;
    private int winningScore;
    private int thinkingTime;
    private String seed;
    private boolean enforceRules;
    private boolean hideCards;
    private Random random;
    private final Properties whistProperties = new Properties();

    // Read configuration file and create an instance of game objects accordingly
    public static void loadSetting(String fileToRead) throws IOException {
        instance = new Setting(fileToRead);
    }

    public static Setting getInstance() {

        return instance;
    }

    public Setting(String fileToRead) throws IOException {
        FileReader inStream = null;
        try {
            inStream = new FileReader(fileToRead);
            whistProperties.load(inStream);
        } finally {
            if (inStream != null) {
                inStream.close();
            }
        }

        nbPlayers = Integer.parseInt(whistProperties.getProperty("nbPlayers"));
        nbStartCards = Integer.parseInt(whistProperties.getProperty("nbStartCards"));
        winningScore = Integer.parseInt(whistProperties.getProperty("winningScore"));
        thinkingTime = Integer.parseInt(whistProperties.getProperty("thinkingTime"));
        seed = whistProperties.getProperty("seed");
        enforceRules = Boolean.parseBoolean(whistProperties.getProperty("enforceRules"));
        hideCards = Boolean.parseBoolean(whistProperties.getProperty("hideCards"));

        if (seed == null){
            random = new Random();
        }
        else{
            random = new Random(Long.parseLong(seed));
        }
    }

    public ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        PlayerFactory pf = PlayerFactory.getInstance();
        int playersAdded = 0;
        try {
            while (playersAdded < nbPlayers){
                String pType = whistProperties.getProperty(String.format("p%dType", playersAdded));
                String pFilter = whistProperties.getProperty(String.format("p%dFilter", playersAdded));
                String pSelection = whistProperties.getProperty(String.format("p%dSelection", playersAdded));

                Player player = pf.getPlayer(pType, pFilter, pSelection);
                if (player == null) continue;
                players.add(player);
                playersAdded++;
            }
        } catch (InvalidPropertiesException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return players;
    }

    public int getNbPlayers() {
        return nbPlayers;
    }

    public int getNbStartCards() {
        return nbStartCards;
    }

    public int getThinkingTime() {
        return thinkingTime;
    }

    public int getWinningScore() {
        return winningScore;
    }

    public boolean isEnforceRules() {
        return enforceRules;
    }

    public boolean isHideCards() {
        return hideCards;
    }

    public Random getRandom() {
        return random;
    }

    public void setSeed(String seed) {
        this.seed = seed;
        this.random = new Random(Long.parseLong(seed));
    }
}
