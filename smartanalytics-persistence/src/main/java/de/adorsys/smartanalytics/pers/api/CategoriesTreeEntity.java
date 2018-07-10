package de.adorsys.smartanalytics.pers.api;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class CategoriesTreeEntity extends CategoriesTree {

    public static final String CONTAINER_ID = "categories";

    @Id
    private String id;
}
