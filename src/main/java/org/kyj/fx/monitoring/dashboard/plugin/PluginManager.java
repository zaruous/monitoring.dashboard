package org.kyj.fx.monitoring.dashboard.plugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginManager {

    private static final String PLUGIN_DIR = "plugin";

    public static List<MonitoringPlugin> loadPlugins() {
        List<MonitoringPlugin> plugins = new ArrayList<>();
        File pluginDir = new File(PLUGIN_DIR);

        if (!pluginDir.exists() || !pluginDir.isDirectory()) {
            System.out.println("Plugin directory not found: " + PLUGIN_DIR);
            return plugins;
        }

        File[] files = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (files == null) {
            return plugins;
        }

        for (File file : files) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()});
                ServiceLoader<MonitoringPlugin> loader = ServiceLoader.load(MonitoringPlugin.class, classLoader);
                for (MonitoringPlugin plugin : loader) {
                    plugins.add(plugin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return plugins;
    }
}
