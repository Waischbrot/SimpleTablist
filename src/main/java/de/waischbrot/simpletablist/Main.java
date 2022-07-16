package de.waischbrot.simpletablist;

import de.waischbrot.simpletablist.commands.ScoreboardCommand;
import de.waischbrot.simpletablist.commands.TabListCommand;
import de.waischbrot.simpletablist.scoreboard.ScoreBoardManager;
import de.waischbrot.simpletablist.tab.TabListManager;
import de.waischbrot.simpletablist.util.SpigotStringUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§6TabList§7] ";
    public boolean running = true;
    public int waitRank;
    public int waitHeader;
    private TabListManager tabListManager;

    @Override
    public void onEnable() {
        log("Start Loading Config");
        File configDir = new File("./plugins/TabList");
        if (!configDir.isDirectory() || !configDir.exists())
            configDir.mkdir();
        File configFile = new File("./plugins/TabList/config.yml");
        if (!configFile.exists())
            writeDefaultConfig(configFile);
        var config = getConfig();

        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            warn("Config File may not exist yet");
        }

        /*
        if (!validateConfig(config)) {
            warn("Invalid Config.");
            warn("Default get written");
            writeDefaultConfig(configFile);
        } else log("Config valid");
         */

        var header = config.getStringList("tabList.header");
        var footer = config.getStringList("tabList.footer");

        waitRank = config.getInt("tabList.rankUpdate", 3000);
        waitHeader = config.getInt("tabList.textUpdate", 3000);

        var chatFormatString = config.getString("chat.prefixString", "§4Set 'chat.prefixString' for chat message formatting!\n$rank§7$player_name ");

        log("Config loaded successfully");

        tabListManager = new TabListManager(this,
                SpigotStringUtil.format(String.join("\n", header)),
                SpigotStringUtil.format(String.join("\n", footer)),
                chatFormatString);
        tabListManager.startSorter();

        var title = config.getString("scoreboard.title", "&4No title defined in Config");
        var content = config.getStringList("scoreboard.content");

        var scoreboardManager = new ScoreBoardManager(content, title, this);

        Bukkit.getOnlinePlayers().forEach(tabListManager::sendTab);

        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent ev) {
                tabListManager.sendTab(ev.getPlayer());
            }

        }, this);

        getCommand("scoreboard").setExecutor(new ScoreboardCommand(scoreboardManager));
        getCommand("tablist").setExecutor(new TabListCommand(this));

        log("plugin started Successfully");
    }

    private boolean validateConfig(FileConfiguration config) {
        return config.contains("tabList.header") &&
                config.contains("tabList.footer") &&
                config.contains("tabList.textUpdate") &&
                config.contains("tabList.rankUpdate");
    }

    private void writeDefaultConfig(File config)  {
        try {
            config.createNewFile();
            new FileOutputStream(config).write("""
                    ##################################
                    #           TabListPlugin        #
                    ##################################
                    #
                    #by Waischbrot
                    tabList.rankUpdate: 3000
                    tabList.textUpdate: 5000
                    tabList.header:
                      - '&7---------------&6header1&7---------------'
                      - 'ezerhze'
                      - '#123456header2'
                      - '#ade405ethh'
                    tabList.footer:
                      - ''
                      - 'sometext'
                      - '&adiscord.gg/rubymc'
                      - '&7---------------&6footer4&7--------------'
                    chat.prefixString: '<$rank§7$player_name§r> '
                    scoreboard.title: '#0f0f0fThis is a Hex Title'
                    scoreboard.content:
                      - 'qerthqehth'
                    
                    """.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {

        running = false;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        var config = getConfig();
        var header = config.getStringList("tabList.header");
        var footer = config.getStringList("tabList.footer");

        waitRank = config.getInt("tabList.rankUpdate", 3000);
        waitHeader = config.getInt("tabList.textUpdate", 3000);

        var chatFormatString = config.getString("chat.prefixString", "§4Set 'chat.prefixString' for chat message formatting!\n$rank§7$player_name ");

        log("Config loaded successfully");

        tabListManager = new TabListManager(this,
                SpigotStringUtil.format(String.join("\n", header)),
                SpigotStringUtil.format(String.join("\n", footer)),
                chatFormatString);
        running = true;

        tabListManager.startSorter();

        Bukkit.getOnlinePlayers().forEach(tabListManager::sendTab);
    }

    @Override
    public void onDisable() {
        running = false;
    }

    private void log(String message) {
        getLogger().log(Level.INFO, message);
    }

    private void warn(String message) {
        getLogger().log(Level.WARNING, message);
    }

    public TabListManager getTabListManager() {
        return tabListManager;
    }
}
