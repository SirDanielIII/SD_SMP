package ca.sirdanieliii.SD_SMP.utilities;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isAlphabetic;

public class Utilities {
    public static String getCardinalDirection(Player event) {
        double rotation = (event.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
        if (0 <= rotation && rotation < 22.5) {
            return "W";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NW";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "N";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "NE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "E";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SE";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "S";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "SW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }

    // Get Offset Based off Direction
    public static Vector offsetFromDirection(Player event, Double offset) {
        Vector loc = null;
        switch (Objects.requireNonNull(getCardinalDirection(event))) {
            case ("N") -> loc = new Vector(0, 0, -offset);
            case ("E") -> loc = new Vector(offset, 0, 0);
            case ("S") -> loc = new Vector(0, 0, offset);
            case ("W") -> loc = new Vector(-offset, 0, 0);
            case ("NE") -> loc = new Vector(offset, 0, -offset);
            case ("SE") -> loc = new Vector(offset, 0, offset);
            case ("NW") -> loc = new Vector(-offset, 0, -offset);
            case ("SW") -> loc = new Vector(-offset, 0, offset);
        }
        return loc;
    }

    public static String randomMsgFromLst(List<String> lst) {
        if (lst.size() == 0) throw new IllegalArgumentException("List is empty");
        return lst.get(new Random().nextInt(lst.size()));
    }

    public static String replaceStr(String msg, String toReplace, String replace) {
        return translateMsgClr(msg.replace(toReplace, replace));
    }

    public static String replaceStr(String msg, List<String> toReplace, List<String> replace) {
        if (toReplace.size() != replace.size()) throw new IllegalArgumentException("toReplace and replace must have the same size!");
        for (int i = 0; i < toReplace.size(); i++) {
            msg = msg.replace(toReplace.get(i), replace.get(i));
        }
        return translateMsgClr(msg);
    }

    public static TextComponent replaceStr(String msg, String textToReplace, TextComponent replacementComponent) {
        TextComponent message = new TextComponent();
        List<String> msgParts = splitStringByEncasedWords(msg);
        for (String key : msgParts) {
            if (key.equals(textToReplace)) {
                message.addExtra(replacementComponent);
            } else {
                message.addExtra(translateMsgClrComponent(key));
            }
        }
        return message;
    }


    public static TextComponent replaceStr(String msg, Map<String, TextComponent> toReplace) {
        TextComponent message = new TextComponent();
        List<String> msgParts = splitStringByEncasedWords(msg);
        for (String key : msgParts) {
            if (toReplace.get(key) != null) {
                message.addExtra(toReplace.get(key));
            } else {
                message.addExtra(translateMsgClrComponent(key));
            }
        }
        return message;
    }

    /**
     * Thanks Chat-GPT
     *
     * @param input String to be split by {any_word}
     * @return List of strings split by {any_word}
     */
    public static List<String> splitStringByEncasedWords(String input) {
        List<String> splitStrings = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{[^{}]+}");
        Matcher matcher = pattern.matcher(input);
        int currentIndex = 0;
        while (matcher.find()) {
            int matchStart = matcher.start();
            if (matchStart > currentIndex) {
                splitStrings.add(input.substring(currentIndex, matchStart));
            }
            splitStrings.add(matcher.group());
            currentIndex = matcher.end();
        }
        if (currentIndex < input.length()) {
            splitStrings.add(input.substring(currentIndex));
        }
        return splitStrings;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;
        for (char c : input.toLowerCase().toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }

    public static String translateMsgClr(String msg) {
        // Original code by Kody Simpson -> https://gitlab.com/kody-simpson/spigot/1.16-color-translator/-/blob/master/ColorUtils.java
        String[] texts = msg.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        StringBuilder finalMsg = new StringBuilder();
        for (int i = 0; i < texts.length; i++) {
            if (texts[i].equalsIgnoreCase("&")) {
                i++; // Get the next string
                if (texts[i].charAt(0) == '#') finalMsg.append(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7))).append(texts[i].substring(7));
                else finalMsg.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
            } else finalMsg.append(texts[i]);
        }
        return finalMsg.toString();
    }

    public static TextComponent translateMsgClrComponent(String text) {
        /*
         Same result as translateMsgClr(), but for TextComponents instead since hex codes need to be implemented differently
         Original code by Kody Simpson -> https://gitlab.com/kody-simpson/spigot/1.16-color-translator/-/blob/master/ColorUtils.java
         */
        String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
        ComponentBuilder builder = new ComponentBuilder();
        for (int i = 0; i < texts.length; i++) {
            TextComponent subComponent = new TextComponent();
            if (texts[i].equalsIgnoreCase("&")) {
                // Get the next string
                i++;
                if (texts[i].charAt(0) == '#') {
                    subComponent.setText(texts[i].substring(7));
                    subComponent.setColor(net.md_5.bungee.api.ChatColor.of(texts[i].substring(0, 7)));
                    builder.append(subComponent);
                } else {
                    if (texts[i].length() > 1) {
                        subComponent.setText(texts[i].substring(1));
                    } else {
                        subComponent.setText(" ");
                    }
                    switch (Character.toLowerCase(texts[i].charAt(0))) {
                        case '0' -> subComponent.setColor(ChatColor.BLACK);
                        case '1' -> subComponent.setColor(ChatColor.DARK_BLUE);
                        case '2' -> subComponent.setColor(ChatColor.DARK_GREEN);
                        case '3' -> subComponent.setColor(ChatColor.DARK_AQUA);
                        case '4' -> subComponent.setColor(ChatColor.DARK_RED);
                        case '5' -> subComponent.setColor(ChatColor.DARK_PURPLE);
                        case '6' -> subComponent.setColor(ChatColor.GOLD);
                        case '7' -> subComponent.setColor(ChatColor.GRAY);
                        case '8' -> subComponent.setColor(ChatColor.DARK_GRAY);
                        case '9' -> subComponent.setColor(ChatColor.BLUE);
                        case 'a' -> subComponent.setColor(ChatColor.GREEN);
                        case 'b' -> subComponent.setColor(ChatColor.AQUA);
                        case 'c' -> subComponent.setColor(ChatColor.RED);
                        case 'd' -> subComponent.setColor(ChatColor.LIGHT_PURPLE);
                        case 'e' -> subComponent.setColor(ChatColor.YELLOW);
                        case 'f' -> subComponent.setColor(ChatColor.WHITE);
                        case 'k' -> subComponent.setObfuscated(true);
                        case 'l' -> subComponent.setBold(true);
                        case 'm' -> subComponent.setStrikethrough(true);
                        case 'n' -> subComponent.setUnderlined(true);
                        case 'o' -> subComponent.setItalic(true);
                        case 'r' -> subComponent.setColor(ChatColor.RESET);
                    }
                    builder.append(subComponent);
                }
            } else {
                builder.append(texts[i]);
            }
        }
        return new TextComponent(builder.create());
    }

    @Nullable
    public static String cleanStrForYMLKey(String arg) {
        if (arg == null) return null;
        StringBuilder finalStr = new StringBuilder();
        for (Character c : arg.toCharArray()) {
            if (isAlphabetic(c) || Arrays.asList('_', '-', '\'').contains(c) || Character.isDigit(c)) {
                finalStr.append(c);
            }
        }
        if (finalStr.length() != 0) return finalStr.toString();
        else return null;
    }
}
