package com.financetracker.ui;

import com.financetracker.service.AuthService;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.function.Consumer;

public class RegisterScreen extends StackPane {

    private final AuthService authService;
    private final Runnable onLoginLink;
    private final Consumer<String> onSuccess;

    public RegisterScreen(AuthService authService, Runnable onLoginLink, Consumer<String> onSuccess) {
        this.authService = authService;
        this.onLoginLink = onLoginLink;
        this.onSuccess = onSuccess;
        buildUI();
    }

    private void buildUI() {
        setStyle("-fx-background-color: #0f1117;");
        setAlignment(Pos.CENTER);

        VBox card = new VBox(16);
        card.getStyleClass().add("auth-card");
        card.setMaxWidth(440);
        card.setMinWidth(380);
        card.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Create Account");
        title.getStyleClass().add("auth-title");

        Label subtitle = new Label("Join FinTrack — it's free forever");
        subtitle.getStyleClass().add("auth-subtitle");

        VBox form = new VBox(13);
        form.setPadding(new Insets(8, 0, 0, 0));

        TextField fullNameField = makeField(form, "FULL NAME", "Your full name");
        TextField usernameField = makeField(form, "USERNAME", "Choose a username");
        TextField emailField = makeField(form, "EMAIL ADDRESS", "your@email.com");
        PasswordField passwordField = makePassField(form, "PASSWORD", "Create a strong password");
        PasswordField confirmPassField = makePassField(form, "CONFIRM PASSWORD", "Repeat your password");

        Label errorLabel = new Label("");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(380);

        Button createBtn = new Button("Create Account");
        createBtn.getStyleClass().add("btn-primary");
        createBtn.setMaxWidth(Double.MAX_VALUE);

        HBox loginRow = new HBox(6);
        loginRow.setAlignment(Pos.CENTER);
        Label hasAccount = new Label("Already have an account?");
        hasAccount.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px;");
        Button loginBtn = new Button("Sign in");
        loginBtn.getStyleClass().add("link-btn");
        loginRow.getChildren().addAll(hasAccount, loginBtn);

        card.getChildren().addAll(title, subtitle, form, errorLabel, createBtn, loginRow);

        LoadingOverlay loadingOverlay = new LoadingOverlay();
        loadingOverlay.setVisible(false);

        getChildren().addAll(card, loadingOverlay);

        // Animate in
        card.setOpacity(0);
        card.setTranslateY(20);
        FadeTransition fade = new FadeTransition(Duration.millis(500), card);
        fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), card);
        slide.setToY(0);
        new ParallelTransition(fade, slide).play();

        createBtn.setOnAction(e -> {
            String fullName = fullNameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String pass = passwordField.getText();
            String confirm = confirmPassField.getText();

            if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                showError(errorLabel, "Please fill in all fields.");
                return;
            }
            if (username.length() < 3) {
                showError(errorLabel, "Username must be at least 3 characters.");
                return;
            }
            if (!email.contains("@") || !email.contains(".")) {
                showError(errorLabel, "Please enter a valid email address.");
                return;
            }
            if (pass.length() < 6) {
                showError(errorLabel, "Password must be at least 6 characters.");
                return;
            }
            if (!pass.equals(confirm)) {
                showError(errorLabel, "Passwords do not match.");
                return;
            }

            loadingOverlay.setVisible(true);
            loadingOverlay.setTitle("Creating your account...");
            loadingOverlay.setStatus("Preparing your profile...");
            loadingOverlay.setProgress(0.1);

            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    loadingOverlay.setStatus("Encrypting password...");
                    loadingOverlay.setProgress(0.35);
                    Thread.sleep(400);
                    loadingOverlay.setStatus("Saving to database...");
                    loadingOverlay.setProgress(0.65);
                    Thread.sleep(350);

                    AuthService.RegisterResult result = authService.register(username, email, pass, fullName);

                    loadingOverlay.setProgress(0.9);
                    Thread.sleep(250);
                    loadingOverlay.setProgress(1.0);
                    Thread.sleep(200);

                    Platform.runLater(() -> {
                        loadingOverlay.setVisible(false);
                        switch (result) {
                            case SUCCESS -> onSuccess.accept("Account created! Please sign in.");
                            case USERNAME_TAKEN -> showError(errorLabel, "That username is already taken. Try another.");
                            case EMAIL_TAKEN -> showError(errorLabel, "An account with that email already exists.");
                            case INVALID_INPUT -> showError(errorLabel, "Please check your input and try again.");
                        }
                    });
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });

        loginBtn.setOnAction(e -> onLoginLink.run());
    }

    private TextField makeField(VBox form, String labelText, String prompt) {
        VBox group = new VBox(5);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("field-label");
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("modern-field");
        field.setMaxWidth(Double.MAX_VALUE);
        group.getChildren().addAll(lbl, field);
        form.getChildren().add(group);
        return field;
    }

    private PasswordField makePassField(VBox form, String labelText, String prompt) {
        VBox group = new VBox(5);
        Label lbl = new Label(labelText);
        lbl.getStyleClass().add("field-label");
        PasswordField field = new PasswordField();
        field.setPromptText(prompt);
        field.getStyleClass().add("modern-field");
        field.setMaxWidth(Double.MAX_VALUE);
        group.getChildren().addAll(lbl, field);
        form.getChildren().add(group);
        return field;
    }

    private void showError(Label label, String msg) {
        label.setText(msg);
        label.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(200), label);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }
}
