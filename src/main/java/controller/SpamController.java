package controller;

import model.DecisionTree;
import view.MailFilterFrame;

public class SpamController {
    MailFilterFrame view = new MailFilterFrame();
    DecisionTree model = new DecisionTree();

    public SpamController(MailFilterFrame view, DecisionTree model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Gọi khi muốn khởi tạo pipeline: đọc dữ liệu và build cây
     */
    public void initializeModelFromDataset() {
        // 1. Dùng utils.ReadFile để đọc dataList
        // 2. Xác định danh sách attributes: List<String> attributes = Arrays.asList("free","strangeLink","upperCase");
        // 3. Node root = model.buildTree(dataList, attributes);
        // 4. model.setRoot(root);
    }

    /**
     * Gọi khi người dùng submit 1 email mới:
     * - Convert subject+content -> EmailData (features)
     * - String label = model.classify(email)
     * - String[] explain = model.explainClassification(email)
     * - Update view tương ứng (hiển thị label, explain)
     */
    public void checkEmailAndUpdateView(String subject, String content) {
    }
}
