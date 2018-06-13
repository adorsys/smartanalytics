package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class StatementTest {

    @Test
    public void isTrue_should_return_true_for_same_value() {
        Statement statement = new Statement(null, createContext("RFN", "=", "TESTVALUE"));
        Booking input = new Booking();
        input.setReferenceName("TESTVALUE");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void isTrue_should_use_uppercase_for_comparison() {
        Statement statement = new Statement(null, createContext("RFN", "=", "ABCDE12345AEOEUE SS"));
        Booking input = new Booking();
        input.setReferenceName("abcde12345äöü ß");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void isTrue_should_remove_whitespaces_at_start_and_end() {
        Statement statement = new Statement(null, createContext("RFN", "=", "TESTVALUE"));
        Booking input = new Booking();
        input.setReferenceName("     TESTVALUE     ");

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isTrue();
    }

    @Test
    public void isTrue_should_return_false_for_null_value() {
        Statement statement = new Statement(null, createContext("RFN", "=", "DUMMY"));
        Booking input = new Booking();

        boolean result = statement.match(new WrappedBooking(input));

        assertThat(result).isFalse();
    }

    @Test
    public void toString_should_return_the_readable_string() {
        Statement statement = new Statement(null, createContext("RFN", "=", "DUMMY"));

        String result = statement.toString();

        assertThat(result).isEqualTo("RFN EQUALS 'DUMMY'");
    }

    private ExpressionParser.StatementContext createContext(String variable, String compare, String value) {
        // attribute
        ExpressionParser.AttributeContext variableCtx = Mockito.mock(ExpressionParser.AttributeContext.class);
        when(variableCtx.getText()).thenReturn(variable);
        // compare
        ExpressionParser.ComparatorContext compareCtx = Mockito.mock(ExpressionParser.ComparatorContext.class);
        when(compareCtx.getText()).thenReturn(compare);
        // value
        ExpressionParser.ValueContext valueCtx = Mockito.mock(ExpressionParser.ValueContext.class);
        when(valueCtx.getText()).thenReturn(value);
        // ... and the statement
        ExpressionParser.StatementContext statementCtx = Mockito.mock(ExpressionParser.StatementContext.class);
        when(statementCtx.attribute()).thenReturn(variableCtx);
        when(statementCtx.comparator()).thenReturn(compareCtx);
        when(statementCtx.value()).thenReturn(valueCtx);
        return statementCtx;
    }

}
