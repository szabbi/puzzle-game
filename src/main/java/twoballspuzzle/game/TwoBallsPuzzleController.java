package twoballspuzzle.game;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import twoballspuzzle.model.Direction;
import twoballspuzzle.model.Position;
import twoballspuzzle.model.PuzzleState;
import util.javafx.ImageStorage;
import utilities.ImageLoader;

import javafx.scene.image.ImageView;

import java.util.Optional;

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
        moveCountTextField.textProperty().bind(moveCount.asString());
        restartGame();
    }

    private void restartGame() {
        createNewState();
        createGameBoard();
        moveCount.set(0);
    }

    private void createGameBoard() {
        grid.getChildren().clear();
        for (var row = 0; row < grid.getRowCount(); row++) {
            for (var column = 0; column < grid.getColumnCount(); column++) {
                var tile = createTilesAndAddImages(column, row);
                tile.setOnMouseClicked(this::onMouseClick);
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


    private void onMouseClick(MouseEvent mouseEvent) {
        var source = (Node) mouseEvent.getSource();
        var row = GridPane.getRowIndex(source);
        var col = GridPane.getColumnIndex(source);

        Position playerPosition = state.getPosition(PuzzleState.RED_BALL);
        Direction directionToMove = Direction.of(row - playerPosition.row(), col - playerPosition.col());
        if (Direction.isValidDirection(directionToMove)) {
            movePieces(directionToMove);
        }
    }

    private void movePieces(Direction direction) {
        state.makeMove(direction);
        moveCount.set(moveCount.get() + 1);
    }

    private void createNewState() {
        state = new PuzzleState();
        state.solvedProperty().addListener(this::checkIfSolved);
    }

    private void checkIfSolved(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
        if(newValue) {
            Platform.runLater(this::solvedDialog);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void solvedDialog() {
        Dialog dialog = new Dialog();
        dialog.setTitle("Game Over");
        dialog.setContentText("You have solved the puzzle!");

        ButtonType buttonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonType);

        dialog.setOnCloseRequest(dialogEvent -> System.exit(0));

        dialog.showAndWait();
    }

}
