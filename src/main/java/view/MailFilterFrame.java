package view;

import controller.SpamController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.DecisionTree;

public class MailFilterFrame extends Application {

    private Label resultLabel;
    private TextArea reasonArea;
    private TextField subjectField;
    private TextArea contentArea;
    private SpamController controller;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mail Filter System");

        // Khởi tạo Controller và Model
        DecisionTree model = new DecisionTree();
        controller = new SpamController(this, model);
        controller.initializeModelFromDataset();

        // 1. Phần tiêu đề
        Label subjectLabel = new Label("Tiêu đề (Subject):");
        subjectLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        subjectField = new TextField();
        subjectField.setPromptText("Nhập tiêu đề mail...");

        // 2. Phần nội dung
        Label contentLabel = new Label("Nội dung (Content):");
        contentLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        contentArea = new TextArea();
        contentArea.setPromptText("Nhập nội dung mail cần kiểm tra...");
        contentArea.setWrapText(true);

        // 3. Các nút bấm
        Button submitBtn = new Button("Kiểm tra (Submit)");
        submitBtn.setStyle("-fx-base: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        submitBtn.setPrefWidth(120);

        Button clearBtn = new Button("Xóa (Clear)");
        clearBtn.setPrefWidth(100);

        // 4. Phần kết quả
        Label resultTitleLabel = new Label("Kết quả phân tích:");
        resultTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        this.resultLabel = new Label("waiting...");
        this.resultLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
        this.resultLabel.setTextFill(Color.DARKGRAY);

        Label reasonLabel = new Label("Chi tiết (Reason):");
        reasonLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        this.reasonArea = new TextArea();
        this.reasonArea.setEditable(false);
        this.reasonArea.setWrapText(true);
        this.reasonArea.setPromptText("Lý do hoặc xác suất sẽ hiện ở đây...");
        this.reasonArea.setPrefRowCount(3);

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: #f4f4f4;");

        grid.add(subjectLabel, 0, 0);
        grid.add(subjectField, 1, 0);

        grid.add(contentLabel, 0, 1);
        grid.add(contentArea, 1, 1);
        GridPane.setValignment(contentLabel, VPos.TOP);
        GridPane.setVgrow(contentArea, Priority.ALWAYS);

        HBox buttons = new HBox(10, submitBtn, clearBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        grid.add(buttons, 1, 2);

        Separator separator = new Separator();
        grid.add(separator, 0, 3, 2, 1);

        HBox resultBox = new HBox(10, resultTitleLabel, this.resultLabel);
        resultBox.setAlignment(Pos.CENTER_LEFT);
        grid.add(resultBox, 0, 4, 2, 1);

        grid.add(reasonLabel, 0, 5);
        grid.add(this.reasonArea, 1, 5);
        GridPane.setValignment(reasonLabel, VPos.TOP);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(col1, col2);

        // xử lí sự kiện
        submitBtn.setOnAction(e -> {
            String subject = subjectField.getText();
            String content = contentArea.getText();
            if(subject.isEmpty() && content.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập nội dung!");
                alert.show();
                return;
            }
            controller.checkEmailAndUpdateView(subject, content);
        });

        clearBtn.setOnAction(e -> {
            subjectField.clear();
            contentArea.clear();
            this.resultLabel.setText("waiting...");
            this.resultLabel.setTextFill(Color.DARKGRAY);
            this.reasonArea.clear();
        });

        Scene scene = new Scene(grid, 750, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void setResultText(String text) {
        if (resultLabel != null) {
            Platform.runLater(() -> {
                resultLabel.setText(text.toUpperCase());
                if (text.toLowerCase().contains("spam")) {
                    resultLabel.setTextFill(Color.WHITE);
                    resultLabel.setStyle("-fx-background-color: #e53935; -fx-padding: 5px 10px; -fx-background-radius: 5px; -fx-font-weight: bold;");
                } else if (text.toLowerCase().contains("ham")) {
                    resultLabel.setTextFill(Color.WHITE);
                    resultLabel.setStyle("-fx-background-color: #43A047; -fx-padding: 5px 10px; -fx-background-radius: 5px; -fx-font-weight: bold;");
                } else {
                    resultLabel.setTextFill(Color.DARKBLUE);
                    resultLabel.setStyle("-fx-background-color: #e0e0e0; -fx-padding: 5px 10px; -fx-background-radius: 5px;");
                }
            });
        }
    }

    public void setReasonText(String text) {
        if (reasonArea != null) {
            Platform.runLater(() -> reasonArea.setText(text));
        }
    }
}