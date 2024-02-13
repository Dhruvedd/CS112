package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        StdIn.setFile(file);
        
        int rows = StdIn.readInt();

        int columns = StdIn.readInt();


        

        grid = new boolean[rows][columns];

        for(int i = 0; i < rows;i++)
        {
            for(int j = 0; j < columns;j++)
            {
                grid[i][j] = StdIn.readBoolean();
            }

        }

        // WRITE YOUR CODE HERE
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {



        return grid[row][col]; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        boolean life = false;

        for(int i = 0; i < grid.length;i++)
        {
            for(int j = 0; j < grid[i].length;j++)
            {
                if(grid[i][j] == true){life = true;} 
            }

        }

        // WRITE YOUR CODE HERE
        return life; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    
    private boolean CheckCell(int r, int c){      //new method to test all edgecases
        
        if((r < 0) && (c < grid[0].length) && ( 0 <= c)){

            return getCellState(grid.length-1, c);
        }

        if((c < 0) && (r < grid.length) && ( 0 <= r)){

            return getCellState(r, grid[0].length-1);
        }

        if((c >= grid[0].length) && (r < grid.length) && ( 0 <= r)){

            return getCellState(r, 0);
        }

        if((r >= grid.length) && (c < grid[0].length) && ( 0 <= c)){

            return getCellState(0, c);
        }

        if((r >= grid.length) && (c >= grid[0].length)){

            return getCellState(0, 0);
        }

        if((r >= grid.length) && (c < 0)){

            return getCellState(0, grid[0].length-1);
        }

        if((r < 0) && (c >= grid[0].length)){

            return getCellState(grid.length-1, 0);
        }

        if((r < 0) && (c < 0)){

            return getCellState(grid.length-1, grid[0].length-1);
        }

        
        else return getCellState(r, c);


    }

    private int CheckIndex(int r, int c){      //new method to test all edgecases
        
        if((r < 0) && (c < grid[0].length) && ( 0 <= c)){

            return 1;
        }

        if((c < 0) && (r < grid.length) && ( 0 <= r)){

            return 2;
        }

        if((c >= grid[0].length) && (r < grid.length) && ( 0 <= r)){

            return 2;
        }

        if((r >= grid.length) && (c < grid[0].length) && ( 0 <= c)){

            return 1;
        }

        if((r >= grid.length) && (c >= grid[0].length)){

            return 3;
        }

        if((r >= grid.length) && (c < 0)){

            return 3;
        }

        if((r < 0) && (c >= grid[0].length)){

            return 3;
        }

        if((r < 0) && (c < 0)){

            return 3;
        }

        
        else return 0;

        //1 is when the row is wrong, 2 is when the column is wrong and 3 is when both are wrong


    }
    
    
    
     public int numOfAliveNeighbors (int row, int col) {

        
        int r = row;
        int c = col;

        int count = 0;

        if(CheckCell(r-1, c) == true){count++;}

        if(CheckCell(r-1, c+1) == true){count++;}

        if(CheckCell(r, c+1) == true){count++;}

        if(CheckCell(r+1, c+1) == true){count++;}

        if(CheckCell(r+1, c) == true){count++;}

        if(CheckCell(r+1, c-1) == true){count++;}

        if(CheckCell(r, c-1) == true){count++;}

        if(CheckCell(r-1, c-1) == true){count++;}


        return count; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {

        // WRITE YOUR CODE HERE

        totalAliveCells = 0;

        boolean[][] board = new boolean[grid.length][grid[0].length];

        for(int i = 0; i < grid.length;i++){

            for(int j = 0; j < grid[0].length;j++){

                int a = numOfAliveNeighbors(i, j);

                if(grid[i][j]){

                    if(a >= 4){board[i][j] = false;}

                    else if((a == 2) || (a == 3)){board[i][j] = true;}

                    else if(a < 1){board[i][j] = false;}

                    totalAliveCells++;
                
                
                }

                if((a == 3) && (grid[i][j] == false)){board[i][j] = true;}

            }

        }



        return board;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        grid = computeNewGrid();
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        for(int i = 0; i < n; i++){
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        
        WeightedQuickUnionUF gu = new WeightedQuickUnionUF(grid.length, grid[0].length);


        
        
        for(int i = 0; i < grid.length;i++){

            for(int j = 0; j < grid[0].length;j++){

             if(getCellState(i, j)){

                if(CheckCell(i-1, j) == true){

                    if(CheckIndex(i-1, j) == 0){
                        gu.union(i-1, j, i,j);
                    }

                    else if(CheckIndex(i-1, j) == 1){gu.union(grid.length-1, j, i,j);}
                }

                if(CheckCell(i-1, j+1) == true){

                    if(CheckIndex(i-1, j+1) == 0){
                        gu.union(i-1, j+1, i,j);
                    }

                    else if(CheckIndex(i-1, j+1) == 1){gu.union(grid.length-1, j, i,j);}

                    else if(CheckIndex(i-1, j+1) == 2){gu.union(i-1, 0, i,j);}

                    else if(CheckIndex(i-1, j+1) == 3){gu.union(grid.length-1, 0, i,j);}

                   
                }

                if(CheckCell(i, j+1) == true){
                    
                    if(CheckIndex(i, j+1) == 0){
                        gu.union(i, j+1, i,j);
                    }

                    else if(CheckIndex(i, j+1) == 2){gu.union(i, 0, i,j);}
                    
                }

                if(CheckCell(i+1, j+1) == true){
                   
                    if(CheckIndex(i+1, j+1) == 0){
                        gu.union(i+1, j+1, i,j);
                    }

                    else if(CheckIndex(i+1, j+1) == 1){gu.union(0, j+1, i,j);}

                    else if(CheckIndex(i+1, j+1) == 2){gu.union(i+1, 0, i,j);}

                    else if(CheckIndex(i+1, j+1) == 3){gu.union(0, 0, i,j);}
                }

                if(CheckCell(i+1, j) == true){
                   
                    if(CheckIndex(i+1, j) == 0){
                        gu.union(i+1, j, i,j);
                    }

                    else if(CheckIndex(i+1, j) == 1){gu.union(0, j, i,j);}

                }

                if(CheckCell(i+1, j-1) == true){
                    
                    if(CheckIndex(i+1, j-1) == 0){
                        gu.union(i+1, j-1, i,j);
                    }

                    else if(CheckIndex(i+1, j-1) == 1){gu.union(0, j-1, i,j);}

                    else if(CheckIndex(i+1, j-1) == 2){gu.union(i+1, grid[0].length-1, i,j);}

                    else if(CheckIndex(i+1, j-1) == 3){gu.union(0, grid[0].length-1, i,j);}

                }

                if(CheckCell(i, j-1) == true){
                   
                    if(CheckIndex(i, j-1) == 0){
                        gu.union(i, j-1, i,j);
                    }

                    else if(CheckIndex(i, j-1) == 2){gu.union(i, grid[0].length-1, i,j);}
                }

                if(CheckCell(i-1, j-1) == true){
                    if(CheckIndex(i-1, j-1) == 0){
                        gu.union(i-1, j-1, i,j);
                    }

                    else if(CheckIndex(i-1, j-1) == 1){gu.union(grid.length-1, j-1, i,j);}

                    else if(CheckIndex(i-1, j-1) == 2){gu.union(i-1, grid[0].length-1, i,j);}

                    else if(CheckIndex(i-1, j-1) == 3){gu.union(grid.length-1, grid[0].length-1, i,j);}
                }
            }
            
            }

        }

        boolean[] hehe = new boolean[grid.length * grid[0].length];

        for(int i = 0; i < grid.length;i++){

            for(int j = 0; j < grid[0].length;j++){

                
                if(grid[i][j]){

                    int root = gu.find(i, j);
                    hehe[root] = true;
                }
 

            }

        }

        int count = 0;

        for(int i = 0; i < hehe.length; i++)
        {
            if(hehe[i]){
                count++;
            }
        }


        
        return count; // update this line, provided so that code compiles
    }
}
