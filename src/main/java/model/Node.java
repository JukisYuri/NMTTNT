package model;

import java.util.Map;

public class Node {
    private boolean isLeaf; // Kiểm tra xem có phải nút lá không
    private String label; // Đã kiểm tra lá xong, thì nhãn là spam hoặc ham (not spam)
    private String splitAttribute;
    Map<String, Node> children; // Các nhánh con
}
