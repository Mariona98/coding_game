package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class GameMap {

    @FXML
    private Button lv1;

    @FXML
    private Button lv11;

    @FXML
    private Button mainMenuButton;

    private String saveFilename;

    // Receives the save filename from the menu or GameScreen.
    public void setSaveFilename(String saveFilename) {
        this.saveFilename = saveFilename;

        lv1.setText("1");
        lv11.setText("2");
        lv1.setDisable(false);
        lv11.setDisable(false);
        lv1.setStyle("");
        lv11.setStyle("");
        if (saveFilename != null && !saveFilename.isEmpty()) {
            Levels_Info saveData = new Levels_Info();
            saveData.loadGame(saveFilename);
            if (saveData.isLevelPassed(1)) {
                markLevelPassed(lv1);
            }
            if (saveData.isLevelPassed(2)) {
                markLevelPassed(lv11);
            }
        }

        System.out.println("GameMap received save file: " + saveFilename);
    }

    private void markLevelPassed(Button levelButton) {
        levelButton.setDisable(true);
        levelButton.setStyle(
            "-fx-background-color: black; " +
            "-fx-background-radius: 50%; " +
            "-fx-border-color: #606060; " +
            "-fx-border-radius: 50%; " +
            "-fx-text-fill: white; " +
            "-fx-opacity: 1; " +
            "-fx-cursor: default;"
        );
    }

    @FXML
    private void onLevelClicked() throws IOException {
        openLevel(1, "/com/example/fxml/GameScene.fxml", lv1);
    }

    @FXML
    private void onLevel2Clicked() throws IOException {
        openLevel(2, "/com/example/fxml/GameScene2.fxml", lv11);
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