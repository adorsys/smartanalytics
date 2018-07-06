package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.api.CategoriesContainer;
import de.adorsys.smartanalytics.pers.api.CategoriesContainerEntity;
import de.adorsys.smartanalytics.pers.spi.BookingCategoryRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriesService {

    @Autowired
    private BookingCategoryRepositoryIf bookingCategoryRepository;
    @Autowired
    private StatusService statusService;

    public Optional<CategoriesContainerEntity> getCategoriesContainer() {
        return bookingCategoryRepository.getCategoriesContainer();
    }

    public void saveCategoriesContainer(CategoriesContainer categoriesContainer) {
        bookingCategoryRepository.saveCategoriesContainer(categoriesContainer);
        statusService.categoriesConfigChanged(categoriesContainer.getVersion());
    }
}
