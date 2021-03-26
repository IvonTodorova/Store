import java.time.LocalDate;

//avtomatichno vika i sravnqva s Comparable Items
public class Item implements Comparable<Item> {
    private int itemId;
    private String name;
    private int inventoryNum;
    private double deliveryPrice;
    private boolean isFood;
    private LocalDate expireDate;

    public Item(int itemId,
                double deliveryPrice,
                boolean isFood,
                LocalDate expireDate,
                String name,
                int inventoryNum) {
        this.itemId = itemId;
        this.deliveryPrice = deliveryPrice;
        this.isFood = isFood;
        this.expireDate = expireDate;
        this.name = name;
        this.inventoryNum = inventoryNum;
    }

    public double  calculatePrice(Store store) throws ExpiredItemException {
        double price = getDeliveryPrice();
        if (isFood)
        {
            price += price * (store.getOverchargeFood()) / 100.0;
        }
        else
        {
            price+=price*(store.getOverchargeNonFood()) / 100.0;
        }
        LocalDate currentDate = LocalDate.now();
        if (currentDate.isAfter(expireDate)) {
            throw new ExpiredItemException(this);
        }
        if (currentDate.until(expireDate).getDays() <=store.getExpireDiscountPeriod())
        {
            price-=price *(store.getPercentDiscount()) /100.0;
        }


        return price;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInventoryNum() {
        return inventoryNum;
    }

    public void setInventoryNum(int inventoryNum) {
        this.inventoryNum = inventoryNum;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public boolean isFood() {
        return isFood;
    }

    public void setFood(boolean food) {
        isFood = food;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
    }

    @Override
    public int compareTo(Item o) {

        return this.getExpireDate().compareTo(o.getExpireDate());

    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", name='" + name + '\'' +
                ", inventoryNum=" + inventoryNum +
                ", deliveryPrice=" + deliveryPrice +
                ", isFood=" + isFood +
                ", expireDate=" + expireDate +
                '}';
    }
}
