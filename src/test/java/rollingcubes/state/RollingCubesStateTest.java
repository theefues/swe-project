package rollingcubes.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class RollingCubesStateTest {

    private void assertEmptySpace(int expectedEmptyRow, int expectedEmptyCol, RollingCubesState state) {
        assertAll(
                () -> assertEquals(expectedEmptyRow, state.getEmptyRow()),
                () -> assertEquals(expectedEmptyCol, state.getEmptyCol())
        );
    }

    @Test
    void testRollingCubesStateByteArrayArrayInvalidArgument() {
        assertThrows(IllegalArgumentException.class, () -> new RollingCubesState(null));
        assertThrows(IllegalArgumentException.class, () -> new RollingCubesState(new int[][] {
                {1, 1},
                {1, 0}})
        );
        assertThrows(IllegalArgumentException.class, () -> new RollingCubesState(new int[][] {
                {0},
                {1, 2},
                {3, 4, 5}})
        );
        assertThrows(IllegalArgumentException.class, () -> new RollingCubesState(new int[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}})
        );
        assertThrows(IllegalArgumentException.class, () -> new RollingCubesState(new int[][] {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}})
        );
        assertThrows(IllegalArgumentException.class, () -> new RollingCubesState(new int[][] {
                {0, 1, 1},
                {1, 1, 1},
                {1, 1, 0}})
        );
    }

    @Test
    void testRollingCubesStateByteArrayArrayValidArgument() {
        int[][] a = new int[][] {
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        };
        RollingCubesState state = new RollingCubesState(a);
        assertArrayEquals(new Cube[][] {
                {Cube.CUBE1, Cube.CUBE1, Cube.CUBE1},
                {Cube.CUBE1, Cube.EMPTY, Cube.CUBE1},
                {Cube.CUBE1, Cube.CUBE1, Cube.CUBE1}
        }, state.getTray());
        assertEmptySpace(1, 1, state);
    }

    @Test
    void testIsSolved() {
        assertFalse(new RollingCubesState().isSolved());
        assertTrue(new RollingCubesState(new int[][] {
                {6, 6, 6},
                {6, 0, 6},
                {6, 6, 6}}).isSolved());
    }

    @Test
    void testCanRollToEmptySpace() {
        RollingCubesState state = new RollingCubesState();
        assertEmptySpace(1, 1, state);
        assertFalse(state.canRollToEmptySpace(1, 1));
        assertFalse(state.canRollToEmptySpace(0, 0));
        assertFalse(state.canRollToEmptySpace(0, 2));
        assertFalse(state.canRollToEmptySpace(2, 0));
        assertFalse(state.canRollToEmptySpace(2, 2));
        assertTrue(state.canRollToEmptySpace(0, 1));
        assertTrue(state.canRollToEmptySpace(1, 0));
        assertTrue(state.canRollToEmptySpace(1, 2));
        assertTrue(state.canRollToEmptySpace(2, 1));
    }

    @Test
    void testGetRollDirection() {
        RollingCubesState state = new RollingCubesState();
        assertEmptySpace(1, 1, state);
        assertEquals(Direction.UP, state.getRollDirection(2, 1));
        assertEquals(Direction.RIGHT, state.getRollDirection(1, 0));
        assertEquals(Direction.DOWN, state.getRollDirection(0, 1));
        assertEquals(Direction.LEFT, state.getRollDirection(1, 2));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(1, 1));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(0, 0));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(0, 2));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(2, 0));
        assertThrows(IllegalArgumentException.class, () -> state.getRollDirection(2, 2));
    }

    @Test
    void testRollToEmptySpace() {
        RollingCubesState state = new RollingCubesState();
        assertEmptySpace(1, 1, state);
        Cube cube = state.getTray()[0][1];
        state.rollToEmptySpace(0, 1);
        assertEmptySpace(0, 1, state);
        assertEquals(cube.rollTo(Direction.DOWN), state.getTray()[1][1]);
        state.rollToEmptySpace(1, 1);
        assertEmptySpace(1, 1, state);
        assertEquals(cube, state.getTray()[0][1]);
    }

}
