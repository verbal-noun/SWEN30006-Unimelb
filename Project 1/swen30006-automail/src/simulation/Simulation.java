package simulation;

import automail.*;
import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import exceptions.MailAlreadyDeliveredException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 * This class simulates the behaviour of AutoMail
 */
public class Simulation {
	private static int NUM_ROBOTS;
	
    /** Constant for the mail generator */
    private static int MAIL_TO_CREATE;
    private static int MAIL_MAX_WEIGHT;
    
    private static boolean OVERDRIVE_ENABLED;
    private static boolean STATISTICS_ENABLED;
    private static boolean DELIVER_FOOD_ENABLED;
    
    private static ArrayList<Item> MAIL_DELIVERED;
    private static double totalDelay = 0;
    private static int totalFoodDelivered = 0;
	private static int totalMailDelivered = 0;
	private static double totalMailWeight = 0;
	private static double totalFoodWeight = 0;
	private static int totalTimeFoodTubeUsed = 0;

    public static void main(String[] args) throws IOException {
    	/** Load properties for simulation based on either default or a properties file.**/
    	Properties automailProperties = setUpProperties();

    	//An array list to record mails that have been delivered
        MAIL_DELIVERED = new ArrayList<Item>();

        /** This code section below is to save a random seed for generating mails.
         * If a program argument is entered, the first argument will be a random seed.
         * If not a random seed will be from a properties file.
         * Otherwise, no a random seed. */

        /** Used to see whether a seed is initialized or not */
        HashMap<Boolean, Integer> seedMap = new HashMap<>();
        if (args.length == 0 ) { // No arg
        	String seedProp = automailProperties.getProperty("Seed");
        	if (seedProp == null) { // and no property
        		seedMap.put(false, 0); // so randomise
        	} else { // Use property seed
        		seedMap.put(true, Integer.parseInt(seedProp));
        	}
        } else { // Use arg seed - overrides property
        	seedMap.put(true, Integer.parseInt(args[0]));
        }
        Integer seed = seedMap.get(true);
        System.out.println("A Random Seed: " + (seed == null ? "null" : seed.toString()));


        /**
         * This code section is for running a simulation
         */
        /* Instantiate ItemPool and Automail */
     	ItemPool itemPool = new ItemPool(NUM_ROBOTS);
        Automail automail = new Automail(itemPool, new ReportDelivery(), NUM_ROBOTS);
        ItemGenerator itemGenerator = new ItemGenerator(MAIL_TO_CREATE, MAIL_MAX_WEIGHT, itemPool, seedMap, DELIVER_FOOD_ENABLED);

        /** Generate all the mails */
        itemGenerator.generateAllItems();
        // PriorityMailItem priority;  // Not used in this version
        while(MAIL_DELIVERED.size() != itemGenerator.ITEM_TO_CREATE) {
            itemGenerator.addToMailPool();
            try {
                automail.itemPool.loadItemsToRobot();
				for (int i=0; i < NUM_ROBOTS; i++) {
					automail.robots[i].operate();
				}
			} catch (ExcessiveDeliveryException|ItemTooHeavyException e) {
				e.printStackTrace();
				System.out.println("Simulation unable to complete.");
				System.exit(0);
			}
            Clock.Tick();
        }
        printResults();
    }

