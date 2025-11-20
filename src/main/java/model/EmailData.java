package model;

public class EmailData {
    private int featureFree;
    private int featureStrangeLink;
    private int featureUpperCase;
    private Boolean isSpam;

    public EmailData(int featureFree, int featureStrangeLink, int featureUpperCase) {
        this.featureFree = featureFree;
        this.featureStrangeLink = featureStrangeLink;
        this.featureUpperCase = featureUpperCase;
        this.isSpam = null;
    }

    // Nếu dataset có nhãn, dùng constructor này
    public EmailData(int featureFree, int featureStrangeLink, int featureUpperCase, boolean isSpam) {
        this.featureFree = featureFree;
        this.featureStrangeLink = featureStrangeLink;
        this.featureUpperCase = featureUpperCase;
        this.isSpam = isSpam;
    }

    /**
     * Helper: lấy giá trị của attribute theo tên (dùng khi tách data theo attribute).
     * attributeName có thể là: "free", "strangeLink", "upperCase"
     */
    public int getAttributeValue(String attributeName) {
        return switch (attributeName) {
            case "free" -> featureFree;
            case "strangeLink" -> featureStrangeLink;
            case "upperCase" -> featureUpperCase;
            default -> 0;
        };
    }

    public int getFeatureFree() {
        return featureFree;
    }

    public void setFeatureFree(int featureFree) {
        this.featureFree = featureFree;
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

    @Override
    public String toString() {
        return "EmailData{" +
                "featureFree=" + featureFree +
                ", featureStrangeLink=" + featureStrangeLink +
                ", featureUpperCase=" + featureUpperCase +
                ", isSpam=" + isSpam +
                '}';
    }
}
