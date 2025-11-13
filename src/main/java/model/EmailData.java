package model;

public class EmailData {
    private String label;
    private int featureFree;
    private int featureStrangeLink;
    private int featureUpperCase;

    public EmailData(String label, int featureFree, int featureStrangeLink, int featureUpperCase) {
        this.label = label;
        this.featureFree = featureFree;
        this.featureStrangeLink = featureStrangeLink;
        this.featureUpperCase = featureUpperCase;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
}
