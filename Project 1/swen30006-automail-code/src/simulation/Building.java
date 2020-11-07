package simulation;

import java.util.ArrayList;

public class Building {


    /** Represents the ground floor location */
    public static final int LOWEST_FLOOR = 1;
    
    /** Represents the mailroom location */
    public static final int MAILROOM_LOCATION = 1;

    /** The number of floors in the building **/
    private static int floors;

    private static final ArrayList<FloorLock> floorLocks = new ArrayList<>();
    private static final ArrayList<Integer> expiryTime = new ArrayList<>();

    public static int getFloors() {
        return floors;
    }

    /**
     * Set number of floors, all locks are initialized to FREE
     * @param floors
     */
    public static void setFloors(int floors) {
        Building.floors = floors;
        floorLocks.clear();
        expiryTime.clear();
        for (int i=0; i<floors; i++) {
            floorLocks.add(FloorLock.FREE);
            expiryTime.add(Integer.MIN_VALUE);
        }
    }

    /**
     * @param floor
     * @return lock status
     */
    public static FloorLock getLock(int floor) {
        int index = floor - 1;
        if (expiryTime.get(index) < Clock.Time()) return FloorLock.FREE;
        return floorLocks.get(index);
    }

    /**
     * Set lock on the floor. Do not use this method to free a lock, use freeLock instead.
     * A mail lock can be obtained on top of an existing mail lock
     * Otherwise locks can only be obtained if the lock is free
     * @param floor
     * @param lock
     * @return boolean, whether lock was obtained successfully
     */
    public static boolean setLock(int floor, FloorLock lock) {
        FloorLock curLock = getLock(floor);
        if (lock == FloorLock.FREE) return false;
        if (curLock == FloorLock.FREE || (curLock == FloorLock.MAIL && lock == FloorLock.MAIL)) {
            floorLocks.set(floor-1, lock);
            expiryTime.set(floor-1, Integer.MAX_VALUE);
            return true;
        }
        return false;
    }

    /**
     * Frees lock in next time step
     * @param floor
     */
    public static void freeLock(int floor) {
        expiryTime.set(floor-1, Clock.Time());
    }
}
