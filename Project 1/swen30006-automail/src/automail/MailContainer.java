package automail;

/**
 * Container for MailItems
 * Represents the combined carrying capacity of
 * the robot's hand and mail tube
 * Holds 2 MailItems max
 */
public class MailContainer extends Container{
    private static final int MAIL_CONTAINER_CAPACITY = 2;


    public MailContainer() {
        capacity = MAIL_CONTAINER_CAPACITY;
    }

    @Override
    public void addItem(Item item){
        assert(item instanceof MailItem);
        super.addItem(item);
    }

    @Override
    public boolean isReady() {
        return true;
    }
}

