package utilities;

import javafx.scene.image.Image;
import util.javafx.ImageStorage;

import java.util.Optional;

public class ImageLoader implements ImageStorage<Integer> {

    public Image[] images;

    public ImageLoader(Class<?> className, String... imageNames) {
        images = new Image[imageNames.length];
        for (int i = 0; i < imageNames.length; i++) {
            var imagePath = String.format("%s/%s", className.getPackageName().replace(".", "/"), imageNames[i]);
            try {
                images[i] = new Image(imagePath);
                System.out.println("image loaded from: "+ imagePath);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    @Override
    public Optional<Image> get(Integer integer) {
        return Optional.of(images[integer]);
    }
}
