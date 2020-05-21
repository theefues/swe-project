package queen.state;

import java.util.Random;

public class Table {
    private int n;
    private int[][] table;
    private Random rand;

    public Table (int n) {
        this.n = n;
        this.table = new int[n][n];
        this.rand = new Random();

        this.addRandomQueen(1);
        this.addRandomQueen(2);
    }

    public int get(int x, int y) {
        return this.table[x][y];
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

    public boolean tryMoveQueenTo(int x, int y, int index) {
        if (y == 0) return false; // First row
        if (x == n - 1) return false; // Last col

        if (this.table[x][y] != 0) return false;

        // Try move left
        if (this.table[x + 1][y] == index) {
            this.table[x + 1][y] = 0;
            this.table[x][y] = index;
            return true;
        }

        // Try move down
        if (this.table[x][y - 1] == index) {
            this.table[x][y - 1] = 0;
            this.table[x][y] = index;
            return true;
        }

        // Try move one left and one down
        if (this.table[x + 1][y - 1] == index) {
            this.table[x + 1][y - 1] = 0;
            this.table[x][y] = index;
            return true;
        }

        return false;
    }

    public void printTable() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                System.out.print(this.table[i][j] + " ");
            System.out.println();
        }
    }

}
