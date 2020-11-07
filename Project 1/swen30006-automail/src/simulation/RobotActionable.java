package simulation;

import automail.Item;
import automail.MailItem;
import automail.Robot;

/**
 * a MailDelivery is used by the Robot to deliver mail once it has arrived at the correct location
 */
public interface RobotActionable {

	/**
     * Delivers an item at its floor
     * @param item the mail item being delivered.
     */
	void deliver(Item item);
    void chargeContainer(Robot robot);
}