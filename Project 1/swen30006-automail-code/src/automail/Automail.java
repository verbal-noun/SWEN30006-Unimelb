package automail;

import simulation.RobotActionable;

public class Automail {
	      
    public Robot[] robots;
    public ItemPool itemPool;
    
    public Automail(ItemPool itemPool, RobotActionable delivery, int numRobots) {
    	/** Initialize the ItemPool */
    	
    	this.itemPool = itemPool;
    	
    	/** Initialize robots */
    	robots = new Robot[numRobots];
    	for (int i = 0; i < numRobots; i++) robots[i] = new Robot(delivery, itemPool, i);
    }
    
}
