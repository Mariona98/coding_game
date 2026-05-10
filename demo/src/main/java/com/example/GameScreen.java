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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
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
    private ImageView door;

    @FXML
    private ImageView scroll;

    private Level_1_Mechanics engine;
    private List<Level_1_Commands> commandsList;
    private int currentCommandIndex = 0;

    public void initialize(){
        // Level 1 from your FXML
        Level_1_Data level1 = new Level_1_Data(
                0,5, // hero start
                2,1, // key
                4,4  // door
        );

        engine = new Level_1_Mechanics(level1);
        updateVisuals();
    }

    @FXML
    private void onRunClicked() throws Exception {
        System.out.println("===== RUN BUTTON PRESSED =====");
        
        // Reset engine to initial state before running new code
        Level_1_Data level1 = new Level_1_Data(
                0,5, // hero start
                2,1, // key
                4,4  // door
        );
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
        
        // Get and execute current command
        Level_1_Commands cmd = commandsList.get(currentCommandIndex);
        int step = currentCommandIndex + 1;
        
        System.out.println("Executing step " + step + ": " + cmd);
        
        engine.executeCommand(cmd);
        
        System.out.println("Hero position after command:");
        System.out.println("X = " + engine.getHeroX());
        System.out.println("Y = " + engine.getHeroY());
        
        updateVisuals();
        
        // Check for win after execution
        if (engine.checkWin()) {
            System.out.println("LEVEL COMPLETE!");
            System.out.println("===== RUN FINISHED =====");
            return;
        }
        
        
        currentCommandIndex++;
        
        
        PauseTransition delay = new PauseTransition(Duration.millis(1000)); // 1 second delay
        delay.setOnFinished(event -> executeNextCommand());
        delay.play();
    }
    
    private void updateVisuals(){
        GridPane.setColumnIndex(character, engine.getHeroX());
        GridPane.setRowIndex(character, engine.getHeroY());  

        if (engine.hasKey == true){
            key.setVisible(false);
        } 
        
        System.out.println("Visuals updated.");
    }
    
    
    private void stopExecution() {
        currentCommandIndex = commandsList.size(); 
    }

    @FXML
    private void onDarkmodeToggled() {
        if (darkmode.isSelected()) {
            code.getStylesheets().add(
                    getClass().getResource("/com/example/css/darkmode.css")
                            .toExternalForm());
        } else {
            code.getStylesheets().clear();
        }
    }

   
  @FXML
public void onScrollclick() {
    // default , to be changed later
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scroll.fxml"));
        Parent root = loader.load();
        
        
        InfoPane infoPane = loader.getController();
        
        
        infoPane.setTitle("📜 Custom Documentation");
        infoPane.setIntroText("This is my custom introduction text...");
        infoPane.setSection1Text("First section explanation...");
        infoPane.setSection2Text("Second section explanation...");
        infoPane.setConclusionText("Final conclusion here...");
        
        
        infoPane.addBulletPoint("First important point");
        infoPane.addBulletPoint("Second important point");
        infoPane.addBulletPoint("Third important point");
        
        
        infoPane.addNote("Remember to save your work");
        infoPane.addNote("Check for updates regularly");
        
        
        
        
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Information");
        stage.show();
        
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
