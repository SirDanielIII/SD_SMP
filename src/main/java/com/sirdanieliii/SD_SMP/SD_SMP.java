package com.sirdanieliii.SD_SMP;

import com.sirdanieliii.SD_SMP.commands.CommandManager;
import com.sirdanieliii.SD_SMP.commands.ShowAllCommands;
import com.sirdanieliii.SD_SMP.commands.Wand;
import com.sirdanieliii.SD_SMP.configuration.ConfigManager;
import com.sirdanieliii.SD_SMP.events.Events;
import com.sirdanieliii.SD_SMP.items.ItemManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public class SD_SMP extends JavaPlugin {
    public static ConfigManager CONFIG_MANAGER;
    private static SD_SMP instance;

    public SD_SMP() {
        instance = this;
    }

    public static SD_SMP getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Config Files
        CONFIG_MANAGER = new ConfigManager();
        CONFIG_MANAGER.setup();
        // Initialize Events
        getServer().getPluginManager().registerEvents(new Events(), this);
        // Custom Items
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
