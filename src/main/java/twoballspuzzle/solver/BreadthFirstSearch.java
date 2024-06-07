package twoballspuzzle.solver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twoballspuzzle.model.Direction;
import twoballspuzzle.model.PuzzleState;


public class BreadthFirstSearch {

    private static final Logger logger = LogManager.getLogger(BreadthFirstSearch.class);


    public static void main(String[] args) {
        logger.info("Solver started.");
        var bfs = new puzzle.solver.BreadthFirstSearch<Direction>();
        bfs.solveAndPrintSolution(new PuzzleState());
    }
}
