package twoballspuzzle.model;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Pos;
import puzzle.State;

import java.util.Objects;
import java.util.Set;

public class PuzzleState implements State<Direction>{

    private final ReadOnlyObjectWrapper<Position>[] positions;

    public static final int RED_BALL = 0;
    private static final int BLUE_BALL = 1;

    private final ReadOnlyBooleanWrapper isPuzzleSolved;


    public PuzzleState() {
        this(new Position(0, 4),
                new Position(6, 2));
    }

    public PuzzleState(Position... positions) {
        this.positions = new ReadOnlyObjectWrapper[2];
        for (int i = 0; i < 2; i++) {
            this.positions[i] = new ReadOnlyObjectWrapper<>(positions[i]);
        }
        isPuzzleSolved = new ReadOnlyBooleanWrapper();
        isPuzzleSolved.bind(this.positions[RED_BALL].isEqualTo(this.positions[BLUE_BALL]));
    }

    public Position getPosition(int index) {
        return positions[index].get();
    }

    public ReadOnlyObjectProperty<Position> positionProperty(int index) {
        return positions[index].getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty solvedProperty() {
        return isPuzzleSolved.getReadOnlyProperty();
    }

    @Override
    public boolean isSolved() {
        return isPuzzleSolved.get();
    }

    @Override
    public boolean isLegalMove(Direction direction) {
        return false;
    }

    @Override
    public void makeMove(Direction direction) {
        switch (direction) {
            case UP -> {
                movePiece(RED_BALL, Direction.UP);
                movePiece(BLUE_BALL, Direction.DOWN);
            }
            case DOWN -> {
                movePiece(RED_BALL, Direction.DOWN);
                movePiece(BLUE_BALL, Direction.UP);
            }
            case LEFT -> {
                movePiece(RED_BALL, Direction.LEFT);
                movePiece(BLUE_BALL, Direction.RIGHT);
            }
            case RIGHT -> {
                movePiece(RED_BALL, Direction.RIGHT);
                movePiece(BLUE_BALL, Direction.LEFT);
            }
        }
    }

    private void movePiece(int index, Direction direction) {
        var newPosition = getPosition(index).move(direction);
        System.out.println(String.format("piece [%s] moves to [%s] from [%s] - (%s)", index, newPosition, getPosition(index), direction));
        positions[index].set(newPosition);
    }

    @Override
    public Set<Direction> getLegalMoves() {
        return null;
    }

    @Override
    public State<Direction> clone() {
        return null;
    }
}
