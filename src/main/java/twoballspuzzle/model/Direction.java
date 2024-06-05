package twoballspuzzle.model;

import lombok.Getter;

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

    public static Boolean isValidDirection(Direction dir) {
        return dir.equals(UP) || dir.equals(DOWN) || dir.equals(LEFT) || dir.equals(RIGHT);
    }

    public static Direction of(int rowIndex, int colIndex) {
        for (var direction : values()) {
            if (direction.rowIndex == rowIndex && direction.colIndex == colIndex) {
                return direction;
            }
        }
        throw new RuntimeException();
    }
}
