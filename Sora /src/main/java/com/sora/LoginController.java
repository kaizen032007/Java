package com.sora;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField userIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private UserServices userService = new UserServices();

    @FXML
    public void handleLogin() {
        String userId = userIdField.getText();
        String password = passwordField.getText();

        if (userService.loginUsers(userId, password)) {
            messageLabel.setText("Login successful!");
            messageLabel.setStyle("-fx-text-fill: green;");
        } else {
            messageLabel.setText("Invalid credentials. Try again.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }
}
