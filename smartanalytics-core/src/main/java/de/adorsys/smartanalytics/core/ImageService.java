package de.adorsys.smartanalytics.core;

import de.adorsys.smartanalytics.pers.spi.ImageRepositoryIf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
public class ImageService {

    @Autowired
    private ImageRepositoryIf imageRepository;

    public byte[] getImage(String imageName) {
        return imageRepository.getImage(imageName);
    }

    public void importImages(InputStream inputStream) throws IOException {
        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            imageRepository.deleteAllImages();
            log.info("images deleted");

            log.info("start unzip image files");
            int count = 0;
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                unzipAndSaveImage(zis, zipEntry);
                count++;
            }

            log.info("successfully imported {} images", count);
        }
    }

    private void unzipAndSaveImage(ZipInputStream zis, ZipEntry zipEntry) throws IOException {
        if (zipEntry.isDirectory() || zipEntry.getName().contains("__MACOSX")) {
            return;
        }

        byte[] buffer = new byte[2048];
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        int len;
        while ((len = zis.read(buffer)) > 0) {
            output.write(buffer, 0, len);
        }
        imageRepository.saveImage(output.toByteArray(), zipEntry.getName());
    }
}
