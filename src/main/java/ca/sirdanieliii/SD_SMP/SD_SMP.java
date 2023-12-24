package ca.sirdanieliii.SD_SMP;

import ca.sirdanieliii.SD_SMP.commands.CommandManager;
import ca.sirdanieliii.SD_SMP.commands.SMP;
import ca.sirdanieliii.SD_SMP.commands.Wand;
import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import ca.sirdanieliii.SD_SMP.events.Events;
import ca.sirdanieliii.SD_SMP.events.Scoreboards;
import ca.sirdanieliii.SD_SMP.items.ItemManager;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


public final class SD_SMP extends JavaPlugin {
    private static SD_SMP instance;

    public SD_SMP() {
        instance = this;
    }

    public static SD_SMP getThisPlugin() {
        return instance;
    }

    public static Server getThisPluginServer() {
        return getThisPlugin().getServer();
    }

    @Override
    public void onEnable() {
        ConfigManager.reloadConfigs();
        getServer().getPluginManager().registerEvents(new Events(), this);
        ItemManager.init();
        // Custom Commands
        Objects.requireNonNull(getCommand("ivan")).setExecutor(new CommandManager());
//        Objects.requireNonNull(getCommand("coords")).setExecutor(new CommandManager());
        Objects.requireNonNull(getCommand("death")).setExecutor(new CommandManager());
        Objects.requireNonNull(getCommand("smp")).setExecutor(new SMP());
        Objects.requireNonNull(getCommand("wand")).setExecutor(new Wand());
        if (ConfigManager.healthUnderName) {
            Scoreboards.registerHealthScoreboard(); // Register scoreboard on plugin start
            Scoreboards.addAllPlayersToScoreboard(); // In case of server forced plugin reloads
        }
        this.getLogger().info(String.format("Version %s has finished loading", this.getDescription().getVersion()));
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabling scoreboards...");
        Scoreboards.disableHealthScoreboard();
        this.getLogger().info("Cancelling tasks...");
        this.getServer().getScheduler().cancelTasks(this);
    }
}
