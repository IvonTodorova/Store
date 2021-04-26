import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
    private String cashReceiptFolder;
    private double expenses;
    private double revenue;


    public double getExpenses() {
        return expenses;
    }

    public void close() {

        System.out.println("Closing store  " + nameStore);
        System.out.println("Number of receipts: " + CashRegister.getNumReceiptsIssued());
        System.out.println("Revenue: " + getRevenue());
        System.out.println("Expenses: " + getExpenses());
        System.out.println("Profit: " + profit());
        System.exit(0);

    }

    public double getRevenue() {
        return revenue;
    }

    public String getCashReceiptFolder() {
        return cashReceiptFolder;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public Store(int percentDiscount, int expireDiscountPeriod, int overchargeFood, int overchargeNonFood, String name) {
        this.cashiers = new ArrayList<Cashier>();
        this.inventory = new HashMap<>();
        this.overchargeNonFood = overchargeNonFood;
        this.percentDiscount = percentDiscount;
        this.expireDiscountPeriod = expireDiscountPeriod;
        this.overchargeFood = overchargeFood;
        this.nameStore = name;
        this.revenue = 0;
        this.expenses = 0;
        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd-HH-mm-ss");
        this.cashReceiptFolder = this.nameStore + "-" + df.format(new Date());
        try {
            Path p = Path.of(cashReceiptFolder);
            Files.createDirectory(p);
            System.out.println("Created receipt folder: " + p.toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void addToExpenses(double expense) {
        expenses += expense;
    }

    public void addToRevenue(double revenue) {
        this.revenue += revenue;
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
                if (items.isEmpty()) {
                    inventory.remove(id);
                }
            }
        }
        if (countAvailable >= num)
            return num;

        else
            return countAvailable;
    }

    public double profit() {
        return getRevenue() - getExpenses();
    }

    public void addCashier(Cashier cashier) {
        cashiers.add(cashier);
        addToExpenses(cashier.getSalary());
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
        addToExpenses(item.getDeliveryPrice() * item.getInventoryNum());

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
