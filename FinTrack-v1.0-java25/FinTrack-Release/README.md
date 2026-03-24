# 💰 FinTrack — Personal Finance Manager

<div align="center">

![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-11-blue?style=for-the-badge&logo=java&logoColor=white)
![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-lightgrey?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge)

**A modern, fully offline personal finance tracker built with Java 25 and JavaFX.**  
Track your income, expenses, budgets, and export your data — all stored locally on your machine.

</div>

---

## 📸 Screenshots

> Dashboard · Transactions · Settings

| Login | Dashboard | Transactions |
|-------|-----------|--------------|
| ![Login Screen](docs/screenshots/login.png) | ![Dashboard](docs/screenshots/dashboard.png) | ![Transactions](docs/screenshots/transactions.png) |

---

## ✨ Features

- 🔐 **Multi-user accounts** — Register and login with SHA-256 hashed passwords
- 📊 **Dashboard** — Live stat cards for income, expenses, balance, and budget
- 📈 **6-Month Bar Chart** — Visual income vs. expenses comparison over time
- 💳 **Transaction Management** — Add, delete, filter, and search transactions
- 🗂️ **14 Expense Categories** — Food, Transport, Housing, Healthcare, and more
- 💵 **8 Income Categories** — Salary, Freelance, Business, Investment, and more
- 🎯 **Monthly Budget Tracker** — Color-coded progress bar (green → yellow → red)
- 📤 **CSV Export** — Export all transactions to a spreadsheet-ready `.csv` file
- 📥 **CSV Import** — Import transactions from an existing CSV file
- ⚙️ **Settings Panel** — Edit profile, set monthly budget, view data folder path
- ✨ **Animated Loading Screens** — Status messages and progress bars on every action
- 💾 **100% Offline** — Zero network calls, all data stored as local JSON files

---

## 🛠️ Tech Stack

| Technology | Purpose |
|---|---|
| **Java 25** | Core application language |
| **JavaFX 11** | GUI framework (system-installed) |
| **Apache Commons CSV 1.9** | CSV export and import |
| **SHA-256 (built-in)** | Password hashing |
| **Pure Java JSON parser** | Zero-dependency JSON read/write |

---

## 📁 Project Structure

```
FinTrack/
├── src/main/java/com/financetracker/
│   ├── MainApp.java                  # JavaFX Application entry point
│   ├── model/
│   │   ├── User.java                 # User data model
│   │   └── Transaction.java          # Transaction data model
│   ├── service/
│   │   ├── JsonDatabaseService.java  # Local JSON read/write + parser
│   │   ├── AuthService.java          # Register, login, password hashing
│   │   ├── TransactionService.java   # Transaction business logic
│   │   └── CsvService.java           # CSV export and import
│   └── ui/
│       ├── LoginScreen.java          # Animated login screen
│       ├── RegisterScreen.java       # Registration with progress bar
│       ├── MainWindow.java           # Sidebar navigation shell
│       ├── DashboardPanel.java       # Stats, charts, recent transactions
│       ├── TransactionsPanel.java    # Full transaction table + filters
│       ├── SettingsPanel.java        # Profile, budget, data settings
│       └── LoadingOverlay.java       # Reusable animated loading overlay
└── src/main/resources/
    └── com/financetracker/css/
        └── style.css                 # Full dark theme stylesheet
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17 or later** (optimised for Java 25)  
  → Download: [https://adoptium.net](https://adoptium.net)
- **JavaFX SDK** (for running from source in Eclipse/IntelliJ)  
  → Download: [https://gluonhq.com/products/javafx/](https://gluonhq.com/products/javafx/)

---

### ▶️ Option A — Run the prebuilt JAR (Easiest)

1. Download the latest release ZIP from the [Releases](../../releases) page
2. Unzip it anywhere
3. Open a terminal / command prompt in the folder
4. Run:

```bash
java -jar finance-tracker.jar
```

**Windows shortcut:** double-click `run-windows.bat`  
**Mac/Linux shortcut:** run `./run.sh`

---

### 🔧 Option B — Build from Source (Maven)

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/FinTrack.git
cd FinTrack

# 2. Build the fat JAR
mvn package -DskipTests

# 3. Run it
java -jar target/finance-tracker-1.0.0.jar
```

---

### 🔧 Option C — Open in Eclipse IDE

1. **File → New → Java Project** — name it `FinTrack`, select JDK 25, uncheck `module-info.java`
2. Copy `src/main/java/com` and `src/main/resources/com` into your Eclipse project's `src/` folder
3. Right-click project → **Build Path → Configure Build Path → Libraries → Add External JARs**  
   → Add all `.jar` files from your `javafx-sdk/lib/` folder + `commons-csv.jar`
4. Right-click `MainApp.java` → **Run As → Run Configurations → Arguments tab**  
   → Add to **VM Arguments**:

```
--module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml,javafx.graphics
```

5. Click **Run** ✅

---

## 💾 Data Storage

All data is saved **locally on your machine** — no cloud, no internet, no telemetry.

| OS | Data Location |
|---|---|
| Windows | `C:\Users\<YourName>\FinanceTrackerData\` |
| macOS / Linux | `~/FinanceTrackerData/` |

**Files created:**

```
FinanceTrackerData/
├── users.json           # All registered user accounts
└── transactions.json    # All transaction records
```

You can back up your data at any time by copying that folder.

---

## 📤 CSV Format

When exporting, the CSV file follows this structure:

```csv
ID,Type,Amount,Category,Description,Date,Notes
abc-123,EXPENSE,150.00,Food & Dining,Grocery shopping,2026-03-23,Weekly groceries
def-456,INCOME,3000.00,Salary,March salary,2026-03-01,
```

You can also **import** a CSV in this same format to bulk-load transactions.

---

## 🗺️ Roadmap

- [ ] Recurring transactions (weekly/monthly)
- [ ] Multiple currencies with live conversion
- [ ] PDF report generation
- [ ] Dark / Light theme toggle
- [ ] Pie chart for expense breakdown
- [ ] Data backup & restore via ZIP
- [ ] Windows `.exe` installer via jpackage

---

## 🤝 Contributing

Contributions are welcome! Here's how:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m "Add your feature"`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a **Pull Request**

Please make sure your code follows the existing style and compiles cleanly with Java 25.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Mark** — Built with ☕ and JavaFX

> If you found this useful, consider giving it a ⭐ on GitHub!

---

<div align="center">
  <sub>Built with Java 25 · JavaFX · Apache Commons CSV · Zero external dependencies for core logic</sub>
</div>
