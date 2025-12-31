public class TransactionRecord {
    private String type;
    private String stock;
    private int quantity;
    private double price;

    public TransactionRecord(String type, String stock, int quantity, double price) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
    }

    public String getType() { return type; }
    public String getStock() { return stock; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}