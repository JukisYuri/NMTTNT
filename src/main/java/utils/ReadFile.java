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
    ContainFeaturesCheck featuresCheck = new ContainFeaturesCheck();
    private final String[] conditionInputSuspiciousWords = {
            "free", "limited time", "offer", "special offer", "buy now", "discount", "deal", "save", "promotion", "congratulations", "winner", "following", "copy"
    };
    private final String[] conditionInputStrangeLink = {
            "http://", "https://", "click here", "bit.ly/", "tinyurl.com", "goo.gl/", "gg.gg", "t.co", "cutt.ly", "is.gd", "ouo.io"
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
    public void initializeModelFromDataset() throws IOException {
        // 1. Dùng utils.ReadFile để đọc dataList
        // 2. Xác định danh sách attributes: List<String> attributes = Arrays.asList("free","strangeLink","upperCase");
        // 3. Node root = model.buildTree(dataList, attributes);
        // 4. model.setRoot(root);
        try {
            ReadFile readFile = new ReadFile();
            readFile.readFromPath();
            List<EmailData> dataList = readFile.getDataList();
            if (dataList.isEmpty()) {
                return;
            }
            List<String> attributes = Arrays.asList("free","strangeLink","upperCase");
            Node root = model.buildTree(dataList, attributes);
            model.setRoot(root);
        } catch (IOException e) {
            System.err.println("Can't read data list");
            e.printStackTrace();
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
        // subject + content
        String fullText = subject + " " + content;
        ReadFile readFile = new ReadFile();

        int featureSuspiciousWords = featuresCheck.containsWord(fullText,conditionInputSuspiciousWords);
        int featureStrangeLink = featuresCheck.containsWord(fullText,conditionInputStrangeLink);
        int featureSpecialChar =  featuresCheck.containSpecialChar(fullText, conditionSpecialChar);
        int featureUpperCase = featuresCheck.containsUpperCase(fullText);
        int featureLongDescription = featuresCheck.howLongDescription(fullText);

        EmailData email = new EmailData(featureSuspiciousWords,featureStrangeLink,featureUpperCase,featureSpecialChar,featureLongDescription);

        String label = model.classify(email);
        String[] explain = model.explainClassification(email);
    }

}
