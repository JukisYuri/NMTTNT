package view;

import controller.SpamController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.DecisionTree;
import model.EmailResult;
import java.util.List;

public class MailFilterFrame extends Application {

    private SpamController controller;
    private TextArea bulkInputArea;
    private TableView<EmailResult> spamTable;
    private TableView<EmailResult> hamTable;
    private TextArea reasonArea;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hệ thống Phân loại Email Hàng loạt");

        // Khởi tạo Model & Controller
        DecisionTree model = new DecisionTree();
        controller = new SpamController(this, model);
        controller.initializeModelFromDataset();

        // --- 1. KHU VỰC NHẬP LIỆU (TOP) ---
        VBox inputSection = new VBox(10);
        Label inputLabel = new Label("Nhập nội dung các Email (Phân cách bằng 3 lần xuống dòng):");
        inputLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        bulkInputArea = new TextArea();
        bulkInputArea.setPromptText("Email 1...\n\n\nEmail 2...\n\n\nEmail 3...");
        bulkInputArea.setPrefHeight(150);
        bulkInputArea.setWrapText(true);

        Button submitBtn = new Button("KIỂM TRA HÀNG LOẠT (SUBMIT)");
        submitBtn.setStyle("-fx-base: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        submitBtn.setPrefWidth(250);

        Button clearBtn = new Button("Xóa tất cả");

        HBox actionBox = new HBox(15, submitBtn, clearBtn);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        inputSection.getChildren().addAll(inputLabel, bulkInputArea, actionBox);

        // --- 2. KHU VỰC BẢNG KẾT QUẢ (CENTER) ---
        HBox tablesBox = new HBox(15);
        VBox.setVgrow(tablesBox, Priority.ALWAYS);

        spamTable = createTable("DANH SÁCH SPAM (NGUY HIỂM)", "#ffebee");
        hamTable = createTable("DANH SÁCH HAM (HỢP LỆ)", "#e8f5e9");

        HBox.setHgrow(spamTable, Priority.ALWAYS);
        HBox.setHgrow(hamTable, Priority.ALWAYS);
        tablesBox.getChildren().addAll(spamTable, hamTable);

        // --- 3. KHU VỰC CHI TIẾT (BOTTOM) ---
        VBox detailSection = new VBox(10);
        Label detailLabel = new Label("Chi tiết phân tích (Reason Trace):");
        detailLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        reasonArea = new TextArea();
        reasonArea.setEditable(false);
        reasonArea.setPrefHeight(120);
        reasonArea.setPromptText("Chọn một email từ bảng trên để xem lý do phân loại...");
        reasonArea.setStyle("-fx-control-inner-background: #fdfdfd;");

        detailSection.getChildren().addAll(detailLabel, reasonArea);

        // --- THIẾT LẬP LAYOUT CHÍNH ---
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");
        mainLayout.getChildren().addAll(inputSection, tablesBox, detailSection);

        // --- XỬ LÝ SỰ KIỆN ---
        submitBtn.setOnAction(e -> handleBulkProcess());
        clearBtn.setOnAction(e -> clearAll());

        // Lắng nghe sự kiện chọn dòng trên bảng để hiện Reason
        setupSelectionListener(spamTable);
        setupSelectionListener(hamTable);

        Scene scene = new Scene(mainLayout, 1000, 750);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<EmailResult> createTable(String title, String bgColor) {
        TableView<EmailResult> table = new TableView<>();
        table.setStyle("-fx-background-color: " + bgColor + ";");

        TableColumn<EmailResult, String> contentCol = new TableColumn<>(title);
        contentCol.setCellValueFactory(cellData -> cellData.getValue().contentProperty());

        // Custom hiển thị để không bị tràn text quá dài
        contentCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.replace("\n", " ").substring(0, Math.min(item.length(), 80)) + "...");
                }
            }
        });

        table.getColumns().add(contentCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return table;
    }

    private void setupSelectionListener(TableView<EmailResult> table) {
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Xóa chọn ở bảng kia để tránh nhầm lẫn
                if (table == spamTable) hamTable.getSelectionModel().clearSelection();
                else spamTable.getSelectionModel().clearSelection();

                reasonArea.setText(newSelection.getReason());
            }
        });
    }

    private void handleBulkProcess() {
        String input = bulkInputArea.getText();
        if (input.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng nhập nội dung!").show();
            return;
        }

        List<EmailResult> results = controller.processBulkEmails(input);

        spamTable.getItems().clear();
        hamTable.getItems().clear();
        reasonArea.clear();

        for (EmailResult res : results) {
            if ("spam".equalsIgnoreCase(res.getLabel())) {
                spamTable.getItems().add(res);
            } else {
                hamTable.getItems().add(res);
            }
        }
    }

    private void clearAll() {
        bulkInputArea.clear();
        spamTable.getItems().clear();
        hamTable.getItems().clear();
        reasonArea.clear();
    }

    // Các phương thức cũ (setResultText, setReasonText) có thể xóa hoặc giữ nếu cần tương thích
    public void setResultText(String text) {}
    public void setReasonText(String text) { reasonArea.setText(text); }
}