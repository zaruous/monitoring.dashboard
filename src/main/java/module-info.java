module org.kyj.fx {
    // JavaFX Modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    // Database Module
    requires java.sql;

    // --- Google API & HTTP Client Modules ---

    // Google API Services
    requires com.google.api.services.sheets;

    // Google Authentication & Authorization
    requires com.google.api.client.auth;
    // 'requires com.google.api.client.extensions.java6.auth.oauth2;' -> Incorrect (This is a package name)
//    requires com.google.oauth.client.java6; // Corrected: This is the automatic module name
//    requires com.google.api.client.extensions.jetty.auth.oauth2;

    // JSON Processor for Google API
    requires com.google.api.client.json.gson;

    // Jetty Server (for local OAuth2 callback)
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.util;
    requires org.eclipse.jetty.http;

    // Opens the package to be used by JavaFX and Google's GSON library
    opens org.kyj.fx.monitoring.dashboard to javafx.fxml, com.google.gson;

    // Exports the main package
    exports org.kyj.fx.monitoring.dashboard;
}
