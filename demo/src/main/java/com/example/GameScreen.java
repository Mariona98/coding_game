package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class GameScreen {

    @FXML
    private TextArea code;

    @FXML
    private RadioButton darkmode;

    @FXML
    private Button Run;

    @FXML
    private ImageView character;

    @FXML
    private GridPane grid; 

    @FXML
    private ImageView key;

    @FXML
    private ImageView scroll;

    @FXML
    private ImageView tower1;

    @FXML
    private ImageView tower2;

    @FXML
    private ImageView bow;

    @FXML
    private ImageView door;

    @FXML
    private ImageView chest1;

    @FXML
    private ImageView chest2;

    @FXML
    private ImageView bug1;

    @FXML
    private ImageView bug2;

    @FXML
    private ImageView book;

    private Level_1_Mechanics engine;
    private List<Level_1_Commands> commandsList;
    private int currentCommandIndex = 0;

    public void initialize() {
    Level_1_Data level1 = new Level_1_Data();

    engine = new Level_1_Mechanics(level1);

    updateVisuals();
}

    @FXML
    private void onRunClicked() throws Exception {
        System.out.println("===== RUN BUTTON PRESSED =====");
        
        // Reset engine to initial state before running new code
       Level_1_Data level1 = new Level_1_Data();
        engine = new Level_1_Mechanics(level1);
        
        
        String userCode = code.getText();
        System.out.println("User code received:");
        System.out.println(userCode);
        
        System.out.println("Calling Python_Reader.runPython()...");
        commandsList = Python_Reader.runPython(userCode);
        
        if(commandsList == null){
            System.out.println("ERROR: Python returned null!");
            return;
        }
        
        System.out.println("Commands returned from Python:");
        System.out.println(commandsList);
        
        if(commandsList.isEmpty()){
            System.out.println("WARNING: No commands returned.");
            return;
        }
        
        // Start executing commands with delay
        currentCommandIndex = 0;
        updateVisuals(); // Initial visual update
        executeNextCommand();
    }
    
    private void executeNextCommand() {
        // Check if all commands are executed
        if (currentCommandIndex >= commandsList.size()) {
            System.out.println("===== RUN FINISHED =====");
            engine.resetGame();
            updateVisuals(); // Reset visuals after run
            return;
        }
        
        // Check if level is already complete
        if (engine.checkWin()) {
            System.out.println("LEVEL COMPLETE!");
            System.out.println("===== RUN FINISHED =====");
            //thinking what the starts logic should be maybe based on the number of commands used
            engine.setStars(3); // Set stars for the completed level
            System.out.println("Stars set for completed level.");
            return;
        }

        // Check if hero is dead
        if (engine.checkHeroDead()) {
            System.out.println("HERO IS DEAD!");
            System.out.println("===== RUN FINISHED =====");



            try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("Losing.fxml")
        );
        Font irishGrover = Font.loadFont(
            getClass().getResourceAsStream(
                "/com/example/fonts/IrishGrover-Regular.ttf"
            ),
            48
        );

        if (irishGrover == null) {
            throw new IllegalStateException("Could not load Irish Grover font");
        }

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Level 1 Commands");
        stage.setScene(new Scene(root,1260, 810));
        stage.show();

    } catch (IOException e) {
        System.out.println(
            "Could not open the Level 1 information window."
        );
        e.printStackTrace();
    }



            engine.resetGame();
            updateVisuals(); // Reset visuals after run
            return;
        }
        
        // Get and execute current command
        Level_1_Commands cmd = commandsList.get(currentCommandIndex);
        int step = currentCommandIndex + 1;
        
        System.out.println("Executing step " + step + ": " + cmd);
        
        engine.executeCommand(cmd);
        
        System.out.println("Hero position after command:");
        System.out.println("X = " + engine.getHeroX());
        System.out.println("Y = " + engine.getHeroY());
        
        updateVisuals();
        
        
        
        currentCommandIndex++;
        
        
        PauseTransition delay = new PauseTransition(Duration.millis(1000)); // 1 second delay
        delay.setOnFinished(event -> executeNextCommand());
        delay.play();
    }
    
    private void updateVisuals(){
        GridPane.setColumnIndex(character, engine.getHeroX());
        GridPane.setRowIndex(character, engine.getHeroY());  
        GridPane.setColumnIndex(bug1, engine.getBug1X());
        GridPane.setRowIndex(bug1, engine.getBug1Y());
        GridPane.setColumnIndex(bug2, engine.getBug2X());
        GridPane.setRowIndex(bug2, engine.getBug2Y());

        if(engine.isBug1Dead == true){
            bug1.setVisible(false);
        }
        else{
            bug1.setVisible(true);
        }
        if(engine.isBug2Dead == true){
            bug2.setVisible(false);
        }
        else{
            bug2.setVisible(true);
        }


        if (engine.hasKey == true){
            key.setVisible(false);
        } 
        if (engine.hasBow == true){
            bow.setVisible(false);
        }
        if (engine.chest1Opened == true){
            chest1.setVisible(false);
        }
        if (engine.chest2Opened == true){
            chest2.setVisible(false);
        }
        System.out.println("Visuals updated.");
    }
    
    

   
  @FXML

public void onScrollclick() {
    
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("commands.fxml")
        );
        Font irishGrover = Font.loadFont(
            getClass().getResourceAsStream(
                "/com/example/fonts/IrishGrover-Regular.ttf"
            ),
            48
        );

        if (irishGrover == null) {
            throw new IllegalStateException("Could not load Irish Grover font");
        }

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Level 1 Commands");
        stage.setScene(new Scene(root,1260, 810));
        stage.show();

    } catch (IOException e) {
        System.out.println(
            "Could not open the Level 1 information window."
        );
        e.printStackTrace();
    }
}

public void onBookclick() {
    
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("Syntax.fxml")
        );
        Font irishGrover = Font.loadFont(
            getClass().getResourceAsStream(
                "/com/example/fonts/IrishGrover-Regular.ttf"
            ),
            48
        );

        if (irishGrover == null) {
            throw new IllegalStateException("Could not load Irish Grover font");
        }
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Usefull syntax");
        stage.setScene(new Scene(root,1260, 810));
        stage.show();

    } catch (IOException e) {
        System.out.println(
            "Could not open the Level 1 information window."
        );
        e.printStackTrace();
    }
}
}
