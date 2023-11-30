package eu.carlosjai.me;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import eu.carlosjai.me.definition.Constants;
import org.apache.commons.io.FileUtils;
import lombok.extern.java.Log;

import static eu.carlosjai.me.helper.ImageScalerHelper.*;

/**
 * This class is responsible for resizing images to a specific width and height.
 */
@Log
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
            Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                    .forEach(ImageScaler::resizeFile);
        } else {
            log.log(Level.WARNING, "The raw directory is empty: "+Constants.RAW_PATH);
        }
    }

    /**
     * This method resizes the specified file if necessary and saves the resized
     * image in the appropriate format.
     *
     * @param file The file to resize.
     */
    public static void resizeFile(final File file) {
        if (fileOrDirectoryExists(file)) {
            try {
                var originalSize = getFileSize(file);
                getAndValidateImageFormat(file.getName());
                if (originalSize > Constants.MAX_FILE_SIZE) {
                    resizeAndSaveImage(ImageIO.read(file), file.getName(), originalSize);
                } else {
                    log.log(Level.INFO, Constants.NO_RESIZE_NEEDED);
                    FileUtils.copyFile(file, new File(Constants.RESIZED_PATH + Constants.SLASH + file.getName()));
                }
            } catch (IOException e) {
                log.log(Level.SEVERE, "[ERROR] -> Image: " + file.getName() + " not processed: " + e);
            }
        }
    }

    /**
     * This method resizes the specified image if necessary and saves the resized image
     * with the given name and format.
     *
     * @param image     The image to resize.
     * @param imageName The name of the image
     */
    public static void resizeAndSaveImage(BufferedImage image, String imageName, long imageSize) throws IOException {
        if (image != null) {
            saveImage(image, imageName, getWidth(image), Constants.RESIZED_PATH);
            var newFile = new File(Constants.RESIZED_PATH + Constants.SLASH + imageName);
            var newSize = getFileSize(newFile);
            log.log(Level.INFO, "[START] processing -> " + imageName + " size: " + imageSize + "KB");
            if (newSize > Constants.MAX_FILE_SIZE) {
                log.log(Level.INFO, "[INFO] Size continues high, reducing quality of " + newFile.getName() + " size: " + newSize + "KB");
                saveImage(image, imageName, Constants.MAX_WIDTH - Constants.DECREMENT_WIDTH, Constants.RESIZED_PATH);
                newFile = new File(Constants.RESIZED_PATH + Constants.SLASH + imageName);
                newSize = getFileSize(newFile);
            }

            log.log(Level.INFO, "[FINISH] -> " + newFile.getName() + " size: " + newSize + "KB");
            if (newSize > imageSize) {
                log.log(Level.WARNING, "[WARNING] -> size was increased");
            }
        }
    }

}