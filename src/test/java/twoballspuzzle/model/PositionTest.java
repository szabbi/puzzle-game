package twoballspuzzle.model;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    Position position;

    @BeforeEach
    void init() {position = new Position(0, 0);}

    @Test
    void move() {
        assertEquals(new Position(-1, 0), position.move(Direction.UP));
        assertEquals(new Position(1, 0), position.move(Direction.DOWN));
        assertEquals(new Position(0, -1), position.move(Direction.LEFT));
        assertEquals(new Position(0, 1), position.move(Direction.RIGHT));
    }

    @Test
    void testToString() {
        assertEquals("(0, 0)", position.toString());
    }
}