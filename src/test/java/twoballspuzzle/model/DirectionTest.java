package twoballspuzzle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void getInverseOf() {
        assertSame(Direction.UP, Direction.getInverseOf(Direction.DOWN));
        assertSame(Direction.DOWN, Direction.getInverseOf(Direction.UP));
        assertSame(Direction.LEFT, Direction.getInverseOf(Direction.RIGHT));
        assertSame(Direction.RIGHT, Direction.getInverseOf(Direction.LEFT));
    }

    @Test
    void of() {
        assertSame(Direction.UP, Direction.of(-1, 0));
        assertSame(Direction.DOWN, Direction.of(1, 0));
        assertSame(Direction.LEFT, Direction.of(0, -1));
        assertSame(Direction.RIGHT, Direction.of(0, 1));
    }
}