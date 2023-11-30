package eu.carlosjai.me.helper;

import eu.carlosjai.me.definition.Constants;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ImageScalerHelperTest {
    public static final String DIR_PATH = "src/test/resources/testDir";
    public static final String RESIZED_DIR_PATH = "src/test/resources/resizedDir";
    public static final String RESIZED_PNG_FILE_PATH = "src/test/resources/resizedDir/test.png";
    public static final String PNG_FILE_PATH = "src/test/resources/testDir/test.png";
    public static final String FILE_WITHOUT_EXCEPTION_PATH = "src/test/resources/testDir/test";
    public static final String PDF_FILE_PATH = "src/test/resources/testDir/test.pdf";
    public static final String NOT_EXISTING_FILE_PATH = "src/test/resources/testDir/fake.jpg";
    public static final String EMPTY_DIR_PATH = "src/test/resources/emptyDir";
    @Test
    public void saveImage() throws IOException {
        var file = new File(PNG_FILE_PATH);
        var image = ImageIO.read(file);
        ImageScalerHelper.saveImage(image, file.getName(), Constants.MAX_WIDTH, RESIZED_DIR_PATH);
        var resizedFile = new File(RESIZED_PNG_FILE_PATH);
        var resizedImage = ImageIO.read(resizedFile);
        assertTrue(resizedFile.exists());
        assertEquals(file.getName(), resizedFile.getName());
        assertTrue(ImageScalerHelper.getFileSize(file) > ImageScalerHelper.getFileSize(resizedFile));
        assertTrue(image.getWidth() > resizedImage.getWidth());
        resizedFile.delete();
        assertFalse(resizedFile.exists());
    }

    @Test
    public void saveImage_checkWrongUseCasesTests() throws IOException {
        ImageScalerHelper.saveImage(null, "test", Constants.MAX_WIDTH, RESIZED_DIR_PATH);
        var file = new File(PNG_FILE_PATH);
        var image = ImageIO.read(file);
        var thrown = assertThrows(IOException.class, () ->ImageScalerHelper.saveImage(image, null, Constants.MAX_WIDTH, RESIZED_DIR_PATH));
        assertTrue(thrown.getMessage().contains("File extension is mandatory: null"));
        var resizedFile = new File(RESIZED_PNG_FILE_PATH);
        assertFalse(resizedFile.exists());
        thrown = assertThrows(IOException.class, () ->ImageScalerHelper.saveImage(image, "test", Constants.MAX_WIDTH, RESIZED_DIR_PATH));
        assertTrue(thrown.getMessage().contains("File extension is mandatory: test"));
        resizedFile = new File(RESIZED_PNG_FILE_PATH);
        assertFalse(resizedFile.exists());
    }

    @Test
    public  void directoryHasFiles() {
        assertFalse("Null file has not files inside", ImageScalerHelper.directoryHasFiles(null));
        assertFalse("File with empty path has not files inside", ImageScalerHelper.directoryHasFiles(new File(StringUtils.EMPTY)));
        assertFalse("File with not directory path has not files inside", ImageScalerHelper.directoryHasFiles(new File(PNG_FILE_PATH)));
        assertFalse("File with not directory path has not files inside", ImageScalerHelper.directoryHasFiles(new File(PDF_FILE_PATH)));
        assertFalse("File with empty directory has not files inside", ImageScalerHelper.directoryHasFiles(new File(EMPTY_DIR_PATH)));
        assertFalse("File with fake path has not files inside", ImageScalerHelper.directoryHasFiles(new File(NOT_EXISTING_FILE_PATH)));
        assertTrue("Directory with files inside, has files", ImageScalerHelper.directoryHasFiles(new File(DIR_PATH)));
    }

    @Test
    public  void getAndValidateImageFormat() throws IOException {
        var file = new File(PNG_FILE_PATH);
        assertEquals("png", ImageScalerHelper.getAndValidateImageFormat(file.getName()));
        final var notValidFileType = new File(PDF_FILE_PATH);
        var thrown = assertThrows(IOException.class, () -> ImageScalerHelper.getAndValidateImageFormat(notValidFileType.getName()));
        assertTrue(thrown.getMessage().contains("Not supported image extension: pdf"));
        final var notValidFileExtension = new File(FILE_WITHOUT_EXCEPTION_PATH);
        thrown = assertThrows(IOException.class, () -> ImageScalerHelper.getAndValidateImageFormat(notValidFileExtension.getName()));
        assertTrue(thrown.getMessage().contains("File extension is mandatory: test"));
    }

    @Test
    public  void getWidth() throws IOException {
        assertEquals(0, ImageScalerHelper.getWidth(null));
        var image = ImageIO.read(new File(PNG_FILE_PATH));
        assertEquals(960, ImageScalerHelper.getWidth(image));
    }

    @Test
    public  void getFileSize() {
        assertEquals(0, ImageScalerHelper.getFileSize(null));
        assertEquals(0, ImageScalerHelper.getFileSize(new File(StringUtils.EMPTY)));
        assertEquals(0, ImageScalerHelper.getFileSize(new File(NOT_EXISTING_FILE_PATH)));
        assertEquals(0, ImageScalerHelper.getFileSize(new File(DIR_PATH)));
        assertEquals(0, ImageScalerHelper.getFileSize(new File(EMPTY_DIR_PATH)));
        assertEquals(12, ImageScalerHelper.getFileSize(new File(PDF_FILE_PATH)));
        assertEquals(7193, ImageScalerHelper.getFileSize(new File(PNG_FILE_PATH)));
    }

    @Test
    public void fileExists() {
        assertFalse(ImageScalerHelper.fileOrDirectoryExists(null));
        assertFalse(ImageScalerHelper.fileOrDirectoryExists(new File(NOT_EXISTING_FILE_PATH)));
        assertFalse(ImageScalerHelper.fileOrDirectoryExists(new File(StringUtils.EMPTY)));
        assertFalse(ImageScalerHelper.fileOrDirectoryExists(new File(RESIZED_PNG_FILE_PATH)));
        assertTrue(ImageScalerHelper.fileOrDirectoryExists(new File(PNG_FILE_PATH)));
        assertTrue(ImageScalerHelper.fileOrDirectoryExists(new File(EMPTY_DIR_PATH)));
    }
}