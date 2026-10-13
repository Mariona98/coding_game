package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

/**
 * JavaFX entry point for the Coders' Haven application.
 * The screen layout is loaded from {@code /com/example/fxml/MM.fxml}.
 */
public class App extends Application {

    /**
     * Creates and displays the application's main window.
     * Update the FXML resource path here to load a different starting screen.
     */
    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Resolve the FXML file from the application's classpath resources.
            URL fxmlUrl = getClass().getResource("/com/example/fxml/MM.fxml");
            if (fxmlUrl == null) {
                throw new IOException(
                    "Could not find /com/example/fxml/MM.fxml on the classpath."
                );
            }

            // FXMLLoader builds the JavaFX node tree described by the FXML file.
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            Parent root = fxmlLoader.load();

            // The Stage retains its Scene, so a static Scene field is unnecessary.
            // These dimensions set the initial window size; change them to resize the layout.
            Scene scene = new Scene(root, 586, 438);
            stage.setTitle("Coders' Haven");
            stage.setScene(scene);
            stage.setResizable(false); // Remove or set to true to let users resize the window.
            stage.show();
        } catch (Exception e) {
            // Preserve diagnostic details in the console and report startup failure to JavaFX.
            System.err.println("Error loading FXML: " + e.getMessage());
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * Standard Java application entry point; JavaFX invokes {@link #start(Stage)} after launch.
     * Pass {@code args} to {@code launch(args)} if the application needs command-line options.
     */
    public static void main(String[] args) {
        // No command-line arguments are currently used, so launch() is sufficient.
        launch();
    }
}