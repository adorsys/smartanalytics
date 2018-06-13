package de.adorsys.smartanalytics.group;

import de.adorsys.smartanalytics.api.WrappedBooking;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VariableGroupBuilderTest {

    @Test
    public void categoryShouldBeCreated_should_return_false_when_no_matcher_is_provided() {
        CustomGroupBuilder sut = new CustomGroupBuilder("testname", null);

        boolean result = sut.groupShouldBeCreated(new WrappedBooking());

        assertThat(result).isFalse();
    }

    @Test
    public void categoryShouldBeCreated_should_return_false_when_called_with_null() {
        CustomGroupBuilder sut = new CustomGroupBuilder("testname", null);

        boolean result = sut.groupShouldBeCreated(null);

        assertThat(result).isFalse();
    }
}
