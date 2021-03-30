import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class CashRegister {


    private int id;
    private Store store;
    private String receipt;
    private int currentReceiptNum;
    private static int receiptCounter=0;

    public void beginReceipt(String name)
    {
        currentReceiptNum= receiptCounter++;
        receipt = store.getNameStore();
        receipt += "\nreceipt #" + currentReceiptNum;
        receipt += "\nCR " + id + " / " + name;

    }
    public String endReceipt()
    {
        receipt += "\n" + LocalDate.now();
        String fileName = "Receipt_" + currentReceiptNum;
        try {
            Files.writeString(Path.of(fileName), receipt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return receipt;
    }
    public double addPurchase(int id,int quantity) throws Exception {
        double price = store.Purchase(id,quantity);
        receipt+= "\n" + quantity +"\t"+ store.getNameForId(id)+ "\t"+ price;
        return price;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CashRegister(int id,Store store) {
        this.id = id;
        this.store=store;
    }
}
