package twoballspuzzle.model;

import java.util.*;

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
                && isEmpty(tileAboveRed) && getPosition(RED_BALL).row() > 0 && getPosition(BLUE_BALL).row() < 6) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileAboveRed, 3, 5);
    }

    private boolean isMovingDownPossible() {
        var tileBelowRed = getPosition(RED_BALL).move(Direction.DOWN);

        if (isRedNotOnSamePositionAsObstacle(0, 1, 2, 4, 7)
                && isBlueNotOnSamePositionAsObstacle(2, 3, 5)
                && isEmpty(tileBelowRed) && getPosition(RED_BALL).row() < 6 && getPosition(BLUE_BALL).row() > 0) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileBelowRed, 1, 4, 6);
    }

    private boolean isMovingLeftPossible() {
        var tileLeftToRed = getPosition(RED_BALL).move(Direction.LEFT);
        var tileRightToBlue = getPosition(BLUE_BALL).move(Direction.RIGHT);

        if (isRedNotOnSamePositionAsObstacle(0, 1, 2, 4, 5, 6, 7) && isEmpty(tileRightToBlue) && isEmpty(tileLeftToRed)
                && getPosition(RED_BALL).col() > 0 && getPosition(BLUE_BALL).col() < 6) {
            return true;
        }
        return isNotEmptyButAllowedToMove(tileLeftToRed, 0, 1, 2, 3, 4, 6, 7) || tileRightToBlue.equals((getPosition(OBSTACLES[3])));
    }

    private boolean isMovingRightPossible() {
        var tileRightToRed = getPosition(RED_BALL).move(Direction.RIGHT);

        if (isBlueNotOnSamePositionAsObstacle(0, 1, 2, 4, 5, 6, 7) && isEmpty(tileRightToRed)
                && getPosition(RED_BALL).col() < 6 && getPosition(BLUE_BALL).col() > 0) {
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
            case UP -> moveBalls(Direction.UP);
            case DOWN -> moveBalls(Direction.DOWN);
            case LEFT -> moveBalls(Direction.LEFT);
            case RIGHT -> moveBalls(Direction.RIGHT);
        }
    }

    private void moveBalls(Direction direction) {
        Position newRedBallPosition = getPosition(RED_BALL).move(direction);
        Position newBlueBallPosition = getPosition(BLUE_BALL).move(Direction.getInverse(direction));
        positions[RED_BALL].set(newRedBallPosition);
        positions[BLUE_BALL].set(newBlueBallPosition);
    }

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
        return new PuzzleState(getPosition(RED_BALL), getPosition(BLUE_BALL), getPosition(OBSTACLES[0]), getPosition(OBSTACLES[1]),
                getPosition(OBSTACLES[2]), getPosition(OBSTACLES[3]), getPosition(OBSTACLES[4]), getPosition(OBSTACLES[5]),
                getPosition(OBSTACLES[6]), getPosition(OBSTACLES[7]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        return (o instanceof PuzzleState other)
                && getPosition(RED_BALL).equals(other.getPosition(RED_BALL)) && getPosition(BLUE_BALL).equals(other.getPosition(BLUE_BALL))
                && getPosition(OBSTACLES[0]).equals(other.getPosition(OBSTACLES[0])) && getPosition(OBSTACLES[1]).equals(other.getPosition(OBSTACLES[1]))
                && getPosition(OBSTACLES[2]).equals(other.getPosition(OBSTACLES[2])) && getPosition(OBSTACLES[3]).equals(other.getPosition(OBSTACLES[3]))
                && getPosition(OBSTACLES[4]).equals(other.getPosition(OBSTACLES[4])) && getPosition(OBSTACLES[5]).equals(other.getPosition(OBSTACLES[5]))
                && getPosition(OBSTACLES[6]).equals(other.getPosition(OBSTACLES[6])) && getPosition(OBSTACLES[7]).equals(other.getPosition(OBSTACLES[7]));
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getPosition(RED_BALL), getPosition(BLUE_BALL), getPosition(OBSTACLES[0]),
                getPosition(OBSTACLES[1]), getPosition(OBSTACLES[2]), getPosition(OBSTACLES[3]), getPosition(OBSTACLES[4]),
                getPosition(OBSTACLES[5]), getPosition(OBSTACLES[6]), getPosition(OBSTACLES[7]));
        return result;
    }

    @Override
    public String toString() {
        StringJoiner toString = new StringJoiner(" ", "", "");
        int index = 0;
        for (var position : positions){
            if (index < 2) {
                toString.add(position.get().toString());
            }
            else break;
            index++;
        }
        return toString.toString();
    }
}
