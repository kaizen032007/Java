package com.sora.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        StackPane root = new StackPane();

        Label welcomelabel = new Label("Welcome to Sora");
        welcomelabel.getStyleClass().add("welcome-label");

        root.getChildren().add(welcomelabel);
        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/com/sora/ui/style.css").toExternalForm());
        primaryStage.setTitle("Sora");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
