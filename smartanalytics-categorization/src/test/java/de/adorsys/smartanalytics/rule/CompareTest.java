package de.adorsys.smartanalytics.rule;

import org.junit.Assume;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CompareTest {

    @Test
    public void equals_should_return_true_for_same_value() {
        assertThat(Compare.EQUALS.evaluate("Test", "Test")).isTrue();
    }

    @Test
    public void equals_should_not_ignore_whitespaces() {
        assertThat(Compare.EQUALS.evaluate("Test", "Test ")).isFalse();
        assertThat(Compare.EQUALS.evaluate("Test", " Test")).isFalse();
    }

    @Test
    public void equals_should_be_case_sensitive() {
        assertThat(Compare.EQUALS.evaluate("Test", "tesT")).isFalse();
    }

    @Test
    public void like_should_return_true_for_same_value() {
        assertThat(Compare.LIKE.evaluate("%Test%", "Test")).isTrue();
    }

    @Test
    public void like_should_ignore_whitespaces() {
        assertThat(Compare.LIKE.evaluate("%Test%", "Test ")).isTrue();
        assertThat(Compare.LIKE.evaluate("%Test%", " Test")).isTrue();
    }

    /*
     * Test showing that our pattern matcher is case insensitive.
     */
    @Test
    public void like_is_case_insensitive() {
    	String text_lowercase = "test";
    	String text_upercase = text_lowercase.toUpperCase();
    	String pattern_lowercase = "%test%";
    	String pattern_upercase = pattern_lowercase.toUpperCase();
    	Assume.assumeTrue(Compare.LIKE.evaluate(pattern_lowercase, text_lowercase));
    	Assume.assumeTrue(Compare.LIKE.evaluate(pattern_upercase, text_upercase));
        assertThat(Compare.LIKE.evaluate(pattern_upercase, text_lowercase)).isFalse();
        assertThat(Compare.LIKE.evaluate(pattern_lowercase, text_upercase)).isFalse();
    }
    
    @Test
    public void like_should_match_substring() {
        assertThat(Compare.LIKE.evaluate("%Test", "org.junit.Test")).isTrue();
        assertThat(Compare.LIKE.evaluate("Test%", "Test.org.junit.")).isTrue();
        assertThat(Compare.LIKE.evaluate("T%E%S%T", "TEST")).isTrue();
        assertThat(Compare.LIKE.evaluate("T%E%S%T", "T_E_S_T")).isTrue();
    }

    @Test
    public void like_without_wildcards_should_not_ignore_whiteSpaces() {
        assertThat(Compare.LIKE.evaluate("Test", "Test")).isTrue();
        assertThat(Compare.LIKE.evaluate("Test", "Test ")).isFalse();
        assertThat(Compare.LIKE.evaluate("Test", " Test")).isFalse();
    }

    @Test
    public void like_should_be_case_sensitive() {
        assertThat(Compare.LIKE.evaluate("%Test%", "tesT")).isFalse();
    }

    @Test
    public void not_like_should_invert_like_statement() {
        assertThat(Compare.NOT_LIKE.evaluate("%Test%", "Test")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("%Test%", "Test ")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("%Test%", " Test")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("%Test", "org.junit.Test")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("Test%", "Test.org.junit.")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("T%E%S%T", "TEST")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("T%E%S%T", "T_E_S_T")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("Test", "Test")).isFalse();
        assertThat(Compare.NOT_LIKE.evaluate("Test", "Test ")).isTrue();
        assertThat(Compare.NOT_LIKE.evaluate("Test", " Test")).isTrue();
    }

    @Test
    public void lower_than_should_evaluate_to_true_for_correct_big_decimals() {
        assertThat(Compare.LOWER_THAN.evaluate("1.12", "2.34")).isTrue();
    }

    @Test
    public void lower_than_should_evaluate_to_false_for_incorrect_big_decimals() {
        assertThat(Compare.LOWER_THAN.evaluate("2.34", "1.12")).isFalse();
    }

    @Test
    public void lower_than_should_evaluate_to_false_for_no_big_decimals() {
        assertThat(Compare.LOWER_THAN.evaluate("no number", "1.12")).isFalse();
        assertThat(Compare.LOWER_THAN.evaluate("1.12", "no number")).isFalse();
    }

    @Test
    public void greater_than_should_evaluate_to_true_for_correct_big_decimals() {
        assertThat(Compare.GREATER_THAN.evaluate("2.34", "1.12")).isTrue();
    }

    @Test
    public void greater_than_should_evaluate_to_false_for_incorrect_big_decimals() {
        assertThat(Compare.GREATER_THAN.evaluate("1.12", "2.34")).isFalse();
    }

    @Test
    public void greater_than_should_evaluate_to_false_for_no_big_decimals() {
        assertThat(Compare.GREATER_THAN.evaluate("no number", "1.12")).isFalse();
        assertThat(Compare.GREATER_THAN.evaluate("1.12", "no number")).isFalse();
    }

    @Test
    public void lower_than_or_equal_to_should_evaluate_to_true_for_correct_big_decimals() {
        assertThat(Compare.LOWER_THAN_OR_EQUAL_TO.evaluate("1.12", "2.34")).isTrue();
        assertThat(Compare.LOWER_THAN_OR_EQUAL_TO.evaluate("45.67", "45.67")).isTrue();
    }

    @Test
    public void lower_than_or_equal_to_should_evaluate_to_false_for_incorrect_big_decimals() {
        assertThat(Compare.LOWER_THAN_OR_EQUAL_TO.evaluate("2.34", "1.12")).isFalse();
    }

    @Test
    public void lower_than_or_equal_to_should_evaluate_to_false_for_no_big_decimals() {
        assertThat(Compare.LOWER_THAN_OR_EQUAL_TO.evaluate("no number", "1.12")).isFalse();
        assertThat(Compare.LOWER_THAN_OR_EQUAL_TO.evaluate("1.12", "no number")).isFalse();
    }

    @Test
    public void greater_than_or_equal_to_should_evaluate_to_true_for_correct_big_decimals() {
        assertThat(Compare.GREATER_THAN_OR_EQUAL_TO.evaluate("2.34", "1.12")).isTrue();
        assertThat(Compare.GREATER_THAN_OR_EQUAL_TO.evaluate("45.67", "45.67")).isTrue();
    }

    @Test
    public void greater_than_or_equal_to_should_evaluate_to_false_for_incorrect_big_decimals() {
        assertThat(Compare.GREATER_THAN_OR_EQUAL_TO.evaluate("1.12", "2.34")).isFalse();
    }

    @Test
    public void greater_than_or_equal_to_should_evaluate_to_false_for_no_big_decimals() {
        assertThat(Compare.GREATER_THAN_OR_EQUAL_TO.evaluate("no number", "1.12")).isFalse();
        assertThat(Compare.GREATER_THAN_OR_EQUAL_TO.evaluate("1.12", "no number")).isFalse();
    }

}
