import javafx.beans.property.*;

public class PortfolioEntry {
    private final StringProperty stock;
    private final IntegerProperty qty;
    private final DoubleProperty avgPrice;
    private final DoubleProperty mktPrice;
    private final DoubleProperty pnl;

    public PortfolioEntry(String stock, int qty, double avgPrice, double mktPrice, double pnl) {
        this.stock = new SimpleStringProperty(stock);
        this.qty = new SimpleIntegerProperty(qty);
        this.avgPrice = new SimpleDoubleProperty(avgPrice);
        this.mktPrice = new SimpleDoubleProperty(mktPrice);
        this.pnl = new SimpleDoubleProperty(pnl);
    }

    // Properties (TableView bindings)
    public StringProperty stockProperty() { return stock; }
    public IntegerProperty qtyProperty() { return qty; }
    public DoubleProperty avgPriceProperty() { return avgPrice; }
    public DoubleProperty mktPriceProperty() { return mktPrice; }
    public DoubleProperty pnlProperty() { return pnl; }

    // Getters
    public String getStock() { return stock.get(); }
    public int getQty() { return qty.get(); }
    public double getAvgPrice() { return avgPrice.get(); }
    public double getMktPrice() { return mktPrice.get(); }
    public double getPnl() { return pnl.get(); }

    // Setters (THIS is the missing piece)
    public void setQty(int value) { qty.set(value); }
    public void setAvgPrice(double value) { avgPrice.set(value); }
    public void setMktPrice(double value) { mktPrice.set(value); }
    public void setPnl(double value) { pnl.set(value); }
}