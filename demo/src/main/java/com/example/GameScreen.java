package com.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameScreen {
    @FXML
    private TextArea code;
    @FXML
    private Button Run;
    @FXML
    private Label Label1;


    public void initialize() throws Exception {



    }
    @FXML
    private void onRunClicked() throws Exception {
        System.out.println("Run button clicked!");


    String content = code.getText();

    File file = new File("C:/Users/pittm/OneDrive/Desktop/Paper/script.py");

    try (FileWriter writer = new FileWriter(file)) {
        //writer.write("");
       // writer.write(content);
    }

    System.out.println("Saved!");




              ProcessBuilder pb = new ProcessBuilder("python", "script.py");
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




    File myObj = new File("labeltest.txt");

    
    try (Scanner myReader = new Scanner(myObj)) {
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        Label1.setText(data);
        System.out.println(data);
      }
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }


    



    }









}
