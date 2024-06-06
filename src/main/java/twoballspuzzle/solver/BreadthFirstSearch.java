package twoballspuzzle.solver;

import twoballspuzzle.model.Direction;
import twoballspuzzle.model.PuzzleState;

public class BreadthFirstSearch {
    public static void main(String[] args) {
        var bfs = new puzzle.solver.BreadthFirstSearch<Direction>();
        bfs.solveAndPrintSolution(new PuzzleState());
    }
}
