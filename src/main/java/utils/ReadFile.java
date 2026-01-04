package utils;

import model.EmailData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Đọc file CSV sử dụng Apache Commons CSV
 */
public class ReadFile {
    List<EmailData> dataList = new ArrayList<>();
    String path;
    /**
     * Đọc file CSV theo đường dẫn truyền vào.
     */
    public void readFromPath(String filePath) throws IOException {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("The file path is null or empty.");
        }
        this.path = filePath;
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("The file does not exist or is not a valid file: " + filePath);
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreSurroundingSpaces()
                    .withTrim()
                    .parse(reader);

            boolean hasHeaderTarget = parser.getHeaderMap().containsKey("target") || parser.getHeaderMap().containsKey("label");

            for (CSVRecord record : parser) {
                String rawText = "";

                if (parser.getHeaderMap().containsKey("subject")) {
                    String subject = safeGet(record, "subject");
                    if (subject != null && !subject.isBlank()) {
                        rawText += subject + " ";
                    }
                }

                if (parser.getHeaderMap().containsKey("body")) {
                    rawText += safeGet(record, "body");
                } else if (parser.getHeaderMap().containsKey("text")) {
                    rawText += safeGet(record, "text");
                } else if (parser.getHeaderMap().containsKey("content")) {
                    rawText += safeGet(record, "content");
                } else if (parser.getHeaderMap().containsKey("message")) {
                    rawText += safeGet(record, "message");
                } else {
                    if (rawText.trim().isEmpty() && record.size() > 0) {
                        rawText = record.get(0);
                    }
                }
                // ---------------------------------------------

                String rawTarget;
                if (hasHeaderTarget) {
                    if (parser.getHeaderMap().containsKey("target")) {
                        rawTarget = safeGet(record, "target");
                    } else if (parser.getHeaderMap().containsKey("label")) {
                        rawTarget = safeGet(record, "label");
                    } else {
                        rawTarget = "";
                    }
                } else {
                    rawTarget = record.size() > 1 ? record.get(1) : "";
                }

                if (rawText == null) rawText = "";
                if (rawTarget == null) rawTarget = "";

                EmailData emailData = createEmailDataFromRaw(rawText, rawTarget);
                dataList.add(emailData);
            }
        }
    }

    /**
     * Tạo EmailData từ rawText và rawTarget (lấy features, set label).
     */
    private EmailData createEmailDataFromRaw(String rawText, String rawTarget) {
        String text = rawText.trim();
        String target = rawTarget.trim();

        // Xác định isSpam: hỗ trợ "spam"/"ham" theo "1"/"0"
        Boolean isSpam;
        String t = target.toLowerCase();
        if (t.equals("1") || t.equals("spam") || t.equals("true") || t.equals("yes")) {
            isSpam = true;
        } else if (t.equals("0") || t.equals("ham") || t.equals("not spam") || t.equals("false") || t.equals("no")) {
            isSpam = false;
        } else {
            isSpam = null;
        }
        int featureUrgencyWords = ContainFeaturesCheck.containsUrgencyWords(text);
        int featureMoneyWords = ContainFeaturesCheck.containsMoneyWords(text);
        int featureScamFraudWords = ContainFeaturesCheck.containsScamFraudWords(text);
        int featureMarketingWords = ContainFeaturesCheck.containsMarketingWords(text);
        int featureHealthWords = ContainFeaturesCheck.containsHealthWords(text);
        int featureSecurityWords = ContainFeaturesCheck.containSecurityWords(text);
        int featureStrangeLink     = ContainFeaturesCheck.containsStrangeLink(text);
        int featureUpperCase       = ContainFeaturesCheck.containsUpperCase(text);
        int featureSpecialChar     = ContainFeaturesCheck.containsSpecialChar(text);
        int featureLongDesc        = ContainFeaturesCheck.howLongDescription(text);
        if (isSpam != null) {
            return new EmailData(featureUrgencyWords, featureMoneyWords, featureScamFraudWords, featureMarketingWords, featureHealthWords, featureSecurityWords, featureStrangeLink, featureUpperCase, featureLongDesc, featureSpecialChar, isSpam);
        } else {
            return new EmailData(featureUrgencyWords, featureMoneyWords, featureScamFraudWords, featureMarketingWords, featureHealthWords, featureSecurityWords, featureStrangeLink, featureUpperCase, featureLongDesc, featureSpecialChar);
        }
    }

    private String safeGet(CSVRecord record, String name) {
        try {
            if (record.isSet(name)) {
                return record.get(name);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Warning: Unable to get value for field '" + name + "'. " + e.getMessage());
        }
        return "";
    }


    public List<EmailData> getDataList() {
        return dataList;
    }

    public static void main(String[] args) throws IOException {
        ReadFile rf = new ReadFile();
        rf.readFromPath("src/main/java/datasets/spam_assassin.csv");
        rf.readFromPath("src/main/java/datasets/SpamAssasin.csv");
        List<EmailData> list = rf.getDataList();
        int spamCount = 0;
        int hamCount = 0;

        for (EmailData email : list) {
            if (Boolean.TRUE.equals(email.getSpam())) {
                spamCount++;
            } else {
                hamCount++;
            }
        }

        System.out.println("Tổng số email: " + list.size());
        System.out.println("Số lượng SPAM: " + spamCount);
        System.out.println("Số lượng HAM:  " + hamCount);
        int limit = Math.min(5, rf.getDataList().size());
        for (int i = 0; i < limit; i++) {
            System.out.println(rf.getDataList().get(i));
        }
    }
}