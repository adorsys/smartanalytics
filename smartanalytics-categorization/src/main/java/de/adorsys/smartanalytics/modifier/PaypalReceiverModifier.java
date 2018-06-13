package de.adorsys.smartanalytics.modifier;

import de.adorsys.smartanalytics.api.WrappedBooking;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This modifier sets the real receiver for paypal bookings
 */
public class PaypalReceiverModifier implements Modifier {

    private static final Pattern PAYPAL_PURPOSE_PATTERN = Pattern.compile(".*?(PP).*(PP\\s?.\\s?)(.*)(,\\s?Ihr).*",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void modify(WrappedBooking booking) {
        if (StringUtils.startsWithIgnoreCase(booking.getReferenceName(), "PayPal")
                && booking.getPurpose() != null) {
            Matcher matcher = PAYPAL_PURPOSE_PATTERN.matcher(booking.getPurpose());
            if (matcher.matches() && matcher.groupCount() == 4) {
                booking.setOtherAccount("PayPal Zahlung an " + matcher.group(3));
            }
        }
    }

}
