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
    /*
     * Level 1 documentation.
     * This text can be replaced later with different information
     * for each level.
     */
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("scroll.fxml")
        );

        Parent root = loader.load();
        InfoPane infoPane = loader.getController();

        infoPane.setTitle("Level 1 Commands and Game Elements");

        infoPane.setIntroText(
            "Write commands for the hero in the code editor, then press Run. "
            + "The commands are executed in order, one at a time."
        );

        infoPane.setSection1Text(
            "Available commands:\n\n"
            + "hero.moveRight()\n"
            + "Moves the hero one tile to the right.\n\n"

            + "hero.moveLeft()\n"
            + "Moves the hero one tile to the left.\n\n"

            + "hero.moveUp()\n"
            + "Moves the hero one tile upward.\n\n"

            + "hero.moveDown()\n"
            + "Moves the hero one tile downward.\n\n"

            + "hero.collect()\n"
            + "Collects the bow when the hero is standing on the bow tile.\n\n"

            + "hero.openChest()\n"
            + "Opens a chest when the hero is standing on its tile.\n\n"

            + "hero.shootUp()\n"
            + "Shoots an arrow upward.\n\n"

            + "hero.shootDown()\n"
            + "Shoots an arrow downward.\n\n"

            + "hero.shootLeft()\n"
            + "Shoots an arrow to the left.\n\n"

            + "hero.shootRight()\n"
            + "Shoots an arrow to the right."
        );

        infoPane.setSection2Text(
            "Game elements:\n\n"
            + "Hero\n"
            + "The character controlled by your commands.\n\n"

            + "Bow\n"
            + "Must be collected before the hero can shoot arrows. "
            + "After collection, it disappears from the map.\n\n"

            + "Bugs\n"
            + "Move toward the hero after movement commands. "
            + "They kill the hero if they reach the same tile. "
            + "They can be killed by arrows or by moving into lava.\n\n"

            + "Towers\n"
            + "Kill the hero when the hero enters one of the eight surrounding "
            + "tiles, including diagonal tiles.\n\n"

            + "Chest 1\n"
            + "Contains either lava boots or an invisibility cloak. "
            + "The reward is selected randomly.\n\n"

            + "Chest 2\n"
            + "Contains the key needed to unlock the door.\n\n"

            + "Lava boots\n"
            + "Allow the hero to walk safely over lava.\n\n"

            + "Invisibility cloak\n"
            + "Prevents the towers from detecting and killing the hero.\n\n"

            + "Lava\n"
            + "Kills the hero unless the hero has the boots. "
            + "Bugs that enter dangerous lava are also killed.\n\n"

            + "Bridge\n"
            + "Provides a safe path through the lava area.\n\n"

            + "Door\n"
            + "Completes the level when the hero reaches it while carrying the key."
        );

        infoPane.setConclusionText(
            "Plan the command order carefully. Avoid the bugs, towers, lava, "
            + "and map boundaries. Collect the required items and reach the door "
            + "with the key to complete Level 1."
        );

        infoPane.addBulletPoint(
            "Commands are executed from top to bottom."
        );

        infoPane.addBulletPoint(
            "Movement outside the grid is blocked and prints an out-of-bounds warning."
        );

        infoPane.addBulletPoint(
            "The bow must be collected before any shooting command can succeed."
        );

        infoPane.addBulletPoint(
            "An arrow kills the first living bug in the selected direction."
        );

        infoPane.addBulletPoint(
            "Opened chests, collected items, and dead bugs are removed from the map."
        );

        infoPane.addBulletPoint(
            "The level resets after every completed run."
        );

        infoPane.addNote(
            "The hero can only collect or open something while standing on its tile."
        );

        infoPane.addNote(
            "Shooting does not depend on the hero's previous movement direction."
        );

        infoPane.addNote(
            "Tower danger includes horizontal, vertical, and diagonal neighboring tiles."
        );

        Scene scene = new Scene(root);
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle("Level 1 Information");
        stage.setResizable(false);
        stage.show();

    } catch (IOException e) {
        System.out.println(
            "Could not open the Level 1 information window."
        );
        e.printStackTrace();
    }
}
}
