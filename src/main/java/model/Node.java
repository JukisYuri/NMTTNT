package model;

import java.util.Map;

public class Node {
    private boolean isLeaf; // Kiểm tra xem có phải nút lá không
    private String label; // Đã kiểm tra lá xong, thì nhãn là spam hoặc ham (not spam)
    private String splitAttribute; // Nếu không phải lá, tiêu chí tách là từ nào
    private Node leftChild; // Nhánh đi theo nếu Email có chứa từ khóa trong splitAttribute
    private Node rightChild; // Nhánh đi theo nếu Email không chứa từ khoá

    public Node() {
    }
}
