package twoballspuzzle.model;


public record Position(int row, int col) {

    public Position move(Direction dir) {
        return new Position(row + dir.getRowIndex(), col + dir.getColIndex());
    }
}
