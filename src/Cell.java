class Cell {
    int x, y;   // Declare integer variables x and y to represent coordinates of the cell
    String direction;  // Declare a String variable direction to represent the direction
    Cell parent;  // Declare a Cell variable parent to represent the parent cell

    // Constructor to initialize the Cell object with provided values.
    public Cell(int x, int y, String direction, Cell parent) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.parent = parent;
    }
}

