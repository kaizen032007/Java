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
import com.sora.model.User;
import com.sora.service.UsersServices;
import com.sora.service.AccountServices;
import com.sora.service.TransactionServices;

/**
 * Builds the UI layout for the login page, manages user credentials input,
 * handles eye toggles for password visibility, and processes form submissions.
 */
public class LoginView {

    // Member field references used to access inputs inside the login action handler
    private TextField emailFieldRef;
    private PasswordField pwFieldRef;
    private TextField visibleFieldRef;
    private Label titleHeader;

    /**
     * Builds and returns the main HBox structure containing the login form
     * (left panel) and the decorative branding panel (right panel).
     */
    public HBox getLayout() {

        // Setup the left column container holding the inputs and buttons
        VBox leftPanel = new VBox(14);
        leftPanel.setAlignment(Pos.CENTER_LEFT);
        leftPanel.setPadding(new Insets(50, 50, 50, 50));
        leftPanel.getStyleClass().add("left-panel");
        leftPanel.setMinWidth(520);
        leftPanel.setMaxWidth(520);

        // Logo section header
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        ImageView logoIcon = new ImageView(loadImage("Resources/icons/logo.jpg"));
        logoIcon.setFitWidth(45);
        logoIcon.setFitHeight(45);
        logoIcon.setPreserveRatio(true);

        Label logoText = new Label("SORA");
        logoText.getStyleClass().add("logo-text");

        logoBox.getChildren().addAll(logoIcon, logoText);

        // Main welcome slogan heading
        titleHeader = new Label("Unlock Your\nFinancial Clarity");
        titleHeader.getStyleClass().add("titleHeader-text");
        VBox.setMargin(titleHeader, new Insets(18, 0, 6, 0));

        // Construct email and password input panels
        HBox emailBox = buildFieldBox("Resources/icons/person.png", "Email");
        HBox passwordBox = buildPasswordBox();

        // Initialize user service backend connection
        UsersServices usersServices = new UsersServices();

        // Construct the row containing the "Remember Info" checkbox and "Forgot Password" link
        HBox rememberMe = new HBox();
        rememberMe.setAlignment(Pos.CENTER_LEFT);

        CheckBox rememberCheck = new CheckBox("Remember Info");
        rememberCheck.getStyleClass().add("remember-check");

        // Load credentials from disk if "Remember Info" was checked on the previous login session
        String remembered = usersServices.loadRememberedInfo();
        if (remembered != null) {
            String[] parts = remembered.split(" , ", -1);
            if (parts.length >= 2) {
                if (emailFieldRef != null) emailFieldRef.setText(parts[0]);
                if (pwFieldRef != null) pwFieldRef.setText(parts[1]);
                if (visibleFieldRef != null) visibleFieldRef.setText(parts[1]);
                rememberCheck.setSelected(true);
            }
        }

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Hyperlink forgotLink = new Hyperlink("Forgot Password?");
        forgotLink.getStyleClass().add("forgot-link");

        rememberMe.getChildren().addAll(rememberCheck, spacer, forgotLink);

        // Form submission Login button
        Button loginBtn = new Button("Log In");
        loginBtn.getStyleClass().add("login-btn");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        VBox.setMargin(loginBtn, new Insets(6, 0, 0, 0));

        // Submit action: synchronizes password fields, verifies details, and logs user in
        loginBtn.setOnAction(e -> {
            String email = emailFieldRef.getText().trim();

            // Sync hidden and visible password fields in case they toggled the eye button
            if (visibleFieldRef != null && visibleFieldRef.isVisible()) {
                pwFieldRef.setText(visibleFieldRef.getText());
            }

            String password = pwFieldRef.getText().trim();

            UsersServices loginServices = new UsersServices();
            User loggedInUser = loginServices.loginUser(email, password);

            // Display error if credentials mismatch
            if (loggedInUser == null) {
                titleHeader.setText("Invalid email or password.");
                titleHeader.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
                return;
            }

            // Save or clear remember-me data based on user preference checkbox
            if (rememberCheck.isSelected()) {
                loginServices.saveRememberedInfo(email + " , " + password);
            } else {
                loginServices.clearRememberedUser();
            }

            // Initialize dynamic context services for this user's workspace
            AccountServices accountServices = new AccountServices(loggedInUser.getUserId());
            TransactionServices transactionServices = new TransactionServices(loggedInUser.getUserId());

            // Build dashboard UI and swap active scene
            Dashboard dashboard = new Dashboard(loggedInUser, accountServices, transactionServices);
            Scene dashScene = new Scene(dashboard.getLayoutDashboard(), 1200, 750);

            File css1 = new File("Resources/loginStyle.css");
            File css2 = new File("Resources/DashboardStyle.css");
            if (css1.exists())
                dashScene.getStylesheets().add(css1.toURI().toString());
            if (css2.exists())
                dashScene.getStylesheets().add(css2.toURI().toString());

            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(dashScene);
        });

        // Or separator line design
        HBox orRow = new HBox(10);
        orRow.setAlignment(Pos.CENTER);

        Separator sep1 = new Separator();
        HBox.setHgrow(sep1, Priority.ALWAYS);

        Label orLabel = new Label("Or");
        orLabel.getStyleClass().add("or-label");

        Separator sep2 = new Separator();
        HBox.setHgrow(sep2, Priority.ALWAYS);

        orRow.getChildren().addAll(sep1, orLabel, sep2);

        // Navigation button to switch over to the signup page
        Button signupBtn = new Button("Sign up");
        signupBtn.getStyleClass().add("signup-btn");
        signupBtn.setMaxWidth(Double.MAX_VALUE);

        signupBtn.setOnAction(e -> {
            SignupView signupView = new SignupView();
            Scene signupScene = new Scene(signupView.getLayout(), 1200, 750);

            File css = new File("Resources/loginStyle.css");
            if (css.exists())
                signupScene.getStylesheets().add(css.toURI().toString());

            Stage stage = (Stage) signupBtn.getScene().getWindow();
            stage.setScene(signupScene);
        });

        // Continue with Google placeholder button
        Button googleBtn = new Button("Continue with Google");
        googleBtn.getStyleClass().add("google-btn");
        googleBtn.setMaxWidth(Double.MAX_VALUE);

        // Dynamic footer text explaining legal agreements
        Label terms = new Label(
                "By signing in, creating an account, or checking out as Guest " +
                        "you are agreeing to our Terms of Use and our Privacy Policy");
        terms.getStyleClass().add("terms-label");
        terms.setWrapText(true);
        terms.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        VBox.setMargin(terms, new Insets(10, 0, 0, 0));

        // Add all constructed elements sequentially to the left pane
        leftPanel.getChildren().addAll(
                logoBox,
                titleHeader,
                emailBox,
                passwordBox,
                rememberMe,
                loginBtn,
                orRow,
                signupBtn,
                googleBtn,
                terms);

        // Right side decorative hero graphic panel
        StackPane rightPanel = new StackPane();
        rightPanel.getStyleClass().add("right-panel");
        HBox.setHgrow(rightPanel, Priority.ALWAYS);

        ImageView birdImage = new ImageView(loadImage("Resources/icons/bird.jpg"));
        birdImage.setPreserveRatio(false);
        birdImage.fitWidthProperty().bind(rightPanel.widthProperty());
        birdImage.fitHeightProperty().bind(rightPanel.heightProperty());

        rightPanel.getChildren().add(birdImage);

        // Combine left input column and right decorative banner together
        HBox root = new HBox();
        root.getChildren().addAll(leftPanel, rightPanel);

        return root;
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
        field.setPrefWidth(400);
        HBox.setHgrow(field, Priority.ALWAYS);

        // Resets the welcome message style from error back to normal when user changes inputs
        field.textProperty().addListener((obs, oldV, newV) -> {
            titleHeader.setText("Unlock Your\nFinancial Clarity");
            titleHeader.setStyle("-fx-text-fill: #1A3C5E;");
        });

        // Store active reference
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

        // Standard masked field
        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Password");
        pwField.getStyleClass().add("input-field");
        pwField.setPrefWidth(400);
        HBox.setHgrow(pwField, Priority.ALWAYS);

        pwField.textProperty().addListener((obs, oldV, newV) -> {
            titleHeader.setText("Unlock Your\nFinancial Clarity");
            titleHeader.setStyle("-fx-text-fill: #1A3C5E;");
        });

        pwFieldRef = pwField;

        // Plaintext visible field used during eye toggle state
        TextField visibleField = new TextField();
        visibleField.setPromptText("Password");
        visibleField.getStyleClass().add("input-field");
        visibleField.setPrefWidth(400);
        visibleField.setManaged(false);
        visibleField.setVisible(false);
        HBox.setHgrow(visibleField, Priority.ALWAYS);

        visibleField.textProperty().addListener((obs, oldV, newV) -> {
            titleHeader.setText("Unlock Your\nFinancial Clarity");
            titleHeader.setStyle("-fx-text-fill: #1A3C5E;");
        });
        visibleFieldRef = visibleField;

        // Interactive toggle button
        ImageView eyeIcon = new ImageView(loadImage("Resources/icons/eye.png"));
        eyeIcon.setFitWidth(18);
        eyeIcon.setFitHeight(18);
        eyeIcon.setPreserveRatio(true);

        Button eyeBtn = new Button();
        eyeBtn.setGraphic(eyeIcon);
        eyeBtn.getStyleClass().add("eye-btn");

        // Swap components state (visible vs hidden) dynamically on click
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
}