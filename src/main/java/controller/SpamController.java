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

public class SpamController {
    MailFilterFrame view = new MailFilterFrame();
    DecisionTree model = new DecisionTree();
    private final ContainFeaturesCheck featuresCheck = new ContainFeaturesCheck();

    private final String[] conditionInputSuspiciousWords = {
            "free", "limited time", "offer", "special offer", "buy now", "discount", "deal",
            "save", "promotion", "congratulations", "winner", "following", "copy"
    };
    private final String[] conditionInputStrangeLink = {
            "http://", "https://", "click here", "bit.ly/", "tinyurl.com", "goo.gl/", "gg.gg",
            "t.co", "cutt.ly", "is.gd", "ouo.io"
    };
    private final String[] conditionSpecialChar = {
            "!", "@", "#", "$", "%", "&", "*",
            "?", "...", "-",
            "★", "☆", "£", "¢", "€", "¥",
            "✓", "✔", "✖", "→", "⇒"
    };

    public SpamController(MailFilterFrame view, DecisionTree model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Gọi khi muốn khởi tạo pipeline: đọc dữ liệu và build cây
     */
    public void initializeModelFromDataset() {
        // 1. Dùng utils.ReadFile để đọc dataList
        // 2. Xác định danh sách attributes: List<String> attributes = Arrays.asList("free","strangeLink","upperCase");
        // 3. Node root = model.buildTree(dataList, attributes);
        // 4. model.setRoot(root);
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
     * - Convert subject+content -> EmailData (features)
     * - String label = model.classify(email)
     * - String[] explain = model.explainClassification(email)
     * - Update view tương ứng (hiển thị label, explain)
     */
    public void checkEmailAndUpdateView(String subject, String content) {
        // Xử lý input (tránh null)
        String s = subject == null ? "" : subject;
        String c = content == null ? "" : content;
        String text = (s + "\n" + c).trim();

        // Trích xuất các đặc trưng từ email
        int featureSuspiciousWords = featuresCheck.containsWord(text, conditionInputSuspiciousWords);
        int featureStrangeLink = featuresCheck.containsWord(text, conditionInputStrangeLink);
        int featureUpperCase = featuresCheck.containsUpperCase(text);
        int featureSpecialChar = featuresCheck.containSpecialChar(text, conditionSpecialChar);
        int featureHowLongDescription = featuresCheck.howLongDescription(text);

        // Tạo đối tượng EmailData với các features đã trích xuất
        EmailData email = new EmailData(
                featureSuspiciousWords,
                featureStrangeLink,
                featureUpperCase,
                featureHowLongDescription,
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
