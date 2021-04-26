import java.util.*;

public class Cashier implements Runnable {

    private CashRegister cashRegister;
    private String name;
    private double salary;
    private int id;
    private boolean isInTransaction;
    private static int maxNumReceipts = 5;

    public static int getMaxNumReceipts() {
        return maxNumReceipts;
    }

    public static void setMaxNumReceipts(int maxNumReceipts) {
        Cashier.maxNumReceipts = maxNumReceipts;
    }


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
                    cashRegister.beginReceipt(getName());
                    printMessage("Welcome");
                }

                CustomerInput ci = null;
                boolean isSuccessInput = false;
                while (!isSuccessInput) {
                    ci = getCustomerInput();
                    isSuccessInput = ci.isSuccess();
                }
                //svyrshva stransactiona
                if (ci.isEndPurchase()) {
                    isInTransaction = false;
                    String receipt = cashRegister.endReceipt();
                    printMessage(receipt);
                    if (CashRegister.getNumReceiptsIssued() >= maxNumReceipts) {
                        cashRegister.getStore().close();
                    }

                }
                //dobavqme purchase kym pokupkata
                else {
                    isInTransaction = true;
                    try {
//                        cashRegister.getStore().Purchase(pi.getId(),pi.getQuantity());
                        double price = cashRegister.addPurchase(ci.getId(), ci.getQuantity());
                        printMessage("Purchased " + ci.getQuantity() + " "
                                + cashRegister.getStore().getNameForId(ci.getId()));
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

    public CustomerInput getCustomerInput() {
        printInventory();
        System.out.print("CR " + cashRegister.getId() + "/ " + name + ": ");
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        if (line == null || line.isBlank()) {
            return new CustomerInput(0, 0, true, true);
        } else {
            try {
                Scanner strSc = new Scanner(line);
                int id = strSc.nextInt();
                int quantity = strSc.nextInt();
                if (!(id > 0 && quantity > 0)) {
                    throw new Exception();
                }
                return new CustomerInput(id, quantity, true, false);
            } catch (Exception e) {
                printMessage("Please enter product id and quantity, example: \n 1 2");
                return new CustomerInput(0, 0, false, false);
            }
        }
    }

    public void printInventory() {
        HashMap<Integer, TreeSet<Item>> inventoryBuf = cashRegister.getStore().getInventory();

        for (Map.Entry<Integer, TreeSet<Item>> item : inventoryBuf.entrySet()) {
            int key = item.getKey();
            TreeSet<Item> values = item.getValue();

            System.out.println(key + " " + values.first().getName());

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

    class CustomerInput {
        private int id;
        private int quantity;
        private boolean isSuccess;
        private boolean isEndPurchase;

        public CustomerInput(int id, int quantity, boolean isSuccess, boolean isEndPurchase) {
            this.id = id;
            this.quantity = quantity;
            this.isSuccess = isSuccess;
            this.isEndPurchase = isEndPurchase;
        }

        public boolean isSuccess() {
            return isSuccess;
        }

        public boolean isEndPurchase() {
            return isEndPurchase;
        }

        public int getId() {
            return id;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}


