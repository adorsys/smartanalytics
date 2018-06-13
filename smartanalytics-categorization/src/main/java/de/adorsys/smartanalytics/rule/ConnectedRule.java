package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.matcher.ExpressionMatcher;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;
import org.apache.commons.lang3.StringUtils;

/**
 * Repräsentation einer Rule in der Ausprägung "regel operator regel".
 */
public class ConnectedRule implements Matcher {

    private String creditorId;
    private ExpressionMatcher left;
    private Operator operator;
    private ExpressionMatcher right;

    public ConnectedRule(Rule ruleDefinition, ExpressionParser.ExpressionContext ctx) {
        this.creditorId = ruleDefinition.getCreditorId();
        left = new ExpressionMatcher(ctx.expression(0), ruleDefinition);
        operator = Operator.fromKey(ctx.operator().getText());
        right = new ExpressionMatcher(ctx.expression(1), ruleDefinition);
    }

    @Override
    public boolean match(WrappedBooking input) {
        if (creditorId != null && StringUtils.equalsIgnoreCase(creditorId, input.getCreditorId())) {
            return true;
        }
        return operator.link(input, left, right);
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	if(this.creditorId!=null)
    		sb = sb.append("CREDITOR-ID: ").append(this.creditorId);
        return sb.append("(")
                .append(left)
                .append(") ")
                .append(operator)
                .append(" (")
                .append(right)
                .append(")")
                .toString();
    }
}
