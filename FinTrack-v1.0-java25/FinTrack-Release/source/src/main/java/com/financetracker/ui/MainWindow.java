package com.financetracker.ui;

import com.financetracker.model.User;
import com.financetracker.service.*;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class MainWindow extends BorderPane {

    private final User currentUser;
    private final AuthService authService;
    private final TransactionService txService;
    private final CsvService csvService;
    private final JsonDatabaseService db;
    private final Runnable onLogout;

    private Button activeNavBtn;
    private final BorderPane contentArea;

    // Panels (lazy-loaded, refreshed on switch)
    private DashboardPanel dashboardPanel;
    private TransactionsPanel transactionsPanel;
    private SettingsPanel settingsPanel;

    public MainWindow(User currentUser, AuthService authService, TransactionService txService,
                      CsvService csvService, JsonDatabaseService db, Runnable onLogout) {
        this.currentUser = currentUser;
        this.authService = authService;
        this.txService = txService;
        this.csvService = csvService;
        this.db = db;
        this.onLogout = onLogout;
        contentArea = new BorderPane();
        contentArea.setStyle("-fx-background-color: #0f1117;");
        buildUI();
    }

    private void buildUI() {
        setLeft(buildSidebar());
        setCenter(contentArea);
        setStyle("-fx-background-color: #0f1117;");
        showDashboard();
    }

    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.getStyleClass().add("sidebar");
        sidebar.setPrefWidth(230);

        // Logo
        VBox logoBox = new VBox(2);
        logoBox.setPadding(new Insets(24, 20, 16, 20));
        Label logo = new Label("💰 FinTrack");
        logo.getStyleClass().add("sidebar-logo");
        Label logoSub = new Label("Personal Finance Manager");
        logoSub.getStyleClass().add("sidebar-logo-sub");
        logoBox.getChildren().addAll(logo, logoSub);

        Region divider1 = new Region();
        divider1.getStyleClass().add("sidebar-divider");
        divider1.setPrefHeight(1);
        divider1.setStyle("-fx-background-color: #1e2231;");
        VBox.setMargin(divider1, new Insets(0, 16, 0, 16));

        Label mainSection = new Label("MAIN");
        mainSection.getStyleClass().add("sidebar-section-label");

        Button dashBtn = makeNavButton("📊  Dashboard");
        Button txBtn = makeNavButton("💳  Transactions");

        Label settingsSection = new Label("ACCOUNT");
        settingsSection.getStyleClass().add("sidebar-section-label");

        Button settingsBtn = makeNavButton("⚙   Settings");

        // Nav actions
        dashBtn.setOnAction(e -> { setActive(dashBtn); showDashboard(); });
        txBtn.setOnAction(e -> { setActive(txBtn); showTransactions(); });
        settingsBtn.setOnAction(e -> { setActive(settingsBtn); showSettings(); });

        activeNavBtn = dashBtn;
        dashBtn.getStyleClass().add("nav-item-active");

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // User info at bottom
        VBox userBox = new VBox(2);
        userBox.getStyleClass().add("user-info-box");
        userBox.setPadding(new Insets(12, 14, 12, 14));
        VBox.setMargin(userBox, new Insets(0, 12, 16, 12));

        Label initials = new Label(getInitials(currentUser.getFullName()));
        initials.setStyle("-fx-background-color: #6366f1; -fx-text-fill: white; " +
                "-fx-background-radius: 20; -fx-padding: 8 12; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label userName = new Label(currentUser.getFullName());
        userName.getStyleClass().add("user-name-label");
        Label userEmail = new Label("@" + currentUser.getUsername());
        userEmail.getStyleClass().add("user-email-label");

        VBox userText = new VBox(2, userName, userEmail);
        HBox userRow = new HBox(10, initials, userText);
        userRow.setAlignment(Pos.CENTER_LEFT);

        userBox.getChildren().add(userRow);

        sidebar.getChildren().addAll(
                logoBox, divider1,
                mainSection, dashBtn, txBtn,
                settingsSection, settingsBtn,
                spacer, userBox
        );

        // Slide in animation
        sidebar.setTranslateX(-230);
        TranslateTransition slide = new TranslateTransition(Duration.millis(400), sidebar);
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_OUT);
        slide.play();

        return sidebar;
    }

    private Button makeNavButton(String text) {
        Button btn = new Button(text);
        btn.getStyleClass().add("nav-item");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    private void setActive(Button btn) {
        if (activeNavBtn != null) {
            activeNavBtn.getStyleClass().remove("nav-item-active");
        }
        btn.getStyleClass().add("nav-item-active");
        activeNavBtn = btn;
    }

    private void showDashboard() {
        dashboardPanel = new DashboardPanel(currentUser, txService);
        VBox.setVgrow(dashboardPanel, Priority.ALWAYS);
        contentArea.setCenter(dashboardPanel);
    }

    private void showTransactions() {
        transactionsPanel = new TransactionsPanel(currentUser, txService, csvService);
        transactionsPanel.setOnDataChanged(() -> {
            // If dashboard visible, it'll refresh next time
        });
        VBox.setVgrow(transactionsPanel, Priority.ALWAYS);
        contentArea.setCenter(transactionsPanel);
    }

    private void showSettings() {
        settingsPanel = new SettingsPanel(currentUser, authService, db, () -> {
            authService.logout();
            onLogout.run();
        });
        contentArea.setCenter(settingsPanel);
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) return parts[0].substring(0, Math.min(2, parts[0].length())).toUpperCase();
        return (parts[0].charAt(0) + "" + parts[parts.length - 1].charAt(0)).toUpperCase();
    }
}
