package de.adorsys.smartanalytics.matcher;

import de.adorsys.smartanalytics.api.Matcher;
import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.rule.ConnectedRule;
import de.adorsys.smartanalytics.rule.Statement;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;

public class ExpressionMatcher implements BookingMatcher {

    private Rule rule;
    private boolean isNegative;
    private Matcher matcher;

    public ExpressionMatcher(ExpressionParser.ExpressionContext ctx, Rule definition) {
        this.rule = definition;

        if (ctx == null) {
            matcher = new CreditorIdMatcher(definition.getCreditorId());
        } else {
            if (ctx.expression().size() == 1) {
                isNegative = isChildNegative(ctx);
                matcher = new ExpressionMatcher(ctx.expression(0), definition);
            } else if (ctx.expression().size() == 2 && ctx.operator() != null) {
                matcher = new ConnectedRule(definition, ctx);
            } else if (ctx.statement() != null) {
                matcher = new Statement(definition.getCreditorId(), ctx.statement());
            }
        }

        if (matcher == null) {
            throw new IllegalArgumentException("Invalid matcher expression: " + definition.getExpression());
        }
    }

    @Override
    public boolean match(WrappedBooking input) {
        return isNegative ^ matcher.match(input);
    }

    @Override
    public String getId() {
        return rule.getRuleId();
    }

    @Override
    public void extend(WrappedBooking wrappedBooking) {
        wrappedBooking.applyRule(rule);
    }

    @Override
    public boolean isFinal() {
        return rule.isStop();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isNegative) {
            sb.append("NOT (");
        }
        sb.append(matcher.toString());
        if (isNegative) {
            sb.append(")");
        }
        return sb.toString();
    }

    /**
     * Prüft, ob dem im Knoten enthaltenen Ausdruck ein 'NOT' zur Negation vorangestellt ist.
     */
    private boolean isChildNegative(ExpressionParser.ExpressionContext ctx) {
        return (ctx.getChild(0) != null && "NOT".equals(ctx.getChild(0).getText()));
    }

}
