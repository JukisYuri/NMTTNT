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
            rf.readFromPath();

            List<EmailData> dataList = rf.getDataList();
            java.util.Collections.shuffle(dataList);
            int trainSize = (int) (dataList.size() * 0.7); // Lấy 70% để học
            List<EmailData> trainSet = dataList.subList(0, trainSize);
            List<EmailData> testSet = dataList.subList(trainSize, dataList.size()); // 30% để test

            System.out.println("Dataset size: " + dataList.size());
            System.out.println("Training size: " + trainSet.size());
            System.out.println("Test size: " + testSet.size());

            List<String> attributes = Arrays.asList(
                    "urgencyWords",
                    "moneyWords",
                    "scamFraudWords",
                    "marketingWords",
                    "healthWords",
                    "strangeLink",
                    "upperCase",
                    "longDescription",
                    "specialChar"
            );

            Node root = model.buildTree(dataList, attributes);
            model.setRoot(root);
            model.evaluate(testSet);

        } catch (IOException e) {
            throw new RuntimeException("Không đọc được dataset" + e.getMessage(), e);
        }
    }

    /**
     * Gọi khi người dùng submit 1 email mới:
     */
    public void checkEmailAndUpdateView(String subject, String content) {
        String s = subject == null ? "" : subject;
        String c = content == null ? "" : content;
        String text = (s + "\n" + c).trim();

        // Trích xuất các đặc trưng từ email
        int featureUrgencyWords = ContainFeaturesCheck.containsUrgencyWords(text);
        int featureMoneyWords = ContainFeaturesCheck.containsMoneyWords(text);
        int featureScamFraudWords = ContainFeaturesCheck.containsScamFraudWords(text);
        int featureMarketingWords = ContainFeaturesCheck.containsMarketingWords(text);
        int featureHealthWords = ContainFeaturesCheck.containsHealthWords(text);
        int featureStrangeLink = ContainFeaturesCheck.containsStrangeLink(text);
        int featureUpperCase = ContainFeaturesCheck.containsUpperCase(text);
        int featureSpecialChar = ContainFeaturesCheck.containsSpecialChar(text);
        int featureLongDesc = ContainFeaturesCheck.howLongDescription(text);

        EmailData email = new EmailData(
                featureUrgencyWords,
                featureMoneyWords,
                featureScamFraudWords,
                featureMarketingWords,
                featureHealthWords,
                featureStrangeLink,
                featureUpperCase,
                featureLongDesc,
                featureSpecialChar
        );

        String label = model.classify(email);
        String[] explain = model.explainClassification(email);

        // === THÊM DÒNG NÀY - IN CÂY ĐƯỜNG ĐI RA TERMINAL ===
        model.printClassificationTree(email);

        view.setResultText("Kết quả: " + label);
        view.setReasonText(explain == null || explain.length == 0 ? "" : String.join("\n", explain));
    }
}
