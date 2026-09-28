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
            
            Path fxmlPath = Paths.get("src/main/resources/com/example/fxml/MM.fxml");
            if (fxmlPath.toFile().exists()) {
                System.out.println("Found FXML in file system: " + fxmlPath.toAbsolutePath());
                FXMLLoader fxmlLoader = new FXMLLoader(fxmlPath.toUri().toURL());
                Parent root = fxmlLoader.load();
                scene = new Scene(root, 586, 438);
            } else {
                
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fxml/MM.fxml"));
                Parent root = fxmlLoader.load();
                scene = new Scene(root, 586, 438);
            }
            stage.setTitle("Coders' Haven");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
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