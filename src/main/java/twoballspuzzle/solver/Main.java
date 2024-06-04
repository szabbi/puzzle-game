package twoballspuzzle.solver;

import puzzle.solver.BreadthFirstSearch;
import twoballspuzzle.model.Direction;
import twoballspuzzle.model.PuzzleState;

public class Main {
    public static void main(String[] args) {
        var bfs = new BreadthFirstSearch<Direction>();
        bfs.solveAndPrintSolution(new PuzzleState());
    }
}