    static private Properties setUpProperties() throws IOException {
    	Properties automailProperties = new Properties();
		// Default properties
    	automailProperties.setProperty("Robots", "Standard");
    	automailProperties.setProperty("ItemPool", "strategies.SimpleMailPool");
    	automailProperties.setProperty("Floors", "10");
    	automailProperties.setProperty("Fragile", "false");
    	automailProperties.setProperty("Mail_to_Create", "3	");
    	automailProperties.setProperty("Mail_Receiving_Length", "3");
    	automailProperties.setProperty("Overdrive", "false");
    	automailProperties.setProperty("Statistics", "false");
    	automailProperties.setProperty("DeliverFood", "false");

    	// Read properties
		FileReader inStream = null;
		try {
			inStream = new FileReader("automail.properties");
			automailProperties.load(inStream);
		} finally {
			 if (inStream != null) {
	                inStream.close();
	            }
		}
		
		// Floors
		Building.setFloors(Integer.parseInt(automailProperties.getProperty("Floors")));
        System.out.println("#Floors: " + Building.getFloors());
		// Mail_to_Create
		MAIL_TO_CREATE = Integer.parseInt(automailProperties.getProperty("Mail_to_Create"));
        System.out.println("#Created mails: " + MAIL_TO_CREATE);
        // Mail_to_Create
     	MAIL_MAX_WEIGHT = Integer.parseInt(automailProperties.getProperty("Mail_Max_Weight"));
        System.out.println("Maximum weight: " + MAIL_MAX_WEIGHT);
		// Last_Delivery_Time
		Clock.MAIL_RECEVING_LENGTH = Integer.parseInt(automailProperties.getProperty("Mail_Receving_Length"));
        System.out.println("Mail receving length: " + Clock.MAIL_RECEVING_LENGTH);
        // Overdrive ability
        OVERDRIVE_ENABLED = Boolean.parseBoolean(automailProperties.getProperty("Overdrive"));
        System.out.println("Overdrive enabled: " + OVERDRIVE_ENABLED);
        // Statistics tracking
        STATISTICS_ENABLED = Boolean.parseBoolean(automailProperties.getProperty("Statistics"));
        System.out.println("Statistics enabled: " + STATISTICS_ENABLED);
        // DeliverFood
		DELIVER_FOOD_ENABLED = Boolean.parseBoolean(automailProperties.getProperty("DeliverFood"));
		if (DELIVER_FOOD_ENABLED) System.out.println("Deliver Food enabled: " + true);
		// Robots
		NUM_ROBOTS = Integer.parseInt(automailProperties.getProperty("Robots"));
		System.out.print("#Robots: "); System.out.println(NUM_ROBOTS);
		assert(NUM_ROBOTS > 0);
		
		return automailProperties;
    }
    static class ReportDelivery implements RobotActionable {
    	
    	/** Confirm the delivery and calculate the total score */
    	public void deliver(Item deliveryItem){
    		if(!MAIL_DELIVERED.contains(deliveryItem)){
    			MAIL_DELIVERED.add(deliveryItem);
                System.out.printf("T: %3d > Delivered(%4d) [%s]%n", Clock.Time(), MAIL_DELIVERED.size(), deliveryItem.toString());
    			// Calculate delivery score
    			totalDelay += calculateDeliveryDelay(deliveryItem);

    			if (deliveryItem instanceof FoodItem){
    				totalFoodDelivered++;
    				totalFoodWeight += deliveryItem.getWeight();
				}
    			else {
					totalMailDelivered++;
					totalMailWeight += deliveryItem.getWeight();
				}
    		}
    		else{
    			try {
    				throw new MailAlreadyDeliveredException();
    			} catch (MailAlreadyDeliveredException e) {
    				e.printStackTrace();
    			}
    		}
    	}

    	public void chargeContainer(Robot robot){
			totalTimeFoodTubeUsed++;
		}

    }
    
    private static double calculateDeliveryDelay(Item deliveryItem) {
    	// Penalty for longer delivery times
    	final double penalty = 1.2;
    	double priority_weight = 0;
        // Take (delivery time - arrivalTime)**penalty * (1+sqrt(priority_weight))
    	if(deliveryItem.isPriority()){
    		priority_weight = deliveryItem.getPriorityLevel();
    	}
        return Math.pow(Clock.Time() - deliveryItem.getArrivalTime(),penalty)*(1+Math.sqrt(priority_weight));
    }

    public static void printResults(){
        System.out.println("T: "+Clock.Time()+" | Simulation complete!");
        System.out.println("Final Delivery time: "+Clock.Time());
        System.out.printf("Delay: %.2f%n", totalDelay);
        if (STATISTICS_ENABLED){
			System.out.format("\nSTATISTICS\n\n");
			System.out.format("Total mail items delivered : %d\n", totalMailDelivered);
			System.out.format("Total food items delivered : %d\n", totalFoodDelivered);
			System.out.format("Total mail weight delivered : %.2f\n", totalMailWeight);
			System.out.format("Total mail weight delivered : %.2f\n", totalFoodWeight);
			System.out.format("Total no. of times food tube is used : %d\n", totalTimeFoodTubeUsed);
		}

    }
}
