package model;

public class EmailData {
    private int featureFree;
    private int featureStrangeLink;
    private int featureUpperCase;

    public EmailData(int featureFree, int featureStrangeLink, int featureUpperCase) {
        this.featureFree = featureFree;
        this.featureStrangeLink = featureStrangeLink;
        this.featureUpperCase = featureUpperCase;
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
