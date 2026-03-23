package com.financetracker;

import com.financetracker.service.*;
import com.financetracker.ui.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private Scene scene;
    private StackPane root;

    private JsonDatabaseService db;
    private AuthService authService;
    private TransactionService txService;
    private CsvService csvService;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        // Initialize services
        db = new JsonDatabaseService();
        authService = new AuthService(db);
        txService = new TransactionService(db);
        csvService = new CsvService();

        root = new StackPane();
        scene = new Scene(root, 1200, 760);

        // Load CSS
        try {
            String css = getClass().getResource("/com/financetracker/css/style.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Warning: Could not load CSS: " + e.getMessage());
        }

        scene.setFill(javafx.scene.paint.Color.web("#0f1117"));

        showLogin();

        stage.setTitle("FinTrack — Personal Finance Manager");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
        stage.centerOnScreen();
    }

    private void showLogin() {
        LoginScreen loginScreen = new LoginScreen(
                authService,
                v -> showMainWindow(),
                this::showRegister
        );
        root.getChildren().setAll(loginScreen);
    }

    private void showRegister() {
        RegisterScreen registerScreen = new RegisterScreen(
                authService,
                this::showLogin,
                successMsg -> showLoginWithMessage(successMsg)
        );
        root.getChildren().setAll(registerScreen);
    }

    private void showLoginWithMessage(String message) {
        // Show login with a success hint
        showLogin();
    }

    private void showMainWindow() {
        MainWindow mainWindow = new MainWindow(
                authService.getCurrentUser(),
                authService,
                txService,
                csvService,
                db,
                this::showLogin
        );
        root.getChildren().setAll(mainWindow);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
