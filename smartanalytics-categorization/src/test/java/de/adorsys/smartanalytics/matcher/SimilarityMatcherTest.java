package de.adorsys.smartanalytics.matcher;

import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Assert;
import org.junit.Test;

public class SimilarityMatcherTest {

    WrappedBooking wrappedBooking = new WrappedBooking() {

        @Override
        public String getBankConnection() {
            return "Alexander Geist";
        }

        @Override
        public String getReferenceName() {
            return "Alexander Geist";
        }

        @Override
        public String getPurpose() {
            return "Alexander Geist";
        }
    };

    @Test
    public void test() {
        Rule rule = new de.adorsys.smartanalytics.api.Rule();
        rule.setExpression("Alexander Geist");
        rule.setSimilarityMatchType(Rule.SIMILARITY_MATCH_TYPE.REFERENCE_NAME);

        SimilarityMatcher similarityMatcher = new SimilarityMatcher(rule);
        Assert.assertTrue(similarityMatcher.match(wrappedBooking));

        rule.setSimilarityMatchType(Rule.SIMILARITY_MATCH_TYPE.PURPOSE);
        similarityMatcher = new SimilarityMatcher(rule);
        Assert.assertTrue(similarityMatcher.match(wrappedBooking));

    }
}
