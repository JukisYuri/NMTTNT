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
    String path = "src/main/java/datasets/spam_assassin.csv";
    List<EmailData> dataList = new ArrayList<>();

    /**
     * Đọc file CSV theo đường dẫn truyền vào.
     */
    public void readFromPath() throws IOException {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("The file path is null or empty.");
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("The file does not exist or is not a valid file: " + path);
        }
        dataList.clear();
        try (Reader reader = new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8)) {
            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreSurroundingSpaces()
                    .withTrim()
                    .parse(reader);

            boolean hasHeaderText = parser.getHeaderMap().containsKey("text");
            boolean hasHeaderTarget = parser.getHeaderMap().containsKey("target") || parser.getHeaderMap().containsKey("label");

            for (CSVRecord record : parser) {
                String rawText;
                String rawTarget;

                if (hasHeaderText) {
                    rawText = safeGet(record, "text");
                } else if (parser.getHeaderMap().containsKey("content")) {
                    rawText = safeGet(record, "content");
                } else if (parser.getHeaderMap().containsKey("message")) {
                    rawText = safeGet(record, "message");
                } else {
                    rawText = record.size() > 0 ? record.get(0) : "";
                }

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
        int featureSuspiciousWords = ContainFeaturesCheck.containsSuspiciousWord(text);
        int featureStrangeLink     = ContainFeaturesCheck.containsStrangeLink(text);
        int featureUpperCase       = ContainFeaturesCheck.containsUpperCase(text);
        int featureSpecialChar     = ContainFeaturesCheck.containsSpecialChar(text);
        int featureLongDesc        = ContainFeaturesCheck.howLongDescription(text);
        if (isSpam != null) {
            return new EmailData(featureSuspiciousWords, featureStrangeLink, featureUpperCase, featureLongDesc, featureSpecialChar, isSpam);
        } else {
            return new EmailData(featureSuspiciousWords, featureStrangeLink, featureUpperCase, featureLongDesc, featureSpecialChar);
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
        rf.readFromPath();
        System.out.println("Đọc được " + rf.getDataList().size() + " bản ghi.");
        int limit = Math.min(50, rf.getDataList().size());
        for (int i = 0; i < limit; i++) {
            System.out.println(rf.getDataList().get(i));
        }
    }
}