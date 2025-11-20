module org.example.decisiontreemail {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.csv;


    opens view to javafx.fxml;
    exports view;
}