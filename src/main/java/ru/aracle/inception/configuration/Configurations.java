package ru.aracle.inception.configuration;

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ru.aracle.inception.Inception;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Configurations {

    File file;
    FileConfiguration configuration;
    JavaPlugin instance;
    String filename;

    public Configurations(JavaPlugin instance, String filename) {
        this.file = new File(instance.getDataFolder(), filename);
        this.configuration = YamlConfiguration.loadConfiguration(file);
        this.instance = instance;
        this.filename = filename;
    }

    public void create() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                instance.saveResource(filename, true);
                configuration.load(file);
            } catch (IOException | InvalidConfigurationException exception) {
                Inception.error(exception.getMessage());
            }
        }
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException exception) {
            Inception.error(exception.getMessage());
        }
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration configuration() {
        return configuration;
    }

    public String string(String path) {
        return configuration.getString(path);
    }

    public Integer integer(String path) {
        return configuration.getInt(path);
    }

    public boolean statement(String path) {
        return configuration.getBoolean(path);
    }

    public Location location(String path) {
        return configuration.getLocation(path);
    }

    public List<String> strings(String path) {
        return configuration.getStringList(path);
    }

    public List<Integer> integers(String path) {
        return configuration.getIntegerList(path);
    }

    public List<Boolean> statements(String path) {
        return configuration.getBooleanList(path);
    }

    public void set(String path, Object value) {
        configuration.set(path, value);
    }

    public String convert() {
        return configuration.saveToString();
    }

    public void deconvert(String converted) {
        try {
            configuration.loadFromString(converted);
        } catch (InvalidConfigurationException exception) {
            Inception.error(exception.getMessage());
        }
    }
}
