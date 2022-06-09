package de.waischbrot.simpletablist.config;

import de.waischbrot.simpletablist.Main;
import de.waischbrot.simpletablist.utils.Pair;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ConfigManager {
    private final Main plugin;
    private final FileConfiguration config;
    private final String configName;

    public ConfigManager(Main plugin, String configName) {
        this.plugin = plugin;
        config = plugin.getConfig();
        this.configName = configName;
    }

    public Pair<Integer, Integer> getUpdateRates() {
        return new Pair<>(config.getInt("updatePrefix"), config.getInt("updateHeader"));
    }

    public Pair<List<String>, List<String>> loadConfig() {
        var dir = new File("./"+plugin.pluginName+"/");
        var configFile = new File("./"+plugin.pluginName+"/"+configName);
        if (!dir.exists() || !dir.isDirectory() || !configFile.exists()) createDefaultConfig(); //makes sure that the config file exists

        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        var header = config.getStringList("header");
        var footer = config.getStringList("footer");
        return new Pair<>(header, footer);
    }

    private void createDefaultConfig() {
        var dir = new File("./"+plugin.pluginName+"/");
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        var configFile = new File("./"+plugin.pluginName+"/"+configName);
        if (!configFile.exists() || !configFile.isFile()) {
            try {
                configFile.createNewFile();
                new FileOutputStream(configFile).write(
                        ("##################################\n" +
                                "#           TabListPlugin        #\n" +
                                "##################################\n" +
                                "#\n" +
                                "#by ff__\n" +
                                "updatePrefix: 3000\n" +
                                "updateHeader: 5000\n" +
                                "header:\n" +
                                "  - '&7---------------&6header1&7---------------'\n" +
                                "  - 'ezerhze'\n" +
                                "  - '#123456header2'\n" +
                                "  - '#ade405ethh'\n" +
                                "footer:\n" +
                                "  - ''\n" +
                                "  - 'sometext'\n" +
                                "  - '&adiscord.gg/rubymc'\n" +
                                "  - '&7---------------&6footer4&7--------------'\n").getBytes()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
