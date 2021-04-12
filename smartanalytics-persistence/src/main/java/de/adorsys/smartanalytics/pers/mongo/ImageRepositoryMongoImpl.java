package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.spi.ImageRepositoryIf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Profile({"mongo-persistence", "fongo"})
@Service
public class ImageRepositoryMongoImpl implements ImageRepositoryIf {

    private final GridFsTemplate gridFsTemplate;

    @Override
    public @Nullable
    byte[] getImage(String filename) {
        return Optional.ofNullable(gridFsTemplate.getResource(filename))
                .map(gridFsResource -> {
                    try {
                        return IOUtils.toByteArray(gridFsResource.getInputStream());
                    } catch (IOException e) {
                        log.warn(e.getMessage());;
                        return null;
                    }
                })
                .orElse(null);
    }

    @Override
    public void saveImage(byte[] byteArray, String fileName) {
        gridFsTemplate.store(new ByteArrayInputStream(byteArray), fileName);
    }

    @Override
    public void deleteAllImages() {
        gridFsTemplate.delete(new Query());
    }
}
