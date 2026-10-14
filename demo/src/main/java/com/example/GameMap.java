package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Controller for the level-selection screen. Loads save progress, shows each
 * level's completion state, and opens the selected level or main menu.
 */
public class GameMap {

    @FXML
    private Button lv1;

    @FXML
    private Button lv11;

    @FXML
    private Button mainMenuButton;

    private String saveFilename;
    private Levels_Info saveData = new Levels_Info();

    /**
     * Called by the menu/game screen after FXML loading to connect this map to
     * the current save file and refresh the level buttons.
     */
    public void setSaveFilename(String saveFilename) {
        this.saveFilename = saveFilename;

        saveData = new Levels_Info();
        if (saveFilename != null && !saveFilename.isEmpty()) {
            saveData.loadGame(saveFilename);
        }
        configureLevelButton(lv1, 1);
        configureLevelButton(lv11, 2);

        System.out.println("GameMap received save file: " + saveFilename);
    }

    private void configureLevelButton(Button levelButton, int levelNumber) {
        // The CSS class controls the visual style for completed levels.
        boolean completed = saveData.isLevelPassed(levelNumber);
        if (completed) {
            if (!levelButton.getStyleClass().contains("completed")) {
                levelButton.getStyleClass().add("completed");
            }
        } else {
            levelButton.getStyleClass().remove("completed");
        }
    }

    public void markLevelCompleted(int levelNumber) {
        saveData.passLevel(levelNumber);
        configureLevelButton(getLevelButton(levelNumber), levelNumber);
    }

    private Button getLevelButton(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return lv1;
            case 2:
                return lv11;
            default:
                throw new IllegalArgumentException(
                    "No map button is configured for level " + levelNumber
                );
        }
    }

    @FXML
    private void onLevelClicked() throws IOException {
        if (showLevelInfo(1)) {
            openLevel(1, "/com/example/fxml/GameScene.fxml", lv1);
        }
    }

    @FXML
    private void onLevel2Clicked() throws IOException {
        if (showLevelInfo(2)) {
            openLevel(2, "/com/example/fxml/GameScene.fxml", lv11);
        }
    }
//Review and rewrite
    private boolean showLevelInfo(int levelNumber) {
        // Let players review instructions and their best result before entering.
        String description = levelNumber == 1
            ? "Learn to activate the floor switch to open the door, then guide the hero to the exit."
            : "The switch and door are close together. Plan the hero's moves carefully to reach the exit.";
        description += "\n\nYou can revisit this level at any time.";

        Integer bestSteps = saveData.getBestSteps(levelNumber);
        if (bestSteps != null) {
            description += "\nBest run: " + bestSteps + " steps.";
        } else {
            description += "\nBest run will be recorded when you finish the level.";
        }

        ButtonType startLevel = new ButtonType(
            "Start Level",
            ButtonBar.ButtonData.OK_DONE
        );
        ButtonType cancel = new ButtonType(
            "Cancel",
            ButtonBar.ButtonData.CANCEL_CLOSE
        );
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Level " + levelNumber);
        alert.setHeaderText("Level " + levelNumber + " Information");
        alert.setContentText(description);
        alert.getButtonTypes().setAll(startLevel, cancel);

        return alert.showAndWait()
            .filter(startLevel::equals)
            .isPresent();
    }

    @FXML
    private void onMainMenuClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/fxml/MM.fxml")
        );
        Parent root = loader.load();

        MainMenu mainMenu = loader.getController();
        mainMenu.setContinueMap(saveFilename);

        Stage stage = (Stage) mainMenuButton.getScene().getWindow();
        Scene scene = new Scene(root, 586, 438);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void openLevel(int levelNumber, String fxmlPath, Button source)
        throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();

        GameScreen gameScreen = loader.getController();
        gameScreen.setLevelNumber(levelNumber);
        gameScreen.setSaveFilename(saveFilename);

        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = new Scene(root, 1260, 810);

        stage.setScene(scene);
        stage.setTitle("Coders' Haven");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}