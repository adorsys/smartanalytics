package de.adorsys.smartanalytics.category;

public class BookingGroupServiceTest {

//    private CategoryService sut;
//
//    @Before
//    public void setup() {
//        sut = new CategoryService();
//    }
//
//    @Test
//    public void should_return_null_for_null_bookings_input() {
//
//        CategoryResult result = sut.categorize(null, null, null);
//
//        assertThat(result).isNull();
//    }
//
//    @Test
//    public void should_return_null_for_empty_bookings_input() {
//
//        CategoryResult result = sut.categorize(new ArrayList<Booking>(), null, null);
//
//        assertThat(result).isNull();
//    }
//
//    @Test
//    public void should_return_a_result_object_for_non_empty_bookings_input() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//
//        CategoryResult result = sut.categorize(bookings, null, null);
//
//        assertThat(result).isInstanceOf(CategoryResult.class);
//    }
//
//    @Test
//    public void should_call_modifier() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//        Modifier modifierMock = mock(Modifier.class);
//        List<Modifier> modifier = Arrays.asList(modifierMock);
//
//        sut.categorize(bookings, modifier, null);
//
//        verify(modifierMock).modify(any(ExtendedBooking.class));
//    }
//
//    @Test
//    public void should_modify_booking_with_modifier() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//        Modifier modifierMock = mock(Modifier.class);
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//                ExtendedBooking booking = (ExtendedBooking) invocationOnMock.getArguments()[0];
//                booking.setMainCategory("Testcat");
//                return null;
//            }
//        }).when(modifierMock).modify(any(ExtendedBooking.class));
//        List<Modifier> modifier = Arrays.asList(modifierMock);
//
//        CategoryResult result = sut.categorize(bookings, modifier, null);
//
//        assertThat(result.getBookings().get(0).getMainCategory()).as("main category of first booking").isEqualTo("Testcat");
//    }
//
//    @Test
//    public void should_modify_booking_in_order_of_modificators() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//        Modifier modifierMock1 = mock(Modifier.class);
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//                ExtendedBooking booking = (ExtendedBooking) invocationOnMock.getArguments()[0];
//                booking.setMainCategory("first modification");
//                return null;
//            }
//        }).when(modifierMock1).modify(any(ExtendedBooking.class));
//        Modifier modifierMock2 = mock(Modifier.class);
//        doAnswer(new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//                ExtendedBooking booking = (ExtendedBooking) invocationOnMock.getArguments()[0];
//                booking.setMainCategory(booking.getMainCategory() + " was not enough");
//                return null;
//            }
//        }).when(modifierMock2).modify(any(ExtendedBooking.class));
//        List<Modifier> modifier = Arrays.asList(modifierMock1, modifierMock2);
//
//        CategoryResult result = sut.categorize(bookings, modifier, null);
//
//        assertThat(result.getBookings().get(0).getMainCategory()).as("main category of first booking").isEqualTo("first modification was not enough");
//    }
//
//    @Test
//    public void should_call_categorizer() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(false);
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//
//        sut.categorize(bookings, null, groupBuilders);
//
//        verify(groupBuilderMock).groupShouldBeCreated(any(ExtendedBooking.class));
//    }
//
//    @Test
//    public void should_call_category_creation_of_categorizer_when_matched() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock.createGroup(any(ExtendedBooking.class))).thenReturn(null);
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//
//        sut.categorize(bookings, null, groupBuilders);
//
//        verify(groupBuilderMock).createGroup(any(ExtendedBooking.class));
//    }
//
//    @Test
//    public void should_stop_categorize_at_first_match() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//        GroupBuilder groupBuilderMock1 = mock(GroupBuilder.class);
//        when(groupBuilderMock1.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock1.createGroup(any(ExtendedBooking.class))).thenReturn(null);
//        GroupBuilder groupBuilderMock2 = mock(GroupBuilder.class);
//        when(groupBuilderMock2.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(false);
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock1, groupBuilderMock2);
//
//        sut.categorize(bookings, null, groupBuilders);
//
//        verifyZeroInteractions(groupBuilderMock2);
//    }
//
//    @Test
//    public void should_go_one_for_next_booking_categorize_at_first_match() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking(),
//                (Booking) new DummyBooking()
//        );
//        GroupBuilder groupBuilderMock1 = mock(GroupBuilder.class);
//        when(groupBuilderMock1.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true).thenReturn(false);
//        when(groupBuilderMock1.createGroup(any(ExtendedBooking.class))).thenReturn(null);
//        GroupBuilder groupBuilderMock2 = mock(GroupBuilder.class);
//        when(groupBuilderMock2.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(false);
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock1, groupBuilderMock2);
//
//        sut.categorize(bookings, null, groupBuilders);
//
//        verify(groupBuilderMock1, times(2)).groupShouldBeCreated(any(ExtendedBooking.class));
//        verify(groupBuilderMock2, times(1)).groupShouldBeCreated(any(ExtendedBooking.class));
//    }
//
//    @Test
//    public void should_remove_categories_with_one_booking() {
//        List<Booking> bookings = Arrays.asList(
//                (Booking) new DummyBooking()
//        );
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock.createGroup(any(ExtendedBooking.class))).thenReturn(new DummyBookingGroup("test"));
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//
//        CategoryResult result = sut.categorize(bookings, null, groupBuilders);
//
//        assertThat(result.getBookingGroups()).as("categories of result").hasSize(0);
//    }
//
//    @Test
//    public void should_accept_one_booking_categories_when_whitelist_matches() {
//        DummyBooking b1 = new DummyBooking();
//        b1.setExecutionDate(LocalDate.of(2017,1,1));
//        List<Booking> bookings = Arrays.asList(b1);
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock.createGroup(any(ExtendedBooking.class))).thenReturn(new DummyBookingGroup("test"));
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//        List<Matcher> whitelist = Arrays.asList((Matcher) new Matcher() {
//            @Override
//            public boolean match(ExtendedBooking obj) {
//                return true;
//            }
//        });
//
//        CategoryResult result = sut.categorize(bookings, null, groupBuilders, whitelist);
//
//        assertThat(result.getBookingGroups()).as("categories of result").hasSize(1);
//    }
//
//    @Test
//    public void should_not_return_empty_category_after_whitelist_match() {
//        DummyBooking b1 = new DummyBooking();
//        b1.setExecutionDate(LocalDate.of(2017, 1, 1));
//        List<Booking> bookings = Arrays.asList(b1);
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock.createGroup(any(ExtendedBooking.class))).thenReturn(new DummyBookingGroup("test"));
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//        List<Matcher> whitelist = Arrays.asList((Matcher) obj -> true);
//
//        CategoryResult result = sut.categorize(bookings, null, groupBuilders, whitelist);
//
//        assertThat(result.getBookingGroups()).as("categories of result").hasSize(1);
//        assertThat(result.getBookingGroups().get(0).getBookings()).as("bookings of first category").hasSize(1);
//    }
//
//    @Test
//    public void should_set_cycle_to_yearly_at_one_booking_categories_for_whitelist_match() {
//        DummyBooking booking1 = new DummyBooking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//        booking1.setAmount(new BigDecimal(0.00));
//        DummyBooking booking2 = new DummyBooking();
//        booking2.setExecutionDate(createDate("02/01/16"));
//        booking2.setAmount(new BigDecimal(0.00));
//        DummyBooking booking3 = new DummyBooking();
//        booking3.setExecutionDate(createDate("02/10/16"));
//        booking3.setAmount(new BigDecimal(0.00));
//        List<Booking> bookings = Arrays.asList((Booking) booking1, booking2, booking3);
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock.createGroup(any(ExtendedBooking.class)))
//                .thenReturn(new DummyBookingGroup("test1"))
//                .thenReturn(new DummyBookingGroup("test2"));
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//        List<Matcher> whitelist = Arrays.asList((Matcher) new Matcher() {
//            @Override
//            public boolean match(ExtendedBooking obj) {
//                return true;
//            }
//        });
//
//        CategoryResult result = sut.categorize(bookings, null, groupBuilders, whitelist);
//
//        assertThat(result.getBookingGroups()).as("categories of result").hasSize(2).extracting("cycle").contains(new Cycle[] {Cycle.YEARLY, null});
//    }
//
//    @Test
//    public void should_accept_one_booking_categories_when_only_one_matcher_of_whitelist_matches() {
//        DummyBooking b1 = new DummyBooking();
//        b1.setExecutionDate(LocalDate.of(2017, 1, 1));
//        List<Booking> bookings = Arrays.asList(
//                b1
//        );
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock.createGroup(any(ExtendedBooking.class))).thenReturn(new DummyBookingGroup("test"));
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//        List<Matcher> whitelist = Arrays.asList(new Matcher() {
//            @Override
//            public boolean match(ExtendedBooking obj) {
//                return false;
//            }
//        }, new Matcher() {
//            @Override
//            public boolean match(ExtendedBooking obj) {
//                return true;
//            }
//        });
//
//        CategoryResult result = sut.categorize(bookings, null, groupBuilders, whitelist);
//
//        assertThat(result.getBookingGroups()).as("categories of result").hasSize(1);
//    }
//
//    @Test
//    public void should_not_remove_categories_with_more_than_one_booking() {
//        DummyBooking booking1 = new DummyBooking();
//        booking1.setExecutionDate(createDate("01/01/16"));
//        booking1.setAmount(new BigDecimal(0.00));
//        DummyBooking booking2 = new DummyBooking();
//        booking2.setExecutionDate(createDate("02/01/16"));
//        booking2.setAmount(new BigDecimal(0.00));
//        List<Booking> bookings = Arrays.asList((Booking) booking1, (Booking) booking2);
//        GroupBuilder groupBuilderMock = mock(GroupBuilder.class);
//        when(groupBuilderMock.groupShouldBeCreated(any(ExtendedBooking.class))).thenReturn(true);
//        when(groupBuilderMock.createGroup(any(ExtendedBooking.class))).thenReturn(new DummyBookingGroup("test"));
//        List<GroupBuilder> groupBuilders = Arrays.asList(groupBuilderMock);
//
//        CategoryResult result = sut.categorize(bookings, null, groupBuilders);
//
//        assertThat(result.getBookingGroups()).as("categories of result").hasSize(1);
//    }
//
//    private static LocalDate createDate(String date) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
//        return LocalDate.parse(date, formatter);
//    }
}
