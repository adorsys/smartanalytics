package de.adorsys.smartanalytics.api.config;

import lombok.Data;

import java.util.List;

/**
 * Created by alexg on 04.12.17.
 */
@Data
public class Category {

    String id;
    String name;
    List<Category> subcategories;
    List<Category> specifications;

}
