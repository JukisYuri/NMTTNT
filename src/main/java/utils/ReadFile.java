package utils;

import model.EmailData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReadFile {
    String path = "C:\\Users\\Admin\\OneDrive\\Documents\\DecisionTreeMail\\src\\main\\java\\datasets\\test2.csv";
    List<EmailData> dataList = new ArrayList<>();
    private final String[] conditionInputFree = {"miễn phí", "hàng giao nhanh", "giá cả vừa phải", "cho vay trả góp", "khuyến mãi sốc"};
    private final String[] conditionInputStrangeLink = {"https://shopee.vn/", "https://tiki.vn/"};
    // Kaggle
    public void readFromPath() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8));
        String lines;
        while ((lines = bufferedReader.readLine()) != null){
            if (lines.trim().isEmpty()) continue;
            try {
                // Dùng int biểu diễn 1 và 0 tương đương với true/false
                int featureFree = containsWord(lines, conditionInputFree);
                int featureStrangeLink = containsWord(lines, conditionInputStrangeLink);
                int featureUpperCase = containsUpperCase(lines);
                dataList.add(new EmailData(featureFree, featureStrangeLink, featureUpperCase));
            } catch (NumberFormatException e){
                System.err.println("Lỗi parse số ở dòng: " + lines);
            }
        }
    }

    // Hàm kiểm tra điều kiện từ được nhập vào và xét với các condition tương ứng
    public int containsWord(String wordsInput, String[] patterns){
        if (wordsInput.isBlank() || patterns == null) return 0;
        String wordsLowerCase = wordsInput.trim().toLowerCase();
        for (String pattern : patterns) {
            if (wordsLowerCase.contains(pattern)) {
                return 1;
            }
        }
        return 0;
    }

    // Hàm kiểm tra điều kiện chữ in hoa
    public int containsUpperCase(String wordsInput){
        if (wordsInput.isBlank()) return 0;
        int upperCount = 0;
        for (int i = 0; i < wordsInput.length(); i++) {
            char c = wordsInput.charAt(i);
                if (Character.isUpperCase(c)) {
                    upperCount++;
            }
        }
        return (upperCount >= 3) ? 1 : 0;
    }

    public static void main(String[] args) throws IOException {
        ReadFile readFile = new ReadFile();
        readFile.readFromPath();
    }
}
