package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

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

    private Level_1_Mechanics engine;

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

    // 1️⃣ check code from editor
    String userCode = code.getText();
    System.out.println("User code received:");
    System.out.println(userCode);

    // 2️⃣ run python
    System.out.println("Calling Python_Reader.runPython()...");

    List<Level_1_Commands> commands =
            Python_Reader.runPython(userCode);

    // 3️⃣ check python output
    if(commands == null){
        System.out.println("ERROR: Python returned null!");
        return;
    }

    System.out.println("Commands returned from Python:");
    System.out.println(commands);

    if(commands.isEmpty()){
        System.out.println("WARNING: No commands returned.");
    }

    // 4️⃣ execute commands
    int step = 0;

    for(Level_1_Commands cmd : commands){

        step++;

        System.out.println("Executing step " + step + ": " + cmd);

        engine.executeCommand(cmd);

        System.out.println("Hero position after command:");
        System.out.println("X = " + engine.getHeroX());
        System.out.println("Y = " + engine.getHeroY());

        updateVisuals();

        System.out.println("Visuals updated.");

        if(engine.checkWin()){
            System.out.println("LEVEL COMPLETE!");
            break;
        }

    }

    System.out.println("===== RUN FINISHED =====");

}

    private void updateVisuals(){

        GridPane.setColumnIndex(character, engine.getHeroX());
        GridPane.setRowIndex(character, engine.getHeroY());

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

}