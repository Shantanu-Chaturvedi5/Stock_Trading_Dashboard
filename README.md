📈 TradeSphere – JavaFX Stock Trading Dashboard

TradeSphere is a desktop-based stock trading dashboard built using JavaFX, SQLite, and a live stock price API.
It allows users to view live prices, buy/sell stocks, track portfolio performance, and visualize price history in real time.

🧩 Key Features

🔹 1. Live Stock Prices (API-based)

-Supports NSE & global symbols

-Automatically updates on stock selection

🔹 2. Buy & Sell Stocks

-Buy or sell stocks with quantity input

-Uses real-time price during trade execution

-Automatically updates portfolio and transactions

🔹 3. Portfolio Management

-Displays:

  a. Stock Name

  b. Quantity Held

  c. Average Buy Price

  d. Current Market Price

  e. Profit / Loss (P&L)

🔹 4. Transaction History

-Logs every BUY and SELL action

-Shows:

  a. Type (BUY / SELL)

  b. Stock

  c. Quantity

  d. Price

🔹 5. Persistent Storage (SQLite)

- User portfolio and transactions are stored locally

- Data remains intact even after restarting the app

🔹 6. Price History Chart

- Line chart showing stock price movement over time

- Auto-updates when stock changes

- Timestamped data points

🔹 7. Live Stock Ticker

- Animated scrolling ticker at the top

- Displays trending stocks and percentage changes

🔹 8. Modern Dark UI

- Custom JavaFX CSS

- Dark theme with neon accents

Clean table layouts and readable charts

🖼️ Screenshots

a. Graph Depicting Recent Price History of a particular Stock

<img width="1736" height="220" alt="image" src="https://github.com/user-attachments/assets/a28fa2d5-190f-4a1d-bb61-a0978231b046" />

b. Portfolio Table for the user


<img width="1707" height="314" alt="image" src="https://github.com/user-attachments/assets/ce6e8db1-7205-4229-83ec-92028b49da03" />


c. Transaction Table showing User Activity

<img width="360" height="305" alt="image" src="https://github.com/user-attachments/assets/da701e58-81c6-4cce-b88f-4389fd28a209" />

d. Complete Dashboard Image with any Stock Selected and Transactions made

![Dashboard_Screenshot](https://github.com/user-attachments/assets/78946b7e-1238-4895-894b-3d0e9a8f2b10)

🏗️ Project Structure

Stock Trading/
│

├── TradingDashboardFX.java     # Main JavaFX application

├── DatabaseManager.java        # SQLite database handler

├── PortfolioEntry.java         # Portfolio table model

├── TransactionRecord.java      # Transaction table model

├── PriceFetcher.java           # Live price API client

├── styles.css                  # UI styling

│

├── sqlite-jdbc-3.51.0.0.jar

├── json-20140107.jar

├── portfolio.db                # SQLite database file

│

├── javafx-sdk-25.0.1/

└── README.md

⚙️ Technologies Used

-Java 21+

-JavaFX 25

-SQLite

-REST API (JSON)

-CSS (JavaFX styling)

🚀 How to Run the Project

✅ Prerequisites

-Java JDK 17 or higher

-JavaFX SDK (25.0.1 recommended)

-Internet connection (for live prices)

▶️ Compile & Run (Windows)

java --enable-native-access=ALL-UNNAMED ^
--module-path "javafx-sdk-25.0.1\lib" ^
--add-modules javafx.controls,javafx.fxml ^
-cp "bin;sqlite-jdbc-3.51.0.0.jar;json-20140107.jar" ^
TradingDashboardFX

📌 Make sure:

-styles.css is in the same package or resources folder

-JAR paths are correct

-JavaFX lib folder path is valid

🧭 How to Use the Dashboard

1️⃣ Select a Stock

-Choose a stock symbol from the dropdown

-Latest price is fetched automatically

2️⃣ Buy Stock

-Enter quantity

-Click Buy

-Portfolio and transaction table update instantly

3️⃣ Sell Stock

-Enter quantity

-Click Sell

-Holdings and P&L update accordingly

4️⃣ View Portfolio

-See live P&L based on current market price

-Holdings persist across app restarts

5️⃣ Track Price History

-Line chart updates on every stock selection

-Visual view of market movement

🧠 Architecture Overview

-UI Layer: JavaFX (Tables, Charts, Controls)

-Data Layer: SQLite via DatabaseManager

-API Layer: REST calls via PriceFetcher

-Model Layer: PortfolioEntry, TransactionRecord

🔮 Future Enhancements

-Candlestick chart support

-Multi-user login system

-Export portfolio to CSV

-Real-time WebSocket price updates

-Stop-loss & limit orders
