package de.waischbrot.simpletablist;

import de.waischbrot.simpletablist.commands.TablistAutoCompleter;
import de.waischbrot.simpletablist.commands.TablistCommand;
import de.waischbrot.simpletablist.config.ConfigManager;
import de.waischbrot.simpletablist.tablist.TablistManager;
import de.waischbrot.simpletablist.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public String pluginName = "plugins/TabList";
    public boolean running = false;
    private TablistManager tabListManager;

    public int waitHeader;
    public int waitRank;

    @Override
    public void onEnable() {
        //check for dependencies
        System.out.println("Starting Plugin");
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceHolderApi"))
            System.out.println("PlaceHolder API found");
        else throw new IllegalStateException("PlaceHolder API does not exist");
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms"))
            System.out.println("LuckPermsFound");
        else throw new IllegalStateException("LuckPerms not found");

        running = true;
        ConfigManager configManager = new ConfigManager(this, "config.yml");
        initCommands();

        getServer().getPluginManager().registerEvents(new MainListener(this), this);

        //load config
        var text = configManager.loadConfig();

        var header = StringUtil.format(String.join("\n", text.v1()));
        var footer = StringUtil.format(String.join("\n", text.v2()));
        tabListManager = new TablistManager(this, header, footer);

        var rates = configManager.getUpdateRates();
        waitHeader = rates.v2();
        waitRank = rates.v1();

        tabListManager.startSorter();
        tabListManager.sendTab(Bukkit.getOnlinePlayers());
    }

    @Override
    public void onDisable() {
        running = false;
        tabListManager.stopSorter();
    }

    public TablistManager getTabListManager() {
        return tabListManager;
    }

    private void initCommands() {
        var command = getCommand("tablist");
        if (command == null) throw new IllegalStateException("Something went wrong while trying to register commands (plugin broken)");
        command.setTabCompleter(new TablistAutoCompleter());
        command.setExecutor(new TablistCommand(this));
    }
}
