package de.adorsys.smartanalytics.pers.spi;

import de.adorsys.smartanalytics.api.CategoriesContainer;
import de.adorsys.smartanalytics.pers.api.CategoriesContainerEntity;

import java.util.Optional;

public interface BookingCategoryRepositoryIf {

    Optional<CategoriesContainerEntity> getCategoriesContainer();

    void saveCategoriesContainer(CategoriesContainer categoriesContainer);

}
