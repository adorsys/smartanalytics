package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;
import org.apache.commons.lang3.StringUtils;

public class Statement implements Matcher {

    private static final String QUOTE_CHAR = "'";

    private Attribute attribute;
    private Compare compare;
    private String value;
    private String creditorId;

    public Statement(String creditorId, ExpressionParser.StatementContext ctx) {
        this.creditorId = creditorId;
        attribute = Attribute.valueOf(removeQuotes(ctx.attribute().getText()));
        compare = Compare.fromKey(ctx.comparator().getText());
        value = removeQuotes(ctx.value().getText()).toUpperCase();
    }

    private static String removeQuotes(String s) {
        return StringUtils.removeEnd(StringUtils.removeStart(s, QUOTE_CHAR), QUOTE_CHAR);
    }

    @Override
    public boolean match(WrappedBooking input) {
        if (creditorId != null && StringUtils.equalsIgnoreCase(creditorId, input.getCreditorId())) {
            return true;
        }
        return compare.evaluate(value, attribute.value(input));
    }

    @Override
    public String toString() {
        if (creditorId == null) {
            return String.format("%s %s '%s'", attribute, compare, value);
        } else {
            return String.format("CREDITOR-ID %s OR %s %s '%s'", creditorId, attribute, compare, value);
        }
    }
}
