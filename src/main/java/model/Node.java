package model;

import java.util.Map;

public class Node {
    private boolean isLeaf; // Kiểm tra xem có phải nút lá không
    private String label; // Đã kiểm tra lá xong, thì nhãn là spam hoặc ham (not spam)
    private String splitAttribute; // Nếu không phải lá, tiêu chí tách là từ nào

    private Node lowChild;
    private Node midChild;
    private Node highChild;

    private int spamCount;
    private int hamCount;

    public Node() {
    }

    public Node getChildByLevel(int level) {
        return switch (level) {
            case 0 -> lowChild;
            case 1 -> midChild;
            case 2 -> highChild;
            default -> null;
        };
    }

    public Node getLowChild() {
        return lowChild;
    }

    public void setLowChild(Node lowChild) {
        this.lowChild = lowChild;
    }

    public Node getMidChild() {
        return midChild;
    }

    public void setMidChild(Node midChild) {
        this.midChild = midChild;
    }

    public Node getHighChild() {
        return highChild;
    }

    public void setHighChild(Node highChild) {
        this.highChild = highChild;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSplitAttribute() {
        return splitAttribute;
    }

    public void setSplitAttribute(String splitAttribute) {
        this.splitAttribute = splitAttribute;
    }

    public int getSpamCount() {
        return spamCount;
    }

    public void setSpamCount(int spamCount) {
        this.spamCount = spamCount;
    }

    public int getHamCount() {
        return hamCount;
    }

    public void setHamCount(int hamCount) {
        this.hamCount = hamCount;
    }
}
