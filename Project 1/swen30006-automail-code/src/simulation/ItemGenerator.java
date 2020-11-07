package simulation;

import java.util.*;

import automail.FoodItem;
import automail.Item;
import automail.MailItem;
import automail.ItemPool;

/**
 * This class generates the item
 */
public class ItemGenerator {

    public static int DEFAULT_PRIORITY_LEVEL = 1;

    public final int ITEM_TO_CREATE;
    public final int ITEM_MAX_WEIGHT;

    private int itemCreated;

    private final Random random;
    /** This seed is used to make the behaviour deterministic */

    private boolean complete;
    private ItemPool itemPool;

    private Map<Integer,ArrayList<Item>> allItem;

    private boolean deliverFood;



    /**
     * Constructor for item generation
     * @param itemToCreate roughly how many item items to create
     * @param itemMaxWeight limits the maximum weight of the item
     * @param itemPool where item items go on arrival
     * @param seed random seed for generating item
     */
    public ItemGenerator(int itemToCreate, int itemMaxWeight, ItemPool itemPool, HashMap<Boolean,Integer> seed, boolean deliverFood){
        if(seed.containsKey(true)){
            this.random = new Random((long) seed.get(true));
        }
        else{
            this.random = new Random();
        }
        // Vary arriving item by +/-20%
        ITEM_TO_CREATE = itemToCreate*4/5 + random.nextInt(itemToCreate*2/5);
        ITEM_MAX_WEIGHT = itemMaxWeight;
        // System.out.println("Num Mail Items: "+MAIL_TO_CREATE);
        itemCreated = 0;
        complete = false;
        allItem = new HashMap<Integer,ArrayList<Item>>();
        this.deliverFood = deliverFood;
        this.itemPool = itemPool;
    }

    /**
     * Creates an random MailItem or FoodItem
     * @param destinationFloor the destination floor intended for this item
     * @param arrivalTime the time that the arrived
     * @param weight the weight of this item
     * @return Item
     */
    private Item createItem(int destinationFloor, int arrivalTime,int weight){
        if (!deliverFood) return new MailItem(destinationFloor,arrivalTime,weight, DEFAULT_PRIORITY_LEVEL);

        // Equal chance to generate MailItem or FoodItem
        // Change value to skew item generation
        double foodToMailRatio = 0.5;
        if (random.nextDouble() > foodToMailRatio) return new MailItem(destinationFloor,arrivalTime,weight, DEFAULT_PRIORITY_LEVEL);
        return new FoodItem(destinationFloor,arrivalTime,weight, DEFAULT_PRIORITY_LEVEL);
    }
    /**
     * @return a new item item that needs to be delivered
     */
    private Item generateItem(){
        Item newItem;
        int destinationFloor = generateDestinationFloor();
        int priorityLevel = generatePriorityLevel();
        int arrivalTime = generateArrivalTime();
        int weight = generateWeight();
        // Check if arrival time has a priority item
        if(	(random.nextInt(6) > 0) ||  // Skew towards non priority item
                (allItem.containsKey(arrivalTime) &&
                        allItem.get(arrivalTime).stream().anyMatch(Item::isPriority)))
        {
            newItem = createItem(destinationFloor, arrivalTime, weight);
        } else {
            newItem = new MailItem(destinationFloor,arrivalTime,weight, priorityLevel);
        }
        return newItem;
    }

    /**
     * @return a destination floor between the ranges of GROUND_FLOOR to FLOOR
     */
    private int generateDestinationFloor(){
        return Building.LOWEST_FLOOR + random.nextInt(Building.getFloors());
    }

    /**
     * @return a random priority level selected from 1 - 100
     */
    private int generatePriorityLevel(){
        return 10*(1 + random.nextInt(10));
    }

    /**
     * @return a random weight
     */
    private int generateWeight(){
        final double mean = 200.0; // grams for normal item`1
        final double stddev = 1000.0; // grams
        double base = random.nextGaussian();
        if (base < 0) base = -base;
        int weight = (int) (mean + base * stddev);
        return Math.min(weight, ITEM_MAX_WEIGHT);
    }

    /**
     * @return a random arrival time before the last delivery time
     */
    private int generateArrivalTime(){
        return 1 + random.nextInt(Clock.MAIL_RECEVING_LENGTH);
    }

    /**
     * This class initializes all items and sets their corresponding values,
     * All generated items will be saved in allItem
     */
    public void generateAllItems(){
        while(!complete){
            Item newItem =  generateItem();
            int timeToDeliver = newItem.getArrivalTime();
            /** Check if key exists for this time **/
            if(allItem.containsKey(timeToDeliver)){
                /** Add to existing array */
                allItem.get(timeToDeliver).add(newItem);
            }
            else{
                /** If the key doesn't exist then set a new key along with the array of MailItems to add during
                 * that time step.
                 */
                ArrayList<Item> newItemList = new ArrayList<Item>();
                newItemList.add(newItem);
                allItem.put(timeToDeliver,newItemList);
            }
            /** Mark the item as created */
            itemCreated++;

            /** Once we have satisfied the amount of item to create, we're done!*/
            if(itemCreated == ITEM_TO_CREATE){
                complete = true;
            }
        }

    }

    /**
     * Given the clock time, put the generated items into the itemPool.
     * So that the robot will can pick up the items from the pool.
     * @return Priority
     */
    public void addToMailPool(){
        //Priority is not used
//    	PriorityMailItem priority = null;
        // Check if there are any item to create
        if(this.allItem.containsKey(Clock.Time())){
            for(Item item : allItem.get(Clock.Time())){
//            	if (itemItem instanceof PriorityMailItem) priority = ((PriorityMailItem) itemItem);
                System.out.printf("T: %3d > new addToPool [%s]%n", Clock.Time(), item.toString());
                // TODO: addToPool needs to accept Item instead of MailItem
                itemPool.addToPool(item);
            }
        }
//        return priority;
    }

    /**
     * Test function to check item generation
     */
    public void printAllItems(){
        for (int key: allItem.keySet()){
            for (Item item: allItem.get(key)){
                System.out.println(item.toString());
            }
        }
    }
}
