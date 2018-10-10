package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@Document
public class CategoriesTreeEntity extends CategoriesTree {

    public static final String CONTAINER_ID = "categories";

    @Id
    private String id;
}
