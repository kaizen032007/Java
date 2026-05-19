package com.sora.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.sora.model.*;
import com.sora.service.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 * Dashboard class acts as the main user interface container for the dashboard pages.
 * 
 * Flow / Connections:
 * - Instantiated by LoginView or MainApp when a user logs in successfully.
 * - Connects to AccountServices, TransactionServices, GoalsServices, BudgetServices,
 *   SubscriptionServices, and UsersServices to fetch and modify data.
 * - Uses the getLayoutDashboard() method to build the base structure containing the
 *   sidebar on the left and the main scrollable content area on the right.
 * - Replaces children in the contentArea dynamically when user selects sidebar tabs:
 *   - Overview (buildMainContent)
 *   - Transactions (buildTransactionsPage)
 *   - Accounts (buildAccountsPage)
 *   - Goals (buildGoalsPage)
 *   - Subscriptions (buildSubscriptionsPage)
 *   - Budgets (buildBudgetPage)
 *   - Settings (buildSettingsPage)
 *   - Sora Agent (buildAgentPage)
 */
public class Dashboard {

    // Active session details
    private User currentUser;

    // Services representing the controller layer interacting with CSV persistence
    private AccountServices accountServices;
    private TransactionServices transactionServices;
    private GoalsServices goalsServices;
    private BudgetServices budgetServices;
    private SubscriptionServices subscriptionServices;
    private UsersServices usersServices;

    // Layout components
    private VBox contentArea;
    private VBox sidebar;
    private HBox root;

    /**
     * Constructs a dashboard container for the given User.
     * Initializes workspace-specific service context instances.
     */
    public Dashboard(User currentUser, AccountServices accountServices,
            TransactionServices transactionServices) {
        this.currentUser = currentUser;
        this.accountServices = accountServices;
        this.transactionServices = new TransactionServices(currentUser.getUserId());
        this.goalsServices = new GoalsServices(currentUser.getUserId());
        this.budgetServices = new BudgetServices(currentUser.getUserId());
        this.subscriptionServices = new SubscriptionServices(currentUser.getUserId());
        this.usersServices = new UsersServices();
    }

    public HBox getLayoutDashboard() {
        root = new HBox();
        root.setPrefSize(1200, 750);

        contentArea = new VBox(16);
        contentArea.getStyleClass().add("main-content");
        contentArea.setPadding(new Insets(30));
        HBox.setHgrow(contentArea, Priority.ALWAYS);

        ScrollPane mainScroll = new ScrollPane(contentArea);
        mainScroll.setFitToWidth(true);
        mainScroll.getStyleClass().add("main-scroll");
        HBox.setHgrow(mainScroll, Priority.ALWAYS);

        sidebar = buildSidebar();
        root.getChildren().addAll(sidebar, mainScroll);

        showOverview();
        return root;
    }

    private void showOverview() {
        contentArea.getChildren().setAll(buildMainContent());
    }

    private void showAccounts() {
        contentArea.getChildren().setAll(buildAccountsPage());
    }

    private void showGoals() {
        contentArea.getChildren().setAll(buildGoalsPage());
    }

    private void showTransactions() {
        contentArea.getChildren().setAll(buildTransactionsPage());
    }

    private void showSubscriptions() {
        contentArea.getChildren().setAll(buildSubscriptionsPage());
    }

    private void showAgent() {
        contentArea.getChildren().setAll(buildAgentPage());
    }

    private void showBudgets() {
        contentArea.getChildren().setAll(buildBudgetPage());
    }

    private void showSettings() {
        contentArea.getChildren().setAll(buildSettingsPage());
    }

    // ── SIDEBAR ────────────────────────────────────────────────
    private VBox buildSidebar() {
        VBox sb = new VBox(6);
        sb.getStyleClass().add("sidebar");
        sb.setMinWidth(220);
        sb.setPadding(new Insets(30, 20, 20, 20));

        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(logoBox, new Insets(0, 0, 24, 0));
        ImageView logoImg = buildIcon("Resources/icons/logo.jpg", 40, 40, false);
        Label logoLabel = new Label("SORA");
        logoLabel.getStyleClass().add("sidebar-logo-text");
        logoBox.getChildren().addAll(logoImg, logoLabel);

        Label overviewLabel = new Label("Overview");
        overviewLabel.getStyleClass().add("sidebar-section-label");
        VBox.setMargin(overviewLabel, new Insets(10, 0, 4, 0));

        Button dashboardBtn = buildSidebarButton("Resources/icons/dashboard.png", "Dashboard");
        Button transactionsBtn = buildSidebarButton("Resources/icons/transactions.png", "Transactions");
        Button accountsBtn = buildSidebarButton("Resources/icons/accounts.png", "Accounts");

        Label tracerLabel = new Label("App Tracer");
        tracerLabel.getStyleClass().add("sidebar-section-label");
        VBox.setMargin(tracerLabel, new Insets(14, 0, 4, 0));

        Button goalsBtn = buildSidebarButton("Resources/icons/goals.png", "Goals");
        Button subscriptionsBtn = buildSidebarButton("Resources/icons/subscriptions.png", "Subscriptions");
        Button agentBtn = buildSidebarButton("Resources/icons/dashboard.png", "Sora Agent");

        Label systemLabel = new Label("System");
        systemLabel.getStyleClass().add("sidebar-section-label");
        VBox.setMargin(systemLabel, new Insets(14, 0, 4, 0));

        Button settingsBtn = buildSidebarButton("Resources/icons/settings.png", "Settings");

        // ── NAV ACTIONS ────────────────────────────────────────
        dashboardBtn.setOnAction(e -> showOverview());
        transactionsBtn.setOnAction(e -> showTransactions());
        accountsBtn.setOnAction(e -> showAccounts());
        goalsBtn.setOnAction(e -> showGoals());
        subscriptionsBtn.setOnAction(e -> showSubscriptions());
        agentBtn.setOnAction(e -> showAgent());
        settingsBtn.setOnAction(e -> showSettings());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("Logout");
        logoutBtn.getStyleClass().add("logout-btn");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setOnAction(e -> {
            new com.sora.service.UsersServices().clearRememberedUser();
            LoginView loginView = new LoginView();
            Scene loginScene = new Scene(loginView.getLayout(), 1200, 750);
            java.io.File css = new java.io.File("Resources/loginStyle.css");
            if (css.exists())
                loginScene.getStylesheets().add(css.toURI().toString());
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(loginScene);
        });

        sb.getChildren().addAll(
                logoBox,
                overviewLabel, dashboardBtn, transactionsBtn, accountsBtn,
                tracerLabel, goalsBtn, subscriptionsBtn, agentBtn,
                systemLabel, settingsBtn,
                spacer, logoutBtn);
        return sb;
    }

    // ── MAIN CONTENT ───────────────────────────────────────────
    private VBox buildMainContent() {
        VBox main = new VBox(16);
        main.getStyleClass().add("main-content");
        main.setPadding(new Insets(30, 30, 30, 30));

        // HELLO HEADER — ADDED real user name
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);

        HBox greetingBox = new HBox(8);
        greetingBox.setAlignment(Pos.BASELINE_LEFT);

        Label helloLabel = new Label("Hello,");
        helloLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: #1e293b;");

