package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MailFilterFrame extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mail Filter");

        // Labels and inputs
        Label subjectLabel = new Label("Tiêu đề (Subject):");
        TextField subjectField = new TextField();
        subjectField.setPromptText("Nhập tiêu đề mail...");

        Label contentLabel = new Label("Nội dung (Content):");
        TextArea contentArea = new TextArea();
        contentArea.setPromptText("Nhập nội dung mail...");
        contentArea.setPrefRowCount(10);

        Button submitBtn = new Button("Kiểm tra (Submit)");
        Button clearBtn = new Button("Xóa");

        Label resultLabel = new Label("Trạng thái: —");
        resultLabel.setTextFill(Color.DARKBLUE);

        Label reasonLabel = new Label("Lý do (Reason):");
        TextArea reasonArea = new TextArea();
        reasonArea.setEditable(false);
        reasonArea.setWrapText(true);
        reasonArea.setPromptText("Kết quả trả ra");
        reasonArea.setPrefRowCount(4);

        Label noticeLabel = new Label("Chú ý những từ:");
        TextArea noticeArea = new TextArea();
        noticeArea.setEditable(false);
        noticeArea.setWrapText(true);
        noticeArea.setPromptText("Những lưu ý đặc biệt");
        noticeArea.setPrefRowCount(4);

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

        grid.add(resultLabel, 1, 3);

        grid.add(reasonLabel, 0, 4);
        grid.add(reasonArea, 1, 4);
        grid.add(noticeLabel, 0, 5);
        grid.add(noticeArea, 1, 5);

        ColumnConstraints leftCol = new ColumnConstraints();
        leftCol.setPercentWidth(20);
        ColumnConstraints rightCol = new ColumnConstraints();
        rightCol.setPercentWidth(80);
        grid.getColumnConstraints().addAll(leftCol, rightCol);

        clearBtn.setOnAction(e -> {
            subjectField.clear();
            contentArea.clear();
            resultLabel.setText("Trạng thái: —");
            resultLabel.setTextFill(Color.DARKBLUE);
            reasonArea.clear();
        });

        Scene scene = new Scene(grid, 700, 520);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}