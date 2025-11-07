module org.example.decisiontreemail {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.decisiontreemail to javafx.fxml;
    exports org.example.decisiontreemail;
}