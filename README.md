# 💰 Finance Tracker

A simple console-based personal finance tracker built in Java. This application allows users to create an account and manage their money through deposits, withdrawals, and balance checking — all from the terminal.

---

## 📋 Features

### 🔐 Account Creation
- Create a personal account with your name and age
- Credentials are handled in accordance with **R.A. 10173** (Data Privacy Act of the Philippines)
- Confirmation step before finalizing your account

### ➕ Add Money (Deposit)
- Deposit any amount into your balance
- Validates that the amount is greater than 0
- Displays your updated balance after every deposit
- Option to make another deposit transaction without going back to the menu

### ➖ Withdraw Money
- Withdraw any amount from your existing balance
- Prevents withdrawing more than your current balance
- Prevents negative or zero withdrawal amounts
- Option to make another withdrawal transaction without going back to the menu

### 📊 View Balance
- View your current balance in a dedicated balance dashboard
- Displays the **exact date and time** the balance was checked (real-time timestamp)
- Format: `MM/DD/YYYY HH:MM:SS AM/PM`

### 🚪 Exit
- Cleanly exits the application from the main menu

---

## 🛠️ How to Run

### Option 1 — Run from an IDE
1. Clone or download this repository
2. Open the project in any Java IDE (IntelliJ, Eclipse, VS Code)
3. Run the `Main.java` file (or whichever file contains your `main` method)

### Option 2 — Run as a Standalone Console App (JAR + 4jar)

If you want to run this as a standalone `.exe`-like console application without an IDE:

1. **Export the project as a JAR file**
   - In IntelliJ: `File → Project Structure → Artifacts → Add JAR → Build`
   - In Eclipse: `File → Export → Java → Runnable JAR file`
   - Make sure to set your **main class** as the entry point

2. **Download 4jar**
   - Go to [https://www.4jar.com](https://www.4jar.com) and download the tool

3. **Wrap the JAR using 4jar**
   - Open 4jar and load your `.jar` file
   - Configure it to run as a console application
   - Export it as a `.exe` file

4. **Run it**
   - Double-click the generated `.exe`
   - A console window will open and the Finance Tracker will launch

> ✅ No Java installation required on the target machine when using 4jar wrapping.

---

## 📁 Project Structure

```
FinanceTracker/
├── FrontEnd.java        # Handles all UI display and user prompts
├── backEnd.java         # Handles all business logic (deposit, withdraw, balance)
└── Main.java            # Entry point of the application
```

---

## 🔧 Requirements

- Java 11 or higher
- Any terminal / command prompt (if running via JAR)

---

## 🚀 Future Plans

- [ ] Transaction history log
- [ ] Data persistence (save balance between sessions)
- [ ] Password-protected accounts
- [ ] Export balance report to a text file

---

## 👤 Author

Made with 💻 by **Mark**  
Built as a personal Java learning project.
