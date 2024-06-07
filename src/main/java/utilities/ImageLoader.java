package utilities;

import java.util.Optional;

import javafx.scene.image.Image;
import twoballspuzzle.game.TwoBallsPuzzleApp;
import util.javafx.ImageStorage;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


public class ImageLoader implements ImageStorage<Integer> {

    private static final Logger logger = LogManager.getLogger(ImageLoader.class);

    private final Image[] images;


    public ImageLoader(Class<?> className, String... imageNames) {
        images = new Image[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            var imagePath = String.format("%s/%s", className.getPackageName().replace(".", "/"), imageNames[i]);
            try {
                images[i] = new Image(imagePath);
                logger.debug("Image loaded: {}", imagePath);
            } catch (Exception e) {
                logger.error("Image failed to load: {}", imagePath);
            }
        }
    }

    @Override
    public Optional<Image> get(Integer integer) {
        return Optional.of(images[integer]);
    }
}
