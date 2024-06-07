package twoballspuzzle.game;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twoballspuzzle.solver.BreadthFirstSearch;

public class TwoBallsPuzzleLauncher {

    private static final Logger logger = LogManager.getLogger(TwoBallsPuzzleLauncher.class);


    public static void main(String[] args) {
        logger.debug("Application started.");
        Application.launch(TwoBallsPuzzleApp.class, args);
    }
}
