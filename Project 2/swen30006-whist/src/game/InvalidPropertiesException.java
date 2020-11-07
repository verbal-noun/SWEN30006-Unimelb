package game;
/**
 * An exception thrown when a player breaks a rule
 */
@SuppressWarnings("serial")
//Team 1 Tuesday 5:15pm
public class InvalidPropertiesException extends Exception {
	public InvalidPropertiesException(String violation) {
		super(violation);
	}
}
