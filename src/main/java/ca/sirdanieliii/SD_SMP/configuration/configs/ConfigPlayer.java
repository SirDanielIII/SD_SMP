package ca.sirdanieliii.SD_SMP.configuration.configs;

import ca.sirdanieliii.SD_SMP.SD_SMP;
import ca.sirdanieliii.SD_SMP.configuration.ConfigYML;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ca.sirdanieliii.SD_SMP.coords.CoordsUtility.addNewWorldToPlayerConfig;
import static ca.sirdanieliii.SD_SMP.coords.CoordsUtility.getDefaultWorldIDs;
import static ca.sirdanieliii.SD_SMP.utilities.Utilities.cleanStrForYMLKey;

public class ConfigPlayer extends ConfigYML {
    public ConfigPlayer(Player player) {
        super("/playerdata/", player.getUniqueId().toString());
    }

    public ConfigPlayer(String path, String filename, String referencePath, String referenceFile) {
        super(path, filename, referencePath, referenceFile);
    }

    public boolean update(Player player) {
        File oldFile = new File(SD_SMP.getThisPlugin().getDataFolder() + path, filename + "-old.yml");
        if (oldFile.exists()) {
            if (oldFile.delete()) SD_SMP.getThisPlugin().getLogger().info(String.format("Found %s before updating %s.yml! Deleting...", oldFile.getPath(), filename));
        }
        if (!(this.getFile().renameTo(oldFile))) {
            SD_SMP.getThisPlugin().getLogger().warning(String.format("Could not rename %s.yml to %s-old.yml while updating configs", filename, filename));
            return false;
        }
        this.reload(oldFile); // Update variables to reference the renamed file
        ConfigYML newFile = new ConfigYML(path, filename, referencePath, referenceFile);
        // From this point on, "this" refers to the "old file", and newFile is our new "updated file"
        UUID[] defaultWorlds = getDefaultWorldIDs();
        // Only create entries for default worlds at first to avoid clogging up the config file if there are multiple worlds loaded.
        for (UUID id : defaultWorlds) {
            addNewWorldToPlayerConfig(newFile, id);
        }

        if (this.getVersion() == newFile.getVersion()) {
            remapValuesToNew(this, newFile);
            if (this.delete()) { // Delete "old" file
                this.reload(newFile.getFile()); // Update variables to reference renamed file
                return true;
            }
            return false;
        }

        if (Arrays.asList(0, 1).contains(this.getVersion()) && newFile.getVersion() == 2) { // If no config-version value exists it is 0
            SD_SMP.getThisPlugin().getLogger().info(String.format("The config for %s is outdated! Migrating from version 1 to 2", player.getDisplayName()));
            String[] dimensions = {"overworld", "nether", "the_end"};
            try {
                for (int i = 0; i < 3; i++) { // We're going to loop through the dimensions
                    for (String key : Objects.requireNonNull(this.getConfig().getConfigurationSection("coordinates." + dimensions[i])).getKeys(false)) {
                        String name = cleanStrForYMLKey(key);
                        if (name == null) continue; // Go to next key in for loop if name isn't valid
                        for (String axis : List.of("x", "y", "z")) { // Loop through xyz and add the values into the new file
                            newFile.getConfig().set(
                                    String.format("coordinates.%s.coords.%s.%s", defaultWorlds[i].toString(), name, axis),
                                    this.getConfig().getInt(String.format("coordinates.%s.%s.%s", dimensions[i], name, axis.toUpperCase()))); // Axis in old config is uppercase
                        }
                    }
                }
            } catch (NullPointerException ignored) {
            }
            newFile.getConfig().set("uuid", this.getConfig().getString("UUID"));
            newFile.getConfig().set("name", this.getConfig().getString("name"));
            newFile.getConfig().set("kills", this.getConfig().getInt("murders"));
            newFile.getConfig().set("death_by_player", this.getConfig().getInt("death_by_player"));
            newFile.getConfig().set("death_by_nonplayer", this.getConfig().getInt("death_by_other"));
            newFile.save();
            if (this.delete()) { // Delete "old" file
                this.reload(newFile.getFile()); // Update variables to reference renamed file
                SD_SMP.getThisPlugin().getLogger().info(String.format("Successfully migrated the config for %s to version 2", player.getDisplayName()));
                return true;
            } else {
                SD_SMP.getThisPlugin().getLogger().info("Couldn't delete " + newFile.getConfig().getName() + "-old.yml");
            }
        }
        return false;
    }

    public void updateName(Player player) {
        if (Arrays.asList(1, 2).contains(this.getVersion())) {
            this.getConfig().set("uuid", player.getUniqueId().toString());
            this.getConfig().set("name", player.getDisplayName());
            this.save();
        }
    }
}
