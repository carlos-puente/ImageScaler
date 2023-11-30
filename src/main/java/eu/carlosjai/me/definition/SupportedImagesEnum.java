package eu.carlosjai.me.definition;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;


import java.util.Arrays;
import java.util.List;
@Getter
public enum SupportedImagesEnum {
    JPG(Constants.JPG, Constants.JPG),
    JPEG(Constants.JPEG, Constants.JPG),
    JFIF(Constants.JFIF, Constants.JPG),
    PNG(Constants.PNG, Constants.PNG);

    private final String extension;
    private final String format;

    SupportedImagesEnum(final String extension, final String format) {
        this.extension = extension;
        this.format = format;
    }


    public static boolean isMember(String extension) {
        return StringUtils.isNotBlank(extension) &&
                Arrays.stream(SupportedImagesEnum.values())
                .filter(supportedImage -> StringUtils.equals(supportedImage.getExtension(), extension))
                .map(SupportedImagesEnum::getExtension).findFirst().isPresent();
    }

    public static String getFormatByExtension(String extension) {
        return Arrays.stream(SupportedImagesEnum.values())
                .filter(supportedImage -> StringUtils.equals(supportedImage.getExtension(), extension))
                .map(SupportedImagesEnum::getFormat).findFirst().orElse(StringUtils.EMPTY);
    }

    public static List<String> getSupportedExtensions() {
        return Arrays.stream(SupportedImagesEnum.values())
                .map(SupportedImagesEnum::getExtension).toList();
    }
}
