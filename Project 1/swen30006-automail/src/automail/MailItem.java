package automail;

import simulation.ItemGenerator;

public class MailItem extends Item{
    /**
     * Constructor for a MailItem
     *
     * @param  destFloor   the destination floor intended for this mail item
     * @param arrivalTime the time that the mail arrived
     * @param weight       the weight of this mail item
     * @param priorityLevel the priority of the mail item
     */
    public MailItem(int destFloor, int arrivalTime, int weight, int priorityLevel) {
        super( destFloor, arrivalTime, weight, priorityLevel);
    }

    @Override
    public String toString(){
        String addOn = priorityLevel == ItemGenerator.DEFAULT_PRIORITY_LEVEL ? "" : String.format(" | Priority: %3d", priorityLevel);
        return String.format("Mail Item:: ID: %6s | Arrival: %4d | Destination: %2d | Weight: %4d%s", id, arrivalTime, destinationFloor, weight, addOn);
    }

    @Override
    public boolean isPriority() {
        return priorityLevel > ItemGenerator.DEFAULT_PRIORITY_LEVEL;
    }
}