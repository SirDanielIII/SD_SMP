package com.sirdanieliii.SD_SMP.configuration;

import org.bukkit.entity.Player;

import static com.sirdanieliii.SD_SMP.SD_SMP.SMP_CONFIG;

public class CreateSMPConfig {
    public static void createSMPConfig() {
        SMP_CONFIG.setup("", "config");
        String[] headers = {"config-version", "MoTD", "welcome", "enable-custom-join-messages", "join-messages",
                "enable-custom-quit-messages", "quit-messages", "enable-custom-sleep-messages", "sleep-messages", "kill", "death"};
        String configVersion = "EARLY ACCESS";

        String[] joinMessages =
                {"is a CHUBBY CHEEK BOY HONDA CIVIC",
                        "is stupido",
                        "needs to get gooder",
                        "is a donkey",
                        "identifies as a toaster",
                        "is adopted",
                        "is awesome",
                        "is awesomesauce",
                        "is not intelligent",
                        "is a silly little goose",
                        "wants to be a hot tub streamer",
                        "is not deserving of an insult",
                        "doesn't wear socks",
                        "is a chad",
                        "is big brain"};

        String[] quitMessages =
                {"folded",
                        "went to go get a girlfriend",
                        "wanted to touch grass",
                        "went to find their dad at the grocery store",
                        "has a date with Shrek",
                        "got cancelled",
                        "went to go play Fortnite",
                        "opened Premiere Pro",
                        "opened After Effects",
                        "was cringe and left",
                        "didn't want to play anymore",
                        "left the game",
                        "clapped himself",
                        "got a life",
                        "was kidnapped by Monika",
                        "§KLISD FH DFZIO UGHDRIO",
                        "dipped"};

        String[] sleepMessages =
                {"fell asleep",
                        "dozed off dreaming",
                        "crashed like Sir Daniel III's PC",
                        "went AWOL",
                        "commited sleep",
                        "initiated hibernation",
                        "started snoozing like a chad",
                        "snores too loudly",
                        "started dreaming of how dogwater they are at Fortnite",
                        "started to sleep",
                        "died of exhuastion",
                        "collasped",
                        "started napping",
                        "started dreaming",
                        "is having a nightmare",
                        "has gone out like a lamp",
                        "remembered that they are an orphan, \nand is now sleeping while contemplating the meaning of life",
                        "wandered off to bed"};

        String[] kill = {"brutally murdered",
                "slaughtered",
                "clapped",
                "massacred",
                "slayed",
                "dookie'ed on",
                "360 OOGA BOOGA BOOGA'ED",
                "killed"};

        String[] death = {"your own stupidity",
                "lack of skill",
                "incompetence",
                "not having any earnings"};

        for (String header : headers) {
            if (!SMP_CONFIG.getConfig().contains(header)) {
                SMP_CONFIG.getConfig().createSection(header);
                // Set Default Values
                switch (header) {
                    case ("config-version") -> SMP_CONFIG.getConfig().set("config-version", configVersion);
                    case ("MoTD") -> {
                        SMP_CONFIG.getConfig().set("MoTD.1", "A Minecraft SMP");
                        SMP_CONFIG.getConfig().set("MoTD.2", "");
                    }
                    case ("welcome") -> {
                        SMP_CONFIG.getConfig().set("welcome.title", "Hello There");
                        SMP_CONFIG.getConfig().set("welcome.subtitle", "§6Welcome to the SMP");
                    }
                    case ("enable-custom-join-messages") -> SMP_CONFIG.getConfig().set("enable-custom-join-messages", true);
                    case ("join-messages") -> SMP_CONFIG.getConfig().set("join-messages", joinMessages);
                    case ("enable-custom-quit-messages") -> SMP_CONFIG.getConfig().set("enable-custom-quit-messages", true);
                    case ("quit-messages") -> SMP_CONFIG.getConfig().set("quit-messages", quitMessages);
                    case ("enable-custom-sleep-messages") -> SMP_CONFIG.getConfig().set("enable-custom-sleep-messages", true);
                    case ("sleep-messages") -> SMP_CONFIG.getConfig().set("sleep-messages", sleepMessages);
                    case ("kill") -> SMP_CONFIG.getConfig().set("kill", kill);
                    case ("death") -> SMP_CONFIG.getConfig().set("death", death);
                }
            }
        }
        SMP_CONFIG.save();
    }
}
