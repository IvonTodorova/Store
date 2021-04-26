import java.time.LocalDate;

public class Main {

    private static Store billa;

    public static void main(String[] args) {

        billa = new Store(10,5,20,25,"Kaufland");
        Cashier.setMaxNumReceipts(5);
        //sled tolkova receipts zatvarqme magazina
        addCashiers();

        initialDelivery();
        billa.start();


    }

    private static void addCashiers() {
        CashRegister cashRegister1 = new CashRegister(1,billa);
        Cashier cashier1= new Cashier(cashRegister1,"Ivon",1000,1);
        billa.addCashier(cashier1);

        CashRegister cashRegister2 = new CashRegister(2,billa);
        Cashier cashier2= new Cashier(cashRegister2,"Pavlin",1000,2);
        billa.addCashier(cashier2);


    }

    private static void initialDelivery() {
        Item bread = new Item(1,2,true,
                LocalDate.of(2021, 6, 30),"bread",5);
        billa.deliverItem(bread);
        Item banana = new Item(2,5,true,
                LocalDate.of(2021, 8, 1),
                "banana",10);
        billa.deliverItem(banana);
        bread = new Item(1,2,true,
                LocalDate.of(2021, 8, 15),"bread",10);
        billa.deliverItem(bread);
        billa.printInventory();
    }

}
