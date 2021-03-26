import java.util.Scanner;

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
                }
                PurchaseItem pi = getCustomerInput();
                //svyrshva stransactiona
                if (pi==null)
                {
                    isInTransaction=false;
                    printMessage("Bye Bye");
                }
                //dobavqme purchase kym pokupkata
                else
                {
                    isInTransaction = true;
                    printMessage("Purchase"+ pi.getId()+" and "+ pi.getQuantity());

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
        else{
            Scanner strSc = new Scanner(line);
            return new PurchaseItem(strSc.nextInt(), strSc.nextInt());
        }
    }

    public void printInventory(){
        System.out.println("This is the inventory");
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


