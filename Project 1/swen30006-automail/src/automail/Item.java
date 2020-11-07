package automail;

import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a delivery item
 */
public abstract class Item {

    /** Represents the destination floor to which the is intended to go */
    protected final int destinationFloor;
    /** The identifier */
    protected final String id;
    /** The time the item arrived */
    protected final int arrivalTime;
    /** The weight in grams of the item */
    protected final int weight;
    /** The priority of the item*/
    protected final int priorityLevel;

    /**
     * Constructor for a MailItem
     * @param destFloor the destination floor intended for this item
     * @param arrivalTime the time that the arrived
     * @param weight the weight of this item
     */
    public Item(int destFloor, int arrivalTime, int weight, int priorityLevel){
        this.destinationFloor = destFloor;
        this.id = String.valueOf(hashCode());
        this.arrivalTime = arrivalTime;
        this.weight = weight;
        this.priorityLevel = priorityLevel;
    }


    /**
     *
     * @return the destination floor of the item
     */
    public int getDestFloor() {
        return destinationFloor;
    }


    /**
     *
     * @return the arrival time of the item
     */
    public int getArrivalTime(){
        return arrivalTime;
    }

    /**
     *
     * @return the weight of the item
     */
    public int getWeight(){
        return weight;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public abstract boolean isPriority();

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
