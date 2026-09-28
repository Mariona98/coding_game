package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class GameScreen {

    @FXML
    private TextArea code;
    @FXML
    private Button Run;
    @FXML
    private ImageView hero;
    @FXML 
    private ImageView doorclosed;
    @FXML 
    private ImageView button;
    @FXML   
    private ImageView scroll;
    @FXML 
    private ImageView book;
   

     @FXML
    private Label dialogueText;

   @FXML
private StackPane dialoguePane;

    private String[] dialogues;
    private int currentDialogue = 0;
    private Levels_Info saveData;
private String saveFilename;




    private Stage stage;
    private Scene scene;
   



    private Level_1_Mechanics engine;
    private List<Level_1_Commands> commandsList;
    private int currentCommandIndex = 0;

    public void initialize() {
    Level_1_Data level1 = new Level_1_Data();

saveData = new Levels_Info();
    engine = new Level_1_Mechanics(level1);

    setDialogues(Level_1_Data.DIALOGUES);

    updateVisuals();
}

public void setSaveFilename(String saveFilename) {

    this.saveFilename = saveFilename;

    System.out.println("Save file received: " + saveFilename);

    if (saveFilename != null && !saveFilename.isEmpty()) {
        saveData.loadGame(saveFilename);
    }
}

public void setDialogues(String[] dialogues) {
    this.dialogues = dialogues;
    currentDialogue = 0;

    if (dialogues != null && dialogues.length > 0) {
        dialogueText.setText(dialogues[0]);
        dialoguePane.setVisible(true);
    }
}

    @FXML
    private void onDialogueClicked() {
        if (dialogues == null || dialogues.length == 0) {
            return;
        }

        currentDialogue++;

        if (currentDialogue < dialogues.length) {
            dialogueText.setText(dialogues[currentDialogue]);
        } else {
            dialoguePane.setVisible(false);
        }
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
    
    private void executeNextCommand() throws IOException{
       
        // Check if all commands are executed
        if (currentCommandIndex >= commandsList.size()) {
            System.out.println("===== RUN FINISHED =====");


            engine.resetGame();
            updateVisuals(); // Reset visuals after run
            return;
        }
        
        // Check if level is already complete
        

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




if (engine.checkWin()) {

    System.out.println("LEVEL COMPLETE!");
    System.out.println("===== RUN FINISHED =====");


    // =========================
    // SAVE LEVEL PROGRESS
    // =========================

    if (saveData != null) {

        saveData.passLevel(1);

        if (saveFilename != null && !saveFilename.isEmpty()) {

            saveData.saveGame(saveFilename);

            System.out.println(
                "Level 1 saved to: " + saveFilename
            );
        }
    }


    // =========================
    // SUCCESS MESSAGE
    // =========================

    javafx.scene.control.Alert alert =
        new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.INFORMATION
        );

    alert.setTitle("Level Complete!");

    alert.setHeaderText(null);

    alert.setContentText(
        "Level passed successfully!"
    );

   alert.show();


    // =========================
    // OPEN GAME MAP
    // =========================

    FXMLLoader loader = new FXMLLoader(
        getClass().getResource(
            "/com/example/fxml/game_map.fxml"
        )
    );

    Parent root = loader.load();


    // Get GameMap controller
    GameMap gameMap =
        loader.getController();


    // Pass the save file to GameMap
    gameMap.setSaveFilename(
        saveFilename
    );


    // Get current window
    stage =
        (Stage) Run
            .getScene()
            .getWindow();


    scene =
        new Scene(
            root
        );


    stage.setScene(scene);

    stage.setTitle(
        "Coders' Haven"
    );

    stage.setResizable(false);

    stage.centerOnScreen();

    stage.show();


    return;
}        
       
updateVisuals();
        
        
        
        currentCommandIndex++;
        
        
        PauseTransition delay = new PauseTransition(Duration.millis(1000)); // 1 second delay
        delay.setOnFinished(event -> {
            try {
                executeNextCommand();
            } catch (IOException e) {
                throw new RuntimeException("Could not continue command execution.", e);
            }
        });
        delay.play();
    }
    
    private void updateVisuals(){
        GridPane.setColumnIndex(hero, engine.getHeroX());
        GridPane.setRowIndex(hero, engine.getHeroY()); 
        engine.updateButtonState(); // Update button state based on hero position 
        
        if(engine.isDoorOpen()==true)
        {    Image openDoor = new Image(
        getClass().getResource("/com/example/css/door-open.png").toExternalForm()
    );

    doorclosed.setImage(openDoor);

    Image buttonpressed = new Image(
        getClass().getResource("/com/example/css/button-pressed.png").toExternalForm()
    );

    button.setImage(buttonpressed);

}
    

        System.out.println("Visuals updated.");
    }
    


   
  @FXML

public void onScrollclick() {
    
    try {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("fxml/commands.fxml")
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
        stage.centerOnScreen();
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
            getClass().getResource("fxml/Syntax.fxml")
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
        stage.centerOnScreen();
        stage.show();

    } catch (IOException e) {
        System.out.println(
            "Could not open the Level 1 information window."
        );
        e.printStackTrace();
    }
}
}
