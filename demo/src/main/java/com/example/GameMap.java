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
       Parent root=FXMLLoader.load(getClass().getResource("/com/example/game_map.fxml"));
       stage=(Stage)lv1.getScene().getWindow();
       scene=new Scene(root);
         stage.setScene(scene);
            stage.show();
        
    }
    
}
