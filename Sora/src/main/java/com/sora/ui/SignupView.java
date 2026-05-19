package com.sora.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import com.sora.service.UsersServices;

/**
 * Builds the UI layout for the user sign up page, performs password match validation,
 * displays form feedback, and routes users back to LoginView upon successful registration.
 */
public class SignupView {

    // Field references to retrieve inputs from UI components
    private TextField firstNameFieldRef;
    private TextField lastNameFieldRef;
    private TextField emailFieldRef;
    private PasswordField pwFieldRef;
    private PasswordField confirmPwFieldRef;
    private Label statusLabel;

    /**
     * Builds and returns the main HBox structure containing the signup form
     * (left panel) and the decorative branding panel (right panel).
     */
    public HBox getLayout() {

        // Left scrollable container for input elements
        VBox leftPanel = new VBox(12);
        leftPanel.setAlignment(Pos.TOP_CENTER);
        leftPanel.setPadding(new Insets(35, 50, 50, 50));
        leftPanel.getStyleClass().add("left-panel");
        leftPanel.setMinWidth(520);
        leftPanel.setMaxWidth(520);

        ScrollPane scrollPane = new ScrollPane(leftPanel);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-control-inner-background: white;");
        scrollPane.getStyleClass().add("left-panel");

        // Branding Logo Area
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        ImageView logoIcon = new ImageView(loadImage("Resources/icons/logo.jpg"));
        logoIcon.setFitWidth(45);
        logoIcon.setFitHeight(45);
        logoIcon.setPreserveRatio(true);

        Label logoText = new Label("SORA");
        logoText.getStyleClass().add("logo-text");

        logoBox.getChildren().addAll(logoIcon, logoText);

        // Header Title
        Label titleHeader = new Label("Create Your Sora Account");
        titleHeader.getStyleClass().add("titleHeader-text");
        VBox.setMargin(titleHeader, new Insets(12, 0, 6, 0));

        // Form feedback label (for displaying verification warnings)
        statusLabel = new Label();
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        statusLabel.setWrapText(true);
        statusLabel.setVisible(false);

        // Form Fields
        HBox firstNameBox = buildFieldBox("Resources/icons/person.png", "First Name");
        HBox lastNameBox = buildFieldBox("Resources/icons/person.png", "Last Name");
        HBox emailBox = buildEmailBox("Resources/icons/person.png", "Email");
        HBox passwordBox = buildPasswordBox();
        HBox confirmPasswordBox = buildConfirmPasswordBox();

        // Sign Up submission button
        Button signupBtn = new Button("Sign Up");
        signupBtn.getStyleClass().add("login-btn");
        signupBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(signupBtn, new Insets(6, 0, 0, 0));

        // Validates passwords match, invokes registration validation, and redirects on success
        signupBtn.setOnAction(e -> {
            String firstName = firstNameFieldRef.getText().trim();
            String lastName = lastNameFieldRef.getText().trim();
            String email = emailFieldRef.getText().trim();
            String password = pwFieldRef.getText().trim();
            String confirmPassword = confirmPwFieldRef.getText().trim();

            if (!password.equals(confirmPassword)) {
                showError("Passwords do not match.");
                return;
            }

            UsersServices usersServices = new UsersServices();
            boolean signupSuccess = usersServices.usersValidation(firstName, lastName, email, password);

            if (!signupSuccess) {
                showError(usersServices.getErrorMessage());
                return;
            }

            // Success feedback
            statusLabel.setText("Account created successfully! Redirecting to login...");
            statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
            statusLabel.setVisible(true);

            // Redirect user to the login screen after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        LoginView loginView = new LoginView();
                        Scene loginScene = new Scene(loginView.getLayout(), 1200, 750);

                        File css = new File("Resources/loginStyle.css");
                        if (css.exists())
                            loginScene.getStylesheets().add(css.toURI().toString());

                        Stage stage = (Stage) signupBtn.getScene().getWindow();
                        stage.setScene(loginScene);
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // Or separator design
        HBox orRow = new HBox(10);
        orRow.setAlignment(Pos.CENTER);

        Separator sep1 = new Separator();
        HBox.setHgrow(sep1, Priority.ALWAYS);

        Label orLabel = new Label("Or");
        orLabel.getStyleClass().add("or-label");

        Separator sep2 = new Separator();
        HBox.setHgrow(sep2, Priority.ALWAYS);

        orRow.getChildren().addAll(sep1, orLabel, sep2);

        // Login redirect box
        HBox loginLinkBox = new HBox();
        loginLinkBox.setAlignment(Pos.CENTER);

        Label alreadyHaveAccount = new Label("Already have an account? ");
        alreadyHaveAccount.getStyleClass().add("or-label");

        Hyperlink loginLink = new Hyperlink("Log In");
        loginLink.getStyleClass().add("forgot-link");

        loginLink.setOnAction(e -> {
            LoginView loginView = new LoginView();
            Scene loginScene = new Scene(loginView.getLayout(), 1200, 750);

            File css = new File("Resources/loginStyle.css");
            if (css.exists())
                loginScene.getStylesheets().add(css.toURI().toString());

            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(loginScene);
        });

        loginLinkBox.getChildren().addAll(alreadyHaveAccount, loginLink);

        // Footer policy statement
        Label terms = new Label(
                "By creating an account, you are agreeing to our Terms of Use and our Privacy Policy");
        terms.getStyleClass().add("terms-label");
        terms.setWrapText(true);
        terms.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        VBox.setMargin(terms, new Insets(10, 0, 0, 0));

        // Assemble left input panel
        leftPanel.getChildren().addAll(
                logoBox,
                titleHeader,
                statusLabel,
                firstNameBox,
                lastNameBox,
                emailBox,
                passwordBox,
                confirmPasswordBox,
                signupBtn,
                orRow,
                loginLinkBox,
                terms);

        // Right panel decorative hero graphic
        StackPane rightPanel = new StackPane();
        rightPanel.getStyleClass().add("right-panel");
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        ImageView birdImage = new ImageView(loadImage("Resources/icons/bird.jpg"));
        birdImage.setPreserveRatio(false);
        birdImage.fitWidthProperty().bind(rightPanel.widthProperty());
        birdImage.fitHeightProperty().bind(rightPanel.heightProperty());

        rightPanel.getChildren().add(birdImage);

        HBox root = new HBox();
        root.getChildren().addAll(leftPanel, rightPanel);

        return root;
    }

    /**
     * Displays a styled error message in the feedback status label.
     */
    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        statusLabel.setVisible(true);
    }

    /**
     * Safely reads an image file from the path without crashing if the resource doesn't exist.
     */
    private Image loadImage(String path) {
        java.io.File file = new java.io.File(path);
        if (file.exists()) {
            return new Image(file.toURI().toString());
        } else {
            System.out.println("Image not found: " + file.getAbsolutePath());
            return null;
        }
    }

    /**
     * Generates a styled input field containing an icon on the left and a text input field on the right.
     */
    private HBox buildFieldBox(String iconPath, String prompt) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("field-box");

        ImageView icon = new ImageView(loadImage(iconPath));
        icon.setFitWidth(18);
        icon.setFitHeight(18);
        icon.setPreserveRatio(true);

        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("input-field");
        HBox.setHgrow(field, Priority.ALWAYS);

        if (prompt.equals("First Name")) {
            firstNameFieldRef = field;
        } else if (prompt.equals("Last Name")) {
            lastNameFieldRef = field;
        }

        box.getChildren().addAll(icon, field);
        return box;
    }

