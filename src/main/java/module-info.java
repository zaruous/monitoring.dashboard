module org.kyj.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    opens org.kyj.fx.monitoring.dashboard to javafx.fxml;
    exports org.kyj.fx.monitoring.dashboard;
}