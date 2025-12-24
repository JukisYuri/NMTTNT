package model;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private Node root;

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";
    private static final String DIM = "\u001B[2m";

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
     * Xây dựng cây quyết định (Recursive)
     */
    public Node buildTree(List<EmailData> data, List<String> attributes) {

        Node node = new Node();

        // Nếu data rỗng (không có email nào) -> Trả về lá mặc định
        if (data.isEmpty()) {
            node.setLeaf(true);
            node.setLabel("ham");
            return node;
        }

        // Nếu tất cả email đều cùng 1 loại (Thuần nhất) -> Trả về lá nhãn đó
        if (isPure(data)) {
            node.setLeaf(true);
            node.setLabel(data.getFirst().getSpam() ? "spam" : "ham");
            // Set thống kê cho nút lá
            int spamCount = 0, hamCount = 0;
            for (EmailData e : data) {
                if (e.getSpam() != null && e.getSpam()) spamCount++;
                else hamCount++;
            }
            node.setSpamCount(spamCount);
            node.setHamCount(hamCount);
            return node;
        }

        // Nếu đã dùng hết thuộc tính để hỏi -> Chọn nhãn theo số đông
        if (attributes.isEmpty()) {
            node.setLeaf(true);
            node.setLabel(getMajorityLabel(data));
            // Set thống kê cho nút lá
            int spamCount = 0, hamCount = 0;
            for (EmailData e : data) {
                if (e.getSpam() != null && e.getSpam()) spamCount++;
                else hamCount++;
            }
            node.setSpamCount(spamCount);
            node.setHamCount(hamCount);
            return node;
        }

        String bestAttribute = null;
        double maxInfoGain = -1.0;

        // Duyệt qua từng thuộc tính còn lại để tính điểm IG
        for (String attr : attributes) {
            double ig = calculateInformationGain(data, attr);
            if (ig > maxInfoGain) {
                maxInfoGain = ig;
                bestAttribute = attr;
            }
        }

        // Nếu IG quá nhỏ, việc tách thêm ko mang lại lợi ích
        if (maxInfoGain < 0.000001) {
            node.setLeaf(true);
            node.setLabel(getMajorityLabel(data));
            // Set thống kê cho nút lá
            int spamCount = 0, hamCount = 0;
            for (EmailData e : data) {
                if (e.getSpam() != null && e.getSpam()) spamCount++;
                else hamCount++;
            }
            node.setSpamCount(spamCount);
            node.setHamCount(hamCount);
            return node;
        }

        node.setLeaf(false);
        node.setSplitAttribute(bestAttribute);

        // Chia data thành 2 nhóm:
        // s_yes: Những email có value = 1
        // s_no:  Những email có value = 0
        List<EmailData> s_yes = new ArrayList<>();
        List<EmailData> s_no = new ArrayList<>();

        for (EmailData e : data) {
            if (e.getAttributeValue(bestAttribute) == 1) {
                s_yes.add(e);
            } else {
                s_no.add(e);
            }
        }

        // Tạo danh sách thuộc tính mới cho lớp con (loại bỏ thuộc tính vừa dùng)
        List<String> remainingAttributes = new ArrayList<>(attributes);
        remainingAttributes.remove(bestAttribute);

        // Tiếp tục xây cây cho nhánh con
        // đi left = yes, đi right = no
        node.setLeftChild(buildTree(s_yes, remainingAttributes));
        node.setRightChild(buildTree(s_no, remainingAttributes));

        // Set thống kê cho nút không phải lá
        int spamCount = 0, hamCount = 0;
        for (EmailData e : data) {
            if (e.getSpam() != null && e.getSpam()) spamCount++;
            else hamCount++;
        }
        node.setSpamCount(spamCount);
        node.setHamCount(hamCount);

        return node;
    }

    /* Phân loại một EmailData bằng cây đã xây dựng */
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

    public String[] explainClassification(EmailData email) {
        StringBuilder reasonTrace = new StringBuilder(); // trả về lí do
        StringBuilder detectedFeatures = new StringBuilder(); // detect ra theo features

        Node current = root;
        int step = 1;
        // Duyệt từ gốc đến lá
        while (!current.isLeaf()) {
            // Lấy giá trị thuộc tính 0/1 trong email
            String attr = current.getSplitAttribute();
            int value = email.getAttributeValue(attr);
            String attrNameVN = getAttributeNameVN(attr);
            reasonTrace.append(step).append(". ");
            if (value == 1) {
                // Đi nhánh Trái
                reasonTrace.append("Có chứa ").append(attrNameVN).append("\n");
                // Thêm vào danh sách chú ý
                if (!detectedFeatures.isEmpty()) detectedFeatures.append(", ");
                detectedFeatures.append(attrNameVN);
                // Di chuyển xuống con trái
                if (current.getLeftChild() != null) {
                    current = current.getLeftChild();
                } else {
                    break;
                }
            } else {
                // Đi nhánh Phải
                reasonTrace.append("Không chứa ").append(attrNameVN).append("\n");
                // Di chuyển xuống con phải
                if (current.getRightChild() != null) {
                    current = current.getRightChild();
                } else {
                    break;
                }
            }
            step++;
        }
        reasonTrace.append("=> ").append(current.getLabel());
        return new String[]{reasonTrace.toString(), detectedFeatures.toString()};
    }

    /**
     * Kiểm tra xem danh sách email có "thuần" không (toàn bộ là spam hoặc toàn bộ là ham)
     */
    private boolean isPure(List<EmailData> data) {
        if (data.isEmpty()) return true;
        boolean firstIsSpam = data.getFirst().getSpam();
        for (EmailData e : data) {
            if (e.getSpam() != firstIsSpam) {
                return false;
            }
        }
        return true;
    }

    /**
     * Lấy nhãn xuất hiện nhiều nhất
     * Dùng khi không còn thuộc tính nào để tách nhưng dữ liệu vẫn lẫn lộn
     */
    private String getMajorityLabel(List<EmailData> data) {
        int spamCount = 0;
        int hamCount = 0;
        for (EmailData e : data) {
            if (e.getSpam()) spamCount++;
            else hamCount++;
        }
        return (spamCount >= hamCount) ? "spam" : "ham";
    }

    private String getAttributeNameVN(String attribute) {
        if (attribute == null) return "Unknown";
        return switch (attribute) {
            case "urgencyWords" -> "Từ ngữ khẩn cấp";
            case "moneyWords" -> "Từ ngữ tiền tệ";
            case "scamFraudWords" -> "Từ ngữ lừa đảo, gian lận";
            case "marketingWords" -> "Từ ngữ quảng cáo/tiếp thị";
            case "healthWords" -> "Từ ngữ sức khoẻ, liên quan y tế";
            case "strangeLink" -> "Đường dẫn (link) lạ";
            case "upperCase" -> "Quá nhiều chữ in hoa";
            case "longDescription" -> "Nội dung quá dài";
            case "specialChar" -> "Nhiều ký tự đặc biệt";
            default -> attribute;
        };
    }

    public void evaluate(List<EmailData> testData) {
        System.out.println("\n--- KẾT QUẢ ĐÁNH GIÁ MODEL TRÊN TẬP TEST ---");

        int total = testData.size();
        int correct = 0;

        // Confusion Matrix
        int tp = 0; // True Positive: Spam -> Báo đúng là Spam
        int tn = 0; // True Negative: Ham -> Báo đúng là Ham
        int fp = 0; // False Positive: Ham -> Báo nhầm là Spam
        int fn = 0; // False Negative: Spam -> Báo nhầm là Ham

        for (EmailData email : testData) {
            String predictedLabel = classify(email);
            boolean isSpamActual = email.getSpam();
            boolean isSpamPredicted = "spam".equals(predictedLabel);

            if (isSpamActual == isSpamPredicted) {
                correct++;
                if (isSpamActual) tp++;
                else tn++;
            } else {
                if (isSpamPredicted) fp++;
                else fn++;
            }
        }

        double accuracy = (total > 0) ? (double) correct / total * 100 : 0;
        double precision = (tp + fp) > 0 ? (double) tp / (tp + fp) * 100 : 0;
        double recall = (tp + fn) > 0 ? (double) tp / (tp + fn) * 100 : 0;
        double f1 = (precision + recall) > 0 ? 2 * (precision * recall) / (precision + recall) : 0;

        System.out.println("1. Tổng số mẫu kiểm tra: " + total);
        System.out.printf("2. Độ chính xác tổng (Accuracy):  %.2f%%%n", accuracy);
        System.out.println("   (Tỉ lệ đoán đúng chung cho cả 2 loại)");

        System.out.printf("3. Precision (Chất lượng báo Spam): %.2f%%%n", precision);
        System.out.println("   (Nếu máy báo là Spam, thì bao nhiêu % là Spam thật?)");

        System.out.printf("4. Recall (Khả năng tóm Spam):      %.2f%%%n", recall);
        System.out.println("   (Trong thực tế có 100 con Spam, máy tóm được bao nhiêu con?)");
        System.out.println("5. F1 Score: " + f1);
    }

    /**
     * In đường đi phân loại của email đã nhập
     */
    public void printClassificationTree(EmailData email) {
        System.out.println();
        System.out.println(CYAN + "═══════════════════════════════════════════════════════════════════" + RESET);
        System.out.println(CYAN + "   " + BOLD + "🌳 CÂY QUYẾT ĐỊNH CHO EMAIL ĐÃ NHẬP" + RESET);
        System.out.println(CYAN + "═══════════════════════════════════════════════════════════════════" + RESET);

        // In features đã phát hiện
        printDetectedFeatures(email);

        System.out.println();
        System.out.println(YELLOW + "--- ĐƯỜNG ĐI TRÊN CÂY ---" + RESET);

        if (root == null) {
            System.out.println(RED + "⚠ Cây chưa được xây dựng!" + RESET);
            return;
        }

        printPathRecursive(root, email, "");

        System.out.println();
        System.out.println(CYAN + "═══════════════════════════════════════════════════════════════════" + RESET);
        System.out.println();
    }

    /**
     * In các đặc trưng đã phát hiện trong email
     */
    private void printDetectedFeatures(EmailData email) {
        System.out.println();
        System.out.println(PURPLE + "📧 Đặc trưng phát hiện:" + RESET);

        String[][] features = {
                {"urgencyWords", "Từ khẩn cấp"},
                {"moneyWords", "Từ tiền tệ"},
                {"scamFraudWords", "Từ lừa đảo"},
                {"marketingWords", "Từ quảng cáo"},
                {"healthWords", "Từ sức khỏe"},
                {"strangeLink", "Link lạ"},
                {"upperCase", "Chữ in hoa"},
                {"longDescription", "Nội dung dài"},
                {"specialChar", "Ký tự đặc biệt"}
        };

        StringBuilder detected = new StringBuilder();
        int count = 0;
        for (String[] f : features) {
            if (email.getAttributeValue(f[0]) == 1) {
                if (count > 0) detected.append(", ");
                detected.append(GREEN).append(f[1]).append(RESET);
                count++;
            }
        }

        if (count == 0) {
            System.out.println("   " + DIM + "(Không phát hiện đặc trưng spam nào)" + RESET);
        } else {
            System.out.println("   " + detected);
            System.out.println("   " + DIM + "(" + count + " đặc trưng được phát hiện)" + RESET);
        }
    }

    /**
     * In đường đi đệ quy - chỉ highlight nhánh email đi qua
     */
    private void printPathRecursive(Node node, EmailData email, String prefix) {
        if (node == null) return;

        if (node.isLeaf()) {
            // Nút lá - kết quả
            String label = node.getLabel().toUpperCase();
            if ("SPAM".equals(label)) {
                System.out.println(prefix + "└── " + RED + BOLD + "★ [SPAM] ★" + RESET);
                System.out.println(prefix + "    " + DIM + "(Độ tin cậy: " + getConfidence(node) + ")" + RESET);
            } else {
                System.out.println(prefix + "└── " + GREEN + BOLD + "✓ [HAM] ✓" + RESET);
                System.out.println(prefix + "    " + DIM + "(Độ tin cậy: " + getConfidence(node) + ")" + RESET);
            }
        } else {
            String attr = node.getSplitAttribute();
            int value = email.getAttributeValue(attr);
            String attrNameVN = getAttributeNameVN(attr);

            // In câu hỏi với thống kê
            String stats = getNodeStats(node);
            System.out.println(prefix + "└── " + CYAN + BOLD + "Hỏi: " + attrNameVN + "?" + RESET + " " + DIM + stats + RESET);

            if (value == 1) {
                // Đi nhánh CÓ
                System.out.println(prefix + "    ├── " + GREEN + BOLD + "(CÓ)" + RESET + " -> " + YELLOW + "đi đường này ▼" + RESET);
                System.out.println(prefix + "    │   " + DIM + getChildStats(node.getLeftChild()) + RESET);
                printPathRecursive(node.getLeftChild(), email, prefix + "    │   ");
                System.out.println(prefix + "    └── " + DIM + "(KHÔNG)" + RESET);
            } else {
                // Đi nhánh KHÔNG
                System.out.println(prefix + "    ├── " + DIM + "(CÓ)" + RESET);
                System.out.println(prefix + "    └── " + RED + BOLD + "(KHÔNG)" + RESET + " -> " + YELLOW + "đi đường này ▼" + RESET);
                System.out.println(prefix + "        " + DIM + getChildStats(node.getRightChild()) + RESET);
                printPathRecursive(node.getRightChild(), email, prefix + "        ");
            }
        }
    }

    /**
     * Lấy thống kê node
     */
    private String getNodeStats(Node node) {
        int total = node.getHamCount() + node.getSpamCount();
        if (total == 0) return "";
        return String.format("[Ham: %d | Spam: %d]", node.getHamCount(), node.getSpamCount());
    }

    /**
     * Lấy thống kê node con
     */
    private String getChildStats(Node node) {
        if (node == null) return "";
        int total = node.getHamCount() + node.getSpamCount();
        if (total == 0) return "";
        return String.format("(Ham: %d | Spam: %d)", node.getHamCount(), node.getSpamCount());
    }

    /**
     * Tính độ tin cậy của kết quả
     */
    private String getConfidence(Node node) {
        int total = node.getHamCount() + node.getSpamCount();
        if (total == 0) return "N/A";
        int majority = Math.max(node.getSpamCount(), node.getHamCount());
        double confidence = (majority * 100.0) / total;
        return String.format("%.1f%% từ %d mẫu", confidence, total);
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}