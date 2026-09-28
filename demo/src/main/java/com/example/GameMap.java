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
    

    private static Scene scene;
    private static Stage stage;
    @FXML
    public void initialize() {
        
    }
    @FXML
private void onLevelClicked() throws IOException {

        System.out.println("Level clicked!");

        Parent root = FXMLLoader.load(
            getClass().getResource("/com/example/fxml/GameScene.fxml")
        );

        Stage stage = (Stage) lv1.getScene().getWindow();

        Scene scene = new Scene(root, 1260, 830);

        stage.setScene(scene);
        stage.setTitle("Coders' Haven");
        stage.setResizable(false);

        // Center the window after setting the new scene
        stage.centerOnScreen();

        stage.show();
    }
    
}
