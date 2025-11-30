package model;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private Node root;

    /**
     * Tính Entropy cho một danh sách email.
     *
     * Hướng dẫn triển khai:
     * 1. Tính Entropy(S). Ở bài toán này giả sử có 2 lớp: "spam" và "ham" (not spam).
     * 2. Đếm số lượng email spam (n_spam) và ham (n_ham) trong danh sách data.
     * 3. Tính p_spam = n_spam / N, p_ham = n_ham / N (bỏ qua các xác suất = 0 khi log).
     * 4. Trả về kết quả entropy (double). Giá trị nằm trong [0, 1] (với log cơ số 2).
     *
     * Ghi chú:
     * - EmailData phải có thông tin label (isSpam) để đếm được; nếu dataset chưa có label,
     *   cần thêm trường isSpam vào EmailData hoặc truyền nhãn qua tham số khác.
     */
    public double calculateEntropy(List<EmailData> data) {
        if (data.isEmpty()) {
            return 0.0;
        }

        int totalDataSize = data.size();
        int spamCount = 0;
        int hamCount = 0;

        // Đếm spam và ham
        for (EmailData emailData : data) {
            if (emailData.getSpam() != null && emailData.getSpam()) {
                spamCount++;
            } else {
                hamCount++;
            }
        }

        // Tính xác suất xuất hiện
        double p_spam = (double) spamCount / (double) totalDataSize;
        double p_ham = (double) hamCount / (double) totalDataSize;

        double entropy = 0;

        // entropy = -p_spam * log2(p_spam) - p_ham * log2(p_ham)
        if (p_spam > 0) {
            entropy -= p_spam * (Math.log(p_spam) / Math.log(2));
        }
        if (p_ham > 0) {
            entropy -= p_ham * (Math.log(p_ham) / Math.log(2));
        }

        return entropy;
    }

    /**
     * Tính Information Gain (IG) cho một attribute (từ khóa) cụ thể.
     *
     * Hướng dẫn triển khai (giả sử attribute là nhị phân: 1 = có, 0 = không):
     * 1. Tính entropy của tập cha H(S) = calculateEntropy(data).
     * 2. Chia data thành hai tập con theo giá trị attribute:
     *      S_yes  = emails có attribute = 1
     *      S_no   = emails có attribute = 0
     * 3. Tính entropy của từng tập con H(S_yes), H(S_no)
     * 4. Tính entropy có điều kiện sau khi chia:
     *      H_after = (|S_yes|/|S|) * H(S_yes) + (|S_no|/|S|) * H(S_no)
     * 5. Information Gain = H(S) - H_after
     * 6. Trả về giá trị IG (double). Giá trị càng lớn => attribute càng tốt để tách.
     *
     * Ghi chú:
     * - attributeToCheck: tên thuộc tính như "free", "strangeLink", "upperCase" (tự định nghĩa).
     * - EmailData cần có getter để trả về giá trị thuộc tính theo tên; nếu không có,
     *   có thể map tên attribute sang field tương ứng.
     */
    public double calculateInformationGain(List<EmailData> data, String attributeToCheck) {
        if (data.isEmpty()) {
            return 0.0;
        }

        // Tính entropy
        double calculateEntropy = calculateEntropy(data);

        // Chia thành 2 tập con
        List<EmailData> s_yes = new ArrayList<>();
        List<EmailData> s_no = new ArrayList<>();

        for (EmailData emailData : data) {
            if (emailData.getAttributeValue(attributeToCheck) == 1) {
                s_yes.add(emailData);
            } else {
                s_no.add(emailData);
            }
        }

        // Tính entropy của từng tập

        double entropy_yes = calculateEntropy(s_yes);
        double entropy_no = calculateEntropy(s_no);

        // Tính weighted entropy sau khi chia
        int totalDataSize = data.size();
        double weightedEntropy = ((double) s_yes.size() / totalDataSize) * entropy_yes +
                ((double) s_no.size() / totalDataSize) * entropy_no;

        // Information Gain = H(s) - H(sau chia)
        return calculateEntropy - weightedEntropy;
    }

    /**
     * Xây dựng cây quyết định (recursive).
     *
     * Hướng dẫn triển khai:
     * 1. Base cases:
     *    - Nếu tất cả email trong data đều cùng label (tất cả spam hoặc tất cả ham):
     *        -> tạo Node lá với isLeaf = true, label = "spam" hoặc "ham"
     *    - Nếu danh sách attributes rỗng (không còn thuộc tính để tách):
     *        -> tạo Node lá với label là nhãn nhiều nhất (majority class) trong data
     *    - Nếu data rỗng:
     *        -> trả về Node lá với label là nhãn mặc định hoặc null
     * 2. Nếu không phải base case:
     *    - Với mỗi attribute trong attributes, tính IG = calculateInformationGain(data, attribute)
     *    - Chọn attribute có IG lớn nhất (bestAttribute)
     *    - Tạo Node hiện tại: node.splitAttribute = bestAttribute; node.isLeaf = false
     *    - Chia data thành hai tập S_yes (attribute=1) và S_no (attribute=0)
     *    - Gọi đệ quy:
     *         node.leftChild = buildTree(S_yes, remainingAttributes)
     *         node.rightChild = buildTree(S_no, remainingAttributes)
     *    - Trả về node
     *
     * Ghi chú:
     * - remainingAttributes = attributes - {bestAttribute}
     * - Có thể tối ưu bằng cách dừng khi IG nhỏ hơn threshold.
     */
    public Node buildTree(List<EmailData> data, List<String> attributes) {
        // TODO: implement theo comment ở trên
        return null;
    }

    /**
     * Phân loại một EmailData bằng cây đã xây dựng.
     *
     * Hướng dẫn triển khai:
     * 1. Bắt đầu từ root:
     *    - Nếu node.isLeaf => trả về node.label
     *    - Ngược lại, lấy giá trị thuộc tính splitAttribute từ email (0 hoặc 1)
     *      nếu = 1 => đi vào leftChild (theo quy ước)
     *      nếu = 0 => đi vào rightChild
     * 2. Lặp đến khi gặp lá, trả về nhãn.
     *
     * Ghi chú:
     * - Nếu root == null => nên buildTree trước khi classify hoặc trả "unknown".
     */
    public String classify(EmailData email) {
        Node current = root;
        while (!current.isLeaf()) {
            String attr = current.getSplitAttribute();
            int value = email.getAttributeValue(attr);
            if (value == 1) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
        }
        return current.getLabel();
    }

    /**
     * Giải thích/thu thập "lý do" khi một email bị đánh dấu spam.
     *
     * Hướng dẫn triển khai:
     * - Khi classify, có thể thu thập các thuộc tính mà email khớp trên đường đi (ví dụ: chứa "miễn phí", có link lạ, nhiều chữ in hoa).
     * - Trả về một chuỗi mô tả ngắn hoặc danh sách lý do
     *
     * Trả về:
     * - Mảng String nơi phần tử 0 là "lý do" (reason), phần tử 1 là "những từ chú ý" (notice)
     */
    public String[] explainClassification(EmailData email) {
        // TODO: implement to produce human-readable reasons and highlighted words
        return new String[] {"", ""};
    }

    // Getter/Setter cho root để có thể buildTree từ bên ngoài
    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}