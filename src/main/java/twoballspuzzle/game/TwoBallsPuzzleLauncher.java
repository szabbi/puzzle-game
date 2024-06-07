package twoballspuzzle.game;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TwoBallsPuzzleLauncher {

    protected static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        logger.debug("Application started.");
        Application.launch(TwoBallsPuzzleApp.class, args);
    }
}
