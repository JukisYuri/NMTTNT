package model;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private Node root;

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
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

        // Chia thành 3 tập con
        List<EmailData> lowSet = new ArrayList<>();
        List<EmailData> midSet = new ArrayList<>();
        List<EmailData> highSet = new ArrayList<>();

        for (EmailData emailData : data) {
            int value = emailData.getAttributeValue(attributeToCheck);
            switch (value) {
                case 0 -> lowSet.add(emailData);
                case 1 -> midSet.add(emailData);
                case 2 -> highSet.add(emailData);
            }
        }

        // Tính entropy của từng tập
        double entropy_Low = calculateEntropy(lowSet);
        double entropy_Medium = calculateEntropy(midSet);
        double entropy_High = calculateEntropy(highSet);

        // Tính weighted entropy sau khi chia
        int totalDataSize = data.size();
        double weightedEntropy =
                ((double) lowSet.size() / totalDataSize) * entropy_Low
                        + ((double) midSet.size() / totalDataSize) * entropy_Medium
                        + ((double) highSet.size() / totalDataSize) * entropy_High;

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

        if (data.size() < 20) { // Dùng pre-pruning, vì nếu đi vào nhánh mà có size bé quá thì dừng
            node.setLeaf(true);
            node.setLabel(getMajorityLabel(data));
            calculateAndSetStats(node, data);
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
        node.setSplitAttribute(bestAttribute); // Lưu các đặc trưng

        // Chia thành 3 tập con
        List<EmailData> lowSet = new ArrayList<>();
        List<EmailData> midSet = new ArrayList<>();
        List<EmailData> highSet = new ArrayList<>();

        for (EmailData e : data) {
            int value = e.getAttributeValue(bestAttribute);
                switch (value) {
                    case 0 -> lowSet.add(e);
                    case 1 -> midSet.add(e);
                    case 2 -> highSet.add(e);
                }
        }

        // Tạo danh sách thuộc tính mới cho lớp con (loại bỏ thuộc tính vừa dùng)
        List<String> remainingAttributes = new ArrayList<>(attributes);
        remainingAttributes.remove(bestAttribute);

        // Tiếp tục xây cây cho nhánh con
        node.setLowChild(lowSet.isEmpty() ?
                createLeafNode(getMajorityLabel(data), data) : buildTree(lowSet, remainingAttributes));

        node.setMidChild(midSet.isEmpty() ?
                createLeafNode(getMajorityLabel(data), data) : buildTree(midSet, remainingAttributes));

        node.setHighChild(highSet.isEmpty() ?
                createLeafNode(getMajorityLabel(data), data) : buildTree(highSet, remainingAttributes));

        calculateAndSetStats(node, data); // Thống kê
        return node;
    }

    private Node createLeafNode(String label, List<EmailData> data){
        Node node = new Node();
        node.setLeaf(true);
        node.setLabel(label);
        calculateAndSetStats(node, data);
        return node;
    }

    private void calculateAndSetStats(Node node, List<EmailData> data) {
        int spamCount = 0, hamCount = 0;
        for (EmailData e : data) {
            if (e.getSpam() != null && e.getSpam()) spamCount++;
            else hamCount++;
        }
        node.setSpamCount(spamCount);
        node.setHamCount(hamCount);
    }

    /* Phân loại một EmailData bằng cây đã xây dựng */
    public String classify(EmailData email) {
        Node current = root;
        while (!current.isLeaf()) {
            String attr = current.getSplitAttribute();
            int value = email.getAttributeValue(attr);
            current = switch (value) {
                case 0 -> current.getLowChild();
                case 1 -> current.getMidChild();
                case 2 -> current.getHighChild();
                default -> null;
            };
            if (current == null) return "unknown";
        }
        return current.getLabel();
    }

    private String getSpamProbability(Node node) {
        if (node == null) return "N/A";
        int total = node.getSpamCount() + node.getHamCount();
        if (total == 0) return "0%";

        double spamRate = (double) node.getSpamCount() / total * 100;
        return String.format("%.1f%%", spamRate);
    }

    public String[] explainClassification(EmailData email) {
        StringBuilder reasonTrace = new StringBuilder(); // trả về lí do
        StringBuilder detectedFeatures = new StringBuilder(); // detect ra theo features

        Node current = root;
        int step = 1;
        // Duyệt từ gốc đến lá
        while (!current.isLeaf()) {
            String attr = current.getSplitAttribute();
            int value = email.getAttributeValue(attr);
            String attrNameVN = getAttributeNameVN(attr);

            Node nextNode = switch (value) {
                case 0 -> current.getLowChild();
                case 1 -> current.getMidChild();
                case 2 -> current.getHighChild();
                default -> null;
            };
            String spamProb = getSpamProbability(nextNode);

            reasonTrace.append(step).append(". ").append(attrNameVN).append(": ");
            current = switch (value) {
                case 0 -> {
                    reasonTrace.append("Mức độ thấp ").append("(").append(spamProb).append(")").append("\n");
                    yield current.getLowChild();
                }
                case 1 -> {
                    reasonTrace.append("Mức độ trung bình ").append("(").append(spamProb).append(")").append("\n");
                    detectedFeatures.append(attrNameVN);
                    yield current.getMidChild();
                }
                case 2 -> {
                    reasonTrace.append("Mức độ cao ").append("(").append(spamProb).append(")").append("\n");
                    detectedFeatures.append(attrNameVN);
                    yield current.getHighChild();
                }
                default -> {
                    reasonTrace.append("Không xác định\n");
                    yield null;
                }
            };
            if (current == null) break;
            step++;
        }
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
        if (attribute == null) return "Không xác định";
        return switch (attribute) {
            case "urgencyWords"    -> "Mức độ thúc giục, tạo áp lực";
            case "moneyWords"      -> "Đề cập đến tiền bạc, tài chính";
            case "scamFraudWords"  -> "Dấu hiệu hứa hẹn, lừa đảo";
            case "marketingWords"  -> "Nội dung chào mời, quảng bá";
            case "healthWords"     -> "Chủ đề sức khỏe, y tế nhạy cảm";
            case "securityWords"   -> "Cảnh báo bảo mật đáng nghi";
            case "strangeLink"     -> "Liên kết (link) không rõ nguồn gốc";
            case "upperCase"       -> "Tỷ lệ viết hoa bất thường";
            case "longDescription" -> "Độ dài nội dung văn bản";
            case "specialChar"     -> "Sử dụng ký tự đặc biệt quá mức";
            default -> attribute;
        };
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
                {"securityWords", "Từ bảo mật"},
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

    private void printPathRecursive(Node node, EmailData email, String prefix) {
        if (node == null) return;

        if (node.isLeaf()) {
            // Nút lá - kết quả cuối cùng
            String label = node.getLabel().toUpperCase();
            String color = "SPAM".equals(label) ? RED : GREEN;
            String icon = "SPAM".equals(label) ? "★" : "✓";

            System.out.println(prefix + "└── " + color + BOLD + icon + " [" + label + "] " + icon + RESET);
            System.out.println(prefix + "    " + DIM + "(Độ tin cậy: " + getConfidence(node) + ")" + RESET);
        } else {
            String attr = node.getSplitAttribute();
            int value = email.getAttributeValue(attr);
            String attrNameVN = getAttributeNameVN(attr);

            // In câu hỏi hiện tại và thống kê mẫu tại nút đó
            String stats = getNodeStats(node);
            System.out.println(prefix + "└── " + CYAN + BOLD + "Hỏi: " + attrNameVN + "?" + RESET + " " + DIM + stats + RESET);

            // --- HIỂN THỊ 3 NHÁNH (Chỉ highlight nhánh mà email thực sự đi qua) ---

            // 1. Nhánh THẤP (Value = 0)
            printBranch(prefix, "THẤP", value == 0, node.getLowChild(), email);

            // 2. Nhánh TRUNG BÌNH (Value = 1)
            printBranch(prefix, "TRUNG BÌNH", value == 1, node.getMidChild(), email);

            // 3. Nhánh CAO (Value = 2)
            printBranch(prefix, "CAO", value == 2, node.getHighChild(), email, true);
        }
    }

    /**
     * Hàm phụ trợ để in từng nhánh cây một cách gọn gàng
     */
    private void printBranch(String prefix, String label, boolean isPathTaken, Node child, EmailData email) {
        printBranch(prefix, label, isPathTaken, child, email, false);
    }

    private void printBranch(String prefix, String label, boolean isPathTaken, Node child, EmailData email, boolean isLast) {
        String connector = isLast ? "└── " : "├── ";
        String verticalBar = isLast ? "    " : "│   ";

        if (isPathTaken) {
            // Nhánh được chọn: In màu sắc nổi bật và có mũi tên hướng dẫn
            System.out.println(prefix + "    " + connector + YELLOW + BOLD + "[" + label + "]" + RESET + GREEN + " -> đi đường này ▼" + RESET);
            System.out.println(prefix + "    " + verticalBar + DIM + getChildStats(child) + RESET);
            printPathRecursive(child, email, prefix + "    " + verticalBar);
        } else {
            // Nhánh không được chọn: In mờ đi (DIM) để tập trung vào nhánh chính
            System.out.println(prefix + "    " + connector + DIM + "[" + label + "]" + RESET);
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