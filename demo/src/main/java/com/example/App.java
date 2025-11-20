package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        try {
            
            Path fxmlPath = Paths.get("/demo/src/main/resources/com/example/GameScene.fxml");
            if (fxmlPath.toFile().exists()) {
                System.out.println("Found FXML in file system: " + fxmlPath.toAbsolutePath());
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlPath.toUri().toURL());
                Parent root = fxmlLoader.load();
                scene = new Scene(root, 640, 480);
            } else {
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/GameScene.fxml"));
                Parent root = fxmlLoader.load();
                scene = new Scene(root, 640, 480);
            }
            stage.setTitle("Coders' Haven");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}