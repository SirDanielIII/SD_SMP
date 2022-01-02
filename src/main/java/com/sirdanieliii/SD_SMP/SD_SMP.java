package com.sirdanieliii.SD_SMP;

import com.sirdanieliii.SD_SMP.commands.*;
import com.sirdanieliii.SD_SMP.configuration.ConfigManager;
import com.sirdanieliii.SD_SMP.events.Events;
import com.sirdanieliii.SD_SMP.items.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static com.sirdanieliii.SD_SMP.configuration.CreateSMPConfig.createSMPConfig;


public class SD_SMP extends JavaPlugin {

    private static SD_SMP instance;
    public static final ConfigManager SMP_CONFIG = new ConfigManager();
    public static final ConfigManager PLAYER_CONFIG = new ConfigManager();

    public SD_SMP() {
        instance = this;
    }

    public static SD_SMP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Custom Config Files
        SMP_CONFIG.setup("", "config");
        createSMPConfig();
        // Initialize Events
        getServer().getPluginManager().registerEvents(new Events(), this);
        // Custom Members
        ItemManager.init();
        // Custom Commands
        Objects.requireNonNull(getCommand("ivan")).setExecutor(new CommandManager());
        Objects.requireNonNull(getCommand("coords")).setExecutor(new CommandManager());
        Objects.requireNonNull(getCommand("death")).setExecutor(new CommandManager());
        Objects.requireNonNull(getCommand("SMP")).setExecutor(new ShowAllCommands());
        Objects.requireNonNull(getCommand("wand")).setExecutor(new Wand());
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Unloading files...");
        this.getServer().getScheduler().cancelTasks(this);
    }

}
