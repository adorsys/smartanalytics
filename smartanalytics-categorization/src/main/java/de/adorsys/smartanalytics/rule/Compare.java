package de.adorsys.smartanalytics.rule;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

enum Compare {

    EQUALS("=") {
        @Override
        public boolean evaluate(String left, String right) {
            return StringUtils.equals(left, right);
        }
    },
    LIKE("LIKE") {
        @Override
        public boolean evaluate(String pattern, String wert) {
            return pattern(pattern).matcher(wert).matches();
        }
    },
    NOT_LIKE("NOT LIKE") {
        @Override
        public boolean evaluate(String pattern, String wert) {
            return !pattern(pattern).matcher(wert).matches();
        }
    },
    LOWER_THAN("<") {
        @Override
        public boolean evaluate(String left, String right) {
            try {
                return new BigDecimal(right).compareTo(new BigDecimal(left)) < 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    },
    GREATER_THAN(">") {
        @Override
        public boolean evaluate(String left, String right) {
            try {
                return new BigDecimal(right).compareTo(new BigDecimal(left)) > 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    },
    LOWER_THAN_OR_EQUAL_TO("<=") {
        @Override
        public boolean evaluate(String left, String right) {
            try {
                return new BigDecimal(right).compareTo(new BigDecimal(left)) <= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    },
    GREATER_THAN_OR_EQUAL_TO(">=") {
        @Override
        public boolean evaluate(String left, String right) {
            try {
                return new BigDecimal(right).compareTo(new BigDecimal(left)) >= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    };

    private static final Map<String, Compare> KEY_MAP = new HashMap<>();

    static {
        for (Compare compare : values()) {
            KEY_MAP.put(compare.getKey(), compare);
        }
    }

    private String key;

    Compare(String key) {
        this.key = key;
    }

    public static Compare fromKey(String key) {
        return KEY_MAP.get(key);
    }

    private static Pattern pattern(String pattern) {
        String regex = "^" + StringUtils.replace(pattern, "%", "(.*)") + "$";
        return Pattern.compile(regex);
    }

    public String getKey() {
        return key;
    }

    /**
     * Liefert das Ergebnis der Auswertung des zugrunde liegenden Comparators (Match oder Vergleich).
     *
     * @param pattern Pattern (ggf. Konstante)
     * @param wert    Wert für Vergleich oder Match
     */
    public abstract boolean evaluate(String pattern, String wert);

}
