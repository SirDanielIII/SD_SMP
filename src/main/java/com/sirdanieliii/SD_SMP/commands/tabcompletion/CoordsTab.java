package com.sirdanieliii.SD_SMP.commands.tabcompletion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CoordsTab implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> actions = new ArrayList<>();
            actions.add("set");
            actions.add("list");
            actions.add("clear");
            actions.add("send");
            return actions;
        } else if (args.length == 3) {
            List<String> types = new ArrayList<>();
            types.add("here");
            return types;
        }
        return null;
    }
}
