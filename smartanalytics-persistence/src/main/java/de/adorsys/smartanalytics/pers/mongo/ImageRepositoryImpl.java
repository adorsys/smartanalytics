package de.adorsys.smartanalytics.pers.mongo;

import de.adorsys.smartanalytics.pers.spi.ImageRepositoryIf;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Profile({"mongo-persistence", "fongo"})
@Service
public class ImageRepositoryImpl implements ImageRepositoryIf {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Override
    public byte[] getImage(String filename) {
        GridFsResource gridFsResource = gridFsTemplate.getResource(filename);
        if (gridFsResource != null) {
            try {
                return IOUtils.toByteArray(gridFsResource.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
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
