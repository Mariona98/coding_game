package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class GameScreen {
    @FXML
    private TextArea code;
    @FXML
    private Button Run;
    @FXML
    private RadioButton darkmode;
    @FXML
    private ImageView character;
    @FXML
    private ImageView partcode;
    

    public void initialize() throws Exception {



    }
    @FXML
    private void onRunClicked() throws Exception {
        System.out.println("Run button clicked!");


    String content = code.getText();

    File file = new File("C:\\\\Users\\\\pittm\\\\OneDrive\\\\Desktop\\\\Paper\\\\script.py");

    try (FileWriter writer = new FileWriter(file)) {
        writer.write("");
        writer.write(content);
        
    }

    System.out.println("Saved!");



                  ProcessBuilder pb = new ProcessBuilder(
        "C:\\Users\\pittm\\AppData\\Local\\Programs\\Python\\Launcher\\py.exe",
        "C:\\Users\\pittm\\OneDrive\\Desktop\\Paper\\script.py"
    );
              Process process = pb.start();



       BufferedReader reader = new BufferedReader(
           new InputStreamReader(process.getInputStream())
        );
        BufferedReader errorReader = new BufferedReader(
        new InputStreamReader(process.getErrorStream())
);

String errorLine;
while ((errorLine = errorReader.readLine()) != null) {
    System.err.println("PYTHON ERROR: " + errorLine);
}


        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        process.waitFor();
     






    



    }


    @FXML
    private void onDarkmodeToggled() {
        System.out.println("Darkmode toggled!");
    if (darkmode.isSelected()) {
        code.getStylesheets().add(getClass().getResource("/com/example/css/darkmode.css").toExternalForm());
        code.getStyleClass().add("code");
        Image image = new Image("file:/C:/Users/pittm/OneDrive/Desktop/Paper/coding_game/demo/src/main/resources/com/example/dark.png");
partcode.setImage(image);

    } else {
        code.getStylesheets().clear();
        Image image = new Image("file:/C:/Users/pittm/OneDrive/Desktop/Paper/coding_game/demo/src/main/resources/com/example/white.png");
partcode.setImage(image);
    }

}



}
