package queen.state;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TableTest {

    private boolean hasQueen(Table table, int index) {
        for (int i = 0; i < table.getTableSize(); i++)
            for (int j = 0; j < table.getTableSize(); j++)
                if (table.get(i, j) == index)
                    return true;
        return false;
    }

    @Test
    public void testAddRandomQueen(){
        Table table = new Table(8);
        table.addRandomQueen(1);

        assertTrue(hasQueen(table, 1));

        assertFalse(hasQueen(table, 3));
        table.addRandomQueen(3);
        assertTrue(hasQueen(table, 3));
    }

    @Test
    public void testCanMoveQueenTo() {
        Table table = new Table(8);
        table.set(3, 7, 2);

        assertTrue(table.canMoveQueenTo(3, 6, 2) == Direction.LEFT);
        assertTrue(table.canMoveQueenTo(4, 7, 2) == Direction.DOWN);
        assertTrue(table.canMoveQueenTo(4, 6, 2) == Direction.BOTH);
        assertTrue(table.canMoveQueenTo(2, 3, 2) == Direction.NONE);
    }

    @Test
    public void testTryMoveQueenTo() {
        Table table = new Table(8);
        table.set(0, 2, 1);
        table.set(3, 7, 2);

        assertTrue(table.tryMoveQueenTo(0, 1, 1));
        assertTrue(table.get(0, 1) == 1);

        assertFalse(table.tryMoveQueenTo(0, 1, 1));
        assertTrue(table.get(0, 1) == 1);

        assertTrue(table.tryMoveQueenTo(1, 1, 1));
        assertTrue(table.get(1, 1) == 1);

        assertTrue(table.tryMoveQueenTo(2, 0, 1));
        assertTrue(table.get(2, 0) == 1);
    }
}
