package twoballspuzzle.game;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;

import twoballspuzzle.model.PuzzleState;
import util.javafx.ImageStorage;
import utilities.ImageLoader;

import javafx.scene.image.ImageView;

public class TwoBallsPuzzleController {
    @FXML
    private GridPane grid;

    @FXML
    private TextField moveCountTextField;

    private PuzzleState state;

    private final IntegerProperty moveCount = new SimpleIntegerProperty(0);

    private ImageStorage<Integer> imageStorage = new ImageLoader(TwoBallsPuzzleController.class, "red-ball.png", "blue-ball.png");

    @FXML
    private void initialize() {
        bindMoveCount();
        restartGame();
    }

    private void restartGame() {
        createState();
        moveCount.set(0);
        createGameBoard();
    }

    private void createGameBoard() {
        grid.getChildren().clear();
        for (var row = 0; row < grid.getRowCount(); row++) {
            for (var column = 0; column < grid.getColumnCount(); column++) {
                var tile = createTilesAndAddImages(column, row);
                grid.add(tile, row, column);
            }
        }
    }

    private StackPane createTilesAndAddImages(int row, int col) {
        var tile = new StackPane();
        tile.getStyleClass().add("tile");
        for (int index = 0; index < 2; index++) {
            ImageView image = loadImageForPiecesOnPosition(index, row, col);
            tile.getChildren().add(image);
        }
        return tile;
    }

    private ImageView loadImageForPiecesOnPosition(int index, int row, int col) {
        ImageView imageView = new ImageView(imageStorage.get(index).orElseThrow());

        BooleanProperty isPieceOnPosition = new SimpleBooleanProperty();
        isPieceOnPosition.bind(Bindings.createBooleanBinding(() -> {
            var positionOfPieces = state.getPosition(index);
            return positionOfPieces.row() == row && positionOfPieces.col() == col;
        }, state.positionProperty(index)));

        imageView.visibleProperty().bind(isPieceOnPosition);
        return imageView;
    }

    private void createState() {
        state = new PuzzleState();
    }

    private void bindMoveCount() {
        moveCountTextField.textProperty().bind(moveCount.asString());
    }
}
