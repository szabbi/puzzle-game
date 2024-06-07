package twoballspuzzle.model;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleStateTest {

    PuzzleState originalState = new PuzzleState();
    PuzzleState solvedState = new PuzzleState(
            new Position(3, 3),
            new Position(3, 3),
            new Position(1,3),
            new Position(2,2),
            new Position(3,4),
            new Position(4,1),
            new Position(4,3),
            new Position(5,6),
            new Position(6,1),
            new Position(6,3)
    );

    PuzzleState stateAfterDownMove = new PuzzleState(
            new Position(1, 4),
            new Position(5, 2),
            new Position(1,3),
            new Position(2,2),
            new Position(3,4),
            new Position(4,1),
            new Position(4,3),
            new Position(5,6),
            new Position(6,1),
            new Position(6,3)
        );

    @Test
    void isSolved() {
        assertFalse(originalState.isSolved());
        assertTrue(solvedState.isSolved());
    }

    @Test
    void isLegalMove() {
        assertTrue(originalState.isLegalMove(Direction.DOWN));
        assertTrue(originalState.isLegalMove(Direction.RIGHT));
        assertFalse(originalState.isLegalMove(Direction.UP));
        assertFalse(originalState.isLegalMove(Direction.LEFT));

        assertTrue(solvedState.isLegalMove(Direction.UP));
        assertTrue(solvedState.isLegalMove(Direction.DOWN));
        assertFalse(solvedState.isLegalMove(Direction.LEFT));
        assertFalse(solvedState.isLegalMove(Direction.RIGHT));
    }

    @Test
    void makeMove() {
        assertEquals(originalState.getPosition(PuzzleState.RED_BALL).move(Direction.DOWN), stateAfterDownMove.getPosition(PuzzleState.RED_BALL));
        assertNotEquals(originalState.getPosition(PuzzleState.RED_BALL).move(Direction.UP), stateAfterDownMove.getPosition(PuzzleState.RED_BALL));
    }

    @Test
    void getLegalMoves() {
        assertEquals(Set.of(Direction.RIGHT, Direction.DOWN), originalState.getLegalMoves());
        assertEquals(Set.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT), stateAfterDownMove.getLegalMoves());
        assertEquals(Set.of(Direction.UP, Direction.DOWN), solvedState.getLegalMoves());

        assertNotEquals(Set.of(Direction.UP, Direction.DOWN), originalState.getLegalMoves());
        assertNotEquals(Set.of(Direction.UP, Direction.DOWN), stateAfterDownMove.getLegalMoves());
        assertNotEquals(Set.of(Direction.RIGHT, Direction.LEFT), solvedState.getLegalMoves());
    }

    @Test
    void testClone() {
        var clone = originalState.clone();

        assertEquals(clone, originalState);
        assertNotEquals(originalState, solvedState.clone());
    }

    @Test
    void testEquals() {
        assertTrue(originalState.equals(originalState));
        assertEquals(solvedState, solvedState);
        assertFalse(originalState.equals(solvedState));
        assertFalse(originalState.equals(solvedState.clone()));
        assertFalse(solvedState.equals((originalState)));
        assertFalse(solvedState.equals(originalState.clone()));
    }

    @Test
    void testHashCode() {
        assertEquals(originalState.hashCode(), originalState.clone().hashCode());
        assertNotEquals(originalState.hashCode(), solvedState.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("(0, 4) (6, 2)", originalState.toString());
        assertEquals("(3, 3) (3, 3)", solvedState.toString());
        assertNotEquals("(-1, 2)(6, 2)", originalState.toString());
        assertNotEquals("0, 4 - 6, 2", solvedState.toString());
    }
}