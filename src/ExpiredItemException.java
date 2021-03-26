public class ExpiredItemException extends  Exception {
    private Item expiredItem;

    public ExpiredItemException(Item expiredItem) {
        super(expiredItem + " is expired");
        this.expiredItem = expiredItem;
    }
}
