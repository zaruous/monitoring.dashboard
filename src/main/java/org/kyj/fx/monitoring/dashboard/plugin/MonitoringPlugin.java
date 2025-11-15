package org.kyj.fx.monitoring.dashboard.plugin;

import javafx.scene.Node;

public interface MonitoringPlugin {

    String getName();

    String getCategory();

    Node getMonitoringView();
}
