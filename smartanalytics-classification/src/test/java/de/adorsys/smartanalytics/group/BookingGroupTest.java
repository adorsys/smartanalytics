package de.adorsys.smartanalytics.group;

public class BookingGroupTest {

//    private LocalDate today = null;
//
//    @Before
//    public void setup() {
//        today = createDate("01/01/16");
//    }
//
//    @Test
//    public void should_has_null_cycle_without_bookings() {
//        BookingGroup bookingGroup = new DummyBookingGroup("test");
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isNull();
//    }
//
//    @Test
//    public void should_has_yearly_cycle_with_one_booking() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isEqualTo(YEARLY);
//    }
//
//    /**
//     * notice: at least 6 bookings are necessary for weekly difference
//     * 'MIND_ANZAHL_UMSAETZE_FUER_WOECHENTLICHES_INTERVALL=6' in CycleCalculator */
//    @Test
//    public void should_has_weekly_cycle_with_two_booking_with_a_weekly_difference() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("01/07/16"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("01/14/16"));
//        booking3.setAmount(new BigDecimal(300));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        Booking booking4 = new Booking();
//        booking4.setExecutionDate(createDate("01/21/16"));
//        booking4.setAmount(new BigDecimal(400));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//
//        Booking booking5 = new Booking();
//        booking5.setExecutionDate(createDate("01/28/16"));
//        booking5.setAmount(new BigDecimal(500));
//        bookingGroup.addBooking(new WrappedBooking(booking5));
//
//        Booking booking6 = new Booking();
//        booking6.setExecutionDate(createDate("02/05/16"));
//        booking6.setAmount(new BigDecimal(600));
//        bookingGroup.addBooking(new WrappedBooking(booking6));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isEqualTo(Cycle.WEEKLY);
//    }
//
//    @Test
//    public void should_has_monthly_cycle_with_two_booking_with_a_monthly_difference() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("02/01/16"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isEqualTo(Cycle.MONTHLY);
//    }
//
//    @Test
//    public void should_has_quarterly_cycle_with_two_booking_with_a_quarterly_difference() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//        booking1.setAmount(new BigDecimal(100));
//
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("04/01/16"));
//        booking2.setAmount(new BigDecimal(200));
//
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isEqualTo(Cycle.QUARTERLY);
//    }
//
//    @Test
//    public void should_has_halfly_cycle_with_two_booking_with_a_halfly_difference() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("07/01/16"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isEqualTo(Cycle.HALF_YEARLY);
//    }
//
//    @Test
//    public void should_has_yearly_cycle_with_two_booking_with_a_yearly_difference() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/01/15"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("01/01/16"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isEqualTo(YEARLY);
//    }
//
//    @Test
//    public void should_consider_bookings_on_the_same_date_as_one_booking() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("01/01/16"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("02/01/16"));
//        booking3.setAmount(new BigDecimal(300));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        Booking booking4 = new Booking();
//        booking4.setExecutionDate(createDate("03/01/16"));
//        booking4.setAmount(new BigDecimal(400));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isEqualTo(Cycle.MONTHLY);
//    }
//
//    @Test
//    public void should_be_ineffective_without_bookings() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isFalse();
//    }
//
//    @Test
//    public void should_be_ineffective_with_one_booking() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/31/13"));
//
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isFalse();
//    }
//
//    @Test
//    public void should_be_monthly_effective_when_last_booking_is_in_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("10/23/15"));
//        booking1.setAmount(new BigDecimal(100));
//
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("11/23/15"));
//        booking2.setAmount(new BigDecimal(200));
//
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isTrue();
//    }
//
//    @Test
//    public void should_be_monthly_effective_when_last_booking_is_out_of_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("11/22/15"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("11/22/15"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isFalse();
//    }
//
//    @Test
//    public void should_be_quarterly_effective_when_last_booking_is_in_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("06/14/15"));
//        booking1.setAmount(new BigDecimal(100));
//
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("09/14/15"));
//        booking2.setAmount(new BigDecimal(200));
//
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isTrue();
//    }
//
//    @Test
//    public void should_be_quarterly_effective_when_last_booking_is_out_of_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("06/13/15"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("09/13/15"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isFalse();
//    }
//
//    @Test
//    public void should_be_halfly_effective_when_last_booking_is_in_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/16/14"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("06/16/15"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isTrue();
//    }
//
//    @Test
//    public void should_be_halfly_effective_when_last_booking_is_out_of_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/15/14"));
//        booking1.setAmount(new BigDecimal(100));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("06/15/15"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isFalse();
//    }
//
//    @Test
//    public void should_be_yearly_effective_when_last_booking_is_in_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/18/13"));
//        booking1.setAmount(new BigDecimal(100));
//
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/18/14"));
//        booking2.setAmount(new BigDecimal(200));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isTrue();
//    }
//
//    @Test
//    public void should_be_yearly_effective_when_last_booking_is_out_of_range() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/17/13"));
//        booking1.setAmount(new BigDecimal(100));
//
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/17/14"));
//        booking2.setAmount(new BigDecimal(200));
//
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//
//        assertThat(bookingGroup.isEffective(today)).as("categorizer.isEffective").isFalse();
//    }
//
//    /*@Test
//    public void getLastAmount_should_should_ignore_given_date_for_non_variable() throws ParseException {
//        LocalDate today = createDate("12/17/14");
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setExecutionDate(createDate("12/17/12"));
//        booking4.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(booking4);
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/17/13"));
//        booking1.setAmount(new BigDecimal("12.34"));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/17/13"));
//        booking2.setAmount(new BigDecimal("41.98"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("12/17/14"));
//        booking3.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        assertThat(bookingGroup.getAmount()).as("amount of categorizer").isEqualTo(new BigDecimal("54.32"));
//    }*/
//
//    /*@Test
//    public void getLastAmount_should_interpret_bookings_on_same_day_as_one() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setExecutionDate(createDate("12/17/12"));
//        booking4.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(booking4);
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/17/13"));
//        booking1.setAmount(new BigDecimal("12.34"));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/17/13"));
//        booking2.setAmount(new BigDecimal("41.98"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("12/17/14"));
//        booking3.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//
//        assertThat(bookingGroup.getAmount()).as("amount of categorizer").isEqualTo(new BigDecimal("54.32"));
//    }*/
//
//    @Test
//    public void getLastAmount_should_return_same_amount_of_last_three_bookings() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setExecutionDate(createDate("12/17/12"));
//        booking4.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/17/13"));
//        booking1.setAmount(new BigDecimal("12.34"));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/17/13"));
//        booking2.setAmount(new BigDecimal("41.98"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("12/17/14"));
//        booking3.setAmount(new BigDecimal("12.34"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        assertThat(bookingGroup.getAmount()).as("amount of categorizer").isEqualTo(new BigDecimal("12.34"));
//    }
//
//    @Test
//    public void getLastAmount_should_return_null_cycle_when_bookingGroup_is_variable() throws ParseException {
//        LocalDate today = createDate("01/17/14");
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        bookingGroup.setVariable(true);
//        Booking booking4 = new Booking();
//        booking4.setExecutionDate(createDate("01/01/14"));
//        booking4.setAmount(new BigDecimal("100.00"));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("01/07/14"));
//        booking1.setAmount(new BigDecimal("200.00"));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("01/10/14"));
//        booking2.setAmount(new BigDecimal("50.00"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("01/17/14"));
//        booking3.setAmount(new BigDecimal("150.00"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        assertThat(bookingGroup.getCycle()).as("cycle of categorizer").isNull();
//    }
//
//    @Test
//    public void should_has_amount_of_the_last_booking_when_both_last_are_same_amount() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/17/12"));
//        booking1.setAmount(new BigDecimal("123.45"));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/17/13"));
//        booking2.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("12/17/14"));
//        booking3.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        assertThat(bookingGroup.getAmount()).as("amount of categorizer").isEqualTo(new BigDecimal("54.32"));
//    }
//
//    @Test
//    public void should_has_median_amount_of_the_last_three_bookings_when_both_last_are_not_same_amount() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking1 = new Booking();
//        booking1.setExecutionDate(createDate("12/17/12"));
//        booking1.setAmount(new BigDecimal("123.45"));
//        bookingGroup.addBooking(new WrappedBooking(booking1));
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/17/13"));
//        booking2.setAmount(new BigDecimal("54.32"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("12/17/14"));
//        booking3.setAmount(new BigDecimal("76.54"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        assertThat(bookingGroup.getAmount().intValue()).as("amount of categorizer").isEqualTo(84);
//    }
//
//    @Test
//    public void should_has_median_amount_of_the_last_two_bookings_when_both_last_are_not_same_amount() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking2 = new Booking();
//        booking2.setExecutionDate(createDate("12/17/13"));
//        booking2.setAmount(new BigDecimal("54.31"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setExecutionDate(createDate("12/17/14"));
//        booking3.setAmount(new BigDecimal("76.54"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//
//        assertThat(bookingGroup.getAmount()).as("amount of categorizer").isEqualTo(new BigDecimal("66.00"));
//    }
//
//    @Test
//    public void should_has_amount_of_the_only_booking_when_has_one_booking() throws ParseException {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("12/17/13"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//
//        assertThat(bookingGroup.getAmount()).as("amount of categorizer").isEqualTo(new BigDecimal("123.45"));
//    }
//
//    @Test
//    public void should_has_next_year_booking_day_when_has_one_booking() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("03/02/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("03/02/16"));
//    }
//
//    @Test
//    public void should_provide_next_booking_day_for_reference() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("03/02/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        LocalDate referenceDate = createDate("03/02/16");
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(referenceDate);
//    }
//
//    @Test
//    public void should_always_has_a_date_after_given_provided_date() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("02/09/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        bookingGroup.setCycle(Cycle.MONTHLY);
//        LocalDate referenceDate = createDate("03/09/15");
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer")
//                .isAfter(referenceDate);
//    }
//
//    /*@Test
//    public void should_always_has_a_date_after_given_provided_date_when_cycle_is_weekly() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("03/02/15"));
//        bookingGroup.addBooking(booking);
//        bookingGroup.forceCycle(Cycle.WEEKLY);
//        LocalDate referenceDate = createDate("05/01/15");
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("05/04/15"));
//    }*/
//
//    @Test
//    public void should_has_next_booking_day_when_has_two_bookings() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("02/02/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        Booking booking2 = new Booking();
//        booking2.setAmount(new BigDecimal("123.45"));
//        booking2.setExecutionDate(createDate("03/02/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        bookingGroup.setCycle(Cycle.MONTHLY);
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("04/01/15"));
//    }
//
////   @Test
////   public void should_has_next_booking_day_when_has_two_bookings_but_not_on_same_booking_day() {
////       DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
////       Booking booking = new Booking();
////       booking.setAmount(new BigDecimal("123.45"));
////       booking.setExecutionDate(getBookingDay(2015, 1, 18));
////       bookingGroup.addBooking(booking);
////       Booking booking2 = new Booking();
////       booking2.setAmount(new BigDecimal("123.45"));
////       booking2.setExecutionDate(getBookingDay(2015, 2, 17));
////       bookingGroup.addBooking(new WrappedBooking(booking2));
////       bookingGroup.forceCycle(Cycle.MONTHLY);
////
////       assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(getBookingDay(2015, 3, 19));
////   }
//
//    @Test
//    public void should_has_next_booking_day_when_has_three_bookings() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("02/02/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        Booking booking2 = new Booking();
//        booking2.setAmount(new BigDecimal("123.45"));
//        booking2.setExecutionDate(createDate("03/02/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setAmount(new BigDecimal("123.45"));
//        booking3.setExecutionDate(createDate("04/01/15"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//        bookingGroup.setCycle(Cycle.MONTHLY);
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("05/04/15"));
//    }
//
////   @Test
////   public void should_has_next_booking_day_when_has_three_bookings_but_not_on_same_booking_day() {
////       DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
////       Booking booking = new Booking();
////       booking.setAmount(new BigDecimal("123.45"));
////       booking.setExecutionDate(getBookingDay(2015, 1, 18));
////       bookingGroup.addBooking(booking);
////       Booking booking2 = new Booking();
////       booking2.setAmount(new BigDecimal("123.45"));
////       booking2.setExecutionDate(getBookingDay(2015, 2, 17));
////       bookingGroup.addBooking(new WrappedBooking(booking2));
////       Booking booking3 = new Booking();
////       booking3.setAmount(new BigDecimal("123.45"));
////       booking3.setExecutionDate(getBookingDay(2015, 3, 19));
////       bookingGroup.addBooking(new WrappedBooking(booking3));
////       bookingGroup.forceCycle(Cycle.MONTHLY);
////
////       assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(getBookingDay(2015, 4, -3));
////   }
//
//    @Test
//    public void should_has_next_booking_day_on_the_same_day_as_mostly_bookings_at_the_end_of_month() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setAmount(new BigDecimal("123.45"));
//        booking4.setExecutionDate(createDate("01/27/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("02/27/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        Booking booking2 = new Booking();
//        booking2.setAmount(new BigDecimal("123.45"));
//        booking2.setExecutionDate(createDate("03/28/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setAmount(new BigDecimal("123.45"));
//        booking3.setExecutionDate(createDate("04/30/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//        bookingGroup.setCycle(Cycle.MONTHLY);
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("05/27/14"));
//    }
//
//    @Test
//    public void should_has_next_booking_day_on_the_same_day_as_mostly_bookings_at_start_of_month() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setAmount(new BigDecimal("123.45"));
//        booking4.setExecutionDate(createDate("02/03/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("03/03/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        Booking booking2 = new Booking();
//        booking2.setAmount(new BigDecimal("123.45"));
//        booking2.setExecutionDate(createDate("04/04/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setAmount(new BigDecimal("123.45"));
//        booking3.setExecutionDate(createDate("05/02/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//        bookingGroup.setCycle(Cycle.MONTHLY);
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("06/02/14"));
//    }
//
//    @Test
//    public void should_has_next_booking_day_on_the_same_day_as_mostly_bookings_at_any_other_day() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setAmount(new BigDecimal("123.45"));
//        booking4.setExecutionDate(createDate("02/10/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("03/11/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        Booking booking2 = new Booking();
//        booking2.setAmount(new BigDecimal("123.45"));
//        booking2.setExecutionDate(createDate("04/10/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setAmount(new BigDecimal("123.45"));
//        booking3.setExecutionDate(createDate("05/09/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//        bookingGroup.setCycle(Cycle.MONTHLY);
//
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("06/10/14"));
//    }
//
//    @Test
//    public void should_has_next_booking_day_on_the_last_day_of_the_month_if_major_day_is_30_or_31() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setAmount(new BigDecimal("123.45"));
//        booking4.setExecutionDate(createDate("03/31/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//        Booking booking = new Booking();
//        booking.setAmount(new BigDecimal("123.45"));
//        booking.setExecutionDate(createDate("04/30/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking));
//        Booking booking2 = new Booking();
//        booking2.setAmount(new BigDecimal("123.45"));
//        booking2.setExecutionDate(createDate("05/31/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking2));
//        Booking booking3 = new Booking();
//        booking3.setAmount(new BigDecimal("123.45"));
//        booking3.setExecutionDate(createDate("06/30/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking3));
//        bookingGroup.setCycle(Cycle.MONTHLY);
//
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo(createDate("07/31/14"));
//    }
//
//    @Test
//    public void should_has_next_booking_day_return_null_for_empty_booking_list() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isNull();
//    }
//
//    @Test
//    public void should_has_next_booking_day_return_yearly_for_cycle() {
//        DummyBookingGroup bookingGroup = new DummyBookingGroup("test");
//        Booking booking4 = new Booking();
//        booking4.setAmount(new BigDecimal("123.45"));
//        booking4.setExecutionDate(createDate("03/31/14"));
//        bookingGroup.addBooking(new WrappedBooking(booking4));
//
//        assertThat(bookingGroup.getNextExecutionDate()).as("next booking date of categorizer").isEqualTo("2015-03-31");
//    }
//
//    private static LocalDate createDate(String date) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
//        return LocalDate.parse(date, formatter);
//    }
}
