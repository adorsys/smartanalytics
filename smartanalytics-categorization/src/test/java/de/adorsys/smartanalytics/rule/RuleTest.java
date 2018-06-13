package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.matcher.ExpressionMatcher;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class RuleTest {

    @Test
    public void match_with_a_statement_should_return_true_when_the_statement_assert_to_true() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithStatementContext("RFN", "=", "TESTVALUE"), new Rule());
        Booking input = new Booking();
        input.setReferenceName("testvalue");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void match_with_a_statement_should_return_false_when_the_statement_assert_to_false() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithStatementContext("RFN", "=", "TESTVALUE"), new Rule());
        Booking input = new Booking();
        input.setReferenceName("some other value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void match_with_a_rule_should_return_true_when_the_rule_assert_to_true() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE")), new Rule());
        Booking input = new Booking();
        input.setReferenceName("testvalue");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void match_with_a_rule_should_return_false_when_the_rule_assert_to_false() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE")), new Rule());
        Booking input = new Booking();
        input.setReferenceName("some other value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void match_with_two_connected_rules_should_return_false_when_the_rules_assert_to_false() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "AND", createWithStatementContext("VWZ", "=", "TESTVALUE2")), new Rule());
        Booking input = new Booking();
        input.setReferenceName("some other value");
        input.setPurpose("yet another value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void match_with_two_connected_rules_should_return_true_when_the_rules_assert_to_true() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "AND", createWithStatementContext("VWZ", "=", "TESTVALUE2")), new Rule());
        Booking input = new Booking();
        input.setReferenceName("testvalue");
        input.setPurpose("testValue2");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void match_with_a_negative_rule_should_return_false_when_the_rule_assert_to_true() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext("NOT", createWithStatementContext("RFN", "=", "TESTVALUE")), new Rule());
        Booking input = new Booking();
        input.setReferenceName("testvalue");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void match_with_a_negative_rule_should_return_true_when_the_rule_assert_to_false() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext("NOT", createWithStatementContext("RFN", "=", "TESTVALUE")), new Rule());
        Booking input = new Booking();
        input.setReferenceName("some other value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void toString_with_a_statement_should_return_the_statement() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithStatementContext("RFN", "=", "TESTVALUE"), new Rule());

        String result = statement.toString();

        assertThat(result).isEqualTo("RFN EQUALS 'TESTVALUE'");
    }

    @Test
    public void toString_with_a_rule_should_return_the_statement() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext(createWithStatementContext("RFN", "LIKE", "%TEST%")), new Rule());

        String result = statement.toString();

        assertThat(result).isEqualTo("RFN LIKE '%TEST%'");
    }

    @Test
    public void toString_with_two_connected_rules_should_return_the_statement() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "AND", createWithStatementContext("VWZ", "=", "TESTVALUE2")), new Rule());

        String result = statement.toString();

        assertThat(result).isEqualTo("(RFN EQUALS 'TESTVALUE') AND (VWZ EQUALS 'TESTVALUE2')");
    }

    @Test
    public void toString_with_a_negative_rule_should_return_the_statement() {
        ExpressionMatcher statement = new ExpressionMatcher(createWithRuleContext("NOT", createWithStatementContext("RFN", "=", "TESTVALUE")), new Rule());

        String result = statement.toString();

        assertThat(result).isEqualTo("NOT (RFN EQUALS 'TESTVALUE')");
    }

    private ExpressionParser.ExpressionContext createWithRuleContext(ExpressionParser.ExpressionContext ruleContext) {
        // and the rule
        ExpressionParser.ExpressionContext ruleCtx = Mockito.mock(ExpressionParser.ExpressionContext.class);
        List<ExpressionParser.ExpressionContext> rules = new ArrayList<>();
        rules.add(ruleContext);
        when(ruleCtx.expression()).thenReturn(rules);
        when(ruleCtx.expression(0)).thenReturn(ruleContext);
        return ruleCtx;
    }

    private ExpressionParser.ExpressionContext createWithRuleContext(String negative, ExpressionParser.ExpressionContext ruleContext) {
        // negative
        ParseTree negativeContext = Mockito.mock(ParseTree.class);
        when(negativeContext.getText()).thenReturn(negative);
        // and the rule
        ExpressionParser.ExpressionContext ruleCtx = Mockito.mock(ExpressionParser.ExpressionContext.class);
        List<ExpressionParser.ExpressionContext> rules = new ArrayList<>();
        rules.add(ruleContext);
        when(ruleCtx.expression()).thenReturn(rules);
        when(ruleCtx.expression(0)).thenReturn(ruleContext);
        when(ruleCtx.getChild(0)).thenReturn(negativeContext);
        return ruleCtx;
    }

    private ExpressionParser.ExpressionContext createWithRuleContext(ExpressionParser.ExpressionContext leftContext, String operator, ExpressionParser.ExpressionContext rightContext) {
        // operator
        ExpressionParser.OperatorContext operatorCtx = Mockito.mock(ExpressionParser.OperatorContext.class);
        when(operatorCtx.getText()).thenReturn(operator);

        // and the rule
        ExpressionParser.ExpressionContext ruleCtx = Mockito.mock(ExpressionParser.ExpressionContext.class);
        List<ExpressionParser.ExpressionContext> rules = new ArrayList<>();
        rules.add(leftContext);
        rules.add(rightContext);
        when(ruleCtx.expression()).thenReturn(rules);
        when(ruleCtx.expression(0)).thenReturn(leftContext);
        when(ruleCtx.expression(1)).thenReturn(rightContext);
        when(ruleCtx.operator()).thenReturn(operatorCtx);
        return ruleCtx;
    }

    private ExpressionParser.ExpressionContext createWithStatementContext(String variable, String compare, String value) {
        // attribute
        ExpressionParser.AttributeContext variableCtx = Mockito.mock(ExpressionParser.AttributeContext.class);
        when(variableCtx.getText()).thenReturn(variable);
        // compare
        ExpressionParser.ComparatorContext compareCtx = Mockito.mock(ExpressionParser.ComparatorContext.class);
        when(compareCtx.getText()).thenReturn(compare);
        // value
        ExpressionParser.ValueContext valueCtx = Mockito.mock(ExpressionParser.ValueContext.class);
        when(valueCtx.getText()).thenReturn(value);
        // statement
        ExpressionParser.StatementContext statementCtx = Mockito.mock(ExpressionParser.StatementContext.class);
        when(statementCtx.attribute()).thenReturn(variableCtx);
        when(statementCtx.comparator()).thenReturn(compareCtx);
        when(statementCtx.value()).thenReturn(valueCtx);
        // and the rule
        ExpressionParser.ExpressionContext ruleCtx = Mockito.mock(ExpressionParser.ExpressionContext.class);
        when(ruleCtx.statement()).thenReturn(statementCtx);
        return ruleCtx;
    }

}
