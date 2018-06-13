package de.adorsys.smartanalytics.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts the creditor Id from a string.
 */
public final class CreditorIdExtractor {

    private static Pattern creditorIdRegex = Pattern.compile("([deDE]{2}[0-9]{2,2}[A-Za-z0-9]{3,3}[0]{1}[0-9]{10})", Pattern.CASE_INSENSITIVE);

    public static String extractCreditorId(String input) {
        if (input == null) {
            return null;
        }
        Matcher matcher = creditorIdRegex.matcher(input);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    /**
     * assert if the given string is a valid creditor id.
     *
     * @param input the string to be asserted
     * @return true is the string is a valid creditor id otherwise false
     */
    public static boolean isValidCreditorId(String input) {
        if (input == null) {
            return false;
        }

        return extractCreditorId(input) != null;
    }
}
