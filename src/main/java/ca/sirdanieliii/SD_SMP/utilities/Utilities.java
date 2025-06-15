package ca.sirdanieliii.SD_SMP.utilities;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Character.isAlphabetic;

public class Utilities {
    /**
     * Finds a safe location 2 blocks in front of the player.
     *
     * @param player The player for whom to find the location.
     * @return The highest block's location, 2 blocks in front of the player.
     */
    public static Location getSafeLocationInFront(Player player) {
        Location playerLocation = player.getLocation();
        Location targetLocation = playerLocation.clone().add(playerLocation.getDirection().setY(0).normalize().multiply(2));
        World world = targetLocation.getWorld();
        assert world != null;
        return world.getHighestBlockAt(targetLocation.getBlockX(), targetLocation.getBlockZ()).getLocation();
    }

    public static String randomMsgFromLst(List<String> lst) {
        if (lst.isEmpty()) throw new IllegalArgumentException("Given list is empty");
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
                else finalMsg.append(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
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
                        case '0' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.BLACK);
                        case '1' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
                        case '2' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
                        case '3' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
                        case '4' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_RED);
                        case '5' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_PURPLE);
                        case '6' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                        case '7' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.GRAY);
                        case '8' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_GRAY);
                        case '9' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.BLUE);
                        case 'a' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                        case 'b' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                        case 'c' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
                        case 'd' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
                        case 'e' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                        case 'f', 'r' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                        case 'k' -> subComponent.setObfuscated(true);
                        case 'l' -> subComponent.setBold(true);
                        case 'm' -> subComponent.setStrikethrough(true);
                        case 'n' -> subComponent.setUnderlined(true);
                        case 'o' -> subComponent.setItalic(true);
                    }
                    builder.append(subComponent);
                }
            } else {
                builder.append(texts[i]);
            }
        }
        return new TextComponent(builder.create());
    }

    public static TextComponent translateMsgClrComponent(TextComponent inputComponent) {
        ComponentBuilder builder = new ComponentBuilder();
        for (BaseComponent baseComponent : inputComponent.getExtra()) {
            if (baseComponent instanceof TextComponent subComponent) {
                String text = subComponent.getText();
                String[] texts = text.split(String.format("((?<=%1$s)|(?=%1$s))", "&"));
                for (int i = 0; i < texts.length; i++) {
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
                                case '0' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.BLACK);
                                case '1' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_BLUE);
                                case '2' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_GREEN);
                                case '3' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
                                case '4' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_RED);
                                case '5' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_PURPLE);
                                case '6' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.GOLD);
                                case '7' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.GRAY);
                                case '8' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.DARK_GRAY);
                                case '9' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.BLUE);
                                case 'a' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.GREEN);
                                case 'b' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.AQUA);
                                case 'c' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
                                case 'd' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
                                case 'e' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
                                case 'f', 'r' -> subComponent.setColor(net.md_5.bungee.api.ChatColor.WHITE);
                                case 'k' -> subComponent.setObfuscated(true);
                                case 'l' -> subComponent.setBold(true);
                                case 'm' -> subComponent.setStrikethrough(true);
                                case 'n' -> subComponent.setUnderlined(true);
                                case 'o' -> subComponent.setItalic(true);
                            }
                            builder.append(subComponent);
                        }
                    } else {
                        builder.append(texts[i]);
                    }
                }
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
        if (!finalStr.isEmpty()) return finalStr.toString();
        else return null;
    }

    public static boolean isStringNumber(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
