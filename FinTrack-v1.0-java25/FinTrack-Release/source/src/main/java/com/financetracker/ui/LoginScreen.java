package com.financetracker.ui;

import com.financetracker.service.AuthService;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.function.Consumer;

public class LoginScreen extends StackPane {

    private final AuthService authService;
    private final Consumer<Void> onLoginSuccess;
    private final Runnable onRegister;

    public LoginScreen(AuthService authService, Consumer<Void> onLoginSuccess, Runnable onRegister) {
        this.authService = authService;
        this.onLoginSuccess = onLoginSuccess;
        this.onRegister = onRegister;
        buildUI();
    }

    private void buildUI() {
        getStyleClass().add("auth-root");
        setStyle("-fx-background-color: #0f1117;");

        // Background accent shape
        Rectangle bgAccent = new Rectangle(600, 600);
        bgAccent.setFill(new RadialGradient(0, 0, 0.5, 0.5, 0.6, true,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#6366f1", 0.08)),
                new Stop(1, Color.web("#6366f1", 0.0))));
        bgAccent.setTranslateX(-200);
        bgAccent.setTranslateY(-100);

        // Main layout
        HBox mainLayout = new HBox();
        mainLayout.setAlignment(Pos.CENTER);

        // Left branding panel
        VBox brandPanel = new VBox(20);
        brandPanel.setAlignment(Pos.CENTER);
        brandPanel.setPadding(new Insets(60));
        brandPanel.setMinWidth(380);
        brandPanel.setStyle("-fx-background-color: linear-gradient(to bottom right, #0f1117, #1a1d2e);");

        Label logoText = new Label("💰 FinTrack");
        logoText.getStyleClass().add("app-logo-text");

        Label tagline = new Label("Your personal finance\nmanager — offline & secure");
        tagline.getStyleClass().add("app-tagline");
        tagline.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        tagline.setWrapText(true);

        // Feature bullets
        VBox features = new VBox(14);
        features.setPadding(new Insets(30, 0, 0, 0));
        String[] feats = {"📊 Track income & expenses", "📁 Export to CSV anytime",
                "📈 Visual spending charts", "🔒 Fully offline & private"};
        for (String f : feats) {
            Label fl = new Label(f);
            fl.setStyle("-fx-text-fill: #475569; -fx-font-size: 13px;");
            features.getChildren().add(fl);
        }

        brandPanel.getChildren().addAll(logoText, tagline, features);

        // Right login card
        VBox loginCard = new VBox(18);
        loginCard.getStyleClass().add("auth-card");
        loginCard.setMinWidth(380);
        loginCard.setMaxWidth(420);
        loginCard.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Welcome back");
        title.getStyleClass().add("auth-title");

        Label subtitle = new Label("Sign in to your account");
        subtitle.getStyleClass().add("auth-subtitle");

        VBox formBox = new VBox(14);
        formBox.setPadding(new Insets(10, 0, 0, 0));

        // Username field
        VBox userGroup = new VBox(5);
        Label userLabel = new Label("USERNAME OR EMAIL");
        userLabel.getStyleClass().add("field-label");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username or email");
        usernameField.getStyleClass().add("modern-field");
        usernameField.setMaxWidth(Double.MAX_VALUE);
        userGroup.getChildren().addAll(userLabel, usernameField);

        // Password field
        VBox passGroup = new VBox(5);
        Label passLabel = new Label("PASSWORD");
        passLabel.getStyleClass().add("field-label");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.getStyleClass().add("modern-field");
        passwordField.setMaxWidth(Double.MAX_VALUE);
        passGroup.getChildren().addAll(passLabel, passwordField);

        formBox.getChildren().addAll(userGroup, passGroup);

        // Error label
        Label errorLabel = new Label("");
        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);

        // Login button
        Button loginBtn = new Button("Sign In");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);

        // Register link
        HBox registerRow = new HBox(6);
        registerRow.setAlignment(Pos.CENTER);
        Label noAccount = new Label("Don't have an account?");
        noAccount.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13px;");
        Button registerBtn = new Button("Create one");
        registerBtn.getStyleClass().add("link-btn");
        registerRow.getChildren().addAll(noAccount, registerBtn);

        loginCard.getChildren().addAll(title, subtitle, formBox, errorLabel, loginBtn, registerRow);

        mainLayout.getChildren().addAll(brandPanel, loginCard);

        // Loading overlay
        LoadingOverlay loadingOverlay = new LoadingOverlay();
        loadingOverlay.setVisible(false);

        getChildren().addAll(bgAccent, mainLayout, loadingOverlay);

        // Animate in
        loginCard.setOpacity(0);
        loginCard.setTranslateY(20);
        FadeTransition fade = new FadeTransition(Duration.millis(500), loginCard);
        fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), loginCard);
        slide.setToY(0);
        ParallelTransition anim = new ParallelTransition(fade, slide);
        anim.setDelay(Duration.millis(100));
        anim.play();

        // Login action
        loginBtn.setOnAction(e -> {
            String user = usernameField.getText().trim();
            String pass = passwordField.getText();

            if (user.isEmpty() || pass.isEmpty()) {
                showError(errorLabel, "Please fill in all fields.");
                return;
            }

            loadingOverlay.setVisible(true);
            loadingOverlay.setTitle("Signing you in...");
            loadingOverlay.setStatus("Checking credentials...");

            new Thread(() -> {
                try {
                    Thread.sleep(300);
                    loadingOverlay.setStatus("Verifying password...");
                    Thread.sleep(400);

                    AuthService.LoginResult result = authService.login(user, pass);

                    Thread.sleep(200);
                    Platform.runLater(() -> {
                        loadingOverlay.setVisible(false);
                        if (result == AuthService.LoginResult.SUCCESS) {
                            onLoginSuccess.accept(null);
                        } else {
                            showError(errorLabel, "Invalid username/email or password.");
                        }
                    });
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });

        // Enter key
        passwordField.setOnAction(e -> loginBtn.fire());
        usernameField.setOnAction(e -> passwordField.requestFocus());

        registerBtn.setOnAction(e -> onRegister.run());
    }

    private void showError(Label label, String msg) {
        label.setText(msg);
        label.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(200), label);
        ft.setFromValue(0); ft.setToValue(1); ft.play();
    }
}
