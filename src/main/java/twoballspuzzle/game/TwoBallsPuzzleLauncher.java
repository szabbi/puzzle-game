package twoballspuzzle.game;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TwoBallsPuzzleLauncher {

    private static final Logger LOGGER = LogManager.getLogger(TwoBallsPuzzleLauncher.class);


    public static void main(String[] args) {
        LOGGER.debug("Application started.");
        Application.launch(TwoBallsPuzzleApp.class, args);
    }
}
