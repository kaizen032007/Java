package com.financetracker.ui;

import com.financetracker.model.Transaction;
import com.financetracker.model.User;
import com.financetracker.service.CsvService;
import com.financetracker.service.TransactionService;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.util.Duration;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class TransactionsPanel extends VBox {

    private final User currentUser;
    private final TransactionService txService;
    private final CsvService csvService;
    private ObservableList<Transaction> tableData;
    private TableView<Transaction> table;
    private Runnable onDataChanged;

    public TransactionsPanel(User currentUser, TransactionService txService, CsvService csvService) {
        this.currentUser = currentUser;
        this.txService = txService;
        this.csvService = csvService;
        buildUI();
    }

    public void setOnDataChanged(Runnable r) { this.onDataChanged = r; }

    private void buildUI() {
        getStyleClass().add("main-content");
        setPadding(new Insets(28, 32, 28, 32));
        setSpacing(20);
        VBox.setVgrow(this, Priority.ALWAYS);

        // Header
        HBox header = new HBox(12);
        header.setAlignment(Pos.CENTER_LEFT);
        VBox titleBox = new VBox(3);
        Label title = new Label("Transactions");
        title.getStyleClass().add("page-title");
        Label subtitle = new Label("All your income and expense records");
        subtitle.getStyleClass().add("page-subtitle");
        titleBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(titleBox, Priority.ALWAYS);

        Button addBtn = new Button("＋ Add Transaction");
        addBtn.getStyleClass().add("btn-primary");

        Button exportBtn = new Button("↓ Export CSV");
        exportBtn.getStyleClass().add("btn-secondary");

        Button importBtn = new Button("↑ Import CSV");
        importBtn.getStyleClass().add("btn-secondary");

        header.getChildren().addAll(titleBox, importBtn, exportBtn, addBtn);

        // Filters
        HBox filterRow = new HBox(12);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> typeFilter = new ComboBox<>();
        typeFilter.getItems().addAll("All Types", "Income", "Expense");
        typeFilter.setValue("All Types");
        typeFilter.getStyleClass().add("modern-combo");
        typeFilter.setStyle("-fx-background-color: #252836; -fx-text-fill: #e2e8f0; " +
                "-fx-border-color: #2d3748; -fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-border-width: 1.5; -fx-pref-height: 38px;");

        ComboBox<String> monthFilter = new ComboBox<>();
        monthFilter.getItems().add("All Time");
        java.time.LocalDate now = LocalDate.now();
        for (int i = 0; i < 12; i++) {
            java.time.LocalDate m = now.minusMonths(i);
            monthFilter.getItems().add(m.getMonth().name() + " " + m.getYear());
        }
        monthFilter.setValue("All Time");
        monthFilter.getStyleClass().add("modern-combo");
        monthFilter.setStyle(typeFilter.getStyle());

        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Search transactions...");
        searchField.getStyleClass().add("modern-field");
        searchField.setPrefWidth(240);
        searchField.setPrefHeight(38);
        searchField.setStyle("-fx-background-color: #252836; -fx-text-fill: #e2e8f0; " +
                "-fx-prompt-text-fill: #4a5568; -fx-border-color: #2d3748; -fx-border-radius: 10; " +
                "-fx-background-radius: 10; -fx-border-width: 1.5; -fx-padding: 8 14;");

        Label countLabel = new Label();
        countLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");
        HBox.setHgrow(countLabel, Priority.ALWAYS);

        filterRow.getChildren().addAll(typeFilter, monthFilter, searchField, countLabel);

        // Table
        tableData = FXCollections.observableArrayList();
        table = new TableView<>(tableData);
        table.getStyleClass().add("modern-table");
        VBox.setVgrow(table, Priority.ALWAYS);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No transactions found."));

        TableColumn<Transaction, String> dateCol = new TableColumn<>("DATE");
        dateCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate().toString()));
        dateCol.setMinWidth(100);

        TableColumn<Transaction, String> typeCol = new TableColumn<>("TYPE");
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getType().name()));
        typeCol.setMinWidth(90);
        typeCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setGraphic(null); setText(null); return; }
                Label badge = new Label(item);
                badge.getStyleClass().add(item.equals("INCOME") ? "badge-income" : "badge-expense");
                setGraphic(badge);
                setText(null);
            }
        });

        TableColumn<Transaction, String> descCol = new TableColumn<>("DESCRIPTION");
        descCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescription()));
        descCol.setMinWidth(160);

        TableColumn<Transaction, String> catCol = new TableColumn<>("CATEGORY");
        catCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategory()));
        catCol.setMinWidth(130);

        TableColumn<Transaction, String> amtCol = new TableColumn<>("AMOUNT");
        amtCol.setCellValueFactory(c -> {
            Transaction t = c.getValue();
            String prefix = t.isIncome() ? "+" : "-";
            return new SimpleStringProperty(prefix + String.format("$%,.2f", t.getAmount()));
        });
        amtCol.setMinWidth(110);
        amtCol.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); return; }
                setText(item);
                setStyle("-fx-text-fill: " + (item.startsWith("+") ? "#10b981" : "#ef4444") +
                        "; -fx-font-weight: bold; -fx-font-size: 13px;");
            }
        });

        TableColumn<Transaction, String> notesCol = new TableColumn<>("NOTES");
        notesCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNotes()));
        notesCol.setMinWidth(100);

        TableColumn<Transaction, Void> actionsCol = new TableColumn<>("ACTIONS");
        actionsCol.setMinWidth(80);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("🗑 Delete");
            { deleteBtn.getStyleClass().add("btn-danger"); }

            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) { setGraphic(null); return; }
                deleteBtn.setOnAction(e -> {
                    Transaction t = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                            "Delete \"" + t.getDescription() + "\"?",
                            ButtonType.YES, ButtonType.CANCEL);
                    confirm.setTitle("Confirm Delete");
                    confirm.setHeaderText(null);
                    styleDialog(confirm);
                    confirm.showAndWait().ifPresent(btn -> {
                        if (btn == ButtonType.YES) {
                            txService.deleteTransaction(t.getId());
                            refreshData();
                            if (onDataChanged != null) onDataChanged.run();
                        }
                    });
                });
                setGraphic(deleteBtn);
            }
        });

        table.getColumns().addAll(dateCol, typeCol, descCol, catCol, amtCol, notesCol, actionsCol);

        loadData();
        updateCount(countLabel);

        // Filter listeners
        typeFilter.setOnAction(e -> applyFilters(typeFilter, monthFilter, searchField, countLabel));
        monthFilter.setOnAction(e -> applyFilters(typeFilter, monthFilter, searchField, countLabel));
        searchField.textProperty().addListener((o, ov, nv) -> applyFilters(typeFilter, monthFilter, searchField, countLabel));

        // Add button action
        addBtn.setOnAction(e -> showAddDialog());

        // Export
        exportBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Export Transactions as CSV");
            fc.setInitialFileName("transactions_export.csv");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fc.showSaveDialog(getScene().getWindow());
            if (file != null) {
                try {
                    csvService.exportTransactions(txService.getUserTransactions(currentUser.getId()), file.getAbsolutePath());
                    showInfo("Export Successful", "Transactions exported to:\n" + file.getAbsolutePath());
                } catch (Exception ex) {
                    showError("Export Failed", ex.getMessage());
                }
            }
        });

        // Import
        importBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.setTitle("Import Transactions from CSV");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fc.showOpenDialog(getScene().getWindow());
            if (file != null) {
                try {
                    List<Transaction> imported = csvService.importTransactions(file.getAbsolutePath(), currentUser.getId());
                    for (Transaction t : imported) txService.addTransaction(
                            t.getUserId(), t.getType(), t.getAmount(),
                            t.getCategory(), t.getDescription(), t.getDate(), t.getNotes());
                    refreshData();
                    if (onDataChanged != null) onDataChanged.run();
                    showInfo("Import Successful", imported.size() + " transactions imported.");
                } catch (Exception ex) {
                    showError("Import Failed", ex.getMessage());
                }
            }
        });

        getChildren().addAll(header, filterRow, table);

        // Animate
        setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(400), this);
        ft.setToValue(1);
        ft.play();
    }

    private void loadData() {
        tableData.setAll(txService.getUserTransactions(currentUser.getId()));
    }

    public void refreshData() {
        loadData();
    }

    private void applyFilters(ComboBox<String> type, ComboBox<String> month,
                               TextField search, Label count) {
        String typeVal = type.getValue();
        String monthVal = month.getValue();
        String searchVal = search.getText().toLowerCase().trim();

        List<Transaction> filtered = txService.getUserTransactions(currentUser.getId());

        if (!typeVal.equals("All Types")) {
            String tFilter = typeVal.toUpperCase();
            filtered = filtered.stream().filter(t -> t.getType().name().equals(tFilter)).toList();
        }

        if (!monthVal.equals("All Time")) {
            String[] parts = monthVal.split(" ");
            java.time.Month m = java.time.Month.valueOf(parts[0]);
            int y = Integer.parseInt(parts[1]);
            filtered = filtered.stream().filter(t ->
                    t.getDate().getMonth() == m && t.getDate().getYear() == y).toList();
        }

        if (!searchVal.isEmpty()) {
            filtered = filtered.stream().filter(t ->
                    t.getDescription().toLowerCase().contains(searchVal) ||
                    t.getCategory().toLowerCase().contains(searchVal) ||
                    (t.getNotes() != null && t.getNotes().toLowerCase().contains(searchVal))).toList();
        }

        tableData.setAll(filtered);
        count.setText(filtered.size() + " transactions");
    }

    private void updateCount(Label count) {
        count.setText(tableData.size() + " transactions");
    }

    private void showAddDialog() {
        Dialog<Transaction> dialog = new Dialog<>();
        dialog.setTitle("Add Transaction");
        dialog.setHeaderText(null);
        styleDialog(dialog);

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(14);
        grid.setPadding(new Insets(20, 30, 10, 30));
        grid.setStyle("-fx-background-color: #1a1d2e;");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("INCOME", "EXPENSE");
        typeBox.setValue("EXPENSE");
        styleCombo(typeBox);

        TextField descField = new TextField();
        descField.setPromptText("e.g. Grocery shopping");
        styleInput(descField);

        TextField amountField = new TextField();
        amountField.setPromptText("0.00");
        styleInput(amountField);

        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll(txService.getExpenseCategories());
        catBox.setValue("Food & Dining");
        styleCombo(catBox);

        typeBox.setOnAction(e -> {
            catBox.getItems().clear();
            if ("INCOME".equals(typeBox.getValue())) {
                catBox.getItems().addAll(txService.getIncomeCategories());
                catBox.setValue("Salary");
            } else {
                catBox.getItems().addAll(txService.getExpenseCategories());
                catBox.setValue("Food & Dining");
            }
        });

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setStyle("-fx-background-color: #252836; -fx-border-color: #2d3748; -fx-border-radius: 10; " +
                "-fx-background-radius: 10; -fx-pref-height: 40px;");

        TextField notesField = new TextField();
        notesField.setPromptText("Optional notes...");
        styleInput(notesField);

        Label errLabel = new Label("");
        errLabel.getStyleClass().add("error-label");

        grid.add(makeDialogLabel("Type"), 0, 0); grid.add(typeBox, 1, 0);
        grid.add(makeDialogLabel("Description"), 0, 1); grid.add(descField, 1, 1);
        grid.add(makeDialogLabel("Amount ($)"), 0, 2); grid.add(amountField, 1, 2);
        grid.add(makeDialogLabel("Category"), 0, 3); grid.add(catBox, 1, 3);
        grid.add(makeDialogLabel("Date"), 0, 4); grid.add(datePicker, 1, 4);
        grid.add(makeDialogLabel("Notes"), 0, 5); grid.add(notesField, 1, 5);
        grid.add(errLabel, 0, 6, 2, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().setStyle("-fx-background-color: #1a1d2e;");

        ButtonType addType = new ButtonType("Add Transaction", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addType, ButtonType.CANCEL);

        javafx.scene.Node addButton = dialog.getDialogPane().lookupButton(addType);
        addButton.setStyle("-fx-background-color: #6366f1; -fx-text-fill: white; " +
                "-fx-background-radius: 8; -fx-font-weight: bold; -fx-padding: 8 18;");

        dialog.setResultConverter(btn -> {
            if (btn == addType) {
                try {
                    double amount = Double.parseDouble(amountField.getText().trim());
                    if (amount <= 0) throw new NumberFormatException();
                    String desc = descField.getText().trim();
                    if (desc.isEmpty()) {
                        Platform.runLater(() -> errLabel.setText("Description cannot be empty."));
                        return null;
                    }
                    Transaction t = txService.addTransaction(
                            currentUser.getId(),
                            Transaction.Type.valueOf(typeBox.getValue()),
                            amount,
                            catBox.getValue(),
                            desc,
                            datePicker.getValue(),
                            notesField.getText().trim()
                    );
                    return t;
                } catch (NumberFormatException ex) {
                    Platform.runLater(() -> errLabel.setText("Please enter a valid positive amount."));
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(t -> {
            if (t != null) {
                refreshData();
                if (onDataChanged != null) onDataChanged.run();
            }
        });
    }

    private Label makeDialogLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 13px; -fx-font-weight: bold;");
        return lbl;
    }

    private void styleInput(TextField field) {
        field.setStyle("-fx-background-color: #252836; -fx-text-fill: #e2e8f0; " +
                "-fx-prompt-text-fill: #4a5568; -fx-border-color: #2d3748; -fx-border-radius: 8; " +
                "-fx-background-radius: 8; -fx-border-width: 1.5; -fx-padding: 9 12; -fx-min-width: 220;");
    }

    private void styleCombo(ComboBox<?> box) {
        box.setStyle("-fx-background-color: #252836; -fx-border-color: #2d3748; " +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-border-width: 1.5; " +
                "-fx-pref-height: 40px; -fx-min-width: 220;");
    }

    private void styleDialog(Dialog<?> dialog) {
        if (dialog.getDialogPane().getScene() != null &&
            dialog.getDialogPane().getScene().getStylesheets() != null) {
            try {
                String css = getClass().getResource("/com/financetracker/css/style.css").toExternalForm();
                dialog.getDialogPane().getScene().getStylesheets().add(css);
            } catch (Exception ignored) {}
        }
        dialog.getDialogPane().setStyle("-fx-background-color: #1a1d2e;");
    }

    private void showInfo(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        styleDialog(alert);
        alert.showAndWait();
    }

    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        styleDialog(alert);
        alert.showAndWait();
    }
}
