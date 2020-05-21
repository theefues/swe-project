package queen.state;

import java.util.Random;

public class Table {
    private int n;
    private int[][] table;
    private Random rand;
    private int currentIndex;

    public Table (int n) {
        this.n = n;
        this.table = new int[n][n];
        this.rand = new Random();
        this.currentIndex = 0;

        this.addRandomQueen(1);
        this.addRandomQueen(2);
    }

    public int get(int x, int y) {
        return this.table[x][y];
    }

    public void set(int x, int y, int index) {
        this.table[x][y] = index;
    }

    public void setCurrent(int index) {
        this.currentIndex = index;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public int getTableSize() {
        return this.n;
    }

    public boolean isSolved() {
        return this.get(0, this.n - 1) != 0;
    }

    public int getWinnerIndex() {
        return (isSolved() ? get(0, getTableSize() - 1) : getCurrentIndex());
    }

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

    public void printTable() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.print(this.table[i][j] + " ");
            System.out.println();
        }
    }

}
