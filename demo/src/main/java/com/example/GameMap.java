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


public class GameMap {

    @FXML
    private Button lv1;

    @FXML
    private Button lv2;
    @FXML 
    private Button lv3;

    @FXML
    private Button mainMenuButton;

    private String saveFilename;
    private Levels_Info saveData = new Levels_Info();


    public void setSaveFilename(String saveFilename) {
        this.saveFilename = saveFilename;

        saveData = new Levels_Info();
        if (saveFilename != null && !saveFilename.isEmpty()) {
            saveData.loadGame(saveFilename);
        }
        configureLevelButton(lv1, 1);
        configureLevelButton(lv2, 2);
        configureLevelButton(lv3, 3);
        

        System.out.println("GameMap received save file: " + saveFilename);
    }

    private void configureLevelButton(Button levelButton, int levelNumber) {
        levelButton.getStyleClass().removeAll("completed", "gold");
        if (saveData.hasGold(levelNumber)) {
            levelButton.getStyleClass().add("gold");
        } else if (saveData.isLevelPassed(levelNumber)) {
            levelButton.getStyleClass().add("completed");
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
                return lv2;
            case 3:
                return lv3;
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
            openLevel(2, "/com/example/fxml/GameScene.fxml", lv2);
        }
    }

    @FXML
    private void onLevel3Clicked() throws IOException {
        if (showLevelInfo(3)) {
            openLevel(3, "/com/example/fxml/GameScene.fxml", lv3);
        }
    }

//Review and rewrite
    private boolean showLevelInfo(int levelNumber) {
        // Let players review instructions and their best result before entering.
        GameLevelData levelData;
        if (levelNumber == 1) {
            levelData = Level_1_Data.LEVEL;
        } else if (levelNumber == 2) {
            levelData = Level_2_Data.LEVEL;
        } else {
            levelData = Level_3_Data.LEVEL;
        }
        String description = levelData.getCommandHelpIntroduction();
    
           
        description += "\n\nYou can revisit this level at any time.";
        description += "\nGold target: finish in "
            + levelData.getGoldMaxSteps()
            + " steps or fewer and use "
            + levelData.getGoldMaxLines()
            + " non-blank code lines or fewer.";

        Integer bestSteps = saveData.getBestSteps(levelNumber);
        if (bestSteps != null) {
            description += "\nBest run: " + bestSteps + " steps.";
        } else {
            description += "\nBest run will be recorded when you finish the level.";
        }
        Integer bestLines = saveData.getBestLines(levelNumber);
        if (bestLines != null) {
            description += "\nBest solution: " + bestLines + " lines.";
        } else {
            description += "\nBest line count will be recorded when you finish the level.";
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
        alert.setGraphic(null); 

        return alert.showAndWait()
            .filter(startLevel::equals)
            .isPresent();
    }

    @FXML
    private void onMainMenuClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/fxml/MainMenu.fxml")
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
        stage.setTitle("CODING HELL0");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}