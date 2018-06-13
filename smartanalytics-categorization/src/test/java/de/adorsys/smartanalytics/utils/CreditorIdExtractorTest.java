package de.adorsys.smartanalytics.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CreditorIdExtractorTest {

    @Test
    public void extractCreditorId_should_return_null_for_null_input() {

        String result = CreditorIdExtractor.extractCreditorId(null);

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_less_than_11_chars() {

        String result = CreditorIdExtractor.extractCreditorId("1234567890");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_more_than_35_chars() {

        String result = CreditorIdExtractor.extractCreditorId("123456789012345678901234567890123456");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_with_invalid_country_code() {

        String result = CreditorIdExtractor.extractCreditorId("XX12345678901234567890");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_with_nonnumeric_checksum() {

        String result = CreditorIdExtractor.extractCreditorId("DEXX345678901234567890");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_value_for_String_with_no_german_country_code() {

        String result = CreditorIdExtractor.extractCreditorId("AT12345678901234567890");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_with_german_country_code_and_less_than_18_chars() {

        String result = CreditorIdExtractor.extractCreditorId("DE123456789012345");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_with_german_country_code_and_more_than_18_chars() {

        String result = CreditorIdExtractor.extractCreditorId("DE12345678901234567");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_with_german_country_code_and_non_zero_at_position_8() {

        String result = CreditorIdExtractor.extractCreditorId("DE1234567890123456");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_null_for_String_with_german_country_code_and_non_numeric_after_position_8() {

        String result = CreditorIdExtractor.extractCreditorId("DE123450ABCDEFGHIJ");

        assertThat(result).isNull();
    }

    @Test
    public void extractCreditorId_should_return_value_for_String_with_german_country_code_and_zero_on_position_8_and_numeric_chars_after_position_8() {

        String result = CreditorIdExtractor.extractCreditorId("DE12ABC01234567890");

        assertThat(result).isEqualTo("DE12ABC01234567890");
    }

    @Test
    public void extractCreditorId_should_return_find_value_in_multiple_strings_seperated_with_whitespaces() {

        String result = CreditorIdExtractor.extractCreditorId("the value DE12ABC01234567890 is a valid creditor id");

        assertThat(result).isEqualTo("DE12ABC01234567890");
    }

    @Test
    public void extractCreditorId_should_return_find_value_in_multiple_strings_seperated_with_plus() {

        String result = CreditorIdExtractor.extractCreditorId("the+value+DE12ABC01234567890+is+a+valid+creditor+id");

        assertThat(result).isEqualTo("DE12ABC01234567890");
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_less_than_11_chars() {

        boolean result = CreditorIdExtractor.isValidCreditorId("1234567890");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_more_than_35_chars() {

        boolean result = CreditorIdExtractor.isValidCreditorId("123456789012345678901234567890123456");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_with_invalid_country_code() {

        boolean result = CreditorIdExtractor.isValidCreditorId("XX12345678901234567890");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_with_non_numeric_checksum() {

        boolean result = CreditorIdExtractor.isValidCreditorId("DEXX345678901234567890");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_true_for_String_with_no_german_country_code() {

        boolean result = CreditorIdExtractor.isValidCreditorId("AT12345678901234567890");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_with_german_country_code_and_less_than_18_chars() {

        boolean result = CreditorIdExtractor.isValidCreditorId("DE123456789012345");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_with_german_country_code_and_more_than_18_chars() {

        boolean result = CreditorIdExtractor.isValidCreditorId("DE12345678901234567");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_with_german_country_code_and_non_zero_at_position_8() {

        boolean result = CreditorIdExtractor.isValidCreditorId("DE1234567890123456");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_false_for_String_with_german_country_code_and_non_numeric_after_position_8() {

        boolean result = CreditorIdExtractor.isValidCreditorId("DE123450ABCDEFGHIJ");

        assertThat(result).isFalse();
    }

    @Test
    public void isValidCreditorId_should_return_true_for_String_with_german_country_code_and_zero_on_position_8_and_numeric_chars_after_position_8() {

        boolean result = CreditorIdExtractor.isValidCreditorId("DE12ABC01234567890");

        assertThat(result).isTrue();
    }

}
