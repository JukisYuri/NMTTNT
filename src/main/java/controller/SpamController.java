package controller;

import model.DecisionTree;
import model.EmailData;
import model.Node;
import utils.ContainFeaturesCheck;
import utils.ReadFile;
import view.MailFilterFrame;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static utils.ContainFeaturesCheck.*;

public class SpamController {
    MailFilterFrame view = new MailFilterFrame();
    DecisionTree model = new DecisionTree();

    public SpamController(MailFilterFrame view, DecisionTree model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Gọi khi muốn khởi tạo pipeline: đọc dữ liệu và build cây
     */
    public void initializeModelFromDataset() {
        ReadFile rf = new ReadFile();
        try {
            rf.readFromPath();

            List<EmailData> dataList = rf.getDataList();

            List<String> attributes = Arrays.asList(
                    "suspiciousWords",
                    "strangeLink",
                    "upperCase",
                    "longDescription",
                    "specialChar"
            );

            Node root = model.buildTree(dataList, attributes);
            model.setRoot(root);

        } catch (IOException e) {
            throw new RuntimeException("Không đọc được dataset" + e.getMessage(), e);
        }
    }

    /**
     * Gọi khi người dùng submit 1 email mới:
     */
    public void checkEmailAndUpdateView(String subject, String content) {
        // Xử lý input (tránh null)
        String s = subject == null ? "" : subject;
        String c = content == null ? "" : content;
        String text = (s + "\n" + c).trim();

        // Trích xuất các đặc trưng từ email
        int featureSuspiciousWords = ContainFeaturesCheck.containsSuspiciousWord(text);
        int featureStrangeLink     = ContainFeaturesCheck.containsStrangeLink(text);
        int featureUpperCase       = ContainFeaturesCheck.containsUpperCase(text);
        int featureSpecialChar     = ContainFeaturesCheck.containsSpecialChar(text);
        int featureLongDesc        = ContainFeaturesCheck.howLongDescription(text);

        // Tạo đối tượng EmailData với các features đã trích xuất
        EmailData email = new EmailData(
                featureSuspiciousWords,
                featureStrangeLink,
                featureUpperCase,
                featureLongDesc,
                featureSpecialChar
        );

        // Phân loại email bằng Decision Tree
        String label = model.classify(email);

        // Lấy lý do phân loại
        String[] explain = model.explainClassification(email);

        // Cập nhật giao diện
        view.setResultText("Kết quả: " + label);
        view.setReasonText(explain == null || explain.length == 0 ? "" : String.join("\n", explain));
        view.setNoticeText("Đã kiểm tra xong");
    }

}
