package utilities;

import java.util.Optional;

import javafx.scene.image.Image;
import util.javafx.ImageStorage;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * Loads images from resources, labels them with numbers
 * starting from 0 and stores them in the {@code images} array.
 */
public class ImageLoader implements ImageStorage<Integer> {

    private static final Logger LOGGER = LogManager.getLogger(ImageLoader.class);

    private final Image[] images;

    /**
     *
     * @param className the classloader which will be used
     * @param imageNames the names of the images to be loaded
     */
    public ImageLoader(Class<?> className, String... imageNames) {
        images = new Image[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            var imagePath = String.format("%s/%s",
                    className.getPackageName().replace(".", "/"), imageNames[i]);
            try {
                images[i] = new Image(imagePath);
                LOGGER.debug("Image loaded: {}", imagePath);
            } catch (Exception e) {
                LOGGER.error("Image failed to load: {}", imagePath);
            }
        }
    }

    @Override
    public Optional<Image> get(Integer integer) {
        return Optional.of(images[integer]);
    }
}
