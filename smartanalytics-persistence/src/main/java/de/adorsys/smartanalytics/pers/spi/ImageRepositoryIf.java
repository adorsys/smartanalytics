package de.adorsys.smartanalytics.pers.spi;

public interface ImageRepositoryIf {

    byte[] getImage(String imageName);

    void saveImage(byte[] byteArray, String fileName);

    void deleteAllImages();
}
