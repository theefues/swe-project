package rollingcubes.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CubeTest {

    @Test
    void testRollTo() {
        assertThrows(UnsupportedOperationException.class, () -> Cube.EMPTY.rollTo(Direction.UP));
        assertEquals(Cube.CUBE1, Cube.CUBE1
                .rollTo(Direction.UP)
                .rollTo(Direction.DOWN));
        assertEquals(Cube.CUBE1, Cube.CUBE1
                .rollTo(Direction.RIGHT)
                .rollTo(Direction.LEFT));
        assertEquals(Cube.CUBE1, Cube.CUBE1
                .rollTo(Direction.DOWN)
                .rollTo(Direction.UP));
        assertEquals(Cube.CUBE1, Cube.CUBE1
                .rollTo(Direction.LEFT)
                .rollTo(Direction.RIGHT));
        assertEquals(Cube.CUBE1, Cube.CUBE1
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
        );
    }

}