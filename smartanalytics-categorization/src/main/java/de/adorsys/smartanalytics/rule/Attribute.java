package de.adorsys.smartanalytics.rule;

import de.adorsys.smartanalytics.api.WrappedBooking;
import org.apache.commons.lang3.StringUtils;

/**
 * Attributes of the rule syntax.
 */
@SuppressWarnings("unused")
enum Attribute {

    AMOUNT {
        @Override
        public String value(WrappedBooking input) {
            return String.valueOf(input.getAmount());
        }
    },
    RFN {
        @Override
        public String value(WrappedBooking input) {
            return Attribute.normalize(input.getReferenceName());
        }
    },
    GID {
        @Override
        public String value(WrappedBooking input) {
            return Attribute.normalize(input.getCreditorId());
        }
    },
    VWZ {
        @Override
        public String value(WrappedBooking input) {
            return Attribute.normalize(input.getPurpose());
        }
    },
    IBAN {
        @Override
        public String value(WrappedBooking input) {
            return Attribute.normalize(input.getIban());
        }
    },
    KTO {
        @Override
        public String value(WrappedBooking input) {
            return input.getAccountNumber();
        }
    },
    BLZ {
        @Override
        public String value(WrappedBooking input) {
            return input.getBankCode();
        }
    },
    HKAT {
        @Override
        public String value(WrappedBooking input) {
            return Attribute.normalize(input.getMainCategory());
        }
    },
    UKAT {
        @Override
        public String value(WrappedBooking input) {
            return Attribute.normalize(input.getSubCategory());
        }
    },
    SPEZ {
        @Override
        public String value(WrappedBooking input) {
            return Attribute.normalize(input.getSpecification());
        }
    };

    public abstract String value(WrappedBooking input);

    private static String normalize(String s) {
        String output = StringUtils.trimToEmpty(s).toUpperCase();
        output = StringUtils.replace(output, "Ä", "AE");
        output = StringUtils.replace(output, "Ö", "OE");
        output = StringUtils.replace(output, "Ü", "UE");
        output = StringUtils.replace(output, "ß", "SS");
        return output;
    }
}