        String firstName = currentUser.getFirstName();
        if (firstName != null && !firstName.isEmpty()) {
            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        }

        Label userLabel = new Label(firstName);
        userLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: 900; -fx-text-fill: #1e293b;");

        greetingBox.getChildren().addAll(helloLabel, userLabel);

        headerRow.getChildren().add(greetingBox);

        // BALANCE BAR — ADDED real totals
        double totalBalance = accountServices.getTotalBalance();
        double totalIncome = transactionServices.getTotalIncome();
        double totalSpent = transactionServices.getTotalSpent();
        HBox balanceBar = buildBalanceBar(totalBalance, totalIncome, totalSpent);

        // BUDGET TRACKER (Personal Wallet)
        HBox budgetTracker = buildBudgetTracker();

        // MIDDLE ROW
        HBox middleRow = new HBox(24); // Increased spacing
        VBox accountsCard = buildAccountsCard();
        VBox savingsCard = buildSavingsCard();
        VBox spendingCard = buildSpendingCard();

        // Make all cards expand evenly
        HBox.setHgrow(accountsCard, Priority.ALWAYS);
        HBox.setHgrow(savingsCard, Priority.ALWAYS);
        HBox.setHgrow(spendingCard, Priority.ALWAYS);

        middleRow.getChildren().addAll(accountsCard, savingsCard, spendingCard);

        // BUDGET CARD
        VBox budgetCard = buildBudgetCard();

