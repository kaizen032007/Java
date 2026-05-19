package com.sora.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.File;

/**
 * Entry point for the JavaFX application. Loads settings, styling, fonts,
 * and sets up the primary stage using either auto-login credentials or the LoginView.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Load custom application branding font (Public Sans Bold)
        File fontFile = new File("Resources/fonts/Public_Sans/static/PublicSans-Bold.ttf");
        if (fontFile.exists()) {
            Font.loadFont(fontFile.toURI().toString(), 22);
        } else {
            System.out.println("Font not found at: " + fontFile.getAbsolutePath());
        }

        // Initialize user services to verify auto-login credentials if they exist
        com.sora.service.UsersServices usersServices = new com.sora.service.UsersServices();
        String remembered = usersServices.loadRememberedInfo();
        Scene scene = null;
        
        // Auto-login flow: If "Remember Info" was checked previously, try logging in
        if (remembered != null) {
            String[] parts = remembered.split(" , ", -1);
            if (parts.length >= 2) {
                String email = parts[0];
                String password = parts[1];
                com.sora.model.User loggedInUser = usersServices.loginUser(email, password);
                if (loggedInUser != null) {
                    // Pre-load services specific to this logged in user ID
                    com.sora.service.AccountServices accountServices = new com.sora.service.AccountServices(loggedInUser.getUserId());
                    com.sora.service.TransactionServices transactionServices = new com.sora.service.TransactionServices(loggedInUser.getUserId());
                    Dashboard dashboard = new Dashboard(loggedInUser, accountServices, transactionServices);
                    scene = new Scene(dashboard.getLayoutDashboard(), 1200, 750);
                }
            }
        }

        // Fallback: Show standard login page if no remembered session exists
        if (scene == null) {
            LoginView loginView = new LoginView();
            scene = new Scene(loginView.getLayout(), 1200, 750);
        }

        // Load CSS stylesheets
        File cssFile = new File("Resources/loginStyle.css");
        if (cssFile.exists()) {
            scene.getStylesheets().add(cssFile.toURI().toString());
        } else {
            System.out.println("CSS not found at: " + cssFile.getAbsolutePath());
        }

        File cssFile2 = new File("Resources/DashboardStyle.css");
        if (cssFile2.exists()) {
            scene.getStylesheets().add(cssFile2.toURI().toString());
        } else {
            System.out.println("CSS not found at " + cssFile2.getAbsolutePath());
        }

        // Configure window and display
        primaryStage.setTitle("Sora");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}