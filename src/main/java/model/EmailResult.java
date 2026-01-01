package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmailResult {
    private final StringProperty content;
    private final StringProperty label;
    private final String reason;

    public EmailResult(String content, String label, String reason) {
        this.content = new SimpleStringProperty(content);
        this.label = new SimpleStringProperty(label);
        this.reason = reason;
    }

    public String getContent() { return content.get(); } // Dùng cho lưu kết quả file txt
    public StringProperty contentProperty() { return content; }
    public String getLabel() { return label.get(); }
    public String getReason() { return reason; }
}