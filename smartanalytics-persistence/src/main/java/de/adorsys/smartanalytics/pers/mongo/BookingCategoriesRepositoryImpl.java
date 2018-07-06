package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.api.CategoriesContainer;
import de.adorsys.smartanalytics.pers.api.CategoriesContainerEntity;
import de.adorsys.smartanalytics.pers.spi.BookingCategoryRepositoryIf;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Profile({"mongo-persistence", "fongo"})
@Service
public class BookingCategoriesRepositoryImpl implements BookingCategoryRepositoryIf {

    @Autowired
    private BookingCategoriesRepositoryMongodb categoriesRepository;

    @Override
    public Optional<CategoriesContainerEntity> getCategoriesContainer() {
        return categoriesRepository.findById(CategoriesContainerEntity.CONTAINER_ID);
    }

    @Override
    public void saveCategoriesContainer(CategoriesContainer categoriesContainer) {
        CategoriesContainerEntity containerEntity = new CategoriesContainerEntity();
        BeanUtils.copyProperties(categoriesContainer, containerEntity);

        containerEntity.setId(CategoriesContainerEntity.CONTAINER_ID);
        containerEntity.setChangeDate(LocalDate.now());

        categoriesRepository.deleteById(CategoriesContainerEntity.CONTAINER_ID);
        categoriesRepository.save(containerEntity);
    }
}

