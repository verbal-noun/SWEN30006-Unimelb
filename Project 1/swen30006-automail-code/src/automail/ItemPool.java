package automail;

import java.util.LinkedList;
import java.util.Comparator;

import exceptions.ItemTooHeavyException;

/**
 * addToPool is called when there are mail items newly arrived at the building to add to the ItemPool or
 * if a robot returns with some undelivered items - these are added back to the ItemPool.
 * The data structure and algorithms used in the ItemPool is your choice.
 * 
 */
public class ItemPool {
	
	public class ItemComparator implements Comparator<Item> {
		@Override
		public int compare(Item i1, Item i2) {
			int order = 0;
			if (i1.getPriorityLevel() < i2.getPriorityLevel()) {
				order = 1;
			} else if (i1.getPriorityLevel() > i2.getPriorityLevel()) {
				order = -1;
			} else if (i1.getDestinationFloor() < i2.getDestinationFloor()) {
				order = 1;
			} else if (i1.getDestinationFloor() > i2.getDestinationFloor()) {
				order = -1;
			}
			return order;
		}
	}
	
	private LinkedList<Item> pool;
	private LinkedList<Robot> robots;
	private Robot mailRobot = null;
	private Robot foodRobot = null;

	public ItemPool(int nrobots){
		// Start empty
		pool = new LinkedList<Item>();
		robots = new LinkedList<Robot>();
	}

	/**
     * Adds an item to the mail pool
     * @param item the mail item being added.
     */
	public void addToPool(Item item) {
		//Item item = new Item(mailItem);
		pool.add(item);
		pool.sort(new ItemComparator());
	}
	
	
	
	/**
     * load up any waiting robots with mailItems and foodItems, if any.
     */
	public void loadItemsToRobot() throws ItemTooHeavyException {
		int itemsLoaded = 0;
		int robotIndex = 0;
		for (Item item : pool){
			if (item instanceof MailItem){
				//Check if there is a robot with a mail container ready
				if (mailRobot == null){
					// No more robots in queue, break the loop
					if (robotIndex == robots.size()){
						break;
					}
					mailRobot = robots.get(robotIndex);
					mailRobot.switchToMailContainer();
					robotIndex++;
				}
				mailRobot.addToContainer(item);
				//dispatch if full
				if (mailRobot.isFull()){
					mailRobot.dispatch();
					mailRobot = null;
				}
			}
			else {
				//Check if there is a robot with a food container ready
				if (foodRobot == null){
					// No more robots in queue, break the loop
					if (robotIndex == robots.size()){
						break;
					}
					foodRobot = robots.get(robotIndex);
					foodRobot.switchToFoodContainer();
					robotIndex++;
				}
				foodRobot.addToContainer(item);
				//dispatch if full
				if (foodRobot.isFull()){
					foodRobot.dispatch();
					foodRobot = null;
				}
			}
			itemsLoaded++;
		}
		// Dispatch robots that are partially filled as item pool is empty.

		if (mailRobot != null){
			mailRobot.dispatch();
			mailRobot = null;
		}
		if (foodRobot != null){
			foodRobot.dispatch();
			foodRobot = null;
		}

		//Remove loaded items from list
		while (itemsLoaded > 0) {
			pool.pollFirst();
			itemsLoaded--;
		}

		//Remove robots dispatched from list
		while (robotIndex > 0){
			robots.pollFirst();
			robotIndex--;
		}
	}

	/**
     * @param robot refers to a robot which has arrived back ready for more mailItems to deliver
     */	
	public void registerWaiting(Robot robot) { // assumes won't be there already
		robots.add(robot);
	}


}
