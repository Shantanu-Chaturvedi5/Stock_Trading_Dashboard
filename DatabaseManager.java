import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection conn;

    public DatabaseManager(String url) throws SQLException {
        conn = DriverManager.getConnection(url);
        setupTables();
    }

    private void setupTables() throws SQLException {
        Statement stmt = conn.createStatement();

        // Holdings table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS holdings (" +
                "stock TEXT PRIMARY KEY, " +
                "qty INTEGER, " +
                "avg_price REAL)");

        // Transactions table
        stmt.executeUpdate("CREATE TABLE IF NOT EXISTS transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "stock TEXT, " +
                "qty INTEGER, " +
                "price REAL, " +
                "type TEXT, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    // --- Buy stock ---
    public void buyStock(String stock, int qty, double price) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT qty, avg_price FROM holdings WHERE stock = ?");
        ps.setString(1, stock);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int oldQty = rs.getInt("qty");
            double oldAvg = rs.getDouble("avg_price");

            int newQty = oldQty + qty;
            double newAvg = ((oldQty * oldAvg) + (qty * price)) / newQty;

            PreparedStatement update = conn.prepareStatement(
                    "UPDATE holdings SET qty = ?, avg_price = ? WHERE stock = ?");
            update.setInt(1, newQty);
            update.setDouble(2, newAvg);
            update.setString(3, stock);
            update.executeUpdate();
        } else {
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO holdings (stock, qty, avg_price) VALUES (?, ?, ?)");
            insert.setString(1, stock);
            insert.setInt(2, qty);
            insert.setDouble(3, price);
            insert.executeUpdate();
        }

        recordTransaction(stock, qty, price, "BUY");
    }

    // --- Sell stock ---
    public void sellStock(String stock, int qty, double price) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT qty, avg_price FROM holdings WHERE stock = ?");
        ps.setString(1, stock);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int oldQty = rs.getInt("qty");
            double oldAvg = rs.getDouble("avg_price");

            if (qty <= oldQty) {
                int newQty = oldQty - qty;

                if (newQty > 0) {
                    PreparedStatement update = conn.prepareStatement(
                            "UPDATE holdings SET qty = ?, avg_price = ? WHERE stock = ?");
                    update.setInt(1, newQty);
                    update.setDouble(2, oldAvg); // avg price unchanged on sell
                    update.setString(3, stock);
                    update.executeUpdate();
                } else {
                    PreparedStatement delete = conn.prepareStatement(
                            "DELETE FROM holdings WHERE stock = ?");
                    delete.setString(1, stock);
                    delete.executeUpdate();
                }

                recordTransaction(stock, qty, price, "SELL");
            }
        }
    }

    // --- Record transaction ---
    private void recordTransaction(String stock, int qty, double price, String type) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transactions (stock, qty, price, type) VALUES (?, ?, ?, ?)");
        ps.setString(1, stock);
        ps.setInt(2, qty);
        ps.setDouble(3, price);
        ps.setString(4, type);
        ps.executeUpdate();
    }

    // --- Get portfolio ---
    public List<PortfolioEntry> getPortfolio() throws SQLException {
        List<PortfolioEntry> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT stock, qty, avg_price FROM holdings");

        while (rs.next()) {
            String stock = rs.getString("stock");
            int qty = rs.getInt("qty");
            double avgPrice = rs.getDouble("avg_price");

            PortfolioEntry entry = new PortfolioEntry(stock, qty, avgPrice, 0.0, 0.0);
            list.add(entry);
        }
        return list;
    }

    // --- Get transactions ---
    public List<TransactionRecord> getTransactions() throws SQLException {
        List<TransactionRecord> list = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT stock, qty, price, type, timestamp FROM transactions ORDER BY timestamp DESC");

        while (rs.next()) {
            list.add(new TransactionRecord(
                rs.getString("timestamp"),   // String timestamp
                rs.getString("type"),        // String type
                rs.getString("stock"),       // String stock
                rs.getInt("qty"),            // int quantity
                rs.getDouble("price")        // double price
            ));
        }
        return list;
   
    }
} 