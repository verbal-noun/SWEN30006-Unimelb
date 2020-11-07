package game;

//Team 1 Tuesday 5:15pm
/**
 * Factory class to create different types of player.
 * Can be easily extended in the future to support various types of players and NPCs
 */
public class PlayerFactory {
    private static final String PC = "pc";
    private static final String NPC = "npc";
    private static PlayerFactory instance;

    public static PlayerFactory getInstance() {
        if (instance == null){
            instance = new PlayerFactory();
        }
        return instance;
    }

    /*
    * Creates an player with the strategy initialized depending on the settings
    * */

    public Player getPlayer(String pType, String pFilter, String pSelection) throws InvalidPropertiesException {
        if (pType.equals(PC)) {
            return new PlayerCharacter();
        } else if (pType.equals(NPC)) {
            FilterStrategy filterStrategy = getFilterStrategy(pFilter);
            SelectionStrategy selectionStrategy = getSelectionStrategy(pSelection);
            HandStrategyContext context = new HandStrategyContext();
            context.setFilterStrategy(filterStrategy);
            context.setSelectionStrategy(selectionStrategy);
            return new NonPlayerCharacter(context);
        }
        return null;
    }

    private FilterStrategy getFilterStrategy(String pFilter) throws InvalidPropertiesException {
        FilterStrategy filterStrategy;
        // Logic for selecting the appropriate filter strategy
        switch (pFilter) {
            case "null":
                filterStrategy = new NoFilter();
                break;
            case "naive_legal":
                filterStrategy = new NaiveLegalFilter();
                break;
            case "trump_saving":
                filterStrategy = new TrumpSavingFilter();
                break;
            default:
                throw new InvalidPropertiesException("Invalid filter settings");
        }

        return filterStrategy;
    }

    private SelectionStrategy getSelectionStrategy(String pSelection) throws InvalidPropertiesException {
        SelectionStrategy selectionStrategy;
        // Logic for selecting the appropriate selection strategy
        switch (pSelection) {
            case "random":
                selectionStrategy = new RandomSelection();
                break;
            case "highest_rank":
                selectionStrategy = new HighestRankSelection();
                break;
            case "smart":
                selectionStrategy = new SmartSelection();
                break;
            default:
                throw new InvalidPropertiesException("Invalid selection settings");
        }

        return selectionStrategy;
    }
}
