package de.adorsys.smartanalytics.matcher;

import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;

public class SimilarityMatcher implements BookingMatcher {

    private static final JaroWinklerDistance JARO_WINKLER = new JaroWinklerDistance();

    private Rule rule;

    public SimilarityMatcher(Rule definition) {
        this.rule = definition;
    }

    @Override
    public boolean match(WrappedBooking wrappedBooking) {
        switch (rule.getSimilarityMatchType()) {
            case IBAN:
                return StringUtils.equalsIgnoreCase(rule.getExpression(), wrappedBooking.getBankConnection());
            case REFERENCE_NAME:
                if (wrappedBooking.getReferenceName() != null) {
                    return JARO_WINKLER.apply(rule.getExpression(), wrappedBooking.getReferenceName().toLowerCase()) >= 0.75d;
                }
                return false;
            case PURPOSE:
                return JARO_WINKLER.apply(rule.getExpression(), normalize(wrappedBooking.getPurpose())) >= 0.75d;
        }
        throw new IllegalArgumentException("missing match type");
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

    private static String normalize(String s) {
        if (s == null) {
            return null;
        }
        return s.toLowerCase()
                .replace("ü", "ue")
                .replace("ö", "oe")
                .replace("ä", "ae")
                .replace("ß", "ss")
                .replaceAll("[^a-z ]+", " ");
    }

}
