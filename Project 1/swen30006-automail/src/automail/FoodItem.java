package automail;

public class FoodItem extends Item{

    /**
     * Constructor for a MailItem
     *
     * @param destFloor   the destination floor intended for this food item
     * @param arrivalTime the time that the food item arrived
     * @param weight       the weight of this food item
     * @param priorityLevel the priority of the mail item
     */
    public FoodItem(int destFloor, int arrivalTime, int weight, int priorityLevel) {
        super(destFloor, arrivalTime, weight, priorityLevel);
    }

    @Override
    public String toString(){
        return String.format("Food Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d", id, arrivalTime, destinationFloor, weight);
    }

    @Override
    public boolean isPriority() {
        return false;
    }
}