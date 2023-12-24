package ca.sirdanieliii.SD_SMP.configuration.configs;

import ca.sirdanieliii.SD_SMP.SD_SMP;
import ca.sirdanieliii.SD_SMP.configuration.ConfigYML;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConfigMain extends ConfigYML {
    public ConfigMain(String path, String filename, String referencePath, String referenceFile) {
        super(path, filename, referencePath, referenceFile);
    }

    @Override
    public boolean update() {
        File oldFile = new File(SD_SMP.getThisPlugin().getDataFolder() + path, filename + "-old.yml");
        if (oldFile.exists()) {
            if (oldFile.delete()) SD_SMP.getThisPlugin().getLogger().info(String.format("Found %s before updating %s.yml! Deleting...", oldFile, filename));
        }
        if (!(this.getFile().renameTo(oldFile))) {
            SD_SMP.getThisPlugin().getLogger().warning(String.format("Could not rename %s.yml to %s-old.yml while updating configs", filename, filename));
            return false;
        }
        this.reload(oldFile); // Update variables to reference the renamed file
        ConfigYML newFile = new ConfigYML(path, filename, referencePath, referenceFile);
        // From this point on, "this" refers to the "old file", and newFile is our new "updated file"

        if (this.getVersion() == newFile.getVersion()) {
            remapValuesToNew(this, newFile);
            if (this.delete()) { // Delete "old" file
                this.reload(newFile.getFile()); // Update variables to reference renamed file
                SD_SMP.getThisPlugin().getLogger().info(String.format(String.format("Updated %s.yml", filename)));
                return true;
            }
            return false;
        }
        if (Arrays.asList(0, 1).contains(this.getVersion()) && newFile.getVersion() == 2) {
            SD_SMP.getThisPlugin().getLogger().info(String.format("%s.yml is outdated! Migrating from version 1 to 2", filename));
            newFile.getConfig().set("motd.line_1.msg", Objects.requireNonNull(this.getConfig().getString("MoTD.1")).replaceAll("§", "&"));
            newFile.getConfig().set("motd.line_2.msg", Objects.requireNonNull(this.getConfig().getString("MoTD.2")).replaceAll("§", "&"));
            newFile.getConfig().set("welcome.title", Objects.requireNonNull(this.getConfig().getString("welcome.title")).replaceAll("§", "&"));
            newFile.getConfig().set("welcome.subtitle", Objects.requireNonNull(this.getConfig().getString("welcome.subtitle")).replaceAll("§", "&"));
            newFile.getConfig().set("custom-join-messages-enable", this.getConfig().getBoolean("enable-custom-join-messages"));
            newFile.getConfig().set("custom-quit-messages-enable", this.getConfig().getBoolean("enable-custom-quit-messages"));
            newFile.getConfig().set("custom-sleep-messages-enable", this.getConfig().getBoolean("enable-custom-sleep-messages"));
            newFile.save();

            // Migrate custom messages to new file
            ConfigYML messages = new ConfigYML("", "messages");
            messages.getConfig().set("join_messages", replaceSectionSignInLst(this.getConfig().getStringList("join-messages")));
            messages.getConfig().set("quit_messages", replaceSectionSignInLst(this.getConfig().getStringList("quit-messages")));
            messages.getConfig().set("sleep_messages", replaceSectionSignInLst(this.getConfig().getStringList("sleep-messages")));
            messages.getConfig().set("describe_death", replaceSectionSignInLst(this.getConfig().getStringList("death")));
            messages.getConfig().set("describe_kill", replaceSectionSignInLst(this.getConfig().getStringList("kill")));
            messages.save();
            if (this.delete()) { // Delete "old" file
                this.reload(newFile.getFile()); // Update variables to reference renamed file
                SD_SMP.getThisPlugin().getLogger().info(String.format("Successfully migrated %s.yml to version 2", filename));
                SD_SMP.getThisPlugin().getLogger().info("Configurable messages are now found located in messages.yml");
                SD_SMP.getThisPlugin().getLogger().info("Error messages are now customizable in error_messages.yml");
                SD_SMP.getThisPlugin().getLogger().info("Delete messages.yml & run /smp reload if you want to see the new default messages :)");
                return true;
            } else {
                SD_SMP.getThisPlugin().getLogger().info("Couldn't delete " + newFile.getConfig().getName() + "-old.yml");
            }
        }
        return false;
    }

    private List<String> replaceSectionSignInLst(List<String> lst) {
        List<String> newLst = new ArrayList<>();
        for (String i : lst) newLst.add(i.replaceAll("§", "&"));
        return newLst;
    }
}
