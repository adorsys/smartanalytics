package de.adorsys.smartanalytics.pers.file;

import de.adorsys.smartanalytics.api.config.CategoriesTree;
import de.adorsys.smartanalytics.pers.api.CategoriesTreeEntity;
import de.adorsys.smartanalytics.pers.spi.BookingCategoryRepositoryIf;
import de.adorsys.smartanalytics.pers.utils.ImportUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Profile({"file-persistence"})
@Service
public class BookingCategoriesRepositoryFileImpl implements BookingCategoryRepositoryIf {

    private static final String CATEGORIES_FILE_NAME = "categories.yml";

    @Value("${smartanalytics.conf.path:#{systemProperties['java.io.tmpdir']}}")
    private File confDir;

    private CategoriesTreeEntity categoriesTreeEntity;

    @PostConstruct
    public void postConstruct() throws IOException {
        File categoriesFile = new File(confDir, CATEGORIES_FILE_NAME);
        if (categoriesFile.exists()) {
            categoriesTreeEntity = ImportUtils.readAsYaml(categoriesFile, CategoriesTreeEntity.class);
        }
    }

    @Override
    public Optional<CategoriesTreeEntity> getCategories() {
        return Optional.ofNullable(categoriesTreeEntity);
    }

    @Override
    public void saveCategories(CategoriesTree categoriesTree) {
        categoriesTreeEntity = new CategoriesTreeEntity();
        BeanUtils.copyProperties(categoriesTree, categoriesTreeEntity);

        try {
            ImportUtils.writeObjectToYaml(new File(confDir, CATEGORIES_FILE_NAME), categoriesTreeEntity);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

