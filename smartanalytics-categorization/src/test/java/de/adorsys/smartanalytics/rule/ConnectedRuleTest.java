package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ConnectedRuleTest {

    @Test
    public void match_with_and_operator_should_return_true_when_both_statements_assert_to_true() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "AND", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("testvalue");
        input.setPurpose("testvalue2");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void match_with_and_operator_should_return_false_when_left_statement_assert_to_false() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "AND", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("another value");
        input.setPurpose("testvalue2");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void match_with_and_operator_should_return_false_when_right_statement_assert_to_false() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "AND", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("testvalue");
        input.setPurpose("yet another value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void match_with_and_operator_should_return_false_when_both_statements_assert_to_false() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "AND", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("another value");
        input.setPurpose("yet another value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void match_with_or_operator_should_return_true_when_both_statements_assert_to_true() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "OR", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("testvalue");
        input.setPurpose("testvalue2");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void match_with_or_operator_should_return_true_when_only_the_left_statement_assert_to_false() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "OR", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("another value");
        input.setPurpose("testvalue2");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void match_with_or_operator_should_return_true_when_only_the_right_statement_assert_to_false() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "OR", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("testvalue");
        input.setPurpose("yet another value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void match_with_or_operator_should_return_false_when_both_statements_assert_to_false() {
        ConnectedRule statement = new ConnectedRule(new Rule(), createWithRuleContext(createWithStatementContext("RFN", "=", "TESTVALUE"), "OR", createWithStatementContext("VWZ", "=", "TESTVALUE2")));
        Booking input = new Booking();
        input.setReferenceName("another value");
        input.setPurpose("yet another value");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
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
