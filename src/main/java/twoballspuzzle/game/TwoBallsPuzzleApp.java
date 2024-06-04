package twoballspuzzle.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class TwoBallsPuzzleApp extends Application {

    @Override
    public void start(Stage stage) {
        Parent root = null;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/twoballspuzzleUI.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("A puzzle with two colorful balls");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
