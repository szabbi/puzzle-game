package twoballspuzzle.model;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Pos;
import puzzle.State;

import java.util.Objects;
import java.util.Set;

public class PuzzleState implements State<Direction>{

    private final ReadOnlyObjectWrapper<Position>[] positions;

    public PuzzleState() {
        this(new Position(0, 4),
                new Position(6, 3));
    }

    public PuzzleState(Position... positions) {
        this.positions = new ReadOnlyObjectWrapper[2];
        for (int i = 0; i<2; i++) {
            this.positions[i] = new ReadOnlyObjectWrapper<>(positions[i]);
        }
    }

    public Position getPosition(int index) {
        return positions[index].get();
    }

    public ReadOnlyObjectProperty<Position> positionProperty(int index) {
        return positions[index].getReadOnlyProperty();
    }

    @Override
    public boolean isSolved() {
        return false;
    }

    @Override
    public boolean isLegalMove(Direction direction) {
        return false;
    }

    @Override
    public void makeMove(Direction direction) {
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
