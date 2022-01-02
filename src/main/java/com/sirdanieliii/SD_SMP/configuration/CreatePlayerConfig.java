package com.sirdanieliii.SD_SMP.configuration;

import org.bukkit.entity.Player;

public class CreatePlayerConfig {
    private static final ConfigManager PLAYER_CONFIG = new ConfigManager();

    public static void createPlayerConfig(Player player) {
        String[] headers = {"UUID", "name", "murders", "death_by_player", "death_by_other", "coordinates"};
        PLAYER_CONFIG.setup("playerdata", player.getUniqueId().toString());

        for (String header : headers) {
            if (!PLAYER_CONFIG.getConfig().contains(header)) {
                PLAYER_CONFIG.getConfig().createSection(header);
                // Set Default Values
                switch (header) {
                    case ("UUID") -> PLAYER_CONFIG.getConfig().set("UUID", player.getUniqueId().toString());
                    case ("murders") -> PLAYER_CONFIG.getConfig().set("murders", 0.0D);
                    case ("death_by_player") -> PLAYER_CONFIG.getConfig().set("death_by_player", 0.0D);
                    case ("death_by_other") -> PLAYER_CONFIG.getConfig().set("death_by_other", 0.0D);
                }
            }
        }
        // Update Player Name
        if (!player.getName().equalsIgnoreCase(String.valueOf(PLAYER_CONFIG.getConfig().contains("name")))) {
            PLAYER_CONFIG.getConfig().set("name", player.getName());
        }
        PLAYER_CONFIG.save();
    }
}
