package de.adorsys.smartanalytics.matcher;

import de.adorsys.smartanalytics.api.Rule;
import de.adorsys.smartanalytics.api.WrappedBooking;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;

@Slf4j
public class SimilarityMatcher implements BookingMatcher {

    private static final JaroWinklerDistance JARO_WINKLER = new JaroWinklerDistance();
    public static final double MIN_DISTANCE = 0.75d;

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
                    Double result = JARO_WINKLER.apply(rule.getExpression(), wrappedBooking.getReferenceName().toLowerCase());
                    if (result >= MIN_DISTANCE) {
                        log.debug("similarity expression {} compared with {} resulted in score {}", rule.getExpression(), wrappedBooking.getReferenceName().toLowerCase(), result);
                    }
                    return  result >= MIN_DISTANCE;
                }
                return false;
            case PURPOSE:
                Double result = JARO_WINKLER.apply(rule.getExpression(), normalize(wrappedBooking.getPurpose()));
                if (result >= MIN_DISTANCE) {
                    log.debug("similarity expression {} compared with {} resulted in score {}", rule.getExpression(), normalize(wrappedBooking.getPurpose()), result);
                }
                return  result >= MIN_DISTANCE;
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
