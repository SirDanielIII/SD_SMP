package ca.sirdanieliii.SD_SMP.utilities;

import ca.sirdanieliii.SD_SMP.configuration.ConfigManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatPaginator {
    private final TextComponent header;
    private final int page;
    private final int totalPages;
    private final ChatColor colour;
    private final List<TextComponent> dataInPage;
    private TextComponent footer;

    /**
     * @param name the description to put in the header
     * @param data list of data to paginate
     * @param page page number, with 1 being the lowest
     */
    public ChatPaginator(TextComponent name, List<TextComponent> data, int page, ChatColor colour) {
        this.header = Utilities.replaceStr("━━━━━━━━━━━━ ║ {name} ║ ━━━━━━━━━━━━", "{name}", name);
        footer = Utilities.translateMsgClrComponent("&F━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        this.page = page;
        this.dataInPage = getDataInPage(data);
        totalPages = data.size();
        this.colour = colour;
    }

    /**
     * Customize the footer to be different from the default "&F━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
     *
     * @param leftArrow     TextComponent to be used as the left arrow
     * @param rightArrow    TextComponent to be used as the right arrow
     * @param pageLeftCmd   command to run on left arrow click
     * @param pageRightCmd  command to run on right arrow click
     * @param arrowValid    colour of arrows if there are pages to go to
     * @param arrowInvalid  colour of arrows if there are no pages to go to
     * @param pageNumberClr colour of page description
     */
    public void configureFooter(String leftArrow, String rightArrow, String pageLeftCmd, String pageRightCmd, ChatColor arrowValid, ChatColor arrowInvalid, ChatColor pageNumberClr) {
        TextComponent component = new TextComponent(colour + "━━━━━━━━━━");
        TextComponent pageLeft = Utilities.translateMsgClrComponent(leftArrow);
        TextComponent pageRight = Utilities.translateMsgClrComponent(rightArrow);
        if (page > 1) { // Page 1 is the lowest page
            pageLeft.setColor(arrowValid);
            if (pageLeftCmd != null) {
                pageLeft.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, pageLeftCmd));
            }
        } else {
            pageLeft.setColor(arrowInvalid);
        }
        if (page < totalPages) {
            pageRight.setColor(arrowValid);
            if (pageRightCmd != null) {
                pageRight.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, pageRightCmd));
            }
        } else {
            pageRight.setColor(arrowInvalid);
        }

        footer = new TextComponent(component);
        footer.addExtra(pageLeft);
        footer.addExtra(Utilities.translateMsgClrComponent(String.format(colour + "Page " + pageNumberClr + "%d" + colour + " of " + pageNumberClr + "%d", page, totalPages)));
        footer.addExtra(pageRight);
        footer.addExtra(component);
    }

    public void sendPaginatedMessage(Player... players) {
        for (Player player : players) {
            player.spigot().sendMessage(header);
            if (dataInPage.isEmpty() && page == 0) {
                player.spigot().sendMessage(ChatMessageType.valueOf("Throw error here"));
                // throw error
            } else if (dataInPage.isEmpty()) {
                // send no coordinates error
                player.spigot().sendMessage(ChatMessageType.valueOf("&F━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
            } else {
                for (TextComponent component : dataInPage) {
                    player.spigot().sendMessage(component);
                }
                player.spigot().sendMessage(footer);
            }
        }
    }

    /**
     * Return the paginated data (for the current page)
     *
     * @param l list of data to paginate
     * @return list of data in the current page
     */
    private List<TextComponent> getDataInPage(List<TextComponent> l) {
        List<TextComponent> result = new ArrayList<>();

        int startIndex = ConfigManager.linesPerPaginatedChat * page;
        int endIndex = Math.min(startIndex + ConfigManager.linesPerPaginatedChat, l.size());

        for (int i = startIndex; i < endIndex; i++) {
            result.add(l.get(i));
        }
        return result;
    }
}
