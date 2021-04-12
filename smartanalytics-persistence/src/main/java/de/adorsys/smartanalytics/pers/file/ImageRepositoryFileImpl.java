package de.adorsys.smartanalytics.pers.file;

import de.adorsys.smartanalytics.pers.spi.ImageRepositoryIf;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Profile({"file-persistence"})
@Service
public class ImageRepositoryFileImpl implements ImageRepositoryIf {

    @Override
    public byte[] getImage(String imageName) {
        return new byte[0];
    }

    @Override
    public void saveImage(byte[] byteArray, String fileName) {

    }

    @Override
    public void deleteAllImages() {

    }
}
