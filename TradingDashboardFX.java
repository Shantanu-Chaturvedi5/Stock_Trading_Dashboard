import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class TradingDashboardFX extends Application {

    private static final String BASE_URL =
            "https://military-jobye-haiqstudios-14f59639.koyeb.app";

    private ComboBox<String> stockCombo;
    private Label selectedStockLabel;
    private TextField qtyField;
    private Button buyButton, sellButton;

    private TableView<TransactionRecord> transactionTable;
    private ObservableList<TransactionRecord> transactions =
            FXCollections.observableArrayList();

    private TableView<PortfolioEntry> portfolioTable;
    private ObservableList<PortfolioEntry> portfolioData =
            FXCollections.observableArrayList();

    private LineChart<String, Number> priceChart;
    private XYChart.Series<String, Number> priceSeries;

    private HBox tickerContent;
    private Timeline tickerTimeline;

    @Override
    public void start(Stage stage) {

        /* ================= NAVBAR ================= */
        Label brandLabel = new Label("TradeSphere");
        brandLabel.setId("brandLabel");
        brandLabel.setStyle(
                "-fx-font-family: 'Segoe UI Black', 'Montserrat', 'Poppins', sans-serif;" +
                "-fx-font-size: 30px;" +
                "-fx-text-fill: #00e5ff;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,229,255,0.45), 8, 0.4, 0, 0);"
        );

        HBox navbar = new HBox(brandLabel);
        navbar.setId("navbar");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setMinHeight(72);
        navbar.setPrefHeight(72);
        navbar.setMaxHeight(72);

        navbar.setMaxWidth(Double.MAX_VALUE);
        navbar.setStyle(
                "-fx-background-color: linear-gradient(to right, #1a0033, #3b0066);" +
                "-fx-padding: 0 20 0 20;" +
                "-fx-border-color: rgba(255,255,255,0.15);" +
                "-fx-border-width: 0 0 1 0;"
        );

        /* ================= TICKER ================= */
        tickerContent = new HBox(50);
        tickerContent.setAlignment(Pos.CENTER_LEFT);

        String[] tickerData = {
                "ICICI -0.9%",
                "RELIANCE +2.3%",
                "TCS -1.5%",
                "INFY +0.8%",
                "HDFC +1.2%"
        };

        for (int r = 0; r < 2; r++) {
            for (String s : tickerData) {
                Label lbl = new Label(s);
                lbl.setStyle("-fx-text-fill: cyan; -fx-font-weight: bold;");
                tickerContent.getChildren().add(lbl);
            }
        }

        StackPane tickerViewport = new StackPane(tickerContent);
        tickerViewport.setPrefHeight(34);
        tickerViewport.setAlignment(Pos.CENTER_LEFT);

        startTickerAnimation();

        /* ================= CONTROLS ================= */
        stockCombo = new ComboBox<>();
        qtyField = new TextField("1");
        selectedStockLabel = new Label("Selected Stock: ₹0.0");

        buyButton = new Button("Buy");
        buyButton.setId("buyButton");
        sellButton = new Button("Sell");
        sellButton.setId("sellButton");

        HBox controls = new HBox(
                10, stockCombo, new Label("Qty:"), qtyField, buyButton, sellButton
        );
        controls.setAlignment(Pos.CENTER);

        /* ================= TABLES & CHART ================= */
        setupTransactionTable();
        setupPortfolioTable();
        setupPriceChart();
        Label transactionsLabel = new Label("Transactions");
        transactionsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label portfolioLabel = new Label("Portfolio");
        portfolioLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        Label priceHistoryLabel = new Label("Price History");
        priceHistoryLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");


        VBox root = new VBox(
                navbar,
                tickerViewport,
                selectedStockLabel,
                controls,
                transactionsLabel,
                transactionTable,
                portfolioLabel,
                portfolioTable,
                priceHistoryLabel,
                priceChart
        );

        root.setPadding(new Insets(0));
        root.setSpacing(12);

        Scene scene = new Scene(root, 1100, 780);
        scene.getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm()
        );

        stage.setTitle("Trading Dashboard");
        stage.setScene(scene);
        stage.show();

        new Thread(this::populateStockComboBox).start();
        setupListeners();
    }

    /* ================= TICKER ================= */
    private void startTickerAnimation() {
        tickerTimeline = new Timeline(
                new KeyFrame(Duration.millis(16), e -> {
                    tickerContent.setTranslateX(
                            tickerContent.getTranslateX() - 1.1
                    );
                    if (Math.abs(tickerContent.getTranslateX())
                            > tickerContent.getWidth() / 2) {
                        tickerContent.setTranslateX(0);
                    }
                })
        );
        tickerTimeline.setCycleCount(Timeline.INDEFINITE);
        tickerTimeline.play();
    }

    /* ================= DATA ================= */
    private void populateStockComboBox() {
        try {
            HttpURLConnection conn =
                    (HttpURLConnection) new URL(BASE_URL + "/symbols")
                            .openConnection();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(conn.getInputStream())
                    );

            JSONArray arr =
                    new JSONObject(
                            reader.lines().collect(Collectors.joining())
                    ).getJSONArray("symbols");

            ObservableList<String> stocks =
                    FXCollections.observableArrayList();

            for (int i = 0; i < arr.length(); i++) {
                stocks.add(arr.getJSONObject(i).getString("nse_ticker"));
            }

            Platform.runLater(() -> {
                stockCombo.setItems(stocks);
                stockCombo.getSelectionModel().selectFirst();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        stockCombo.setOnAction(e ->
                new Thread(() -> {
                    String stock = stockCombo.getValue();
                    double price = PriceFetcher.getPrice(stock);

                    Platform.runLater(() -> {
                        selectedStockLabel.setText("Selected Stock: ₹" + price);
                        priceSeries.getData().add(
                                new XYChart.Data<>(
                                        LocalDateTime.now()
                                                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                                        price
                                )
                        );
                    });
                }).start()
        );

        buyButton.setOnAction(e -> new Thread(() -> handleTrade("BUY")).start());
        sellButton.setOnAction(e -> new Thread(() -> handleTrade("SELL")).start());
    }

    /* ================= CORE FIX ================= */
    private void handleTrade(String type) {
        String stock = stockCombo.getValue();
        int qty = Integer.parseInt(qtyField.getText());
        double price = PriceFetcher.getPrice(stock);

        Platform.runLater(() -> {

            transactions.add(
                    new TransactionRecord(type, stock, qty, price)
            );

            PortfolioEntry existing = portfolioData.stream()
                    .filter(p -> p.getStock().equals(stock))
                    .findFirst()
                    .orElse(null);

            if (existing == null && type.equals("BUY")) {
                portfolioData.add(
                        new PortfolioEntry(stock, qty, price, price, 0)
                );
            } else if (existing != null) {

                portfolioData.remove(existing);

                int newQty = type.equals("BUY")
                        ? existing.getQty() + qty
                        : existing.getQty() - qty;

                if (newQty > 0) {
                    double newAvg = type.equals("BUY")
                            ? ((existing.getAvgPrice() * existing.getQty())
                            + (price * qty)) / newQty
                            : existing.getAvgPrice();

                    double pnl = (price - newAvg) * newQty;

                    portfolioData.add(
                            new PortfolioEntry(stock, newQty, newAvg, price, pnl)
                    );
                }
            }
        });
    }

    /* ================= TABLES ================= */
    private void setupTransactionTable() {
        transactionTable = new TableView<>(transactions);

        transactionTable.getColumns().addAll(
                col("Type", "type"),
                col("Stock", "stock"),
                col("Qty", "quantity"),
                col("Price", "price")
        );
    }

    private void setupPortfolioTable() {
        portfolioTable = new TableView<>(portfolioData);

        portfolioTable.getColumns().addAll(
                col("Stock", "stock"),
                col("Qty", "qty"),
                col("Avg Price", "avgPrice"),
                col("Mkt Price", "mktPrice"),
                col("P&L", "pnl")
        );
    }

    private void setupPriceChart() {
        CategoryAxis x = new CategoryAxis();
        NumberAxis y = new NumberAxis();

        x.setLabel("Date & Time");
        y.setLabel("Price");

        priceChart = new LineChart<>(x, y);
        priceChart.setPrefHeight(200);
        priceChart.setAnimated(false);
        priceChart.setCreateSymbols(true);

        priceSeries = new XYChart.Series<>();
        priceChart.getData().add(priceSeries);
    }

    private <T> TableColumn<T, ?> col(String name, String prop) {
        TableColumn<T, Object> c = new TableColumn<>(name);
        c.setCellValueFactory(new PropertyValueFactory<>(prop));
        return c;
    }

    public static void main(String[] args) {
        launch(args);
    }
}