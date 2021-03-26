import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Store {

    private HashMap<Integer, TreeSet<Item>> inventory;
    private ArrayList<Cashier> cashiers;

    private int percentDiscount;
    private int expireDiscountPeriod;
    private int overchargeFood;
    private int overchargeNonFood;

    public Store(int percentDiscount, int expireDiscountPeriod, int overchargeFood, int overchargeNonFood) {
        this.cashiers = new ArrayList<Cashier>();
        this.inventory = new HashMap<>();
        this.overchargeNonFood = overchargeNonFood;
        this.percentDiscount = percentDiscount;
        this.expireDiscountPeriod = expireDiscountPeriod;
        this.overchargeFood = overchargeFood;

    }

    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
    }

    public void start()
    {
        for (Cashier cashier : cashiers) {

            Thread aTread = new Thread(cashier);
            aTread.start();
        }
    }
    public void printInventory() {
        for (int i : inventory.keySet()) {
            System.out.println(i);
            TreeSet<Item> ts = inventory.get(i);
            for (Item value : ts) {
                System.out.println(value);
                //toString();
            }
        }
    }

    public void deliverItem(Item item) {

        TreeSet<Item> items = inventory.get(item.getItemId());

        if (items == null) {
            items = new TreeSet<Item>();
            inventory.put(item.getItemId(), items);
            //ako nqmame takova id dobavqme nov TreeSet s novo id
        }

        items.add(item);
        //dobavqme nov hlqb vyv TreeSet s id 1
    }

    public int getPercentDiscount() {
        return percentDiscount;
    }

    public void setPercentDiscount(int percentDiscount) {
        this.percentDiscount = percentDiscount;
    }

    public int getExpireDiscountPeriod() {
        return expireDiscountPeriod;
    }

    public void setExpireDiscountPeriod(int expireDiscountPeriod) {
        this.expireDiscountPeriod = expireDiscountPeriod;
    }

    public int getOverchargeFood() {
        return overchargeFood;
    }

    public void setOverchargeFood(int overchargeFood) {
        this.overchargeFood = overchargeFood;
    }

    public int getOverchargeNonFood() {
        return overchargeNonFood;
    }

    public void setOverchargeNonFood(int overchargeNonFood) {
        this.overchargeNonFood = overchargeNonFood;
    }
}
