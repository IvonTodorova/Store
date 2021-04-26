import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;

public class CashRegister {


    private int id;
    private Store store;
    private String receipt;
    private int currentReceiptNum;
    private static int receiptCounter=1;
    private double receiptTotal;
    private static int numReceiptsIssued=0;

    public static int getReceiptCounter() {
        return receiptCounter;
    }

    public void beginReceipt(String name)
    {

        currentReceiptNum= receiptCounter++;
        receipt = store.getNameStore();
        receipt += "\nreceipt #" + currentReceiptNum;
        receipt += "\nCR " + id + " / " + name;
        receiptTotal=0;

    }
    public String endReceipt()
    {
        receipt += "\nTotal: $" + receiptTotal;
        receipt += "\n" + LocalDate.now();


        String fileName = "Receipt_" + currentReceiptNum;
        try {
            Path path = Path.of(store.getCashReceiptFolder() + "/" + fileName);
            Files.writeString(path, receipt, StandardOpenOption.SYNC, StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        store.addToRevenue(receiptTotal);
          numReceiptsIssued++;
        return receipt;
    }
    public double addPurchase(int id,int quantity) throws Exception {
        double price = store.Purchase(id,quantity);
        receipt+= "\n" + quantity +"\t"+ store.getNameForId(id)+ "\t"+ price;
        receiptTotal+=price;
        return price;
    }

    public static int getNumReceiptsIssued() {
        return numReceiptsIssued;
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
