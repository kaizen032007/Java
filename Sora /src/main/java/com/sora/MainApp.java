package com.sora;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class MainApp extends Application {
    private Stage primaryStage;
    private String currentUserId;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    public void showLoginScreen() {
        primaryStage.setTitle("Sora");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Welcome to Sora");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField userIdField = new TextField();
        userIdField.setPromptText("User ID: ");
        userIdField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password: ");
        passwordField.setMaxWidth(250);

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Create an Account");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String userId = userIdField.getText();
            String password = passwordField.getText();

            UserServices userServices = new UserServices();

            if (userServices.loginUsers(userId, password)) {
                currentUserId = userId;
                showDashboard();
                messageLabel.setText("Login Succesful!");
                messageLabel.setStyle("-fx-text-fill: green;");
            } else {
                messageLabel.setText("Invalid User ID or Password");
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        registerButton.setOnAction(e -> {
            showRegisterScreen();
        });

        layout.getChildren().addAll(titleLabel, userIdField, passwordField, loginButton,
                registerButton, messageLabel);

        Scene scene = new Scene(layout, 1000, 700);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public void showRegisterScreen() {
        primaryStage.setTitle("Sora: Create your account");
        VBox registerLayout = new VBox(10);

        registerLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Create your account here!");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("Enter your First Name: ");
        firstNameField.setMaxWidth(250);

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Enter your Last Name: ");
        lastNameField.setMaxWidth(250);

        TextField emailField = new TextField();
        emailField.setPromptText("Enter your Email: ");
        emailField.setMaxWidth(250);

        PasswordField createPasswordField = new PasswordField();
        createPasswordField.setPromptText("Enter your password: ");
        createPasswordField.setMaxWidth(250);

        Button registerButton = new Button("Register Account");
        Label regMessageLabel = new Label();

        registerButton.setOnAction(e -> {
            String userId = "U-" + System.currentTimeMillis();
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password = createPasswordField.getText();

            UserServices userServices = new UserServices();

            if (userServices.registerUsers(userId, firstName, lastName, email, password)) {
                regMessageLabel.setText("Registration Successful!");
                regMessageLabel.setStyle("-fx-text-fill: green;");
            } else {
                regMessageLabel.setText("Registration unsuccessful!");
                regMessageLabel.setStyle("-fx-text-fill: red;");
            }
        });

        Button backButton = new Button("Go Back");
        backButton.setOnAction(e -> showLoginScreen());

        registerLayout.getChildren().addAll(titleLabel, firstNameField, lastNameField, emailField,
                createPasswordField, registerButton, regMessageLabel, backButton);

        Scene registerScene = new Scene(registerLayout, 1000, 700);
        primaryStage.setScene(registerScene);
    }

    public void showDashboard() {
        UserServices userServices = new UserServices();
        User currentUser = userServices.getUserById(currentUserId);

        String displayName;

        if (currentUser != null) {
            displayName = currentUser.getFirstName();
        } else {
            displayName = currentUserId;
        }
        VBox dashboardLayout = new VBox(10);
        dashboardLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome " + displayName);
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label transactionLabel = new Label("Your transaction will appear here");

        Button incomeButton = new Button("Add Income");
        Button expenseButton = new Button("Add Expense");
        Button logoutBUtton = new Button("Logout");

        logoutBUtton.setOnAction(e -> {
            currentUserId = null;
            showLoginScreen();
        });

        dashboardLayout.getChildren().addAll(welcomeLabel, transactionLabel, incomeButton,
                expenseButton, logoutBUtton);

        Scene dashScene = new Scene(dashboardLayout, 1000, 700);
        primaryStage.setScene(dashScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
