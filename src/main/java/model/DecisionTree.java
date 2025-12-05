package model;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private Node root;

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
            // Lấy nhãn của email đầu tiên làm đại diện
            node.setLabel(data.getFirst().getSpam() ? "spam" : "ham");
            return node;
        }

        // Nếu đã dùng hết thuộc tính để hỏi -> Chọn nhãn theo số đông
        if (attributes.isEmpty()) {
            node.setLeaf(true);
            node.setLabel(getMajorityLabel(data));
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
        // Quy ước: Left = Yes (Có), Right = No (Không)
        node.setLeftChild(buildTree(s_yes, remainingAttributes));
        node.setRightChild(buildTree(s_no, remainingAttributes));

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
        StringBuilder reasonTrace = new StringBuilder();
        StringBuilder detectedFeatures = new StringBuilder();

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
            case "suspiciousWords" -> "Từ khóa quảng cáo/đáng ngờ";
            case "strangeLink" -> "Đường dẫn (link) lạ";
            case "upperCase" -> "Quá nhiều chữ in hoa";
            case "longDescription" -> "Nội dung quá dài";
            case "specialChar" -> "Nhiều ký tự đặc biệt";
            default -> attribute;
        };
    }

    public void setRoot(Node root) {
        this.root = root;
    }
}