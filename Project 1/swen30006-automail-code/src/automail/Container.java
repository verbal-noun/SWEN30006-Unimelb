package automail;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Represents the carrying capacity of robot
 */
public abstract class Container {
    protected int capacity;
        protected Queue<Item> itemQueue;
    public Container() {
        itemQueue = new LinkedList<Item>();
    }

    public void addItem(Item item) {
        if(itemQueue.size() < capacity) {
            itemQueue.add(item);
        }
    }

    public Item getItem() {
        return itemQueue.poll();
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isFull() {
        return itemQueue.size() == capacity;
    }

    public boolean isEmpty() {
        return itemQueue.isEmpty();
    }

    public abstract boolean isReady();

    public int getNumInContainer(){
        return itemQueue.size();
    }
}
