package twoballspuzzle.model;

import lombok.Getter;

/**
 * Stores the 4 directions (UP, DOWN, LEFT, RIGHT) where the player can move.
 */
@Getter
public enum Direction {

    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1);


    private final int rowIndex, colIndex;


    Direction(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    /**
     *
     * @param direction a direction which should be inverted
     * @return a {@code Direction} that is opposite of the given direction
     */
    public static Direction getInverseOf(Direction direction) {
        switch (direction) {
            case UP -> {
                return DOWN;
            }
            case DOWN -> {
                return UP;
            }
            case LEFT -> {
                return RIGHT;
            }
            case RIGHT -> {
                return LEFT;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * @param rowIndex where the row's coordinates will change
     * @param colIndex where the column's coordinates will change
     * @return a {@code Direction} according to the changes
     */
    public static Direction of(int rowIndex, int colIndex) {
        for (var direction : values()) {
            if (direction.rowIndex == rowIndex
                    && direction.colIndex == colIndex) {
                return direction;
            }
        }
        throw new IllegalArgumentException();
    }
}