        main.getChildren().addAll(headerRow, balanceBar, budgetTracker, middleRow, budgetCard);
        return main;
    }

    // ── BALANCE BAR — UPDATED to accept real data ──────────────
    private HBox buildBalanceBar(double totalBalance, double totalIncome, double totalSpent) {
        HBox bar = new HBox(0);
        bar.getStyleClass().add("balance-bar");
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(24, 32, 24, 32)); // Increased padding

        Label totalLabel = new Label("₱ " + String.format("%.2f", totalBalance));
        totalLabel.getStyleClass().add("balance-total");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox statsBox = new HBox(0); // Spacing managed by balance-stat padding
        statsBox.setAlignment(Pos.CENTER_RIGHT);
        statsBox.getChildren().addAll(
                buildBalanceStat("₱ " + String.format("%.2f", totalIncome), "Total Income"),
                buildBalanceStat("₱ " + String.format("%.2f", totalSpent), "Total Spent"),
                buildBalanceStat("₱ " + String.format("%.2f", totalBalance - totalSpent), "Balance"));

        bar.getChildren().addAll(totalLabel, spacer, statsBox);
        return bar;
    }

    private VBox buildBalanceStat(String value, String label) {
        VBox box = new VBox(3);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("balance-stat");

        Label valLabel = new Label(value);
        valLabel.getStyleClass().add("balance-stat-value");

        Label lblLabel = new Label(label);
        lblLabel.getStyleClass().add("balance-stat-label");

        box.getChildren().addAll(valLabel, lblLabel);
        return box;
    }

    // ── ACCOUNTS CARD — UPDATED to use real accounts ───────────
    private VBox buildAccountsCard() {
        VBox card = new VBox(10);
        card.getStyleClass().add("dash-card");
        card.setMinWidth(300); // Set a sensible minimum width

        Label title = new Label("Accounts");
        title.getStyleClass().add("card-title");

        card.getChildren().add(title);

        // ADDED — loop through real accounts
        for (Account account : accountServices.getAllAccounts()) {
            if (account.getAccountName().equalsIgnoreCase("Personal Wallet"))
                continue;
            String iconPath = "Resources/icons/bpi.png";
            if (account.getAccountName().equalsIgnoreCase("GCASH")) {
                iconPath = "Resources/icons/gcash.png";
            }
            HBox accountRow = buildAccountRow(iconPath, account.getAccountName(),
                    account.getAccountType(),
                    "₱ " + String.format("%.2f", account.getBalance()), false);
            card.getChildren().add(accountRow);
        }

        Hyperlink manageLink = new Hyperlink("Manage Accounts →");
        manageLink.getStyleClass().add("card-link");
        card.getChildren().add(manageLink);

        return card;
    }

    private HBox buildAccountRow(String iconPath, String name, String type, String amount, boolean whiteIcon) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("account-row");
        row.setPadding(new Insets(8, 10, 8, 10));

        ImageView icon = buildIcon(iconPath, 28, 28, whiteIcon);

        VBox info = new VBox(2);
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("account-name");
        Label typeLabel = new Label(type);
        typeLabel.getStyleClass().add("account-type");
        info.getChildren().addAll(nameLabel, typeLabel);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label amountLabel = new Label(amount);
        amountLabel.getStyleClass().add("account-amount");

        row.getChildren().addAll(icon, info, amountLabel);
        return row;
    }

    // ── SAVINGS GOALS CARD ─────────────────────────────────────
    private VBox buildSavingsCard() {
        VBox card = new VBox(10);
        card.getStyleClass().add("dash-card");
        card.setMinWidth(300); // Set a sensible minimum width

        Label title = new Label("Savings Goals");
        title.getStyleClass().add("card-title");
        card.getChildren().add(title);

        for (Goal goal : goalsServices.getAllGoals()) {
            double progress = goal.getProgress();
            HBox goalRow = buildGoalRow(goal.getGoalName(), progress,
                    "₱ " + String.format("%.2f", goal.getSavedAmount())
                            + " / ₱ " + String.format("%.2f", goal.getTargetAmount()));
            card.getChildren().add(goalRow);
        }
        if (goalsServices.getAllGoals().isEmpty()) {
            Label empty = new Label("No goals yet.");
            empty.getStyleClass().add("account-type");
            card.getChildren().add(empty);
        }

        Hyperlink seeMore = new Hyperlink("Manage Goals →");
        seeMore.getStyleClass().add("card-link");
        seeMore.setOnAction(e -> showGoals());
        card.getChildren().add(seeMore);
        return card;
    }

    private HBox buildGoalRow(String name, double progress, String amountText) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("account-row");
        row.setPadding(new Insets(8, 10, 8, 10));

        VBox info = new VBox(4);
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("account-name");
        ProgressBar bar = new ProgressBar(progress);
        bar.getStyleClass().add("goal-progress");
        bar.setMaxWidth(Double.MAX_VALUE);
        info.getChildren().addAll(nameLabel, bar);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label amountLabel = new Label(amountText);
        amountLabel.getStyleClass().add("account-type");
        row.getChildren().addAll(info, amountLabel);
        return row;
    }

    // ── SPENDING BREAKDOWN CARD ────────────────────────────────
    private VBox buildSpendingCard() {
        VBox card = new VBox(10);
        card.getStyleClass().add("dash-card");
        HBox.setHgrow(card, Priority.ALWAYS);

        Label title = new Label("Spending Breakdown");
        title.getStyleClass().add("card-title");

        double totalSpent = transactionServices.getTotalSpent();

        PieChart chart = new PieChart();
        chart.setLegendVisible(false);
        chart.setLabelsVisible(false);
        chart.setPrefHeight(200);
        chart.setPrefWidth(200);
        chart.getStyleClass().add("spending-chart");
        chart.setStartAngle(90);

        VBox breakdownList = new VBox(8);

        Map<String, Double> categorySpending = new HashMap<>();
        for (Transaction t : transactionServices.getAllTransactions()) {
            if (t.getType().equalsIgnoreCase("Expense")) {
                categorySpending.put(t.getCategory(), categorySpending.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
            }
        }

        if (categorySpending.isEmpty()) {
            chart.getData().add(new PieChart.Data("No Data", 1));
        } else {
            for (Map.Entry<String, Double> entry : categorySpending.entrySet()) {
                double spent = entry.getValue();
                String cat = entry.getKey();
                chart.getData().add(new PieChart.Data(cat, spent == 0 ? 1 : spent));

                String pct = totalSpent == 0 ? "0%" : String.format("%.0f%%", (spent / totalSpent) * 100);
                breakdownList.getChildren().add(buildBreakdownRow(cat, "₱ " + String.format("%.2f", spent) + " / " + pct));
            }
        }

        // CENTER LABEL OVERLAY
        StackPane chartStack = new StackPane();
        VBox centerLabel = new VBox(2);
        centerLabel.setAlignment(Pos.CENTER);
        Label centerAmount = new Label("₱ " + String.format("%.2f", totalSpent));
        centerAmount.getStyleClass().add("balance-stat-value");
        Label centerMonth = new Label("This month");
        centerMonth.getStyleClass().add("account-type");
        centerLabel.getChildren().addAll(centerAmount, centerMonth);
        chartStack.getChildren().addAll(chart, centerLabel);

        card.getChildren().addAll(title, chartStack, breakdownList);
        return card;
    }

    private HBox buildBreakdownRow(String label, String value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("breakdown-row");
        row.setPadding(new Insets(8, 14, 8, 14));

        Label nameLabel = new Label(label);
        nameLabel.getStyleClass().add("breakdown-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Label valueLabel = new Label(value);
        valueLabel.getStyleClass().add("breakdown-value");

        row.getChildren().addAll(nameLabel, valueLabel);
        return row;
    }

    // ── BUDGET LIMITS CARD ─────────────────────────────────────
    private VBox buildBudgetCard() {
        VBox card = new VBox(10);
        card.getStyleClass().add("dash-card");
        Label title = new Label("Budget Limits");
        title.getStyleClass().add("card-title");

        HBox budgetRow = new HBox(10);
        
        List<Budget> budgets = budgetServices.getAllBudgets();
        if (budgets.isEmpty()) {
            Label empty = new Label("No budgets set.");
            empty.getStyleClass().add("account-type");
            budgetRow.getChildren().add(empty);
        } else {
            for (int i = 0; i < Math.min(budgets.size(), 3); i++) {
                Budget b = budgets.get(i);
                double spent = transactionServices.getSpendingByCategory(b.getBudgetName());
                budgetRow.getChildren().add(
                        buildBudgetRow("Resources/icons/misc.png", b.getBudgetName(),
                                "₱ " + String.format("%.2f", spent) + " / ₱ " + String.format("%.2f", b.getMonthlyLimit())));
            }
        }

        Hyperlink editLink = new Hyperlink("Edit Budgets →");
        editLink.getStyleClass().add("card-link");
        editLink.setOnAction(e -> showBudgets());
        card.getChildren().addAll(title, budgetRow, editLink);
        return card;
    }

    private HBox buildBudgetRow(String iconPath, String name, String amount) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("account-row");
        row.setPadding(new Insets(8, 10, 8, 10));
        HBox.setHgrow(row, Priority.ALWAYS);

        ImageView icon = buildIcon(iconPath, 22, 22, true);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("account-name");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Label amountLabel = new Label(amount);
        amountLabel.getStyleClass().add("account-type");

        row.getChildren().addAll(icon, nameLabel, amountLabel);
        return row;
    }

    // ── HELPER: BUILD ICON ─────────────────────────────────────
    private ImageView buildIcon(String path, double w, double h, boolean whiteBlend) {
        java.io.File file = new java.io.File(path);
        ImageView imageView;
        if (file.exists()) {
            imageView = new ImageView(new Image(file.toURI().toString()));
        } else {
            System.out.println("Icon not found: " + file.getAbsolutePath());
            imageView = new ImageView();
        }
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        imageView.setPreserveRatio(true);
        if (whiteBlend) {
            imageView.getStyleClass().add("white-icon");
            javafx.scene.effect.ColorAdjust colorAdjust = new javafx.scene.effect.ColorAdjust();
            colorAdjust.setBrightness(1.0);
            imageView.setEffect(colorAdjust);
        }
        return imageView;
    }

    // ── HELPER: BUILD SIDEBAR BUTTON ───────────────────────────
    private Button buildSidebarButton(String iconPath, String label) {
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        ImageView icon = buildIcon(iconPath, 18, 18, true);

        Label text = new Label(label);
        text.getStyleClass().add("sidebar-btn-text");

        content.getChildren().addAll(icon, text);

        Button btn = new Button();
        btn.setGraphic(content);
        btn.getStyleClass().add("sidebar-btn");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);

        return btn;
    }

    // ══════════════════════════════════════════════════════════
    // ── ACCOUNTS PAGE ─────────────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private VBox buildAccountsPage() {
        VBox page = new VBox(16);
        Label heading = new Label("Accounts");
        heading.setStyle("-fx-font-size:26px;-fx-font-weight:900;-fx-text-fill:#1e293b;");

        Button addBtn = new Button("+ Add Account");
        addBtn.getStyleClass().add("login-btn");
        addBtn.setOnAction(e -> openAddAccountDialog());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(20, heading, spacer, addBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox list = new VBox(10);
        list.getStyleClass().add("dash-card");
        if (accountServices.getAllAccounts().isEmpty()) {
            Label empty = new Label("No accounts yet. Click '+ Add Account' to start.");
            empty.getStyleClass().add("account-type");
            list.getChildren().add(empty);
        }
        for (Account acc : accountServices.getAllAccounts()) {
            if (acc.getAccountName().equals("Personal Wallet"))
                continue; // Separated from bank accounts
            String ico = acc.getAccountName().equalsIgnoreCase("GCASH")
                    ? "Resources/icons/gcash.png"
                    : "Resources/icons/bpi.png";
            HBox row = accountRow(ico, acc.getAccountName(),
                    acc.getAccountType(), "₱ " + String.format("%.2f", acc.getBalance()));

            row.setSpacing(10);
            Button depBtn = new Button("Deposit");
            depBtn.getStyleClass().add("login-btn");
            depBtn.setOnAction(e -> openAccountActionDialog(acc, "Deposit"));

            Button witBtn = new Button("Withdraw");
            witBtn.getStyleClass().add("login-btn");
            witBtn.setStyle("-fx-background-color: #ef4444;"); // Red for withdraw
            witBtn.setOnAction(e -> openAccountActionDialog(acc, "Withdraw"));

            Button delBtn = new Button("X");
            delBtn.getStyleClass().add("login-btn");
            delBtn.setStyle("-fx-background-color: #ef4444; -fx-padding: 5 10; -fx-font-weight: bold;");
            delBtn.setOnAction(e -> confirmDeleteAccount(acc));

            row.getChildren().addAll(depBtn, witBtn, delBtn);
            list.getChildren().add(row);
        }
        page.getChildren().addAll(header, list);
        return page;
    }

    private void openAddAccountDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Account");
        dlg.setHeaderText("Enter account details");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));

        ComboBox<String> nameBox = new ComboBox<>();
        nameBox.setEditable(true); // Allow custom names if needed
        nameBox.getItems().addAll(
                "GCash", "Maya", "PayPal", "GrabPay", "ShopeePay",
                "BPI", "BDO", "Metrobank", "Landbank", "UnionBank",
                "Security Bank", "PNB", "RCBC", "EastWest", "Chinabank",
                "CIMB", "Tonik", "SeaBank", "Gotyme");
        nameBox.setValue("GCash");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Savings Account", "Checking Account", "Credit Card", "E-Wallet");
        typeBox.setValue("Savings Account");
        TextField balF = new TextField("0.00");
        TextField numF = new TextField();
        numF.setPromptText("Optional");

        g.addRow(0, new Label("Account Name:"), nameBox);
        g.addRow(1, new Label("Account Type:"), typeBox);
        g.addRow(2, new Label("Initial Balance (₱):"), balF);
        g.addRow(3, new Label("Account Number:"), numF);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            String selectedName = nameBox.getEditor().getText(); // Get text in case they typed something custom
            if (btn == ButtonType.OK && selectedName != null && !selectedName.isBlank()) {
                double bal = 0;
                try {
                    bal = Double.parseDouble(balF.getText().trim());
                } catch (Exception ex) {
                }
                accountServices.addAccount(new Account(selectedName.trim(),
                        typeBox.getValue(), bal, numF.getText().trim()));
                showAccounts();
            }
        });
    }

    private void openAccountActionDialog(Account acc, String action) {
        TextInputDialog dlg = new TextInputDialog("0.00");
        dlg.setTitle(action);
        dlg.setHeaderText(action + " " + (action.equals("Deposit") ? "to" : "from") + " " + acc.getAccountName());
        dlg.setContentText("Amount (₱):");

        dlg.showAndWait().ifPresent(val -> {
            try {
                double amount = Double.parseDouble(val.trim());
                if (amount > 0) {
                    if (action.equals("Withdraw")) {
                        if (acc.getBalance() >= amount) {
                            acc.setBalance(acc.getBalance() - amount);
                            transactionServices.addTransaction(new Transaction(
                                    "TXN-" + System.currentTimeMillis(),
                                    "Withdrawal from " + acc.getAccountName(),
                                    "Withdrawal", amount, LocalDate.now().toString(), "Expense",
                                    acc.getAccountName()));
                        }
                    } else {
                        acc.setBalance(acc.getBalance() + amount);
                        transactionServices.addTransaction(new Transaction(
                                "TXN-" + System.currentTimeMillis(),
                                "Deposit to " + acc.getAccountName(),
                                "Income", amount, LocalDate.now().toString(), "Income", acc.getAccountName()));
                    }
                    accountServices.saveToFile();
                    if (acc.getAccountName().equals("Personal Wallet")) {
                        showOverview();
                    } else {
                        showAccounts();
                    }
                }
            } catch (Exception ex) {
            }
        });
    }

    // ══════════════════════════════════════════════════════════
    // ── GOALS PAGE ────────────────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private VBox buildGoalsPage() {
        VBox page = new VBox(16);
        Label heading = new Label("Savings Goals");
        heading.setStyle("-fx-font-size:26px;-fx-font-weight:900;-fx-text-fill:#1e293b;");

        Button addBtn = new Button("+ Add Goal");
        addBtn.getStyleClass().add("login-btn");
        addBtn.setOnAction(e -> openAddGoalDialog());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(20, heading, spacer, addBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox list = new VBox(10);
        list.getStyleClass().add("dash-card");
        if (goalsServices.getAllGoals().isEmpty()) {
            Label empty = new Label("No goals yet. Click '+ Add Goal' to start.");
            empty.getStyleClass().add("account-type");
            list.getChildren().add(empty);
        }
        for (Goal g : goalsServices.getAllGoals()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("account-row");
            row.setPadding(new Insets(10));

            VBox info = new VBox(4);
            Label nl = new Label(g.getGoalName());
            nl.getStyleClass().add("account-name");
            Label al = new Label("₱ " + String.format("%.2f", g.getSavedAmount())
                    + " / ₱ " + String.format("%.2f", g.getTargetAmount()));
            al.getStyleClass().add("account-type");
            ProgressBar pb = new ProgressBar(g.getProgress());
            pb.getStyleClass().add("goal-progress");
            pb.setPrefWidth(220);
            info.getChildren().addAll(nl, al, pb);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button depBtn = new Button("Deposit");
            depBtn.getStyleClass().add("login-btn");
            depBtn.setOnAction(e -> openDepositDialog(g));

            Button witBtn = new Button("Withdraw");
            witBtn.getStyleClass().add("login-btn");
            witBtn.setStyle("-fx-background-color: #ef4444;");
            witBtn.setOnAction(e -> openWithdrawDialog(g));

            Button delBtn = new Button("X");
            delBtn.getStyleClass().add("login-btn");
            delBtn.setStyle("-fx-background-color: #ef4444; -fx-padding: 5 10; -fx-font-weight: bold;");
            delBtn.setOnAction(e -> confirmDeleteGoal(g));

            row.getChildren().addAll(info, depBtn, witBtn, delBtn);
            list.getChildren().add(row);
        }
        page.getChildren().addAll(header, list);
        return page;
    }

    private void openAddGoalDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("New Savings Goal");
        dlg.setHeaderText("Create a savings goal");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        TextField nameF = new TextField();
        nameF.setPromptText("e.g. Vacation");
        TextField targetF = new TextField();
        targetF.setPromptText("e.g. 50000");
        TextField savedF = new TextField("0.00");
        g.addRow(0, new Label("Goal Name:"), nameF);
        g.addRow(1, new Label("Target Amount (₱):"), targetF);
        g.addRow(2, new Label("Already Saved (₱):"), savedF);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK && !nameF.getText().isBlank()) {
                double target = 0, saved = 0;
                try {
                    target = Double.parseDouble(targetF.getText().trim());
                } catch (Exception ex) {
                }
                try {
                    saved = Double.parseDouble(savedF.getText().trim());
                } catch (Exception ex) {
                }
                if (target > 0) {
                    goalsServices.addGoal(new Goal("G-" + System.currentTimeMillis(),
                            nameF.getText().trim(), "Resources/icons/goals.png", target, saved));
                    showGoals();
                }
            }
        });
    }

    private void openDepositDialog(Goal goal) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Deposit to Goal");
        dlg.setHeaderText("Save for: " + goal.getGoalName());
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        TextField amtF = new TextField("0.00");
        ComboBox<String> accBox = new ComboBox<>();
        for (Account a : accountServices.getAllAccounts())
            accBox.getItems().add(a.getAccountName());
        if (!accBox.getItems().isEmpty())
            accBox.setValue(accBox.getItems().get(0));

        g.addRow(0, new Label("Amount (₱):"), amtF);
        g.addRow(1, new Label("From Account:"), accBox);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amtF.getText().trim());
                    String accName = accBox.getValue();
                    Account source = null;
                    for (Account a : accountServices.getAllAccounts())
                        if (a.getAccountName().equals(accName))
                            source = a;

                    if (amount > 0 && source != null && source.getBalance() >= amount) {
                        source.setBalance(source.getBalance() - amount);
                        goal.addSavings(amount);

                        transactionServices.addTransaction(new Transaction(
                                "TXN-" + System.currentTimeMillis(),
                                "Savings: " + goal.getGoalName(),
                                "Goals", amount, LocalDate.now().toString(), "Expense", accName));

                        accountServices.saveToFile();
                        goalsServices.saveToFile();
                        showGoals();
                    }
                } catch (Exception ex) {
                }
            }
        });
    }

    private void openWithdrawDialog(Goal goal) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Withdraw from Goal");
        dlg.setHeaderText("Withdraw from: " + goal.getGoalName());
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        TextField amtF = new TextField("0.00");
        ComboBox<String> accBox = new ComboBox<>();
        for (Account a : accountServices.getAllAccounts())
            accBox.getItems().add(a.getAccountName());
        if (!accBox.getItems().isEmpty())
            accBox.setValue(accBox.getItems().get(0));

        g.addRow(0, new Label("Amount (₱):"), amtF);
        g.addRow(1, new Label("To Account:"), accBox);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amtF.getText().trim());
                    String accName = accBox.getValue();
                    Account target = null;
                    for (Account a : accountServices.getAllAccounts())
                        if (a.getAccountName().equals(accName))
                            target = a;

                    if (amount > 0 && goal.getSavedAmount() >= amount && target != null) {
                        goal.addSavings(-amount);
                        target.setBalance(target.getBalance() + amount);

                        transactionServices.addTransaction(new Transaction(
                                "TXN-" + System.currentTimeMillis(),
                                "Withdrawn from " + goal.getGoalName(),
                                "Income", amount, LocalDate.now().toString(), "Income", accName));

                        accountServices.saveToFile();
                        goalsServices.saveToFile();
                        showGoals();
                    }
                } catch (Exception ex) {
                }
            }
        });
    }

    // ══════════════════════════════════════════════════════════
    // ── TRANSACTIONS PAGE ─────────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private VBox buildTransactionsPage() {
        VBox page = new VBox(16);
        Label heading = new Label("Transactions");
        heading.setStyle("-fx-font-size:26px;-fx-font-weight:900;-fx-text-fill:#1e293b;");

        Button addBtn = new Button("+ Add Transaction");
        addBtn.getStyleClass().add("login-btn");
        addBtn.setOnAction(e -> openAddTransactionDialog());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(20, heading, spacer, addBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox list = new VBox(8);
        list.getStyleClass().add("dash-card");

        // column header
        HBox colHdr = new HBox(0);
        colHdr.setPadding(new Insets(4, 10, 4, 10));
        Label c1 = colLabel("Description", 160);
        Label c2 = colLabel("Category", 120);
        Label c3 = colLabel("Type", 100);
        Label c4 = colLabel("Amount", 120);
        Label c5 = colLabel("Account", 120);
        Label c6 = colLabel("Date", 110);
        colHdr.getChildren().addAll(c1, c2, c3, c4, c5, c6);
        list.getChildren().add(colHdr);

        if (transactionServices.getAllTransactions().isEmpty()) {
            Label empty = new Label("No transactions yet.");
            empty.getStyleClass().add("account-type");
            list.getChildren().add(empty);
        }
        for (Transaction t : transactionServices.getAllTransactions()) {
            HBox row = new HBox(0);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("account-row");
            row.setPadding(new Insets(8, 10, 8, 10));
            row.getChildren().addAll(
                    txnCell(t.getDescription(), 160), txnCell(t.getCategory(), 120),
                    txnCell(t.getType(), 100),
                    txnCell("₱ " + String.format("%.2f", t.getAmount()), 120),
                    txnCell(t.getAccountName(), 120), txnCell(t.getDate(), 110));
            row.setStyle("-fx-cursor: hand;");
            row.setOnMouseClicked(e -> showReceipt(t));
            list.getChildren().add(row);
        }
        page.getChildren().addAll(header, list);
        return page;
    }

    private void openAddTransactionDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Transaction");
        dlg.setHeaderText("Record a new transaction");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        TextField descF = new TextField();
        descF.setPromptText("e.g. Salary");
        TextField catF = new TextField();
        catF.setPromptText("e.g. Food");
        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Income", "Expense");
        typeBox.setValue("Expense");
        TextField amtF = new TextField();
        amtF.setPromptText("e.g. 500");
        ComboBox<String> accBox = new ComboBox<>();
        for (Account a : accountServices.getAllAccounts())
            accBox.getItems().add(a.getAccountName());
        if (!accBox.getItems().isEmpty())
            accBox.setValue(accBox.getItems().get(0));

        g.addRow(0, new Label("Description:"), descF);
        g.addRow(1, new Label("Category:"), catF);
        g.addRow(2, new Label("Type:"), typeBox);
        g.addRow(3, new Label("Amount (₱):"), amtF);
        g.addRow(4, new Label("Account:"), accBox);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK && !descF.getText().isBlank()) {
                double amt = 0;
                try {
                    amt = Double.parseDouble(amtF.getText().trim());
                } catch (Exception ex) {
                }
                if (amt > 0) {
                    String id = "TXN-" + System.currentTimeMillis();
                    String acc = accBox.getValue() != null ? accBox.getValue() : "";
                    Transaction t = new Transaction(id, descF.getText().trim(),
                            catF.getText().trim(), amt, LocalDate.now().toString(),
                            typeBox.getValue(), acc);
                    transactionServices.addTransaction(t);
                    showTransactions();
                }
            }
        });
    }

    // ══════════════════════════════════════════════════════════
    // ── BUDGET PAGE ───────────────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private VBox buildBudgetPage() {
        VBox page = new VBox(16);
        Label heading = new Label("Budget Limits");
        heading.setStyle("-fx-font-size:26px;-fx-font-weight:900;-fx-text-fill:#1e293b;");

        Button editBtn = new Button("+ Set Budget");
        editBtn.getStyleClass().add("login-btn");
        editBtn.setOnAction(e -> openSetBudgetDialog());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(20, heading, spacer, editBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox list = new VBox(10);
        list.getStyleClass().add("dash-card");
        
        List<Budget> budgets = budgetServices.getAllBudgets();
        if (budgets.isEmpty()) {
             Label empty = new Label("No budgets yet. Click '+ Set Budget' to start.");
             empty.getStyleClass().add("account-type");
             list.getChildren().add(empty);
        }

        for (Budget b : budgets) {
            String cat = b.getBudgetName();
            double spent = transactionServices.getSpendingByCategory(cat);
            double limit = b.getMonthlyLimit();
            double progress = limit > 0 ? Math.min(spent / limit, 1.0) : 0;
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("account-row");
            row.setPadding(new Insets(10));
            VBox info = new VBox(4);
            Label nl = new Label(cat);
            nl.getStyleClass().add("account-name");
            Label al = new Label("₱ " + String.format("%.2f", spent)
                    + " / ₱ " + String.format("%.2f", limit));
            al.getStyleClass().add("account-type");
            ProgressBar pb = new ProgressBar(progress);
            pb.getStyleClass().add("goal-progress");
            pb.setPrefWidth(220);
            info.getChildren().addAll(nl, al, pb);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button addExpBtn = new Button("+");
            addExpBtn.getStyleClass().add("login-btn");
            addExpBtn.setStyle("-fx-background-color: #3b82f6; -fx-padding: 5 10; -fx-font-weight: bold;");
            addExpBtn.setOnAction(e -> openAddBudgetExpenseDialog(b));

            Button delBtn = new Button("X");
            delBtn.getStyleClass().add("login-btn");
            delBtn.setStyle("-fx-background-color: #ef4444; -fx-padding: 5 10; -fx-font-weight: bold;");
            delBtn.setOnAction(e -> confirmDeleteBudget(b));

            row.getChildren().addAll(info, addExpBtn, delBtn);
            list.getChildren().add(row);
        }
        page.getChildren().addAll(header, list);
        return page;
    }

    private void openSetBudgetDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Set Monthly Budget");
        dlg.setHeaderText("Enter a category and its spending limit");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        
        TextField catF = new TextField();
        catF.setPromptText("e.g. Groceries");

        TextField limitF = new TextField("0.00");
        
        g.addRow(0, new Label("Category:"), catF);
        g.addRow(1, new Label("Limit (₱):"), limitF);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    String cat = catF.getText().trim();
                    double limit = Double.parseDouble(limitF.getText().trim());
                    if (!cat.isEmpty() && limit > 0) {
                        budgetServices.addBudget(new Budget(cat, limit));
                        showBudgets();
                    }
                } catch (Exception ex) {
                }
            }
        });
    }

    private void openAddBudgetExpenseDialog(Budget b) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Add Budget Expense");
        dlg.setHeaderText("Record an expense for: " + b.getBudgetName());
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        
        TextField descF = new TextField();
        descF.setPromptText("e.g. Bought items");
        TextField amtF = new TextField("0.00");
        ComboBox<String> accBox = new ComboBox<>();
        for (Account a : accountServices.getAllAccounts())
            accBox.getItems().add(a.getAccountName());
        if (!accBox.getItems().isEmpty())
            accBox.setValue(accBox.getItems().get(0));

        g.addRow(0, new Label("Description:"), descF);
        g.addRow(1, new Label("Amount (₱):"), amtF);
        g.addRow(2, new Label("Account:"), accBox);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    double amount = Double.parseDouble(amtF.getText().trim());
                    String accName = accBox.getValue();
                    Account source = null;
                    for (Account a : accountServices.getAllAccounts()) {
                        if (a.getAccountName().equals(accName)) {
                            source = a;
                            break;
                        }
                    }

                    if (amount > 0 && source != null && source.getBalance() >= amount) {
                        source.setBalance(source.getBalance() - amount);
                        
                        transactionServices.addTransaction(new Transaction(
                                "TXN-" + System.currentTimeMillis(),
                                descF.getText().trim(),
                                b.getBudgetName(), amount, LocalDate.now().toString(), "Expense", accName));

                        accountServices.saveToFile();
                        showBudgets();
                    }
                } catch (Exception ex) {
                }
            }
        });
    }

    private HBox buildBudgetTracker() {
        HBox bar = new HBox(20);
        bar.getStyleClass().add("dash-card");
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(15, 25, 15, 25));
        bar.setStyle("-fx-background-color: white; -fx-background-radius: 16; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 4);");

        Label label = new Label("Personal Savings Wallet");
        label.setStyle(
                "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #64748b; -fx-text-transform: uppercase;");

        Account wallet = getOrCreateWallet();
        Label amount = new Label("₱ " + String.format("%.2f", wallet.getBalance()));
        amount.setStyle("-fx-font-size: 26px; -fx-font-weight: 900; -fx-text-fill: #1B2F6E;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button dep = new Button("+ Add Savings");
        dep.getStyleClass().add("login-btn");
        dep.setOnAction(e -> openAccountActionDialog(wallet, "Deposit"));

        Button wit = new Button("- Use Savings");
        wit.getStyleClass().add("login-btn");
        wit.setStyle("-fx-background-color: #ef4444;");
        wit.setOnAction(e -> openAccountActionDialog(wallet, "Withdraw"));

        bar.getChildren().addAll(label, amount, spacer, dep, wit);
        return bar;
    }

    private Account getOrCreateWallet() {
        for (Account a : accountServices.getAllAccounts()) {
            if (a.getAccountName().equals("Personal Wallet"))
                return a;
        }
        Account wallet = new Account("Personal Wallet", "Tracker", 0.0, "TRACKER-001");
        accountServices.addAccount(wallet);
        return wallet;
    }

    // ══════════════════════════════════════════════════════════
    // ── SORA AI AGENT ─────────────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private VBox buildAgentPage() {
        VBox page = new VBox(24);
        page.setAlignment(Pos.TOP_CENTER);
        page.setPadding(new Insets(40));

        VBox agentCard = new VBox(20);
        agentCard.getStyleClass().add("dash-card");
        agentCard.setAlignment(Pos.CENTER);
        agentCard.setMaxWidth(600);
        agentCard.setPadding(new Insets(40));
        agentCard.setStyle("-fx-background-color: linear-gradient(to bottom right, #ffffff, #f8fafc); " +
                "-fx-border-color: #6366f1; -fx-border-width: 2; -fx-border-radius: 20; -fx-background-radius: 20;");

        Label agentName = new Label("SORA AGENT");
        agentName
                .setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #6366f1; -fx-letter-spacing: 2;");

        Label welcome = new Label("Your Personal AI Financial Advisor");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: 900; -fx-text-fill: #1e293b;");

        TextArea adviceArea = new TextArea(
                "Hello! I am Sora, your AI financial agent. Click the button below to get personalized advice based on your current savings and goals.");
        adviceArea.setWrapText(true);
        adviceArea.setEditable(false);
        adviceArea.setPrefHeight(150);
        adviceArea.setStyle(
                "-fx-background-color: transparent; -fx-control-inner-background: transparent; -fx-text-fill: #475569; -fx-font-size: 16px; -fx-border-width: 0;");

        TextField userPrompt = new TextField();
        userPrompt.setPromptText("Ask Sora anything (e.g., 'Should I save more?')");
        userPrompt.getStyleClass().add("login-btn"); // Reuse styling or custom
        userPrompt.setStyle(
                "-fx-background-color: #f1f5f9; -fx-text-fill: #1e293b; -fx-border-color: #e2e8f0; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 12;");
        userPrompt.setPrefWidth(500);

        Button getAdviceBtn = new Button("Ask Sora");
        getAdviceBtn.getStyleClass().add("login-btn");
        getAdviceBtn.setPrefWidth(120);
        getAdviceBtn.setOnAction(e -> {
            String prompt = userPrompt.getText().trim();
            if (prompt.isEmpty())
                prompt = "Give me general financial advice.";
            adviceArea.setText("Sora is thinking...");
            fetchSoraAdvice(adviceArea, prompt);
        });

        HBox inputRow = new HBox(10, userPrompt, getAdviceBtn);
        inputRow.setAlignment(Pos.CENTER);

        agentCard.getChildren().addAll(agentName, welcome, adviceArea, inputRow);
        page.getChildren().add(agentCard);

        return page;
    }

    private void fetchSoraAdvice(TextArea area, String userMsg) {
        try {
            double bal = accountServices.getTotalBalance();
            double income = transactionServices.getTotalIncome();
            double spent = transactionServices.getTotalSpent();
            String name = currentUser.getFirstName();

            String json = String.format(
                    "{\"balance\": %f, \"income\": %f, \"spent\": %f, \"user_name\": \"%s\", \"user_prompt\": \"%s\"}",
                    bal, income, spent, name, userMsg.replace("\"", "\\\""));

            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create("http://127.0.0.1:5000/advise"))
                    .header("Content-Type", "application/json")
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(json,
                            java.nio.charset.StandardCharsets.UTF_8))
                    .build();

            client.sendAsync(request, java.net.http.HttpResponse.BodyHandlers.ofString())
                    .thenApply(java.net.http.HttpResponse::body)
                    .thenAccept(body -> {
                        javafx.application.Platform.runLater(() -> {
                            // Very simple JSON parsing for the advice field
                            String advice = body.contains("\"advice\":")
                                    ? body.substring(body.indexOf("\"advice\":") + 10, body.lastIndexOf("\""))
                                    : "I couldn't reach the agent server. Please make sure the Python backend is running!";
                            // Clean up escaped quotes if any
                            advice = advice.replace("\\n", "\n").replace("\\\"", "\"");
                            area.setText(advice);
                        });
                    })
                    .exceptionally(ex -> {
                        javafx.application.Platform.runLater(() -> area.setText(
                                "Offline: Please run 'python agent.py' in the sora_agent folder to wake me up!"));
                        return null;
                    });

        } catch (Exception e) {
            area.setText("Something went wrong while trying to talk to Sora.");
        }
    }

    // ══════════════════════════════════════════════════════════
    // ── SUBSCRIPTIONS PAGE ────────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private VBox buildSubscriptionsPage() {
        VBox page = new VBox(16);
        Label heading = new Label("Subscriptions");
        heading.setStyle("-fx-font-size:26px;-fx-font-weight:900;-fx-text-fill:#1e293b;");

        Button addBtn = new Button("+ Add Subscription");
        addBtn.getStyleClass().add("login-btn");
        addBtn.setOnAction(e -> openAddSubscriptionDialog());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(20, heading, spacer, addBtn);
        header.setAlignment(Pos.CENTER_LEFT);

        VBox list = new VBox(10);
        list.getStyleClass().add("dash-card");
        if (subscriptionServices.getAllSubscriptions().isEmpty()) {
            Label empty = new Label("No subscriptions yet. Click '+ Add Subscription' to start.");
            empty.getStyleClass().add("account-type");
            list.getChildren().add(empty);
        }
        for (Subscription s : subscriptionServices.getAllSubscriptions()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("account-row");
            row.setPadding(new Insets(10));

            VBox info = new VBox(4);
            Label nl = new Label(s.getSubscriptionName());
            nl.getStyleClass().add("account-name");
            Label al = new Label(
                    "₱ " + String.format("%.2f", s.getMonthlyCost()) + " / month — Due: " + s.getBillingDate());
            al.getStyleClass().add("account-type");
            info.getChildren().addAll(nl, al);
            HBox.setHgrow(info, Priority.ALWAYS);

            Button payBtn = new Button("Pay");
            payBtn.getStyleClass().add("login-btn");
            payBtn.setOnAction(e -> openPaySubscriptionDialog(s));

            Button refundBtn = new Button("Refund");
            refundBtn.getStyleClass().add("login-btn");
            refundBtn.setStyle("-fx-background-color: #ef4444;");
            refundBtn.setOnAction(e -> openRefundSubscriptionDialog(s));

            Button delBtn = new Button("X");
            delBtn.getStyleClass().add("login-btn");
            delBtn.setStyle("-fx-background-color: #ef4444; -fx-padding: 5 10; -fx-font-weight: bold;");
            delBtn.setOnAction(e -> confirmDeleteSubscription(s));

            row.getChildren().addAll(info, payBtn, refundBtn, delBtn);
            list.getChildren().add(row);
        }
        page.getChildren().addAll(header, list);
        return page;
    }

    private void openAddSubscriptionDialog() {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("New Subscription");
        dlg.setHeaderText("Add a monthly subscription");
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        ComboBox<String> nameBox = new ComboBox<>();
        nameBox.setEditable(true);
        nameBox.getItems().addAll("Netflix", "Spotify", "Disney+", "YouTube Premium", "HBO Go", "Amazon Prime", "Canva",
                "Adobe CC", "Microsoft 365");
        nameBox.setValue("Netflix");
        TextField costF = new TextField("0.00");
        TextField dateF = new TextField(LocalDate.now().getDayOfMonth() + "th");

        g.addRow(0, new Label("Service Name:"), nameBox);
        g.addRow(1, new Label("Monthly Cost (₱):"), costF);
        g.addRow(2, new Label("Billing Date:"), dateF);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    double cost = Double.parseDouble(costF.getText().trim());
                    String name = nameBox.getEditor().getText();
                    if (!name.isBlank() && cost > 0) {
                        subscriptionServices.addSubscription(new Subscription("SUB-" + System.currentTimeMillis(),
                                name, cost, dateF.getText().trim(), "Active"));
                        showSubscriptions();
                    }
                } catch (Exception ex) {
                }
            }
        });
    }

    private void openPaySubscriptionDialog(Subscription s) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Pay Subscription");
        dlg.setHeaderText("Payment for: " + s.getSubscriptionName());
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        Label costLbl = new Label("Amount: ₱ " + String.format("%.2f", s.getMonthlyCost()));
        ComboBox<String> accBox = new ComboBox<>();
        for (Account a : accountServices.getAllAccounts())
            accBox.getItems().add(a.getAccountName());
        if (!accBox.getItems().isEmpty())
            accBox.setValue(accBox.getItems().get(0));

        g.addRow(0, costLbl);
        g.addRow(1, new Label("Pay using:"), accBox);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                String accName = accBox.getValue();
                Account source = null;
                for (Account a : accountServices.getAllAccounts())
                    if (a.getAccountName().equals(accName))
                        source = a;

                if (source != null && source.getBalance() >= s.getMonthlyCost()) {
                    source.setBalance(source.getBalance() - s.getMonthlyCost());
                    transactionServices.addTransaction(new Transaction(
                            "TXN-" + System.currentTimeMillis(),
                            "Subscription: " + s.getSubscriptionName(),
                            "Subscriptions", s.getMonthlyCost(), LocalDate.now().toString(), "Expense", accName));
                    accountServices.saveToFile();
                    showSubscriptions();
                }
            }
        });
    }

    private void openRefundSubscriptionDialog(Subscription s) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Refund Subscription");
        dlg.setHeaderText("Refund payment for: " + s.getSubscriptionName());
        dlg.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane g = new GridPane();
        g.setHgap(10);
        g.setVgap(10);
        g.setPadding(new Insets(20));
        Label costLbl = new Label("Refund Amount: ₱ " + String.format("%.2f", s.getMonthlyCost()));
        ComboBox<String> accBox = new ComboBox<>();
        for (Account a : accountServices.getAllAccounts())
            accBox.getItems().add(a.getAccountName());
        if (!accBox.getItems().isEmpty())
            accBox.setValue(accBox.getItems().get(0));

        g.addRow(0, costLbl);
        g.addRow(1, new Label("Refund to:"), accBox);
        dlg.getDialogPane().setContent(g);

        dlg.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                String accName = accBox.getValue();
                Account target = null;
                for (Account a : accountServices.getAllAccounts())
                    if (a.getAccountName().equals(accName))
                        target = a;

                if (target != null) {
                    target.setBalance(target.getBalance() + s.getMonthlyCost());
                    transactionServices.addTransaction(new Transaction(
                            "TXN-" + System.currentTimeMillis(),
                            "Refund: " + s.getSubscriptionName(),
                            "Income", s.getMonthlyCost(), LocalDate.now().toString(), "Income", accName));
                    accountServices.saveToFile();
                    showSubscriptions();
                }
            }
        });
    }

    // ══════════════════════════════════════════════════════════
    // ── SETTINGS PAGE ─────────────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private VBox buildSettingsPage() {
        VBox page = new VBox(24);
        Label heading = new Label("Settings");
        heading.setStyle("-fx-font-size:26px;-fx-font-weight:900;-fx-text-fill:#1e293b;");

        VBox profileCard = new VBox(16);
        profileCard.getStyleClass().add("dash-card");
        Label profileTitle = new Label("Edit Profile");
        profileTitle.setStyle("-fx-font-weight:bold;-fx-text-fill:white;");

        GridPane g = new GridPane();
        g.setHgap(15);
        g.setVgap(15);
        TextField fn = new TextField(currentUser.getFirstName());
        TextField ln = new TextField(currentUser.getLastName());
        
        Label fnLbl = new Label("First Name:");
        fnLbl.setStyle("-fx-text-fill:white;");
        Label lnLbl = new Label("Last Name:");
        lnLbl.setStyle("-fx-text-fill:white;");
        
        g.addRow(0, fnLbl, fn);
        g.addRow(1, lnLbl, ln);

        Button saveProfile = new Button("Save Profile");
        saveProfile.getStyleClass().add("login-btn");
        saveProfile.setOnAction(e -> {
            currentUser.setFirstName(fn.getText());
            currentUser.setLastName(ln.getText());
            usersServices.updateUser(currentUser);
            showSettings();
        });

        profileCard.getChildren().addAll(profileTitle, g, saveProfile);

        VBox themeCard = new VBox(16);
        themeCard.getStyleClass().add("dash-card");
        Label themeTitle = new Label("Dashboard Theme");
        themeTitle.setStyle("-fx-font-weight:bold;-fx-text-fill:white;");

        HBox colors = new HBox(12);
        colors.getChildren().addAll(
                buildColorCircle("#1e3a8a", "Default Blue"),
                buildColorCircle("#4c1d95", "Deep Purple"),
                buildColorCircle("#064e3b", "Forest Green"),
                buildColorCircle("#881337", "Rose Red"),
                buildColorCircle("#0f172a", "Midnight Black"));

        themeCard.getChildren().addAll(themeTitle, colors);
        page.getChildren().addAll(heading, profileCard, themeCard);
        return page;
    }

    private Button buildColorCircle(String hex, String name) {
        Button btn = new Button();
        btn.setTooltip(new Tooltip(name));
        btn.setStyle("-fx-background-color: " + hex
                + "; -fx-min-width: 40; -fx-min-height: 40; -fx-background-radius: 20; -fx-cursor: hand;");
        btn.setOnAction(e -> applyThemeColor(hex));
        return btn;
    }

    private void applyThemeColor(String hex) {
        sidebar.setStyle("-fx-background-color: " + hex + ";");
    }

    private void confirmDeleteBudget(Budget b) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Budget");
        alert.setHeaderText("Do you want to remove this budget?");
        alert.setContentText(b.getBudgetName());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                budgetServices.deleteBudget(b);
                showBudgets();
            }
        });
    }

    private void confirmDeleteAccount(Account acc) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Do you want to remove this account?");
        alert.setContentText(acc.getAccountName());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                accountServices.getAllAccounts().remove(acc);
                accountServices.saveToFile();
                showAccounts();
            }
        });
    }

    private void confirmDeleteGoal(Goal g) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Goal");
        alert.setHeaderText("Do you want to remove this goal?");
        alert.setContentText(g.getGoalName());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                goalsServices.getAllGoals().remove(g);
                goalsServices.saveToFile();
                showGoals();
            }
        });
    }

    private void confirmDeleteSubscription(Subscription s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Subscription");
        alert.setHeaderText("Do you want to remove this subscription?");
        alert.setContentText(s.getSubscriptionName());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                subscriptionServices.getAllSubscriptions().remove(s);
                subscriptionServices.saveToFile();
                showSubscriptions();
            }
        });
    }

    private void showReceipt(Transaction t) {
        Dialog<ButtonType> dlg = new Dialog<>();
        dlg.setTitle("Transaction Receipt");
        dlg.setHeaderText("Receipt Details");
        dlg.getDialogPane().getButtonTypes().add(ButtonType.OK);
        
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white; -fx-border-color: #e2e8f0; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label title = new Label("SORA RECEIPT");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: 900; -fx-text-fill: #1e293b;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        GridPane g = new GridPane();
        g.setHgap(15);
        g.setVgap(10);
        
        g.addRow(0, new Label("Transaction ID:"), new Label(t.getTransactionId()));
        g.addRow(1, new Label("Date:"), new Label(t.getDate()));
        g.addRow(2, new Label("Description:"), new Label(t.getDescription()));
        g.addRow(3, new Label("Account:"), new Label(t.getAccountName()));
        g.addRow(4, new Label("Category:"), new Label(t.getCategory()));
        g.addRow(5, new Label("Type:"), new Label(t.getType()));
        
        Label amount = new Label("₱ " + String.format("%.2f", t.getAmount()));
        amount.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + (t.getType().equals("Income") ? "#16a34a" : "#ef4444") + ";");
        g.addRow(6, new Label("Amount:"), amount);

        content.getChildren().addAll(title, new Separator(), g);
        dlg.getDialogPane().setContent(content);
        dlg.showAndWait();
    }

    // ══════════════════════════════════════════════════════════
    // ── SHARED SMALL HELPERS ──────────────────────────────────
    // ══════════════════════════════════════════════════════════
    private HBox accountRow(String iconPath, String name, String type, String amount) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("account-row");
        row.setPadding(new Insets(8, 10, 8, 10));
        ImageView icon = buildIcon(iconPath, 28, 28, false);
        VBox info = new VBox(2);
        Label nl = new Label(name);
        nl.getStyleClass().add("account-name");
        Label tl = new Label(type);
        tl.getStyleClass().add("account-type");
        info.getChildren().addAll(nl, tl);
        HBox.setHgrow(info, Priority.ALWAYS);
        Label al = new Label(amount);
        al.getStyleClass().add("account-amount");
        row.getChildren().addAll(icon, info, al);
        return row;
    }

    private Label colLabel(String text, double w) {
        Label l = new Label(text);
        l.getStyleClass().add("account-name");
        l.setMinWidth(w);
        return l;
    }

    private Label txnCell(String text, double w) {
        Label l = new Label(text);
        l.getStyleClass().add("account-type");
        l.setMinWidth(w);
        return l;
    }
}
