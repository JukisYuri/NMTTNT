package model;

public class EmailData {
    private int featureSuspiciousWords; // Tỉ lệ những từ đáng ngờ
    private int featureStrangeLink;
    private int featureUpperCase;
    private int featureHowLongDescription; // Tỉ lệ nội dung email dài bao nhiêu
    private int featureSpecialChar; // Tỉ lệ bao nhiêu kí tự đặc biệt
    private Boolean isSpam;

    // Color
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    // Constructor này quan trọng với Controller
    public EmailData(int featureFree, int featureStrangeLink, int featureUpperCase, int featureHowLongDescription, int featureSpecialChar) {
        this.featureSuspiciousWords = featureFree;
        this.featureStrangeLink = featureStrangeLink;
        this.featureUpperCase = featureUpperCase;
        this.featureHowLongDescription = featureHowLongDescription;
        this.featureSpecialChar = featureSpecialChar;
        this.isSpam = null;
    }

    // Nếu dataset có nhãn, dùng constructor này. Chủ yếu được dùng cho đọc file
    public EmailData(int featureFree, int featureStrangeLink, int featureUpperCase, int featureHowLongDescription, int featureSpecialChar, boolean isSpam) {
        this.featureSuspiciousWords = featureFree;
        this.featureStrangeLink = featureStrangeLink;
        this.featureUpperCase = featureUpperCase;
        this.featureHowLongDescription = featureHowLongDescription;
        this.featureSpecialChar = featureSpecialChar;
        this.isSpam = isSpam;
    }

    /**
     * Helper: lấy giá trị của attribute theo tên (dùng khi tách data theo attribute).
     */
    public int getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "suspiciousWords" -> featureSuspiciousWords;
            case "strangeLink" -> featureStrangeLink;
            case "upperCase" -> featureUpperCase;
            case "length" -> featureHowLongDescription;
            case "specialChar" -> featureSpecialChar;
            default -> 0;
        };
    }

    public int getFeatureSuspiciousWords() {
        return featureSuspiciousWords;
    }

    public void setFeatureSuspiciousWords(int featureSuspiciousWords) {
        this.featureSuspiciousWords = featureSuspiciousWords;
    }

    public int getFeatureStrangeLink() {
        return featureStrangeLink;
    }

    public void setFeatureStrangeLink(int featureStrangeLink) {
        this.featureStrangeLink = featureStrangeLink;
    }

    public int getFeatureUpperCase() {
        return featureUpperCase;
    }

    public void setFeatureUpperCase(int featureUpperCase) {
        this.featureUpperCase = featureUpperCase;
    }

    public Boolean getSpam() {
        return isSpam;
    }

    public void setSpam(Boolean spam) {
        isSpam = spam;
    }

    public int getFeatureHowLongDescription() {
        return featureHowLongDescription;
    }

    public void setFeatureHowLongDescription(int featureHowLongDescription) {
        this.featureHowLongDescription = featureHowLongDescription;
    }

    public int getFeatureSpecialChar() {
        return featureSpecialChar;
    }

    public void setFeatureSpecialChar(int featureSpecialChar) {
        this.featureSpecialChar = featureSpecialChar;
    }

    public String colorIsSpam(){
        if (isSpam == true) {
            return ", isSpam=" + ANSI_RED + " " + isSpam + ANSI_RESET;
        } else {
            return ", isSpam=" + ANSI_GREEN + " " + isSpam + ANSI_RESET;
        }
    }

    @Override
    public String toString() {
        return "EmailData{" +
                "SuspiciousWords=" + featureSuspiciousWords +
                ", StrangeLink=" + featureStrangeLink +
                ", UpperCase=" + featureUpperCase +
                ", HowLongDescription=" + featureHowLongDescription +
                ", SpecialChar=" + featureSpecialChar +
                colorIsSpam() + '}';
    }
}
