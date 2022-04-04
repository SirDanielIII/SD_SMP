//package com.sirdanieliii.SD_SMP.configuration;
//
//import org.bukkit.plugin.PluginDescriptionFile;
//
//import static com.sirdanieliii.SD_SMP.SD_SMP.SMP_CONFIG;
//import static com.sirdanieliii.SD_SMP.SD_SMP.getInstance;
//
//public class CreateSMPConfig {
//    public static void createSMPConfig() {
//        SMP_CONFIG.setup("", "config");
//        String[] headers = {"plugin-version", "MoTD", "welcome", "enable-custom-join-messages", "join-messages",
//                "enable-custom-quit-messages", "quit-messages", "enable-custom-sleep-messages", "sleep-messages", "kill", "death"};
//        PluginDescriptionFile version = getInstance().getDescription(); //Gets plugin.yml
//        String configVersion = version.getVersion();
//
//        String[] joinMessages =
//                {
//                        "didn't qualify for World Cup",
//                        "needs to get gooder",
//                        "always sings on-key",
//                        "is awesome",
//                        "is awesomesauce",
//                        "is a silly little goose",
//                        "is not deserving of an insult",
//                        "doesn't wear socks",
//                        "is big brain",
//                        "doesn't even life",
//                        "is a jelly donut",
//                        "can't breathe underwater",
//                        "can't tie their own shoes",
//                        "has nothing better to do right now",
//                        "is a chad",
//                };
//
//        String[] quitMessages =
//                {
//                        "folded",
//                        "went to go get a girlfriend",
//                        "went to go get a snack",
//                        "wanted to touch grass",
//                        "went to find their dad at the grocery store",
//                        "rage quit",
//                        "has a date with Shrek",
//                        "got cancelled",
//                        "went to go play Fortnite",
//                        "opened Premiere Pro",
//                        "opened After Effects",
//                        "was cringe and left",
//                        "didn't want to play anymore",
//                        "left the game",
//                        "got a life",
//                        "was kidnapped by Monika",
//                        "§KLISD FH DFZIO UGHDRIO",
//                        "dipped",
//                        "has to do their homework",
//                        "had to log off because their mom said so",
//                        "yeeted themself",
//                        "says bye",
//                        "got tired of the people here",
//                        "left to ponder their life decisions",
//                        "left the stove on"
//                };
//
//        String[] sleepMessages =
//                {
//                        "fell asleep",
//                        "dozed off dreaming",
//                        "crashed like Sir Daniel III's PC",
//                        "went AWOL",
//                        "committed sleep",
//                        "initiated hibernation",
//                        "went to dream world",
//                        "started snoozing like a chad",
//                        "snores loudly",
//                        "started to sleep",
//                        "died of exhaustion",
//                        "collapsed",
//                        "started napping",
//                        "started dreaming",
//                        "is having a nightmare",
//                        "has gone out like a lamp",
//                        "wandered off to bed"};
//
//        String[] kill = {
//                "brutally murdered",
//                "slaughtered",
//                "clapped",
//                "massacred",
//                "slayed",
//                "dookie'ed on",
//                "360 OOGA BOOGA BOOGA'ED",
//                "killed"};
//
//        String[] death = {
//                "your own stupidity",
//                "lack of skill",
//                "incompetence",
//                "not having any earnings"};
//
//        for (String header : headers) {
//            if (!SMP_CONFIG.getConfig().contains(header)) {
//                SMP_CONFIG.getConfig().createSection(header);
//                // Set Default Values
//                switch (header) {
//                    case ("plugin-version") -> SMP_CONFIG.getConfig().set("plugin-version", Float.parseFloat(configVersion));
//                    case ("MoTD") -> {
//                        SMP_CONFIG.getConfig().set("MoTD.1", "A Minecraft SMP");
//                        SMP_CONFIG.getConfig().set("MoTD.2", "");
//                    }
//                    case ("welcome") -> {
//                        SMP_CONFIG.getConfig().set("welcome.title", "Hello There");
//                        SMP_CONFIG.getConfig().set("welcome.subtitle", "§6Welcome to the SMP");
//                    }
//                    case ("enable-custom-join-messages") -> SMP_CONFIG.getConfig().set("enable-custom-join-messages", true);
//                    case ("join-messages") -> SMP_CONFIG.getConfig().set("join-messages", joinMessages);
//                    case ("enable-custom-quit-messages") -> SMP_CONFIG.getConfig().set("enable-custom-quit-messages", true);
//                    case ("quit-messages") -> SMP_CONFIG.getConfig().set("quit-messages", quitMessages);
//                    case ("enable-custom-sleep-messages") -> SMP_CONFIG.getConfig().set("enable-custom-sleep-messages", true);
//                    case ("sleep-messages") -> SMP_CONFIG.getConfig().set("sleep-messages", sleepMessages);
//                    case ("kill") -> SMP_CONFIG.getConfig().set("kill", kill);
//                    case ("death") -> SMP_CONFIG.getConfig().set("death", death);
//                }
//            }
//        }
//        // Update Config Version
//        if (!configVersion.equalsIgnoreCase(String.valueOf(SMP_CONFIG.getConfig().getString("plugin-version")))) {
//            SMP_CONFIG.getConfig().set("plugin-version", CreateSMPConfig.class.getPackage().getImplementationVersion());
//        }
//        SMP_CONFIG.save();
//    }
//}
