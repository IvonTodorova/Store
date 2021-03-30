import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Store {


    private HashMap<Integer, TreeSet<Item>> inventory;
    private ArrayList<Cashier> cashiers;

    private String nameStore;
    private int percentDiscount;
    private int expireDiscountPeriod;
    private int overchargeFood;
    private int overchargeNonFood;

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public Store(int percentDiscount, int expireDiscountPeriod, int overchargeFood, int overchargeNonFood) {
        this.cashiers = new ArrayList<Cashier>();
        this.inventory = new HashMap<>();
        this.overchargeNonFood = overchargeNonFood;
        this.percentDiscount = percentDiscount;
        this.expireDiscountPeriod = expireDiscountPeriod;
        this.overchargeFood = overchargeFood;

    }

    public double Purchase(int id, int num) throws Exception {

        if (!inventory.containsKey(id)) {
            throw new Exception("Item id " + id + " does not exists");
        }
        int numAvail = checkAvailability(id, num);
        if (numAvail < num) {
            throw new Exception("Not enough quantity. Needed " + num + ", available " + numAvail);
        }

        TreeSet<Item> items = inventory.get(id);
        int remaining = num;

        double price = 0.0;
        for (Item item : (TreeSet<Item>) items.clone()) {

            if (item.getInventoryNum() < remaining) {
                remaining -= item.getInventoryNum();
                price += item.getInventoryNum() * item.calculatePrice(this);
                items.remove(item);
            } else {
                item.setInventoryNum(item.getInventoryNum() - remaining);
                price += remaining * item.calculatePrice(this);
                remaining = 0;
            }
            if (remaining == 0) {
                break;
            }

        }
        return price;
    }

    public String getNameForId(int id) {
        TreeSet<Item> items = inventory.get(id);

        if (items != null) {

            String name = items.first().getName();

            return name;
        } else
            return null;

    }

    public int checkAvailability(int id, int num) {
        TreeSet<Item> items = inventory.get(id);
        int countAvailable = 0;
        for (Item item : (TreeSet<Item>) items.clone()) {
            if (!item.isExpired())
                countAvailable += item.getInventoryNum();
            else {
                items.remove(item);
            }
        }
        if (countAvailable >= num)
            return num;

        else
            return countAvailable;
    }

    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
    }

    public HashMap<Integer, TreeSet<Item>> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<Integer, TreeSet<Item>> inventory) {
        this.inventory = inventory;
    }

    public void start() {
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
