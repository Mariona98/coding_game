package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;


public class App extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Resolve the FXML file from the application's classpath resources.
            URL fxmlUrl = getClass().getResource("/com/example/fxml/MainMenu.fxml");
            if (fxmlUrl == null) {
                throw new IOException(
                    "Could not find /com/example/fxml/MainMenu.fxml on the classpath."
                );
            }

            // FXMLLoader builds the JavaFX node tree described by the FXML file.
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load();

            
            Scene scene = new Scene(root, 586, 438);
            stage.setTitle("Coding Hell0");
            stage.setScene(scene);
            stage.setResizable(false); // Remove or set to true to let users resize the window.
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