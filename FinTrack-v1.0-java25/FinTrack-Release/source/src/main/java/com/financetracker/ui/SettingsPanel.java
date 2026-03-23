package com.financetracker.ui;

import com.financetracker.model.User;
import com.financetracker.service.AuthService;
import com.financetracker.service.JsonDatabaseService;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class SettingsPanel extends VBox {

    private final User currentUser;
    private final AuthService authService;
    private final JsonDatabaseService db;
    private final Runnable onLogout;

    public SettingsPanel(User currentUser, AuthService authService, JsonDatabaseService db, Runnable onLogout) {
        this.currentUser = currentUser;
        this.authService = authService;
        this.db = db;
        this.onLogout = onLogout;
        buildUI();
    }

    private void buildUI() {
        getStyleClass().add("main-content");
        setPadding(new Insets(28, 32, 28, 32));
        setSpacing(20);

        Label title = new Label("Settings");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("Manage your account and preferences");
        subtitle.getStyleClass().add("page-subtitle");

        VBox titleBox = new VBox(3, title, subtitle);

        // Profile card
        VBox profileCard = new VBox(16);
        profileCard.getStyleClass().add("section-card");

        Label cardTitle = new Label("Profile Information");
        cardTitle.getStyleClass().add("section-title");

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(14);

        TextField fullNameField = makeSettingField(grid, "Full Name", currentUser.getFullName(), 0);
        TextField emailField = makeSettingField(grid, "Email", currentUser.getEmail(), 1);
        TextField usernameField = makeSettingField(grid, "Username", currentUser.getUsername(), 2);
        usernameField.setEditable(false);
        usernameField.setStyle(usernameField.getStyle() + "-fx-opacity: 0.6;");

        Label saveStatus = new Label("");
        saveStatus.setVisible(false);

        Button saveBtn = new Button("Save Changes");
        saveBtn.getStyleClass().add("btn-primary");

        saveBtn.setOnAction(e -> {
            String newName = fullNameField.getText().trim();
            String newEmail = emailField.getText().trim();
            if (newName.isEmpty() || newEmail.isEmpty()) {
                saveStatus.setText("Fields cannot be empty.");
                saveStatus.getStyleClass().remove("success-label");
                saveStatus.getStyleClass().add("error-label");
                saveStatus.setVisible(true);
                return;
            }
            if (!newEmail.contains("@")) {
                saveStatus.setText("Please enter a valid email.");
                saveStatus.getStyleClass().remove("success-label");
                saveStatus.getStyleClass().add("error-label");
                saveStatus.setVisible(true);
                return;
            }
            currentUser.setFullName(newName);
            currentUser.setEmail(newEmail.toLowerCase());
            db.saveUser(currentUser);
            saveStatus.setText("✓ Profile saved successfully.");
            saveStatus.getStyleClass().remove("error-label");
            saveStatus.getStyleClass().add("success-label");
            saveStatus.setVisible(true);

            FadeTransition ft = new FadeTransition(Duration.millis(200), saveStatus);
            ft.setFromValue(0); ft.setToValue(1); ft.play();
        });

        profileCard.getChildren().addAll(cardTitle, grid, saveStatus, saveBtn);

        // Budget card
        VBox budgetCard = new VBox(16);
        budgetCard.getStyleClass().add("section-card");

        Label budgetTitle = new Label("Monthly Budget");
        budgetTitle.getStyleClass().add("section-title");

        Label budgetDesc = new Label("Set a monthly spending limit to track your expenses against a goal.");
        budgetDesc.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px;");
        budgetDesc.setWrapText(true);

        HBox budgetRow = new HBox(12);
        budgetRow.setAlignment(Pos.CENTER_LEFT);

        Label dollarSign = new Label("$");
        dollarSign.setStyle("-fx-text-fill: #64748b; -fx-font-size: 18px;");

        TextField budgetField = new TextField(
                currentUser.getMonthlyBudget() > 0 ? String.format("%.2f", currentUser.getMonthlyBudget()) : "");
        budgetField.setPromptText("e.g. 2000.00");
        budgetField.getStyleClass().add("modern-field");
        budgetField.setPrefWidth(200);

        Button saveBudgetBtn = new Button("Set Budget");
        saveBudgetBtn.getStyleClass().add("btn-success");

        Label budgetStatus = new Label("");
        budgetStatus.setVisible(false);

        saveBudgetBtn.setOnAction(e -> {
            try {
                double bud = Double.parseDouble(budgetField.getText().trim());
                if (bud < 0) throw new NumberFormatException();
                authService.updateUserBudget(bud);
                budgetStatus.setText("✓ Budget updated to $" + String.format("%.2f", bud));
                budgetStatus.getStyleClass().remove("error-label");
                budgetStatus.getStyleClass().add("success-label");
                budgetStatus.setVisible(true);
            } catch (NumberFormatException ex) {
                budgetStatus.setText("Please enter a valid amount.");
                budgetStatus.getStyleClass().remove("success-label");
                budgetStatus.getStyleClass().add("error-label");
                budgetStatus.setVisible(true);
            }
        });

        budgetRow.getChildren().addAll(dollarSign, budgetField, saveBudgetBtn);
        budgetCard.getChildren().addAll(budgetTitle, budgetDesc, budgetRow, budgetStatus);

        // Data card
        VBox dataCard = new VBox(16);
        dataCard.getStyleClass().add("section-card");
        Label dataTitle = new Label("Data & Storage");
        dataTitle.getStyleClass().add("section-title");

        String dataPath = db.getDataDirectory();
        Label dataPathLabel = new Label("Data folder: " + dataPath);
        dataPathLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        dataPathLabel.setWrapText(true);

        Label dataInfo = new Label("Your data is stored locally in JSON files. No internet connection required.");
        dataInfo.setStyle("-fx-text-fill: #475569; -fx-font-size: 12px;");
        dataInfo.setWrapText(true);

        dataCard.getChildren().addAll(dataTitle, dataPathLabel, dataInfo);

        // Danger zone
        VBox dangerCard = new VBox(16);
        dangerCard.getStyleClass().add("section-card");
        dangerCard.setStyle(dangerCard.getStyle() +
                "-fx-border-color: rgba(239,68,68,0.2); -fx-border-radius: 16; -fx-border-width: 1;");

        Label dangerTitle = new Label("Sign Out");
        dangerTitle.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label dangerDesc = new Label("You'll need to sign in again to access your account.");
        dangerDesc.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px;");

        Button logoutBtn = new Button("Sign Out");
        logoutBtn.getStyleClass().add("btn-danger");

        logoutBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to sign out?",
                    ButtonType.YES, ButtonType.CANCEL);
            confirm.setTitle("Sign Out");
            confirm.setHeaderText(null);
            confirm.getDialogPane().setStyle("-fx-background-color: #1a1d2e;");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    authService.logout();
                    onLogout.run();
                }
            });
        });

        dangerCard.getChildren().addAll(dangerTitle, dangerDesc, logoutBtn);

        getChildren().addAll(titleBox, profileCard, budgetCard, dataCard, dangerCard);

        // Animate
        setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(400), this);
        ft.setToValue(1); ft.play();
    }

    private TextField makeSettingField(GridPane grid, String label, String value, int row) {
        Label lbl = new Label(label);
        lbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-font-weight: bold;");
        TextField field = new TextField(value);
        field.getStyleClass().add("modern-field");
        field.setMaxWidth(320);
        GridPane.setColumnSpan(field, 1);
        grid.add(lbl, 0, row);
        grid.add(field, 1, row);
        return field;
    }
}
