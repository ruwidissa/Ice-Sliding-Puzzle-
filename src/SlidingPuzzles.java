import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class SlidingPuzzles {
    private static char[][] puzzle;
    private static int rows, cols;
    private static Cell start, finish;

    public static void main(String[] args) {
        readFromFile("inputdata.txt");
        findStartAndFinish();

        long startTime = System.nanoTime(); // Measure the start time

        Cell solution = findpath(); // Solve using BFS algorithm

        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000_000.0; // Convert nanoseconds to seconds

        printResults(solution);
        System.out.printf("Execution Time: %.6f seconds%n", executionTime);
    }

    private static void readFromFile(String filename) {
        try {
            Scanner scanner = new Scanner(new File(filename));
            rows = 0;
            while (scanner.hasNextLine()) {
                rows++;
                String line = scanner.nextLine();  // Read the next line from the file
                if (cols == 0) cols = line.length();  // If cols is 0, set it to the length of the first line, representing the number of columns
            }
            scanner.close();
            puzzle = new char[rows][cols];  // Initialize the puzzle array with the determined number of rows and columns
            scanner = new Scanner(new File(filename));
            for (int i = 0; i < rows; i++) {  // Populate puzzle array
                String line = scanner.nextLine();
                for (int j = 0; j < cols; j++) {
                    puzzle[i][j] = line.charAt(j);  // Assign the character at position (i, j) in the puzzle array from the corresponding position in the line
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void findStartAndFinish() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (puzzle[i][j] == 'S') {  // Check if the current cell contains 'S', representing the start cell
                    start = new Cell(j, i, "", null);  // Create a new Cell object for the start cell and assign it to the start variable
                } else if (puzzle[i][j] == 'F') {
                    finish = new Cell(j, i, "", null);
                }
            }
        }
    }

    private static Cell findpath() {
        boolean[][] visited = new boolean[rows][cols];
        Queue<Cell> queue = new ArrayDeque<>();  // Initialize a queue for BFS traversal
        queue.offer(start);
        visited[start.y][start.x] = true;

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        String[] directionSymbol = {"Down", "Up", "Right", "Left"};

        while (!queue.isEmpty()) {  // BFS loop
            Cell current = queue.poll();
            if (current.x == finish.x && current.y == finish.y) {  // Check if current cell is the finish cell
                return current;
            }
            for (int i = 0; i < 4; i++) {   // Explore adjacent cells
                // Initialize new coordinates
                int nx = current.x;
                int ny = current.y;
                while (true) {  // Move in the current direction until an obstacle is encountered
                    nx += dx[i];
                    ny += dy[i];
                    if (!isValid(nx, ny) || puzzle[ny][nx] == '0') {   // Check if the new position is valid or obstacle
                        break;
                    }
                    if (puzzle[ny][nx] == 'F') {  // Check if the finish is reached
                        return new Cell(nx, ny, directionSymbol[i], current);   // Return the solution
                    }
                }
                nx -= dx[i];
                ny -= dy[i];
                if (!visited[ny][nx]) {
                    queue.offer(new Cell(nx, ny, directionSymbol[i], current));  // Add the new cell to the queue with updated coordinates, direction, and parent cell
                    visited[ny][nx] = true;  // Mark as visited
                }
            }
        }
        return null;
    }

    // Method to check if a position is valid in the puzzle
    private static boolean isValid(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    private static void printResults(Cell solution) {
        if (solution == null) {
            System.out.println("No solution found.");
            return;
        }
        Stack<String> steps = new Stack<>();   // Use the stack to store the steps in reverse order
        while (solution.parent != null) {  // Trace back from finish cell to start cell
            steps.push("Move " + solution.direction + " to (" + (solution.x + 1) + ", " + (solution.y + 1) + ")");
            solution = solution.parent;
        }
        // Add start cell to the steps
        steps.push("Start at (" + (start.x + 1) + ", " + (start.y + 1) + ")");
        int stepNumber = 1;
        while (!steps.isEmpty()) {  // Print steps in correct order
            System.out.println(stepNumber++ + ". " + steps.pop());
        }
        System.out.printf("%d. Done !%n", stepNumber);
    }
}
