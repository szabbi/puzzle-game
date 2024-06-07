package twoballspuzzle.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;

import puzzle.State;


/**
 * Stores a state of the puzzle.
 */
public class PuzzleState implements State<Direction> {


    private final ReadOnlyObjectWrapper<Position>[] positions;

    /**
     * Red ball's index.
     */
    public static final int RED_BALL = 0;

    /**
     * Blue ball's index.
     */
    private static final int BLUE_BALL = 1;

    /**
     * Obstacles' indexes.
     */
    private static final int[] OBSTACLES = new int[8];

    private final ReadOnlyBooleanWrapper isPuzzleSolved;


    /**
     *  Makes an object which is the original/base state of the puzzle that stores the positions of each piece.
     */
    public PuzzleState() {
        this(new Position(0, 4),
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

    /**
     *  Initializes the specified {@code positions} of the pieces and makes a new object.
     *
     * @param positions the positions of the pieces
     */
    public PuzzleState(Position... positions) {
        this.positions = new ReadOnlyObjectWrapper[positions.length];

        for (int i = 0; i < positions.length; i++) {
            this.positions[i] = new ReadOnlyObjectWrapper<>(positions[i]);
            if (i > 1) {
                OBSTACLES[i - 2] = i;
            }
        }

        // Binds isPuzzleSolved to be only true when the two balls positions are equal.
        isPuzzleSolved = new ReadOnlyBooleanWrapper();
        isPuzzleSolved.bind(this.positions[RED_BALL].isEqualTo(this.positions[BLUE_BALL]));
    }

    /**
     *
     * @param index the number corresponding to a piece
     * @return the position of the given piece
     */
    public Position getPosition(int index) {
        return positions[index].get();
    }

    public ReadOnlyObjectProperty<Position> positionProperty(int index) {
        return positions[index].getReadOnlyProperty();
    }

    public ReadOnlyBooleanProperty solvedProperty() {
        return isPuzzleSolved.getReadOnlyProperty();
    }

    /**
     *
     * @return true/false based on if the puzzle has been solved or not
     */
    @Override
    public boolean isSolved() {
        return isPuzzleSolved.get();
    }

    /**
     *
     * @param direction where the red ball should be moved
     * @return whether movement is possible to the given {@code direction}
     */
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
                && isPositionEmpty(tileAboveRed) && getPosition(RED_BALL).row() > 0
                && getPosition(BLUE_BALL).row() < 6) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileAboveRed, 3, 5);
    }

    private boolean isMovingDownPossible() {
        var tileBelowRed = getPosition(RED_BALL).move(Direction.DOWN);

        if (isRedNotOnSamePositionAsObstacle(0, 1, 2, 4, 7)
                && isBlueNotOnSamePositionAsObstacle(2, 3, 5)
                && isPositionEmpty(tileBelowRed) && getPosition(RED_BALL).row() < 6
                && getPosition(BLUE_BALL).row() > 0) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileBelowRed, 1, 4, 6);
    }

    private boolean isMovingLeftPossible() {
        var tileLeftToRed = getPosition(RED_BALL).move(Direction.LEFT);
        var tileRightToBlue = getPosition(BLUE_BALL).move(Direction.RIGHT);

        if (isRedNotOnSamePositionAsObstacle(0, 1, 2, 4, 5, 6, 7)
                && isPositionEmpty(tileRightToBlue) && isPositionEmpty(tileLeftToRed)
                && getPosition(RED_BALL).col() > 0 && getPosition(BLUE_BALL).col() < 6) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileLeftToRed, 0, 1, 2, 3, 4, 6, 7)
                || tileRightToBlue.equals((getPosition(OBSTACLES[3])));
    }

    private boolean isMovingRightPossible() {
        var tileRightToRed = getPosition(RED_BALL).move(Direction.RIGHT);

        if (isBlueNotOnSamePositionAsObstacle(0, 1, 2, 4, 5, 6, 7)
                && isPositionEmpty(tileRightToRed) && getPosition(RED_BALL).col() < 6
                && getPosition(BLUE_BALL).col() > 0) {
            return true;
        }
        return tileRightToRed.equals(getPosition(OBSTACLES[3]));
    }

    private boolean isRedNotOnSamePositionAsObstacle(int... obstacleIndex) {
        for (int index : obstacleIndex) {
            if (getPosition(RED_BALL).equals(getPosition(OBSTACLES[index]))) {
                return false;
            }
        }
        return true;
    }

    private boolean isBlueNotOnSamePositionAsObstacle(int... obstacleIndex) {
        for (int index : obstacleIndex) {
            if (getPosition(BLUE_BALL).equals(getPosition(OBSTACLES[index]))) {
                return false;
            }
        }
        return true;
    }

    private boolean isPositionEmpty(Position pos) {
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

    /**
     *
     * @param direction where the red ball will be moved
     */
    @Override
    public void makeMove(Direction direction) {
        switch (direction) {
            case UP -> moveBalls(Direction.UP);
            case DOWN -> moveBalls(Direction.DOWN);
            case LEFT -> moveBalls(Direction.LEFT);
            case RIGHT -> moveBalls(Direction.RIGHT);
        }
    }

    private void moveBalls(Direction direction) {
        Position newRedBallPosition = getPosition(RED_BALL).move(direction);
        Position newBlueBallPosition = getPosition(BLUE_BALL).move(Direction.getInverseOf(direction));

        positions[RED_BALL].set(newRedBallPosition);
        positions[BLUE_BALL].set(newBlueBallPosition);
    }

    /**
     * @return a set of legal moves/ where movement is possible
     */
    @Override
    public Set<Direction> getLegalMoves() {
        Set<Direction> legalMoves = new HashSet<>();
        for (Direction dir : Direction.values()) {
            if (isLegalMove(dir)) {
                legalMoves.add(dir);
            }
        }
        return legalMoves;
    }

    @Override
    public State<Direction> clone() {
        return new PuzzleState(getPosition(RED_BALL), getPosition(BLUE_BALL),
                getPosition(OBSTACLES[0]), getPosition(OBSTACLES[1]),
                getPosition(OBSTACLES[2]), getPosition(OBSTACLES[3]),
                getPosition(OBSTACLES[4]), getPosition(OBSTACLES[5]),
                getPosition(OBSTACLES[6]), getPosition(OBSTACLES[7]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PuzzleState other)) return false;

        return getPosition(RED_BALL).equals(other.getPosition(RED_BALL))
                && getPosition(BLUE_BALL).equals(other.getPosition(BLUE_BALL))
                && getPosition(OBSTACLES[0]).equals(other.getPosition(OBSTACLES[0]))
                && getPosition(OBSTACLES[1]).equals(other.getPosition(OBSTACLES[1]))
                && getPosition(OBSTACLES[2]).equals(other.getPosition(OBSTACLES[2]))
                && getPosition(OBSTACLES[3]).equals(other.getPosition(OBSTACLES[3]))
                && getPosition(OBSTACLES[4]).equals(other.getPosition(OBSTACLES[4]))
                && getPosition(OBSTACLES[5]).equals(other.getPosition(OBSTACLES[5]))
                && getPosition(OBSTACLES[6]).equals(other.getPosition(OBSTACLES[6]))
                && getPosition(OBSTACLES[7]).equals(other.getPosition(OBSTACLES[7]));
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getPosition(RED_BALL), getPosition(BLUE_BALL),
                getPosition(OBSTACLES[0]), getPosition(OBSTACLES[1]), getPosition(OBSTACLES[2]),
                getPosition(OBSTACLES[3]), getPosition(OBSTACLES[4]), getPosition(OBSTACLES[5]),
                getPosition(OBSTACLES[6]), getPosition(OBSTACLES[7]));

        return result;
    }

    @Override
    public String toString() {
        StringJoiner toString = new StringJoiner(" ", "", "");
        int index = 0;
        for (var position : positions) {
            if (index < 2) {
                toString.add(position.get().toString());
            } else break;
            index++;
        }
        return toString.toString();
    }
}
