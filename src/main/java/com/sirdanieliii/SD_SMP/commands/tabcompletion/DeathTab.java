package com.sirdanieliii.SD_SMP.commands.tabcompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class DeathTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> types = new ArrayList<>();
            types.add("murders");
            types.add("player");
            types.add("nonplayer");
            types.add("total");
            types.add("kdr");
            return types;
        }
        return null;
    }
}
