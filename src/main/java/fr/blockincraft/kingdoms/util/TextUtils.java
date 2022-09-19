package fr.blockincraft.kingdoms.util;

public class TextUtils {
    public static String valueWithCommas(long value) {
        char[] valueAsChars = String.valueOf(value).toCharArray();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < valueAsChars.length; i++) {
            sb.append(valueAsChars[valueAsChars.length - i - 1]);
            if ((i + 1) % 3 == 0 && i != valueAsChars.length - 1) {
                sb.append(",");
            }
        }

        sb.reverse();
        return sb.toString();
    }
}
