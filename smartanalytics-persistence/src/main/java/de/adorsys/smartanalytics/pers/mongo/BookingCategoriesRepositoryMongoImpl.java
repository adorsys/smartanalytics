package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import de.adorsys.smartanalytics.pers.api.CategoriesTreeEntity;
import de.adorsys.smartanalytics.pers.spi.BookingCategoryRepositoryIf;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Profile({"mongo-persistence", "fongo"})
@Service
public class BookingCategoriesRepositoryMongoImpl implements BookingCategoryRepositoryIf {

    private final BookingCategoriesRepositoryMongodb categoriesRepository;

    @Override
    public Optional<CategoriesTreeEntity> getCategories() {
        return categoriesRepository.findById(CategoriesTreeEntity.CONTAINER_ID);
    }

    @Override
    public void saveCategories(CategoriesTree categoriesTree) {
        CategoriesTreeEntity categoriesTreeEntity = new CategoriesTreeEntity();
        BeanUtils.copyProperties(categoriesTree, categoriesTreeEntity);

        categoriesTreeEntity.setId(CategoriesTreeEntity.CONTAINER_ID);
        categoriesTreeEntity.setChangeDate(LocalDate.now());

        categoriesRepository.deleteById(CategoriesTreeEntity.CONTAINER_ID);
        categoriesRepository.save(categoriesTreeEntity);
    }
}

