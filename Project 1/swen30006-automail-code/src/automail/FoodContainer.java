package automail;


import simulation.Clock;

/**
 * Container for FoodItems
 * Holds 3 FoodItems max
 * Requires 5 time ticks to preheat
 */
public class FoodContainer extends Container{

    private static final int FOOD_CONTAINER_CAPACITY = 3;
    private int timeAtLoad;

    public FoodContainer() {
        this.capacity = FOOD_CONTAINER_CAPACITY;
        this.timeAtLoad = Clock.Time();
    }

    @Override
    public void addItem(Item item){
        assert(item instanceof FoodItem);
        super.addItem(item);
    }

    @Override
    public boolean isReady() {
        return Clock.Time() - timeAtLoad >= 5;
    }

}
