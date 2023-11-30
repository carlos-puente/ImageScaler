package eu.carlosjai.me.definition;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class SupportedImagesEnumTest {

    @Test
    public void testGetExtension() {
        assertEquals("jpg", SupportedImagesEnum.JPG.getExtension());
        assertEquals("jpeg", SupportedImagesEnum.JPEG.getExtension());
        assertEquals("jfif", SupportedImagesEnum.JFIF.getExtension());
        assertEquals("png", SupportedImagesEnum.PNG.getExtension());
    }

    @Test
    public void testGetFormat() {
        assertEquals("jpg", SupportedImagesEnum.JPG.getFormat());
        assertEquals("jpg", SupportedImagesEnum.JPEG.getFormat());
        assertEquals("jpg", SupportedImagesEnum.JFIF.getFormat());
        assertEquals("png", SupportedImagesEnum.PNG.getFormat());
    }

    @Test
    public void testGetFormatByExtension() {
        assertEquals("jpg", SupportedImagesEnum.getFormatByExtension("jpg"));
        assertEquals("jpg", SupportedImagesEnum.getFormatByExtension("jpeg"));
        assertEquals("jpg", SupportedImagesEnum.getFormatByExtension("jfif"));
        assertEquals("png", SupportedImagesEnum.getFormatByExtension("png"));
    }

    @Test
    public void testIsMember() {
        assertTrue(SupportedImagesEnum.isMember("jpg"));
        assertTrue(SupportedImagesEnum.isMember("jpeg"));
        assertTrue(SupportedImagesEnum.isMember("jfif"));
        assertTrue(SupportedImagesEnum.isMember("png"));
        assertFalse(SupportedImagesEnum.isMember("gif"));
        assertFalse(SupportedImagesEnum.isMember(null));
    }

    @Test
    public void testGetSupportedExtensions() {
        assertEquals(List.of("jpg", "jpeg", "jfif", "png"), SupportedImagesEnum.getSupportedExtensions());
    }

    @Test
    public void testGetFormatWithInvalidExtension() {
        assertEquals(StringUtils.EMPTY, SupportedImagesEnum.getFormatByExtension("GIF"));
    }
}