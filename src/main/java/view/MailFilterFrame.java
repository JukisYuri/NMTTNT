package view;

import controller.SpamController;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DecisionTree;

public class MailFilterFrame extends Application {
    // Tạo thêm instance để lưu kết quả
    private Label resultLabel;
    private TextArea reasonArea;
    private TextArea noticeArea;
    private TextField subjectField;
    private TextArea contentArea;

    // Gọi Controller để xử lý logic
    private SpamController controller;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mail Filter");

        // Khởi tạo Controller và Model
        DecisionTree model = new DecisionTree();
        controller = new SpamController(this, model);
        controller.initializeModelFromDataset();

        // Labels and inputs
        Label subjectLabel = new Label("Tiêu đề (Subject):");
        subjectField = new TextField();
        subjectField.setPromptText("Nhập tiêu đề mail...");

        Label contentLabel = new Label("Nội dung (Content):");
        contentArea = new TextArea();
        contentArea.setPromptText("Nhập nội dung mail...");
        contentArea.setPrefRowCount(10);

        Button submitBtn = new Button("Kiểm tra (Submit)");
        Button clearBtn = new Button("Xóa");

        // Sử dụng this. để gán vào instance field thay vì tạo biến local
        this.resultLabel = new Label("Trạng thái: —");
        this.resultLabel.setTextFill(Color.DARKBLUE);

        Label reasonLabel = new Label("Lý do (Reason):");
        this.reasonArea = new TextArea();
        this.reasonArea.setEditable(false);
        this.reasonArea.setWrapText(true);
        this.reasonArea.setPromptText("Kết quả trả ra");
        this.reasonArea.setPrefRowCount(4);

        Label noticeLabel = new Label("Chú ý những từ:");
        this.noticeArea = new TextArea();
        this.noticeArea.setEditable(false);
        this.noticeArea.setWrapText(true);
        this.noticeArea.setPromptText("Những lưu ý đặc biệt");
        this.noticeArea.setPrefRowCount(4);

        // Layout
        GridPane grid = new GridPane();
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setPadding(new Insets(12));

        grid.add(subjectLabel, 0, 0);
        grid.add(subjectField, 1, 0);

        grid.add(contentLabel, 0, 1);
        grid.add(contentArea, 1, 1);

        HBox buttons = new HBox(8, submitBtn, clearBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        grid.add(buttons, 1, 2);

        grid.add(this.resultLabel, 1, 3);

        grid.add(reasonLabel, 0, 4);
        grid.add(this.reasonArea, 1, 4);
        grid.add(noticeLabel, 0, 5);
        grid.add(this.noticeArea, 1, 5);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(20);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(80);
        grid.getColumnConstraints().addAll(leftCol, rightCol);

        // Xử lý nút gọi Controller để kiểm tra email
        submitBtn.setOnAction(e -> {
            String subject = subjectField.getText();
            String content = contentArea.getText();
            controller.checkEmailAndUpdateView(subject, content);
        });

        // Xử lý nút xóa hết các trường
        clearBtn.setOnAction(e -> {
            subjectField.clear();
            contentArea.clear();
            this.resultLabel.setText("Trạng thái: —");
            this.resultLabel.setTextFill(Color.DARKBLUE);
            this.reasonArea.clear();
            this.noticeArea.clear();
        });

        Scene scene = new Scene(grid, 700, 520);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Đặt setter methods
    public void setResultText(String text) {
        if (resultLabel != null) {
            Platform.runLater(() -> {
                resultLabel.setText(text);
                //Đổi màu thành đỏ nếu là spam
                if (text.contains("spam")) {
                    resultLabel.setTextFill(Color.RED);
                } else if (text.contains("ham")) {
                    //Xanh nếu ham
                    resultLabel.setTextFill(Color.GREEN);
                } else {
                    //Còn lại xanh đậm
                    resultLabel.setTextFill(Color.DARKBLUE);
                }
            });
        }
    }
    public void setReasonText(String text) {
        if (reasonArea != null) {
            Platform.runLater(() -> reasonArea.setText(text));
        }
    }

    public void setNoticeText(String text) {
        if (noticeArea != null) {
            Platform.runLater(() -> noticeArea.setText(text));
        }
    }
}