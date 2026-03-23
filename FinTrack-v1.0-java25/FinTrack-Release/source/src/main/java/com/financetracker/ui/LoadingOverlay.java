package com.financetracker.ui;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class LoadingOverlay extends StackPane {

    private final Label titleLabel;
    private final Label statusLabel;
    private final ProgressBar progressBar;
    private Timeline dotAnimation;

    public LoadingOverlay() {
        getStyleClass().add("loading-overlay");
        setAlignment(Pos.CENTER);

        // Card
        VBox card = new VBox(16);
        card.getStyleClass().add("loading-card");
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(360);

        // Spinner dots
        HBox dots = new HBox(8);
        dots.setAlignment(Pos.CENTER);
        for (int i = 0; i < 5; i++) {
            Region dot = new Region();
            dot.setPrefSize(8, 8);
            dot.setStyle("-fx-background-color: #6366f1; -fx-background-radius: 4;");
            dots.getChildren().add(dot);
        }
        animateDots(dots);

        titleLabel = new Label("Loading...");
        titleLabel.getStyleClass().add("loading-title");
        titleLabel.setAlignment(Pos.CENTER);

        statusLabel = new Label("Please wait...");
        statusLabel.getStyleClass().add("loading-status");
        statusLabel.setAlignment(Pos.CENTER);

        progressBar = new ProgressBar(-1); // indeterminate
        progressBar.getStyleClass().add("progress-bar-modern");
        progressBar.setPrefWidth(300);
        progressBar.setPrefHeight(6);

        card.getChildren().addAll(dots, titleLabel, statusLabel, progressBar);
        getChildren().add(card);
    }

    private void animateDots(HBox dots) {
        Timeline tl = new Timeline();
        for (int i = 0; i < dots.getChildren().size(); i++) {
            final int idx = i;
            Region dot = (Region) dots.getChildren().get(idx);
            KeyFrame kf1 = new KeyFrame(Duration.millis(i * 120),
                    new KeyValue(dot.opacityProperty(), 0.2));
            KeyFrame kf2 = new KeyFrame(Duration.millis(i * 120 + 300),
                    new KeyValue(dot.opacityProperty(), 1.0));
            KeyFrame kf3 = new KeyFrame(Duration.millis(i * 120 + 600),
                    new KeyValue(dot.opacityProperty(), 0.2));
            tl.getKeyFrames().addAll(kf1, kf2, kf3);
        }
        tl.setCycleCount(Animation.INDEFINITE);
        tl.play();
        this.dotAnimation = tl;
    }

    public void setTitle(String title) {
        Platform.runLater(() -> titleLabel.setText(title));
    }

    public void setStatus(String status) {
        Platform.runLater(() -> statusLabel.setText(status));
    }

    public void setProgress(double progress) {
        Platform.runLater(() -> {
            if (progress < 0) {
                progressBar.setProgress(-1);
            } else {
                progressBar.setProgress(progress);
            }
        });
    }

    public void stop() {
        if (dotAnimation != null) dotAnimation.stop();
    }
}
