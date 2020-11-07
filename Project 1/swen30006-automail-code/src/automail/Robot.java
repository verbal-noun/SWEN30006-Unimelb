package automail;

import exceptions.ExcessiveDeliveryException;
import exceptions.ItemTooHeavyException;
import simulation.Building;
import simulation.Clock;
import simulation.FloorLock;
import simulation.RobotActionable;

import java.util.Map;
import java.util.TreeMap;

/**
 * The robot delivers mail!
 */
public class Robot {
	
    static public final int INDIVIDUAL_MAX_WEIGHT = 2000;

    RobotActionable delivery;
    protected final String id;
    /** Possible states the robot can be in */
    public enum RobotState { DELIVERING, WAITING, CHARGING, RETURNING }
    public RobotState currentState;
    private int currentFloor;
    private int destinationFloor;
    private ItemPool itemPool;
    private boolean receivedDispatch;
    private Container container;

    private Item deliveryItem = null;
    private int deliveryCounter;
    

    /**
     * Initiates the robot's location at the start to be at the mailroom
     * also set it to be waiting for mail.
     * @param delivery governs the final delivery
     * @param itemPool is the source of mail items
     * @param number Robot id number
     */
    public Robot(RobotActionable delivery, ItemPool itemPool, int number){
    	this.id = "R" + number;
        // current_state = RobotState.WAITING;
    	currentState = RobotState.RETURNING;
        currentFloor = Building.MAILROOM_LOCATION;
        this.delivery = delivery;
        this.itemPool = itemPool;
        this.receivedDispatch = false;
        this.deliveryCounter = 0;
        this.container = new MailContainer();
    }
    
    /**
     * This is called when a robot is assigned the mail items and ready to dispatch for the delivery 
     */
    public void dispatch() {
    	receivedDispatch = true;
    	deliveryItem = container.getItem();
    }

    /**
     * This is called on every time step
     * @throws ExcessiveDeliveryException if robot delivers more than the capacity of the tube without refilling
     */
    public void operate() throws ExcessiveDeliveryException {
        // Load the first delivery item
    	switch(currentState) {
    		/** This state is triggered when the robot is returning to the mailroom after a delivery */
    		case RETURNING:
    			/** If its current position is at the mailroom, then the robot should change state */
                if(currentFloor == Building.MAILROOM_LOCATION){
        			/** Tell the sorter the robot is ready */
        			//System.out.println("REACHED");
        			itemPool.registerWaiting(this);
                	changeState(RobotState.WAITING);
                } else {
                	/** If the robot is not at the mailroom floor yet, then move towards it! */
                    moveTowards(Building.MAILROOM_LOCATION);
                	break;
                }
    		case WAITING:
    		    /** If the StorageTube is ready and the Robot is waiting in the mailroom then start the delivery */
                if(receivedDispatch){
                	receivedDispatch = false;
                	deliveryCounter = 0; // reset delivery counter
                	setDestination();
                	// If the FoodContainer is not charged
                	if (container instanceof FoodContainer && !container.isReady()) {
                	    changeState(RobotState.CHARGING);
                	    delivery.chargeContainer(this);
                    }
                	else changeState(RobotState.DELIVERING);
                }
                break;
            case CHARGING:
                if (container.isReady()){
                    changeState(RobotState.DELIVERING);
                }
                break;
    		case DELIVERING:
                // If already here drop off either way
                if(currentFloor == destinationFloor) {
                    deliver();
                }
                else {
                    moveTowards(destinationFloor);
                }
                break;
    	}
    }

    /**
    * Attempt to deliver the item
    * */
    private void deliver() throws ExcessiveDeliveryException{
        // Obtain a lock on the floor, if unsuccessful wait
        FloorLock floorLock = container instanceof MailContainer ? FloorLock.MAIL : FloorLock.FOOD;
        if (!Building.setLock(destinationFloor, floorLock)) {
            System.out.printf("T: %3d > Failed to deliver %s. Floor %d already locked [%s]\n", Clock.Time(), getRobotId(),
                    destinationFloor, Building.getLock(destinationFloor));
            return;
        };
        // Delivery complete, report this to the simulator!
        delivery.deliver(deliveryItem);
        Building.freeLock(destinationFloor);
        // Get the next delivery item
        deliveryItem = null;
        deliveryCounter++;
        if(deliveryCounter > container.getCapacity()){  // Implies a simulation bug
            throw new ExcessiveDeliveryException();
        }
        // Check if want to return, i.e. if there is no item in the tube*/
        if(container.isEmpty() && deliveryItem == null){
            changeState(RobotState.RETURNING);
        }
        else{
            /** If there is another item, set the robot's route to the location to deliver the item */
            deliveryItem = container.getItem();
            //tube = null;
            setDestination();
            changeState(RobotState.DELIVERING);
        }
}

    /**
     * Sets the route for the robot
     */
    private void setDestination() {
        /** Set the destination floor */
        destinationFloor = deliveryItem.getDestFloor();
    }

    /**
     * Generic function that moves the robot towards the destination
     * @param destination the floor towards which the robot is moving
     */
    private void moveTowards(int destination) {
        if(currentFloor < destination){
            currentFloor++;
        } else {
            currentFloor--;
        }
    }
    
    public String getRobotId() {
        int numOnHand = 0;
        if (deliveryItem != null){
            numOnHand++;
        }
        return String.format("%s(%1d)", this.id, container.getNumInContainer() + (container instanceof FoodContainer ? numOnHand : 0));
    }
    
    /**
     * Prints out the change in state
     * @param nextState the state to which the robot is transitioning
     */
    private void changeState(RobotState nextState){
    	if (currentState != nextState) {
            System.out.printf("T: %3d > %7s changed from %s to %s%n", Clock.Time(), getRobotId(), currentState, nextState);
    	}
    	currentState = nextState;
    	if(nextState == RobotState.DELIVERING){
            System.out.printf("T: %3d > %7s-> [%s]%n", Clock.Time(), getRobotId(), deliveryItem.toString());
    	}
    }

    public boolean isFull() {
	    return container.isFull();
    }

    /**
     * Add an item to the Robot's container
     * @param item Item to be added
     * */
    public void addToContainer(Item item) throws ItemTooHeavyException {
	    assert (!this.container.isFull());
	    this.container.addItem(item);
        if (item.weight > INDIVIDUAL_MAX_WEIGHT) throw new ItemTooHeavyException();
    }

    /**
     * Switch current container to a new FoodContainer
     * */
    public void switchToFoodContainer() {
        container = new FoodContainer();
    }

    /**
     * Switch current container to a new MailContainer
     * */
    public void switchToMailContainer(){
        container = new MailContainer();
    }


    // Hashing Logic
    static private int count = 0;
    static private Map<Integer, Integer> hashMap = new TreeMap<Integer, Integer>();

    @Override
    public int hashCode() {
        Integer hash0 = super.hashCode();
        Integer hash = hashMap.get(hash0);
        if (hash == null) { hash = count++; hashMap.put(hash0, hash); }
        return hash;
    }

}
