module org.kyj.fx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;

    // Dependencies for WebApp
    requires io.javalin;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;

    uses org.kyj.fx.monitoring.dashboard.plugin.MonitoringPlugin;

    opens org.kyj.fx.monitoring.dashboard to javafx.fxml, com.google.gson;
    opens org.kyj.fx.monitoring.dashboard.web to com.fasterxml.jackson.databind;

    exports org.kyj.fx.monitoring.dashboard;
    exports org.kyj.fx.monitoring.dashboard.plugin;
    exports org.kyj.fx.monitoring.dashboard.web;
}