package model;

public class EmailData {
    private int featureUrgencyWords; // Những từ chỉ sự khẩn cấp
    private int featureMoneyWords; // Những từ liên quan đến tiền tệ
    private int featureScamFraudWords; // Những từ liên quan đến scam, quá dễ dàng có được
    private int featureMarketingWords; // Những từ liên quan đến chào hàng
    private int featureHealthWords; // Những từ liên quan đến sức khoẻ
    private int featureSecurityWords; // Những từ liên quan đến bảo mật
    private int featureStrangeLink; // Link lạ

    private int featureUpperCase; // Số lượng chữ cái viết hoa
    private int featureHowLongDescription; // Tỉ lệ nội dung email dài bao nhiêu
    private int featureSpecialChar; // Tỉ lệ bao nhiêu kí tự đặc biệt
    private Boolean isSpam;

    // Color
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    // Constructor này quan trọng với Controller
    public EmailData(int featureUrgencyWords, int featureMoneyWords, int featureScamFraudWords, int featureMarketingWords, int featureHealthWords, int featureSecurityWords, int featureStrangeLink, int featureUpperCase, int featureHowLongDescription, int featureSpecialChar) {
        this.featureUrgencyWords = featureUrgencyWords;
        this.featureMoneyWords = featureMoneyWords;
        this.featureScamFraudWords = featureScamFraudWords;
        this.featureMarketingWords = featureMarketingWords;
        this.featureHealthWords = featureHealthWords;
        this.featureStrangeLink = featureStrangeLink;
        this.featureSecurityWords = featureSecurityWords;

        this.featureUpperCase = featureUpperCase;
        this.featureHowLongDescription = featureHowLongDescription;
        this.featureSpecialChar = featureSpecialChar;
        this.isSpam = null;
    }

    // Nếu dataset có nhãn, dùng constructor này. Chủ yếu được dùng cho đọc file
    public EmailData(int featureUrgencyWords, int featureMoneyWords, int featureScamFraudWords, int featureMarketingWords, int featureHealthWords, int featureSecurityWords, int featureStrangeLink, int featureUpperCase, int featureHowLongDescription, int featureSpecialChar, boolean isSpam) {
        this.featureUrgencyWords = featureUrgencyWords;
        this.featureMoneyWords = featureMoneyWords;
        this.featureScamFraudWords = featureScamFraudWords;
        this.featureMarketingWords = featureMarketingWords;
        this.featureHealthWords = featureHealthWords;
        this.featureStrangeLink = featureStrangeLink;
        this.featureSecurityWords = featureSecurityWords;

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
            case "urgencyWords" -> featureUrgencyWords;
            case "moneyWords" -> featureMoneyWords;
            case "scamFraudWords" -> featureScamFraudWords;
            case "marketingWords" -> featureMarketingWords;
            case "healthWords" -> featureHealthWords;
            case "securityWords" -> featureSecurityWords;
            case "strangeLink" -> featureStrangeLink;
            case "upperCase" -> featureUpperCase;
            case "longDescription" -> featureHowLongDescription;
            case "specialChar" -> featureSpecialChar;
            default -> 0;
        };
    }

    public int getFeatureSecurityWords() {
        return featureSecurityWords;
    }

    public void setFeatureSecurityWords(int featureSecurityWords) {
        this.featureSecurityWords = featureSecurityWords;
    }

    public int getFeatureUrgencyWords() {
        return featureUrgencyWords;
    }

    public void setFeatureUrgencyWords(int featureUrgencyWords) {
        this.featureUrgencyWords = featureUrgencyWords;
    }

    public int getFeatureMoneyWords() {
        return featureMoneyWords;
    }

    public void setFeatureMoneyWords(int featureMoneyWords) {
        this.featureMoneyWords = featureMoneyWords;
    }

    public int getFeatureScamFraudWords() {
        return featureScamFraudWords;
    }

    public void setFeatureScamFraudWords(int featureScamFraudWords) {
        this.featureScamFraudWords = featureScamFraudWords;
    }

    public int getFeatureMarketingWords() {
        return featureMarketingWords;
    }

    public void setFeatureMarketingWords(int featureMarketingWords) {
        this.featureMarketingWords = featureMarketingWords;
    }

    public int getFeatureHealthWords() {
        return featureHealthWords;
    }

    public void setFeatureHealthWords(int featureHealthWords) {
        this.featureHealthWords = featureHealthWords;
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
            return " isSpam=" + ANSI_RED + isSpam + ANSI_RESET;
        } else {
            return " isSpam=" + ANSI_GREEN + isSpam + ANSI_RESET;
        }
    }

    @Override
    public String toString() {
        return "EmailData{" +
                "featureUrgencyWords=" + featureUrgencyWords +
                ", featureMoneyWords=" + featureMoneyWords +
                ", featureScamFraudWords=" + featureScamFraudWords +
                ", featureMarketingWords=" + featureMarketingWords +
                ", featureHealthWords=" + featureHealthWords +
                ", featureSecurityWords=" + featureSecurityWords +
                ", featureStrangeLink=" + featureStrangeLink +
                ", featureUpperCase=" + featureUpperCase +
                ", featureHowLongDescription=" + featureHowLongDescription +
                ", featureSpecialChar=" + featureSpecialChar +
                "," + colorIsSpam() +
                '}';
    }
}
