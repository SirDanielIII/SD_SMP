package ca.sirdanieliii.SD_SMP.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 * Reference an enum specifying the pixel length specific characters to create
 * a perfectly centered message for Minecraft's chat, which is 154 pixels wide.
 *
 * <pre>
 *
 * Originally created by SirSpoodles; Enum HashMap optimization by snowyCoder;
 * Method improved by martijnpu; Touched up by Sir Daniel III.
 *
 * <a href="https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/">Source</a>
 */
public class TextCenterUtils {
    private final static int CENTER_PX = 154;

    public static String getCenteredChatMsg(String message) {
        if (message == null) message = "null";
        if (message.isEmpty()) {
            return "";
        }
        String[] lines = Utilities.translateMsgClr(message).split("\n", 40);
        StringBuilder returnMessage = new StringBuilder();

        for (String line : lines) {
            int messagePxSize = 0;
            boolean previousCode = false;
            boolean isBold = false;

            for (char c : line.toCharArray()) {
                if (c == '§') {
                    previousCode = true;
                } else if (previousCode) {
                    previousCode = false;
                    isBold = c == 'l';
                } else {
                    DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                    messagePxSize = isBold ? messagePxSize + dFI.getBoldLength() : messagePxSize + dFI.getLength();
                    messagePxSize++;
                }
            }
            int toCompensate = CENTER_PX - messagePxSize / 2;
            int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            int compensated = 0;
            StringBuilder sb = new StringBuilder();
            while (compensated < toCompensate) {
                sb.append(" ");
                compensated += spaceLength;
            }
            returnMessage.append(sb).append(line).append("\n");
        }
        return returnMessage.toString();
    }

    public enum DefaultFontInfo {
        A('A', 5),
        a('a', 5),
        B('B', 5),
        b('b', 5),
        C('C', 5),
        c('c', 5),
        D('D', 5),
        d('d', 5),
        E('E', 5),
        e('e', 5),
        F('F', 5),
        f('f', 4),
        G('G', 5),
        g('g', 5),
        H('H', 5),
        h('h', 5),
        I('I', 3),
        i('i', 1),
        J('J', 5),
        j('j', 5),
        K('K', 5),
        k('k', 4),
        L('L', 5),
        l('l', 1),
        M('M', 5),
        m('m', 5),
        N('N', 5),
        n('n', 5),
        O('O', 5),
        o('o', 5),
        P('P', 5),
        p('p', 5),
        Q('Q', 5),
        q('q', 5),
        R('R', 5),
        r('r', 5),
        S('S', 5),
        s('s', 5),
        T('T', 5),
        t('t', 4),
        U('U', 5),
        u('u', 5),
        V('V', 5),
        v('v', 5),
        W('W', 5),
        w('w', 5),
        X('X', 5),
        x('x', 5),
        Y('Y', 5),
        y('y', 5),
        Z('Z', 5),
        z('z', 5),
        NUM_1('1', 5),
        NUM_2('2', 5),
        NUM_3('3', 5),
        NUM_4('4', 5),
        NUM_5('5', 5),
        NUM_6('6', 5),
        NUM_7('7', 5),
        NUM_8('8', 5),
        NUM_9('9', 5),
        NUM_0('0', 5),
        EXCLAMATION_POINT('!', 1),
        AT_SYMBOL('@', 6),
        NUM_SIGN('#', 5),
        DOLLAR_SIGN('$', 5),
        PERCENT('%', 5),
        UP_ARROW('^', 5),
        AMPERSAND('&', 5),
        ASTERISK('*', 5),
        LEFT_PARENTHESIS('(', 4),
        RIGHT_PARENTHESIS(')', 4),
        MINUS('-', 5),
        UNDERSCORE('_', 5),
        PLUS_SIGN('+', 5),
        EQUALS_SIGN('=', 5),
        LEFT_CURL_BRACE('{', 4),
        RIGHT_CURL_BRACE('}', 4),
        LEFT_BRACKET('[', 3),
        RIGHT_BRACKET(']', 3),
        COLON(':', 1),
        SEMI_COLON(';', 1),
        DOUBLE_QUOTE('"', 3),
        SINGLE_QUOTE('\'', 1),
        LEFT_ARROW('<', 4),
        RIGHT_ARROW('>', 4),
        QUESTION_MARK('?', 5),
        SLASH('/', 5),
        BACK_SLASH('\\', 5),
        LINE('|', 1),
        TILDE('~', 5),
        TICK('`', 2),
        PERIOD('.', 1),
        COMMA(',', 1),
        SPACE(' ', 3),
        DEFAULT('\0', 4);

        private static final Map<Character, DefaultFontInfo> CHAR_MAP = new HashMap<>(values().length, 1.1f);

        static {
            for (DefaultFontInfo info : values())
                CHAR_MAP.put(info.character, info);
        }

        private final char character;
        private final int length;

        DefaultFontInfo(char character, int length) {
            this.character = character;
            this.length = length;
        }

        public static DefaultFontInfo getDefaultFontInfo(char c) {
            return CHAR_MAP.getOrDefault(c, DEFAULT);
        }

        public char getCharacter() {
            return this.character;
        }

        public int getLength() {
            return this.length;
        }

        public int getBoldLength() {
            if (this == DefaultFontInfo.SPACE) return this.getLength();
            return this.length + 1;
        }
    }

//    /**
//     * This code doesn't work lul - Sir Daniel III
//     *
//     * Original code from <a href="https://www.spigotmc.org/threads/center-motds-and-messages.354209/">...</a>
//     * User: MarcusNerloe & FiXed
//     *
//     * @param text MotD text
//     * @return centered MotD
//     */
//    public static String getCenteredMotDLine(String text, int lineLength) {
//        char[] chars = text.toCharArray();
//
//        boolean isBold = false;
//        double length = 0;
//        ChatColor pholder = null;
//        for (int i = 0; i < chars.length; i++) {
//            if (chars[i] == '&' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
//                // we don't need to change the length...? it's already where we want it if we ignore the colors
//                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC // these 4 don't cancel bold
//                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
//                    isBold = chars[i + 1] == 'l'; // true if the next is a bold modifier
//                }
//                i++; // we don't care about the next since it's a color init
//            } else {
//                length += 1;
//                if (isBold) length += 0.1555555555555556;
//            }
//        }
//        double spaces = (lineLength - length) / 2;
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < spaces; i++) {
//            builder.append(' ');
//        }
//        builder.append(translateMsgClr(text));
//
//        return builder.toString();
//    }
}
