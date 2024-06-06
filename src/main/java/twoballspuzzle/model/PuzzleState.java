package twoballspuzzle.model;

import java.util.Set;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import puzzle.State;


public class PuzzleState implements State<Direction>{

    private final ReadOnlyObjectWrapper<Position>[] positions;

    public static final int RED_BALL = 0;

    private static final int BLUE_BALL = 1;

    private static final int[] OBSTACLES = new int[8];

    private final ReadOnlyBooleanWrapper isPuzzleSolved;


    public PuzzleState() {
        this(   new Position(0, 4),
                new Position(6, 2),
                new Position(1,3),
                new Position(2,2),
                new Position(3,4),
                new Position(4,1),
                new Position(4,3),
                new Position(5,6),
                new Position(6,1),
                new Position(6,3)
        );
    }

    public PuzzleState(Position... positions) {
        this.positions = new ReadOnlyObjectWrapper[positions.length];
        for (int i = 0; i < positions.length; i++) {
            this.positions[i] = new ReadOnlyObjectWrapper<>(positions[i]);
            if (i > 1) {
                OBSTACLES[i-2] = i;
            }
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
        return switch (direction) {
            case UP -> isMovingUpPossible();
            case DOWN -> isMovingDownPossible();
            case LEFT -> isMovingLeftPossible();
            case RIGHT -> isMovingRightPossible();
        };
    }

    private boolean isMovingUpPossible() {
        var tileAboveRed = getPosition(RED_BALL).move(Direction.UP);
        var tileAboveBlue = getPosition(BLUE_BALL).move(Direction.DOWN);

        if (isRedNotOnSamePositionAsObstacle(0, 2, 3, 5, 7)
                && !isNotEmptyButAllowedToMove(tileAboveBlue, 2, 3, 5)
                && isEmpty(tileAboveRed)) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileAboveRed, 3, 5);
    }

    private boolean isMovingDownPossible() {
        var tileBelowRed = getPosition(RED_BALL).move(Direction.DOWN);

        if (isRedNotOnSamePositionAsObstacle(0, 1, 2, 4, 7)
                && isBlueNotOnSamePositionAsObstacle(2, 3, 5)
                && isEmpty(tileBelowRed)) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileBelowRed, 1, 4, 6);
    }

    private boolean isMovingLeftPossible() {
        var tileLeftToRed = getPosition(RED_BALL).move(Direction.LEFT);
        var tileRightToBlue = getPosition(BLUE_BALL).move(Direction.RIGHT);

        if (isRedNotOnSamePositionAsObstacle(0, 1, 2, 4, 5, 6, 7) && isEmpty(tileRightToBlue) && isEmpty(tileLeftToRed)) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileLeftToRed, 0, 1, 2, 3, 4, 6, 7) || tileRightToBlue.equals((getPosition(OBSTACLES[3])));
    }

    private boolean isMovingRightPossible() {
        var tileRightToRed = getPosition(RED_BALL).move(Direction.RIGHT);

        if (isBlueNotOnSamePositionAsObstacle(0, 1, 2, 4, 5, 6, 7) && isEmpty(tileRightToRed)) {
            return true;
        }
        return tileRightToRed.equals(getPosition(OBSTACLES[3]));
    }

    private boolean isRedNotOnSamePositionAsObstacle(int... obstacleIndex) {
        for (int index : obstacleIndex) {
            if (getPosition(RED_BALL).equals(getPosition(OBSTACLES[index]))) {
                System.out.println(String.format("obstacle %s is in the way", index));
                return false;
            }
        }
        return true;
    }

    private boolean isBlueNotOnSamePositionAsObstacle(int... obstacleIndex) {
        for (int index : obstacleIndex) {
            if (getPosition(BLUE_BALL).equals(getPosition(OBSTACLES[index]))) {
                System.out.println(String.format("obstacle %s is in the way", index));
                return false;
            }
        }
        return true;
    }

    private boolean isEmpty(Position pos) {
        for (var p : positions) {
            if (p.get().equals(pos)) {
                return false;
            }
        }
        return true;
    }

    private boolean isNotEmptyButAllowedToMove(Position pos, int... obstacleIndex) {
        for (int index : obstacleIndex) {
            if (pos.equals(getPosition(OBSTACLES[index]))) {
                return true;
            }
        }
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
        Position newPosition = getPosition(index).move(direction);
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
