package eu.carlosjai.me.helper;

import eu.carlosjai.me.definition.Constants;
import eu.carlosjai.me.definition.SupportedImagesEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.shared.utils.StringUtils;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class ImageScalerHelper {

    /**
     * This method saves the provided image to a file with the specified name and settings.
     *
     * @param image     the image to be saved
     * @param imageName the name of the image file
     * @param maxWidth  the maximum width allowed for the resized image
     * @throws IOException if an I/O error occurs while saving the image
     */
    public static void saveImage(BufferedImage image, String imageName, int maxWidth, String resultsPath) throws IOException {
        if (Objects.nonNull(image)) {
            ImageIO.write(
                    Scalr.resize(image, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, maxWidth, Constants.MAX_HEIGHT, Scalr.OP_ANTIALIAS),
                    getAndValidateImageFormat(imageName),
                    new File(resultsPath + Constants.SLASH + imageName)
            );
        }
    }

    /**
     * This method checks if a given directory contains any files.
     *
     * @param directory The directory to check for files.
     * @return {@code true} if the directory contains files, {@code false} otherwise.
     */
    public static boolean directoryHasFiles(final File directory) {
        return isDirectory(directory) && ArrayUtils.isNotEmpty(directory.listFiles());
    }

    /**
     * This method checks if the specified file is a directory.
     *
     * @param directory The file to check.
     * @return {@code true} if the file is a directory, {@code false} otherwise.
     */
    private static boolean isDirectory(final File directory) {
        return fileOrDirectoryExists(directory) && directory.isDirectory();
    }

    /**
     * This method retrieves and validates the image format based on the provided image file name.
     *
     * @param imageFileName the file name of the image
     * @return the validated image format
     * @throws IOException if the image format is not supported or the file name is invalid
     */
    public static String getAndValidateImageFormat(final String imageFileName) throws IOException {
        if (StringUtils.contains(imageFileName, Constants.DOT)) {
            var fileParts = imageFileName.split(Constants.DOT_REGEX);
            var extension = fileParts[fileParts.length - 1].toLowerCase();
            if (SupportedImagesEnum.isMember(extension)) {
                return SupportedImagesEnum.getFormatByExtension(extension);
            } else {
                throw new IOException("Not supported image extension: " + extension + ". Supported extensions: " + SupportedImagesEnum.getSupportedExtensions());
            }
        }
        throw new IOException("File extension is mandatory: " + imageFileName);
    }

    /**
     * This method retrieves the width of the specified image. If the width
     * is greater than the maximum allowed width, it returns the maximum width.
     *
     * @param image the image whose width needs to be obtained
     * @return the width of the image, or the maximum width if it exceeds the limit
     */
    public static int getWidth(BufferedImage image) {
        return Objects.nonNull(image) ? Math.min(image.getWidth(), Constants.MAX_WIDTH) : 0;
    }


    /**
     * This method retrieves the size of the specified file in kilobytes.
     *
     * @param file the file for which the size needs to be determined
     * @return the size of the file in kilobytes
     */
    public static long getFileSize(File file) {
        return fileOrDirectoryExists(file) && !isDirectory(file) ? file.length() / 1024 : 0;
    }

    /**
     * Checks if a file exists
     * @param file to check
     * @return true if exists
     */
    public static boolean fileOrDirectoryExists(File file) {
        return Objects.nonNull(file) && file.exists();
    }

}
