module org.kyj.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql; // 추가된 부분

    opens org.kyj.fx.monitoring.dashboard to javafx.fxml;
    exports org.kyj.fx.monitoring.dashboard;
}