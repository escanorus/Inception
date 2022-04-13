package ru.aracle.inception;

import org.bukkit.plugin.java.JavaPlugin;
import ru.aracle.inception.configuration.Configurations;
import ru.aracle.inception.connection.Connection;
import ru.aracle.inception.connection.DataManager;

import java.util.logging.Level;

public final class Inception extends JavaPlugin {

    public static Inception instance;
    public static Configurations settings;
    public static DataManager connection;

    @Override
    public void onEnable() {
        instance = this;
        settings = new Configurations(instance, "settings.yml");
        settings.create();
        connection = new Connection();
    }

    @Override
    public void onDisable() {
        settings.reload();
        settings.save();
        if (connection != null) connection.close();
    }

    public static void error(String message) {
        instance.getLogger().log(Level.SEVERE, message);
    }

    public static void info(String message) {
        instance.getLogger().log(Level.INFO, message);
    }

    public static void warning(String message) {
        instance.getLogger().log(Level.WARNING, message);
    }

}
