package com.financetracker.ui;

import com.financetracker.model.Transaction;
import com.financetracker.model.User;
import com.financetracker.service.TransactionService;
import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class DashboardPanel extends ScrollPane {

    private final User currentUser;
    private final TransactionService txService;
    private VBox content;

    public DashboardPanel(User currentUser, TransactionService txService) {
        this.currentUser = currentUser;
        this.txService = txService;
        buildUI();
        setFitToWidth(true);
        setStyle("-fx-background-color: #0f1117; -fx-background: #0f1117; -fx-border-color: transparent;");
    }

    private void buildUI() {
        content = new VBox(24);
        content.getStyleClass().add("main-content");
        content.setPadding(new Insets(28, 32, 28, 32));

        // Header
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(3);
        LocalDate now = LocalDate.now();
        String monthName = now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        Label title = new Label("Dashboard");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label(monthName + " " + now.getYear() + " overview");
        subtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(title, subtitle);
        header.getChildren().add(titleBox);

        // Stat cards
        HBox statsRow = new HBox(16);
        statsRow.setAlignment(Pos.CENTER_LEFT);

        List<Transaction> allTx = txService.getUserTransactions(currentUser.getId());
        List<Transaction> monthTx = txService.getTransactionsByMonth(
                currentUser.getId(), now.getYear(), now.getMonthValue());

        double income = txService.getTotalIncome(monthTx);
        double expenses = txService.getTotalExpenses(monthTx);
        double balance = txService.getBalance(allTx);
        double budget = currentUser.getMonthlyBudget();

        statsRow.getChildren().addAll(
            makeStatCard("💵", "Monthly Income", String.format("+$%,.2f", income), "stat-card-income", "stat-value-income"),
            makeStatCard("💸", "Monthly Expenses", String.format("-$%,.2f", expenses), "stat-card-expense", "stat-value-expense"),
            makeStatCard("🏦", "Total Balance", String.format("$%,.2f", balance), "stat-card-balance", "stat-value-balance"),
            makeStatCard("🎯", "Budget Remaining",
                budget > 0 ? String.format("$%,.2f", Math.max(0, budget - expenses)) : "Not set",
                "stat-card-budget", "stat-value-budget")
        );

        // Budget progress bar
        VBox budgetSection = null;
        if (budget > 0 && expenses > 0) {
            budgetSection = new VBox(10);
            budgetSection.getStyleClass().add("section-card");

            Label budgetTitle = new Label("Budget Usage — " + monthName);
            budgetTitle.getStyleClass().add("section-title");

            double ratio = Math.min(1.0, expenses / budget);
            ProgressBar bar = new ProgressBar(ratio);
            bar.setPrefWidth(Double.MAX_VALUE);
            bar.setPrefHeight(12);
            if (ratio < 0.6) {
                bar.getStyleClass().addAll("budget-progress", "budget-progress-ok");
            } else if (ratio < 0.85) {
                bar.getStyleClass().addAll("budget-progress", "budget-progress-warn");
            } else {
                bar.getStyleClass().addAll("budget-progress", "budget-progress-over");
            }

            String budgetLabelStr = String.format("$%,.2f spent of $%,.2f budget (%.0f%%)",
                    expenses, budget, ratio * 100);
            Label budgetLbl = new Label(budgetLabelStr);
            budgetLbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px;");

            budgetSection.getChildren().addAll(budgetTitle, bar, budgetLbl);
        }

        // Charts row
        HBox chartsRow = new HBox(16);
        chartsRow.setFillHeight(true);

        // Bar chart - last 6 months
        VBox barChartCard = new VBox(12);
        barChartCard.getStyleClass().add("section-card");
        HBox.setHgrow(barChartCard, Priority.ALWAYS);

        Label barTitle = new Label("Income vs Expenses (6 months)");
        barTitle.getStyleClass().add("section-title");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#64748b"));
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#64748b"));
        xAxis.setStyle("-fx-tick-label-fill: #64748b;");
        yAxis.setStyle("-fx-tick-label-fill: #64748b;");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLegendVisible(true);
        barChart.setAnimated(true);
        barChart.setPrefHeight(260);
        barChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expenses");

        for (int i = 5; i >= 0; i--) {
            LocalDate m = now.minusMonths(i);
            String label = m.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            List<Transaction> mTx = txService.getTransactionsByMonth(
                    currentUser.getId(), m.getYear(), m.getMonthValue());
            incomeSeries.getData().add(new XYChart.Data<>(label, txService.getTotalIncome(mTx)));
            expenseSeries.getData().add(new XYChart.Data<>(label, txService.getTotalExpenses(mTx)));
        }
        barChart.getData().addAll(incomeSeries, expenseSeries);
        barChartCard.getChildren().addAll(barTitle, barChart);

        // Category breakdown
        VBox catCard = new VBox(12);
        catCard.getStyleClass().add("section-card");
        catCard.setMinWidth(240);
        catCard.setMaxWidth(280);

        Label catTitle = new Label("Top Expenses");
        catTitle.getStyleClass().add("section-title");

        Map<String, Double> byCat = txService.getExpensesByCategory(monthTx);
        VBox catList = new VBox(8);
        byCat.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(6)
                .forEach(entry -> {
                    HBox row = new HBox(8);
                    row.setAlignment(Pos.CENTER_LEFT);
                    Label name = new Label(entry.getKey());
                    name.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");
                    HBox.setHgrow(name, Priority.ALWAYS);
                    Label val = new Label(String.format("$%,.2f", entry.getValue()));
                    val.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 12px; -fx-font-weight: bold;");
                    row.getChildren().addAll(name, val);
                    catList.getChildren().add(row);
                });

        if (byCat.isEmpty()) {
            Label empty = new Label("No expenses this month");
            empty.setStyle("-fx-text-fill: #475569; -fx-font-size: 13px;");
            catList.getChildren().add(empty);
        }

        catCard.getChildren().addAll(catTitle, catList);
        chartsRow.getChildren().addAll(barChartCard, catCard);

        // Recent transactions
        VBox recentCard = new VBox(12);
        recentCard.getStyleClass().add("section-card");

        Label recentTitle = new Label("Recent Transactions");
        recentTitle.getStyleClass().add("section-title");

        VBox txList = new VBox(0);
        List<Transaction> recent = allTx.stream().limit(8).toList();

        for (Transaction t : recent) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10, 4, 10, 4));
            row.setStyle("-fx-border-color: transparent transparent #1e2231 transparent; -fx-border-width: 0 0 1 0;");

            Label typeIcon = new Label(t.isIncome() ? "⬆" : "⬇");
            typeIcon.setStyle("-fx-text-fill: " + (t.isIncome() ? "#10b981" : "#ef4444") + "; -fx-font-size: 14px;");

            VBox info = new VBox(2);
            Label desc = new Label(t.getDescription());
            desc.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 13px;");
            Label cat = new Label(t.getCategory() + " • " + t.getDate().toString());
            cat.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11px;");
            info.getChildren().addAll(desc, cat);
            HBox.setHgrow(info, Priority.ALWAYS);

            Label amount = new Label((t.isIncome() ? "+" : "-") + String.format("$%,.2f", t.getAmount()));
            amount.setStyle("-fx-text-fill: " + (t.isIncome() ? "#10b981" : "#ef4444") +
                    "; -fx-font-size: 14px; -fx-font-weight: bold;");

            row.getChildren().addAll(typeIcon, info, amount);
            txList.getChildren().add(row);
        }

        if (recent.isEmpty()) {
            Label empty = new Label("No transactions yet. Add your first transaction!");
            empty.setStyle("-fx-text-fill: #475569; -fx-font-size: 13px; -fx-padding: 20;");
            txList.getChildren().add(empty);
        }

        recentCard.getChildren().addAll(recentTitle, txList);

        content.getChildren().addAll(header, statsRow);
        if (budgetSection != null) content.getChildren().add(budgetSection);
        content.getChildren().addAll(chartsRow, recentCard);

        setContent(content);

        // Stagger animations
        animateChildren();
    }

    private VBox makeStatCard(String icon, String label, String value, String cardStyle, String valueStyle) {
        VBox card = new VBox(6);
        card.getStyleClass().addAll("stat-card", cardStyle);
        HBox.setHgrow(card, Priority.ALWAYS);

        Label iconLbl = new Label(icon);
        iconLbl.getStyleClass().add("stat-icon");
        Label lbl = new Label(label);
        lbl.getStyleClass().add("stat-label");
        Label val = new Label(value);
        val.getStyleClass().addAll("stat-value", valueStyle);
        val.setWrapText(true);

        card.getChildren().addAll(iconLbl, lbl, val);
        return card;
    }

    private void animateChildren() {
        for (int i = 0; i < content.getChildren().size(); i++) {
            javafx.scene.Node node = content.getChildren().get(i);
            node.setOpacity(0);
            node.setTranslateY(15);
            FadeTransition ft = new FadeTransition(Duration.millis(400), node);
            ft.setToValue(1);
            TranslateTransition tt = new TranslateTransition(Duration.millis(400), node);
            tt.setToY(0);
            ParallelTransition pt = new ParallelTransition(ft, tt);
            pt.setDelay(Duration.millis(60 * i));
            pt.play();
        }
    }

    public void refresh() {
        content.getChildren().clear();
        buildUI();
    }
}
