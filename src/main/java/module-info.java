module org.example.lab4visual {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens org.example.lab4visual to javafx.fxml;
    exports org.example.lab4visual;
}