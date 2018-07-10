package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import de.adorsys.smartanalytics.pers.api.CategoriesTreeEntity;
import de.adorsys.smartanalytics.pers.spi.BookingCategoryRepositoryIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriesService {

    @Autowired
    private BookingCategoryRepositoryIf bookingCategoryRepository;
    @Autowired
    private AnalyticsConfigProvider analyticsConfigProvider;
    @Autowired
    private StatusService statusService;

    public Optional<CategoriesTreeEntity> getCategories() {
        return bookingCategoryRepository.getCategories();
    }

    public void saveCategories(CategoriesTree categoriesTree) {
        bookingCategoryRepository.saveCategories(categoriesTree);
        statusService.categoriesChanged(categoriesTree.getVersion());
        analyticsConfigProvider.initCategories();
    }
}
