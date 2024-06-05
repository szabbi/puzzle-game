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
        var redUp = getPosition(RED_BALL).move(Direction.UP);
        var blueDown = getPosition(BLUE_BALL).move(Direction.DOWN);
        boolean canRedMove = (isRedNotOnSamePositionAsObstacle(OBSTACLES[0]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[2]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[3])
                && isRedNotOnSamePositionAsObstacle(OBSTACLES[5]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[7]));
        boolean canBlueMove = (!blueDown.equals(getPosition(OBSTACLES[3])) && !blueDown.equals(getPosition(OBSTACLES[5])) && !blueDown.equals(getPosition(OBSTACLES[2])));

        if (canRedMove && canBlueMove && isEmpty(redUp)) {
            return true;
        }
        return redUp.equals(getPosition(OBSTACLES[5])) || redUp.equals(getPosition(OBSTACLES[3]));
    }

    private boolean isMovingDownPossible() {
        var redDown = getPosition(RED_BALL).move(Direction.DOWN);
        boolean canRedMove = (isRedNotOnSamePositionAsObstacle(OBSTACLES[0]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[1]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[2])
                && isRedNotOnSamePositionAsObstacle(OBSTACLES[4]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[7]));
        boolean canBlueMove = (isBlueNotOnSamePositionAsObstacle(OBSTACLES[3]) && isBlueNotOnSamePositionAsObstacle(OBSTACLES[2]) && isBlueNotOnSamePositionAsObstacle(OBSTACLES[5]));

        if (canRedMove && canBlueMove && isEmpty(redDown)) {
            return true;
        }
        return redDown.equals(getPosition(OBSTACLES[1])) || redDown.equals(getPosition(OBSTACLES[4])) || redDown.equals(getPosition(OBSTACLES[6]));
    }

    private boolean isMovingLeftPossible() {
        var redLeft = getPosition(RED_BALL).move(Direction.LEFT);
        var blueRight = getPosition(BLUE_BALL).move(Direction.RIGHT);
        boolean canRedMove = (isRedNotOnSamePositionAsObstacle(OBSTACLES[0]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[1]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[2])
                && isRedNotOnSamePositionAsObstacle(OBSTACLES[4]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[5]) && isRedNotOnSamePositionAsObstacle(OBSTACLES[6])
                && isRedNotOnSamePositionAsObstacle(OBSTACLES[7]));

        if (canRedMove && isEmpty(blueRight) && isEmpty(redLeft)) {
            return true;
        }
        return redLeft.equals(getPosition(OBSTACLES[0])) || redLeft.equals(getPosition(OBSTACLES[1])) || redLeft.equals(getPosition(OBSTACLES[2]))
                || redLeft.equals(getPosition(OBSTACLES[3])) || redLeft.equals(getPosition(OBSTACLES[4])) || redLeft.equals(getPosition(OBSTACLES[6]))
                || redLeft.equals(getPosition(OBSTACLES[7])) || blueRight.equals((getPosition(OBSTACLES[3])));
    }

    private boolean isMovingRightPossible() {
        var redRight = getPosition(RED_BALL).move(Direction.RIGHT);
        boolean canBlueMove = (isBlueNotOnSamePositionAsObstacle(OBSTACLES[0]) && isBlueNotOnSamePositionAsObstacle(OBSTACLES[1]) && isBlueNotOnSamePositionAsObstacle(OBSTACLES[2])
                && isBlueNotOnSamePositionAsObstacle(OBSTACLES[4]) && isBlueNotOnSamePositionAsObstacle(OBSTACLES[5]) && isBlueNotOnSamePositionAsObstacle(OBSTACLES[6])
                && isBlueNotOnSamePositionAsObstacle(OBSTACLES[7]));

        if (canBlueMove && isEmpty(redRight)) {
            return true;
        }
        return redRight.equals(getPosition(OBSTACLES[3]));
    }

    private boolean isRedNotOnSamePositionAsObstacle(int obstacleIndex) {
        return !getPosition(RED_BALL).equals(getPosition(obstacleIndex));
    }

    private boolean isBlueNotOnSamePositionAsObstacle(int obstacleIndex) {
        return !getPosition(BLUE_BALL).equals(getPosition(obstacleIndex));
    }

    private boolean isEmpty(Position pos) {
        for (var p : positions) {
            if (p.get().equals(pos)) {
                return false;
            }
        }
        return true;
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
