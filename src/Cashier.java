import com.sun.jdi.IntegerValue;

import java.lang.invoke.VarHandle;
import java.sql.Array;
import java.util.*;

public class Cashier implements Runnable {

    private CashRegister cashRegister;
    private String name;
    private double salary;
    private int id;
    private boolean isInTransaction;


    public Cashier(CashRegister cashRegister, String name, double salary, int id) {
        this.cashRegister = cashRegister;
        this.name = name;
        this.salary = salary;
        this.id = id;

    }

    @Override
    public void run() {
        while (true) {
            synchronized (Cashier.class) {
                //zapochva nov transaction
                if (!isInTransaction) {
                    printMessage("Welcome");
                    cashRegister.beginReceipt(getName());
                }

                PurchaseItem pi = getCustomerInput();
                //svyrshva stransactiona
                if (pi==null)
                {
                    isInTransaction=false;
                    String receipt= cashRegister.endReceipt();
                    printMessage(receipt);
                }
                //dobavqme purchase kym pokupkata
                else
                {
                    isInTransaction = true;
                    try {
//                        cashRegister.getStore().Purchase(pi.getId(),pi.getQuantity());
                        double price = cashRegister.addPurchase(pi.getId(), pi.getQuantity());
                        printMessage("Purchased " +pi.getQuantity() + " "
                                + cashRegister.getStore().getNameForId(pi.getId()));
                    } catch (Exception e) {
                        printMessage(e.getMessage());
                    }

                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public PurchaseItem getCustomerInput() {
        printInventory();
        System.out.print("CR " + cashRegister.getId() + "/ " + name + ": ");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        if( line == null || line.isBlank()) {
            return null;
        }
        else {
            Scanner strSc = new Scanner(line);
            return new PurchaseItem(strSc.nextInt(), strSc.nextInt());
        }


    }

    public void printInventory()
    {
        HashMap<Integer, TreeSet<Item>> inventoryBuf = cashRegister.getStore().getInventory();

        for (Map.Entry<Integer,TreeSet<Item>>item :inventoryBuf.entrySet())
        {
            int key = item.getKey();
            TreeSet<Item> values = item.getValue();

            System.out.println( key+" "+values.first().getName());

        }
    }


    public void printMessage(String msg) {
        System.out.println("CR " + cashRegister.getId() + "/ " + name + ": " + msg);
    }

    public CashRegister getCashRegister() {
        return cashRegister;
    }

    public void setCashRegister(CashRegister cashRegister) {
        this.cashRegister = cashRegister;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    class PurchaseItem {
        private int id;
        private int quantity;

        public PurchaseItem(int id, int quantity) {
            this.id = id;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}


