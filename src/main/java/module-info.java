module org.kyj.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;



    opens org.kyj.fx.monitoring.dashboard to javafx.fxml, com.google.gson;
    exports org.kyj.fx.monitoring.dashboard;
}