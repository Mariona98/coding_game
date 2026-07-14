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
       Parent root=FXMLLoader.load(getClass().getResource("/com/example/GameScene.fxml"));
       stage=(Stage)lv1.getScene().getWindow();
       scene=new Scene(root, 1260, 810);
         stage.setScene(scene);

    stage.setTitle("Coders' Haven");

    // NEW: Prevent resizing.
    stage.setResizable(false);

    // NEW: Keep the exact window size.
    stage.setMinWidth(1260);
    stage.setMaxWidth(1260);
    stage.setMinHeight(810);
    stage.setMaxHeight(810);


            stage.show();
        
    }
    
}
