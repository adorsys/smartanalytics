package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import de.adorsys.smartanalytics.pers.api.CategoriesTreeEntity;

import java.util.Optional;

public interface BookingCategoryRepositoryIf {

    Optional<CategoriesTreeEntity> getCategories();

    void saveCategories(CategoriesTree categoriesContainer);

}