    /**
     * Generates a styled email input field box.
     */
    private HBox buildEmailBox(String iconPath, String prompt) {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("field-box");

        ImageView icon = new ImageView(loadImage(iconPath));
        icon.setFitWidth(18);
        icon.setFitHeight(18);
        icon.setPreserveRatio(true);

        TextField field = new TextField();
        field.setPromptText(prompt);
        field.getStyleClass().add("input-field");
        HBox.setHgrow(field, Priority.ALWAYS);

        emailFieldRef = field;

        box.getChildren().addAll(icon, field);
        return box;
    }

    /**
     * Generates a password input container. Includes eye toggle logic to switch between
     * a PasswordField (masked text) and a TextField (plaintext) dynamically.
     */
    private HBox buildPasswordBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("field-box");

        ImageView lockIcon = new ImageView(loadImage("Resources/icons/lock.png"));
        lockIcon.setFitWidth(18);
        lockIcon.setFitHeight(18);
        lockIcon.setPreserveRatio(true);

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Password");
        pwField.getStyleClass().add("input-field");
        HBox.setHgrow(pwField, Priority.ALWAYS);

        pwFieldRef = pwField;

        TextField visibleField = new TextField();
        visibleField.setPromptText("Password");
        visibleField.getStyleClass().add("input-field");
        visibleField.setManaged(false);
        visibleField.setVisible(false);
        HBox.setHgrow(visibleField, Priority.ALWAYS);

