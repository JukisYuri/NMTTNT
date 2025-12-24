package model;

import java.util.Map;

public class Node {
    private boolean isLeaf; // Kiểm tra xem có phải nút lá không
    private String label; // Đã kiểm tra lá xong, thì nhãn là spam hoặc ham (not spam)
    private String splitAttribute; // Nếu không phải lá, tiêu chí tách là từ nào
    private Node leftChild; // Nhánh đi theo nếu Email có chứa từ khóa trong splitAttribute
    private Node rightChild; // Nhánh đi theo nếu Email không chứa từ khoá

    private int spamCount;
    private int hamCount;

    public Node() {
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

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
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
