package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import de.adorsys.smartanalytics.pers.spi.BookingCategoryRepositoryIf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoriesService {

    private final BookingCategoryRepositoryIf bookingCategoryRepository;
    private final AnalyticsConfigProvider analyticsConfigProvider;
    private final StatusService statusService;

    public void saveCategories(CategoriesTree categoriesTree) {
        bookingCategoryRepository.saveCategories(categoriesTree);
        statusService.categoriesChanged(categoriesTree.getVersion());
        analyticsConfigProvider.initCategories();
    }
}
