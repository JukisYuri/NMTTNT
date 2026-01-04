package controller;

import model.DecisionTree;
import model.EmailData;
import model.EmailResult;
import model.Node;
import utils.ContainFeaturesCheck;
import utils.ReadFile;
import view.MailFilterFrame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpamController {
    MailFilterFrame view;
    DecisionTree model;

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
            rf.readFromPath("src/main/java/datasets/spam_assassin.csv");
            rf.readFromPath("src/main/java/datasets/SpamAssasin.csv");

            List<EmailData> dataList = rf.getDataList();
            System.out.println("Dataset size: " + dataList.size());

            Collections.shuffle(dataList); // Xáo trộn dữ liệu
            // Chia dữ liệu theo tỷ lệ 80/20
            int trainSize = (int) (dataList.size() * 0.8);
            List<EmailData> trainData = dataList.subList(0, trainSize);
            List<EmailData> testData = dataList.subList(trainSize, dataList.size());

            List<String> attributes = Arrays.asList(
                    "urgencyWords",
                    "moneyWords",
                    "scamFraudWords",
                    "marketingWords",
                    "healthWords",
                    "securityWords",
                    "strangeLink",
                    "upperCase",
                    "longDescription",
                    "specialChar"
            );

            Node root = model.buildTree(trainData, attributes);
            model.setRoot(root);
            calculateAccuracy(testData);
            System.out.println("Model đã được khởi tạo thành công!");
        } catch (IOException e) {
            throw new RuntimeException("Không đọc được dataset" + e.getMessage(), e);
        }
    }

    private void calculateAccuracy(List<EmailData> testData) {
        int correctPredictions = 0;
        for (EmailData email : testData) {
            String prediction = model.classify(email);
            boolean isSpamActual = email.getSpam();

            if (("spam".equalsIgnoreCase(prediction) && isSpamActual) ||
                    ("ham".equalsIgnoreCase(prediction) && !isSpamActual)) {
                correctPredictions++;
            }
        }

        double accuracy = (double) correctPredictions / testData.size() * 100;
        System.out.println("==== KẾT QUẢ ĐÁNH GIÁ MODEL ====");
        System.out.println("Tổng mẫu test: " + testData.size());
        System.out.println("Số câu đoán đúng: " + correctPredictions);
        System.out.format("Độ chính xác: %.2f%%\n", accuracy);
        System.out.println("================================");
    }

    public List<EmailResult> processBulkEmails(String rawInput) {
        List<EmailResult> results = new ArrayList<>();
        // Phân tách các email bằng 3 dấu xuống dòng
        String[] emailTexts = rawInput.split("\\n\\s*\\n\\s*\\n");

        for (String text : emailTexts) {
            String trimmedText = text.trim();
            if (trimmedText.isEmpty()) continue;

            // Trích xuất đặc trưng
            EmailData email = new EmailData(
                    ContainFeaturesCheck.containsUrgencyWords(trimmedText),
                    ContainFeaturesCheck.containsMoneyWords(trimmedText),
                    ContainFeaturesCheck.containsScamFraudWords(trimmedText),
                    ContainFeaturesCheck.containsMarketingWords(trimmedText),
                    ContainFeaturesCheck.containsHealthWords(trimmedText),
                    ContainFeaturesCheck.containSecurityWords(trimmedText),
                    ContainFeaturesCheck.containsStrangeLink(trimmedText),
                    ContainFeaturesCheck.containsUpperCase(trimmedText),
                    ContainFeaturesCheck.howLongDescription(trimmedText),
                    ContainFeaturesCheck.containsSpecialChar(trimmedText)
            );

            // Phân loại và lấy lý do
            String label = model.classify(email);
            String[] explain = model.explainClassification(email);
            model.printClassificationTree(email); // hàm này in ra cây
            results.add(new EmailResult(trimmedText, label, explain[0]));
        }
        return results;
    }
}
