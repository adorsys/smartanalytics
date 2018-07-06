package de.adorsys.smartanalytics.api;

import lombok.Data;

import java.util.List;

/**
 * Created by alexg on 04.12.17.
 */
@Data
public class BookingCategory {

    String id;
    String name;
    List<BookingCategory> subcategories;
    List<BookingCategory> specifications;

}
