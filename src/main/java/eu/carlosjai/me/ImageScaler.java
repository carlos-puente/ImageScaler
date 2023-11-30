package eu.carlosjai.me;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import eu.carlosjai.me.definition.Constants;
import eu.carlosjai.me.definition.LoggerConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import static eu.carlosjai.me.definition.LoggerConstants.*;
import static eu.carlosjai.me.helper.ImageScalerHelper.*;

/**
 * This class is responsible for resizing images to a specific width and height.
 */
@Log4j2
public class ImageScaler {

    /**
     * The main method is the entry point of the Java application. It calls the generateValidImages method and then
     * processes the files in a specified directory by resizing them.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File directory = new File(Constants.RAW_PATH);
        if (directoryHasFiles(directory)) {
            log.info(String.format(START_MESSAGE, directory.listFiles().length));
            Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                    .forEach(ImageScaler::resizeFile);
            log.info(String.format(FINISH_MESSAGE));
        } else {
           log.error(String.format(DIRECTORY_IS_EMPTY, Constants.RAW_PATH));
        }
    }

    /**
     * This method resizes the specified file if necessary and saves the resized
     * image in the appropriate format.
     *
     * @param file The file to resize.
     */
    public static void resizeFile(final File file) {
        log.info(SEPARATOR_START);
        if (fileOrDirectoryExists(file)) {
            try {
                var originalSize = getFileSize(file);
                log.info(String.format(RESIZE_PROCESS_STARTED, Constants.RAW_PATH+Constants.SLASH+file.getName(), originalSize));
                getAndValidateImageFormat(file.getName());
                if (originalSize == 0) {
                    log.error(String.format(IMAGE_NOT_EXISTS, Objects.nonNull(file) ? file.getName() : StringUtils.EMPTY));
                }
                else if (originalSize > Constants.MAX_FILE_SIZE) {
                    resizeAndSaveImage(ImageIO.read(file), file.getName(), originalSize);
                } else {
                    log.info(RESIZE_NOT_NEEDED);
                    FileUtils.copyFile(file, new File(Constants.RESIZED_PATH + Constants.SLASH + file.getName()));
                }
            } catch (IOException e) {
                log.error(String.format(FILE_NOT_PROCESSED, Objects.nonNull(file) ? file.getName() : StringUtils.EMPTY, e.getMessage()));
            }
        } else {
            log.error(String.format(IMAGE_NOT_EXISTS, Objects.nonNull(file) ? file.getName() : StringUtils.EMPTY));
        }
        log.info(SEPARATOR_END);
    }

    /**
     * This method resizes the specified image if necessary and saves the resized image
     * with the given name and format.
     *
     * @param image     The image to resize.
     * @param imageName The name of the image
     */
    public static void resizeAndSaveImage(BufferedImage image, String imageName, long imageSize) throws IOException {
        if (Objects.nonNull(image)) {
            saveImage(image, imageName, getWidth(image), Constants.RESIZED_PATH);
            var newFile = new File(Constants.RESIZED_PATH + Constants.SLASH + imageName);
            var newSize = getFileSize(newFile);
            if (newSize > Constants.MAX_FILE_SIZE) {
                log.info(RESIZE_PROCESS_SECOND_ATTEMPT, newFile.getName(), newSize);
                saveImage(image, imageName, Constants.MAX_WIDTH - Constants.DECREMENT_WIDTH, Constants.RESIZED_PATH);
                newFile = new File(Constants.RESIZED_PATH + Constants.SLASH + imageName);
                newSize = getFileSize(newFile);
            }
            log.info(String.format(RESIZE_PROCESS_FINSIHED, Constants.RESIZED_PATH+Constants.SLASH+newFile.getName(), newSize));
            if (newSize > imageSize) {
                log.warn(SIZE_INCREASED);
            }
        } else {
            log.error(String.format(IMAGE_NOT_EXISTS, StringUtils.defaultString(imageName, StringUtils.EMPTY)));
        }
    }

}