        ImageView eyeIcon = new ImageView(loadImage("Resources/icons/eye.png"));
        eyeIcon.setFitWidth(18);
        eyeIcon.setFitHeight(18);
        eyeIcon.setPreserveRatio(true);

        Button eyeBtn = new Button();
        eyeBtn.setGraphic(eyeIcon);
        eyeBtn.getStyleClass().add("eye-btn");

        final boolean[] shown = { false };
        eyeBtn.setOnAction(e -> {
            shown[0] = !shown[0];
            if (shown[0]) {
                visibleField.setText(pwField.getText());
                visibleField.setVisible(true);
                visibleField.setManaged(true);
                pwField.setVisible(false);
                pwField.setManaged(false);
            } else {
                pwField.setText(visibleField.getText());
                pwField.setVisible(true);
                pwField.setManaged(true);
                visibleField.setVisible(false);
                visibleField.setManaged(false);
            }
        });

        box.getChildren().addAll(lockIcon, pwField, visibleField, eyeBtn);
        return box;
    }

    /**
     * Generates a confirm password input container. Includes eye toggle logic.
     */
    private HBox buildConfirmPasswordBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getStyleClass().add("field-box");

        ImageView lockIcon = new ImageView(loadImage("Resources/icons/lock.png"));
        lockIcon.setFitWidth(18);
        lockIcon.setFitHeight(18);
        lockIcon.setPreserveRatio(true);

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Confirm Password");
        pwField.getStyleClass().add("input-field");
        HBox.setHgrow(pwField, Priority.ALWAYS);

        confirmPwFieldRef = pwField;

        TextField visibleField = new TextField();
        visibleField.setPromptText("Confirm Password");
        visibleField.getStyleClass().add("input-field");
        visibleField.setManaged(false);
        visibleField.setVisible(false);
        HBox.setHgrow(visibleField, Priority.ALWAYS);

        ImageView eyeIcon = new ImageView(loadImage("Resources/icons/eye.png"));
        eyeIcon.setFitWidth(18);
        eyeIcon.setFitHeight(18);
        eyeIcon.setPreserveRatio(true);

        Button eyeBtn = new Button();
        eyeBtn.setGraphic(eyeIcon);
        eyeBtn.getStyleClass().add("eye-btn");

        final boolean[] shown = { false };
        eyeBtn.setOnAction(e -> {
            shown[0] = !shown[0];
            if (shown[0]) {
                visibleField.setText(pwField.getText());
                visibleField.setVisible(true);
                visibleField.setManaged(true);
                pwField.setVisible(false);
                pwField.setManaged(false);
            } else {
                pwField.setText(visibleField.getText());
                pwField.setVisible(true);
                pwField.setManaged(true);
                visibleField.setVisible(false);
                visibleField.setManaged(false);
            }
        });

        box.getChildren().addAll(lockIcon, pwField, visibleField, eyeBtn);
        return box;
    }

    public String getErrorMessage() {
        return statusLabel.getText();
    }
}
