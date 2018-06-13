package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.matcher.ExpressionMatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Operator gemäß Grammatik "Category.g4"
 */
enum Operator {

    AND("AND") {
        @Override
        public boolean link(WrappedBooking input, ExpressionMatcher left, ExpressionMatcher right) {
            return left.match(input) && right.match(input);
        }
    },
    OR("OR") {
        @Override
        public boolean link(WrappedBooking input, ExpressionMatcher left, ExpressionMatcher right) {
            return left.match(input) || right.match(input);
        }
    };

    private static final Map<String, Operator> KEY_MAP = new HashMap<>();

    static {
        for (Operator operator : values()) {
            KEY_MAP.put(operator.getKey(), operator);
        }
    }

    private String key;

    Operator(String key) {
        this.key = key;
    }

    public static Operator fromKey(String key) {
        return KEY_MAP.get(key);
    }

    public String getKey() {
        return key;
    }

    /**
     * Liefert das Ergebnis der logischen Vernküpfung zweier boolescher Werte mit dem zugrundeliegenden Operanden.
     */
    public abstract boolean link(WrappedBooking input, ExpressionMatcher left, ExpressionMatcher right);

}
