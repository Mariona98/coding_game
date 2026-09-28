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

    private String saveFilename;

    @FXML
    public void initialize() {
    }

    // Receives the save filename from GameScreen
    public void setSaveFilename(String saveFilename) {
        this.saveFilename = saveFilename;

        System.out.println(
            "GameMap received save file: " + saveFilename
        );
    }

    @FXML
    private void onLevelClicked() throws IOException {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/com/example/fxml/GameScene.fxml"
            )
        );

        Parent root = loader.load();

        GameScreen gameScreen = loader.getController();

        // Pass the same save file from GameMap to GameScreen
        gameScreen.setSaveFilename(saveFilename);

        Stage stage = (Stage) lv1.getScene().getWindow();

        Scene scene = new Scene(root, 1260, 810);

        stage.setScene(scene);
        stage.setTitle("Coders' Haven");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
}