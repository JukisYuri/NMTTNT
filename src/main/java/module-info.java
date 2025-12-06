module org.example.decisiontreemail {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.csv;
    requires java.desktop;


    opens view to javafx.fxml;
    exports view;
    exports controller;
    exports model;
    exports utils;
}