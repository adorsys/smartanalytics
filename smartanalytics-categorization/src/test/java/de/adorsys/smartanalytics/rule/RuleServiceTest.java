package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.Booking;
import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import de.adorsys.smartanalytics.matcher.ExpressionMatcher;
import de.adorsys.smartanalytics.modifier.RulesModifier;
import de.adorsys.smartanalytics.rule.syntax.ExpressionLexer;
import de.adorsys.smartanalytics.rule.syntax.ExpressionParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class RuleServiceTest {


    @Test
    public void test() {
        RulesModifier modifier = new RulesModifier(Arrays.asList(
                createExpressionMatcher(createRule("RFN LIKE '1%1%MAIL%'", "Testreceiver", "Testgroup", true))
        ));
        Booking booking = new Booking();
        booking.setPurpose("Sepa-basislastschrift Eref+ 2622256 Mref+ Okf100000032243 Cred+ De37zzz00000000661 Svwz+ 45846/Fad 45846-2 Abfall 2017 Am Wasen 8/37,05/15.11.17");
        booking.setReferenceName("1u1 MAILUMEDIA GMBH-GMX");

        WrappedBooking wrappedBooking = new WrappedBooking(booking);
        modifier.useRulesOn(wrappedBooking);

        assertThat(wrappedBooking).extracting("otherAccount").containsSequence("Testreceiver");
        assertThat(wrappedBooking).extracting("mainCategory").containsSequence("Testgroup");
    }

    @Test
    public void extendBooking() {
        RulesModifier modifier = new RulesModifier(Arrays.asList(
                createExpressionMatcher(createRule("GID LIKE '%T3STREC31V3R%'", "Testreceiver", "Testgroup", true))
        ));
        Booking booking = new Booking();
        booking.setCreditorId("CrypticT3STREC31V3RId");

        WrappedBooking wrappedBooking = new WrappedBooking(booking);
        modifier.useRulesOn(wrappedBooking);

        assertThat(wrappedBooking).extracting("otherAccount").containsSequence("Testreceiver");
        assertThat(wrappedBooking).extracting("mainCategory").containsSequence("Testgroup");
    }

    @Test
    public void overwriteGroup() {
        RulesModifier modifier = new RulesModifier(Arrays.asList(
                createExpressionMatcher(createRule("GID LIKE '%T3STREC31V3R%'", "Testreceiver", "Testgroup", false)),
                createExpressionMatcher(createRule("VWZ LIKE '%TESTGROUP%'", "Testreceiver", "OverwritenGroup", true))
        ));
        Booking booking = new Booking();
        booking.setCreditorId("CrypticT3STREC31V3RId");
        booking.setPurpose("BOOKING FOR TESTGROUP");

        WrappedBooking wrappedBooking = new WrappedBooking(booking);
        modifier.useRulesOn(wrappedBooking);

        assertThat(wrappedBooking).extracting("otherAccount").containsSequence("Testreceiver");
        assertThat(wrappedBooking).extracting("mainCategory").containsSequence("OverwritenGroup");
    }

    @Test
    public void stopAtCancel() {
        RulesModifier modifier = new RulesModifier(Arrays.asList(
                createExpressionMatcher(createRule("GID LIKE '%T3STREC31V3R%'", "Testreceiver", "Testgroup", true)),
                createExpressionMatcher(createRule("VWZ LIKE '%TESTGROUP%'", "Testreceiver", "OverwritenGroup", true))
        ));
        Booking booking = new Booking();
        booking.setCreditorId("CrypticT3STREC31V3RId");
        booking.setPurpose("BOOKING FOR TESTGROUP");

        WrappedBooking wrappedBooking = new WrappedBooking(booking);
        modifier.useRulesOn(wrappedBooking);

        assertThat(wrappedBooking).extracting("otherAccount").containsSequence("Testreceiver");
        assertThat(wrappedBooking).extracting("mainCategory").containsSequence("Testgroup");
    }

    private ExpressionMatcher createExpressionMatcher(Rule rule) {
        try {
            CodePointCharStream codePointCharStream = CharStreams.fromReader(new StringReader(rule.getExpression()));
            ExpressionLexer lexer = new ExpressionLexer(codePointCharStream);
            ExpressionParser parser = new ExpressionParser(new CommonTokenStream(lexer));
            return new ExpressionMatcher(parser.expression(), rule);
        } catch (IOException e) {
            throw new IllegalArgumentException("Wrong test rule expression", e);
        }
    }

    private Rule createRule(final String expression, final String receiver, final String mainCategory, final boolean isFinal) {
        Rule rule = new Rule();
        rule.setExpression(expression);
        rule.setMainCategory(mainCategory);
        rule.setReceiver(receiver);
        rule.setStop(isFinal);
        rule.setStop(isFinal);

        return rule;
    }
}
