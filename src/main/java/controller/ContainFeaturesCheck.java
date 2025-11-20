package controller;

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

    // Hàm kiểm tra điều kiện chữ in hoa (>=3 ký tự in hoa => 1)
    public int containsUpperCase(String wordsInput) {
        if (wordsInput == null || wordsInput.isBlank()) return 0;
        int upperCount = 0;
        for (int i = 0; i < wordsInput.length(); i++) {
            char c = wordsInput.charAt(i);
            if (Character.isUpperCase(c)) {
                upperCount++;
            }
        }
        return (upperCount >= 3) ? 1 : 0;
    }
}
