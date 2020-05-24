package queen.state;

import java.util.Random;

public class Table {
    private int n;
    private int[][] table;
    private Random rand;
    private int currentIndex;

    /**
     * Create table and add 2 random Queens to the board.
     * @param n Height and width of the grid
     */
    public Table (int n) {
        this.n = n;
        this.table = new int[n][n];
        this.rand = new Random();
        this.currentIndex = 0;

        this.addRandomQueen(1);
        this.addRandomQueen(2);
    }

    /**
     * Returns the table's data from x and y coordinates.
     * @param x X coordinate of the desired item
     * @param y Y coordinate of the desired item
     * @return Desired item
     */
    public int get(int x, int y) {
        return this.table[x][y];
    }

    /**
     * Sets the table's data from x and y coordinates.
     * @param x X coordinate of the desired item
     * @param y Y coordinate of the desired item
     * @return Desired item
     */
    public void set(int x, int y, int index) {
        this.table[x][y] = index;
    }

    /**
     * Sets the given queen's id.
     * @param index ID for the queen
     */
    public void setCurrent(int index) {
        this.currentIndex = index;
    }
    /**
     * Gets current queen's id.
     * @return ID of the current queen
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * Returns table size
     * @return Table size
     */
    public int getTableSize() {
        return this.n;
    }

    /**
     * Check if the bottom left grid contains a queen or not
     * @return
     */
    public boolean isSolved() {
        return this.get(this.n - 1, 0) != 0;
    }

    /**
     * Get the index of the winner queen.
     * @return ID of the queen
     */
    public int getWinnerIndex() {
        return (isSolved() ? get(getTableSize() - 1, 0) : getCurrentIndex());
    }

    /**
     * Add queen to a random position.
     * @param index ID of the queen
     */
    public void addRandomQueen(int index) {
        int x = this.rand.nextInt(this.n);
        boolean side = this.rand.nextInt(2) == 1;
        if (side) {
            while (this.table[0][x] != 0)
                x = this.rand.nextInt(this.n);
            this.table[0][x] = index;
        } else {
            while (this.table[x][n - 1] != 0)
                x = this.rand.nextInt(this.n);
            this.table[x][n - 1] = index;
        }
    }

    /**
     * Check if queen can move to the given field.
     * @param x X coordinate of the field
     * @param y Y coordinate of the field
     * @param index ID of the queen
     * @return Direction
     */
    public Direction canMoveQueenTo(int x, int y, int index) {
        if (this.table[x][y] != 0)
            return Direction.NONE;

        // Try move left
        if (y + 1 < this.n && this.table[x][y + 1] == index)
            return Direction.LEFT;

        // Try move down
        if (x > 0 && this.table[x - 1][y] == index)
            return Direction.DOWN;

        // Try move one left and one down
        if (y + 1 < this.n && x > 0 && this.table[x - 1][y + 1] == index)
            return Direction.BOTH;

        return Direction.NONE;
    }

    /**
     * Try to move queen to the given field.
     * @param x X coordinate of the field
     * @param y Y coordinate of the field
     * @param index ID of the queen
     * @return Boolean depends on the queen can be moved or not
     */
    public boolean tryMoveQueenTo(int x, int y, int index) {
        Direction dir = canMoveQueenTo(x, y, index);

        if (dir == Direction.NONE)
            return false;

        switch (dir) {
            case LEFT:
                this.table[x][y + 1] = 0;
                this.table[x][y] = index;
                break;
            case DOWN:
                this.table[x - 1][y] = 0;
                this.table[x][y] = index;
                break;
            case BOTH:
                this.table[x - 1][y + 1] = 0;
                this.table[x][y] = index;
                break;
        }

        return true;
    }

}
