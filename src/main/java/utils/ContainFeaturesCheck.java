package utils;

public class ContainFeaturesCheck {
    // Hàm kiểm tra đầu vào theo điều kiện patterns
    public int containsWord(String wordsInput, String[] patterns) {
        if (wordsInput == null || wordsInput.isBlank() || patterns == null || patterns.length == 0) return 0;
        String wordsLowerCase = wordsInput.toLowerCase();
        for (String pattern : patterns) {
            if (pattern == null || pattern.isBlank()) continue;
            String p = pattern.toLowerCase();
            if (wordsLowerCase.contains(p)) {
                return 1;
            }
        }
        return 0;
    }

    // Hàm kiểm tra điều kiện chữ in hoa
    public int containsUpperCase(String wordsInput) {
        if (wordsInput == null || wordsInput.isBlank()) return 0;
        int upperCount = 0;
        for (int i = 0; i < wordsInput.length(); i++) {
            char c = wordsInput.charAt(i);
            if (Character.isUpperCase(c)) {
                upperCount++;
            }
        }
        return (upperCount >= 453) ? 1 : 0; // Cần chứng minh tại sao lấy số này
    }

    // Hàm kiểm tra nội dung email dài bao nhiêu
    public int howLongDescription(String wordsInput){
        if (wordsInput == null || wordsInput.isEmpty()) return 0;
        int textLength = wordsInput.split("\\s+").length;
        if (textLength > 138) { // Cần chứng minh tại sao lấy số này
            return 1;
        }
        return 0;
    }

    // Hàm kiểm tra điều kiện chứa kí tự đặc biệt
    public int containSpecialChar(String wordsInput, String[] patterns) {
        if (wordsInput == null || wordsInput.isEmpty()) return 0;
        int specialCharCount = 0;
        for (int i = 0; i < wordsInput.length(); i++) {
            String currentChar = String.valueOf(wordsInput.charAt(i));
            for (String pattern : patterns) {
                if (currentChar.contains(pattern)){
                    specialCharCount++;
                }
            }
        }
        return (specialCharCount >= 23) ? 1 : 0; // Cần chứng minh tại sao lấy số này
    }
}
