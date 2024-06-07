package twoballspuzzle.game;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TwoBallsPuzzleApp extends Application {

    private static final Logger LOGGER = LogManager.getLogger(TwoBallsPuzzleApp.class);

    @Override
    public final void start(Stage stage) {
        Parent root = null;

        try {
            root = FXMLLoader.load(Objects.requireNonNull(
                    getClass().getResource("/twoballspuzzleUI.fxml")));
        } catch (IOException e) {
            LOGGER.error("Failed to create the main application window.", e);
        }
        stage.setTitle("A puzzle with two colorful balls");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        LOGGER.debug("UI opened.");

    }
}
