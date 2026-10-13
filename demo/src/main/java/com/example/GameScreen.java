package com.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * FXML controller for gameplay: selects level data, submits the player's Python
 * code, animates generated moves, updates visuals, and saves completed runs.
 * UI element fields are injected from GameScene.fxml.
 */
public class GameScreen {

    @FXML
    private TextArea code;
    @FXML
    private Button Run;
    @FXML
    private Button mainMenuButton;
    @FXML
    private Label stepCounter;
    @FXML
    private ImageView hero;
    @FXML
    private ImageView levelBackground;
    @FXML
    private GridPane grid;
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

   @FXML
   private StackPane infoPopup;

   @FXML
   private StackPane infoPopupContent;

   @FXML
   private Label infoPopupTitle;

    private String[] dialogues;
    private int currentDialogue = 0;
    private Levels_Info saveData;
private String saveFilename;




    private Stage stage;
    private Scene scene;
   



    private Level_1_Mechanics engine;
    private GameLevelData levelData;
    private List<Level_1_Commands> commandsList;
    private int currentCommandIndex = 0;
    private int currentRunSteps = 0;
    private int levelNumber = 1;

    // FXML lifecycle hook: initialize the default level after controls are injected.
    public void initialize() {
        saveData = new Levels_Info();
        updateStepCounter();
        configureLevel();
        setLevelDialogues();
        updateVisuals();
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
        configureLevel();
        setLevelDialogues();
        updateVisuals();
    }

    private void configureLevel() {
        // Match the data configuration to this screen's grid and reset the engine.
        switch (levelNumber) {
            case 1:
                levelData = Level_1_Data.LEVEL;
                break;
            case 2:
                levelData = Level_2_Data.LEVEL;
                break;
            default:
                throw new IllegalArgumentException("No data is configured for level " + levelNumber);
        }
        engine = new Level_1_Mechanics(levelData);
        if (levelData.getWidth() != grid.getColumnConstraints().size()
            || levelData.getHeight() != grid.getRowConstraints().size()) {
            throw new IllegalStateException(
                "Level tile table dimensions must match the game-scene grid."
            );
        }
        levelBackground.setImage(loadImage(levelData.getBackgroundPath()));
    }

    private void setLevelDialogues() {
        setDialogues(levelData.getDialogues());
    }

public void setSaveFilename(String saveFilename) {

    this.saveFilename = saveFilename;

    System.out.println("Save file received: " + saveFilename);

    if (saveFilename != null && !saveFilename.isEmpty()) {
        saveData.loadGame(saveFilename);
    }
}

    @FXML
    private void onReturnToMapClicked() throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/fxml/game_map.fxml")
        );
        Parent root = loader.load();

        GameMap gameMap = loader.getController();
        gameMap.setSaveFilename(saveFilename);

        stage = (Stage) mainMenuButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Coders' Haven");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
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
        // Convert submitted Python to commands first, then animate them on the JavaFX thread.
        System.out.println("===== RUN BUTTON PRESSED =====");
        
        // Reset engine to initial state before running new code
        configureLevel();
        currentRunSteps = 0;
        updateStepCounter();
        
        
        String userCode = code.getText();
        System.out.println("User code received:");
        System.out.println(userCode);
        
        System.out.println("Calling Python_Reader.runPython()...");
        try {
            commandsList = Python_Reader.runPython(userCode);
        } catch (Python_Reader.PythonExecutionException e) {
            System.err.println("Sandboxed Python run failed: " + e.getMessage());
            finishFailedRun(
                "The sandboxed Python run failed:\n" + e.getMessage()
            );
            return;
        } catch (IOException e) {
            e.printStackTrace();
            finishFailedRun(
                "Python could not run this code. Check your code and try again."
            );
            return;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            finishFailedRun("The Python run was interrupted. Please try again.");
            return;
        }
        
        if(commandsList == null){
            System.out.println("ERROR: Python returned null!");
            finishFailedRun(
                "The hero did not reach the goal. There may be another way to solve this level. Keep trying!"
            );
            return;
        }
        
        System.out.println("Commands returned from Python:");
        System.out.println(commandsList);
        
        if(commandsList.isEmpty()){
            System.out.println("WARNING: No commands returned.");
            finishFailedRun(
                "The hero did not reach the goal. There may be another way to solve this level. Keep trying!"
            );
            return;
        }
        
        // Start executing commands with delay
        currentCommandIndex = 0;
        updateVisuals(); // Initial visual update
        executeNextCommand();
    }

    private void finishFailedRun(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Level Failed");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();

        engine.resetGame();
        updateVisuals();
    }
    
    private void executeNextCommand() throws IOException{
       
        // Check if all commands are executed
        if (currentCommandIndex >= commandsList.size()) {
            System.out.println("===== RUN FINISHED =====");
            finishFailedRun(
                "The hero did not reach the goal. There may be another way to solve this level. Keep trying!"
            );
            return;
        }
        
        // Check if level is already complete
        

        // Check if hero is dead
        if (engine.checkHeroDead()) {
            System.out.println("HERO IS DEAD!");
            System.out.println("===== RUN FINISHED =====");
            finishFailedRun(
                "The hero did not reach the goal. There may be another way to solve this level. Keep trying!"
            );
            return;
        }
        
        // Get and execute current command
        Level_1_Commands cmd = commandsList.get(currentCommandIndex);
        int step = currentCommandIndex + 1;
        currentRunSteps++;
        updateStepCounter();
        
        System.out.println("Executing step " + step + ": " + cmd);
        
        engine.executeCommand(cmd);
        
        System.out.println("Hero position after command:");
        System.out.println("X = " + engine.getHeroX());
        System.out.println("Y = " + engine.getHeroY());

        updateVisuals();
        String tileMessage = engine.getLastMovementMessage();
        if (tileMessage != null && !tileMessage.isEmpty()) {
            showTileMessage(tileMessage);
            return;
        }

        continueAfterCommand();
    }

    private void continueAfterCommand() {
        if (engine.checkWin()) {
            finishLevel();
            return;
        }

        currentCommandIndex++;

        PauseTransition delay = new PauseTransition(Duration.millis(1000));
        delay.setOnFinished(event -> {
            try {
                executeNextCommand();
            } catch (IOException e) {
                throw new RuntimeException("Could not continue command execution.", e);
            }
        });
        delay.play();
    }

    private void finishLevel() {
        // Persist progress and offer the player a route back to the level map.
        System.out.println("LEVEL COMPLETE!");
        System.out.println("===== RUN FINISHED =====");

        saveData.passLevel(levelNumber);
        saveData.setLevels(Math.max(saveData.getLevels(), levelNumber));
        saveData.setSteps(currentRunSteps);
        saveData.recordBestSteps(levelNumber, currentRunSteps);

        if (saveFilename != null && !saveFilename.isEmpty()) {
            saveData.saveGame(saveFilename);
            System.out.println("Level " + levelNumber + " saved to: " + saveFilename);
        }

        ButtonType backToMap = new ButtonType(
            "Back to Map",
            ButtonBar.ButtonData.OK_DONE
        );
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Level Complete");
        alert.setHeaderText("You won!");
        alert.setContentText(
            "Level passed in " + currentRunSteps + " steps.\n"
                + "Best run: " + saveData.getBestSteps(levelNumber) + " steps.\n"
                + "You can revisit this level from the map at any time."
        );
        alert.getButtonTypes().setAll(backToMap);
        alert.setOnHidden(event -> {
            try {
                openGameMap();
            } catch (IOException e) {
                throw new RuntimeException("Could not return to the game map.", e);
            }
        });
        alert.show();
    }

    private void openGameMap() throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/fxml/game_map.fxml")
        );
        Parent root = loader.load();

        GameMap gameMap = loader.getController();
        gameMap.setSaveFilename(saveFilename);
        gameMap.markLevelCompleted(levelNumber);

        stage = (Stage) Run.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Coders' Haven");
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }
    
    private void updateVisuals() {
        // Synchronize map objects and their images with the current engine state.
        setGridPosition(hero, engine.getHeroX(), engine.getHeroY());
        setGridPosition(button, levelData.getButtonX(), levelData.getButtonY());
        setGridPosition(doorclosed, levelData.getDoorX(), levelData.getDoorY());

        engine.updateButtonState();

        String doorImagePath = engine.isDoorOpen()
            ? "/com/example/css/door-open.png"
            : "/com/example/css/door (2).png";
        String buttonImagePath = engine.isDoorOpen()
            ? "/com/example/css/button-pressed.png"
            : "/com/example/css/button.png";
        doorclosed.setImage(loadImage(doorImagePath));
        button.setImage(loadImage(buttonImagePath));
    }

    private void setGridPosition(ImageView image, int x, int y) {
        GridPane.setColumnIndex(image, x);
        GridPane.setRowIndex(image, y);
    }

    private Image loadImage(String resourcePath) {
        URL resource = getClass().getResource(resourcePath);
        if (resource == null) {
            throw new IllegalStateException("Could not find game image: " + resourcePath);
        }
        return new Image(resource.toExternalForm());
    }

    private void showTileMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Level " + levelNumber);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setOnHidden(event -> continueAfterCommand());
        alert.show();
    }

    private void updateStepCounter() {
        if (stepCounter != null) {
            stepCounter.setText("Steps: " + currentRunSteps);
        }
    }


   
    @FXML
    private void onScrollclick() {
        ScrollPane commandContent = new ScrollPane(createCommandReference());
        commandContent.setFitToWidth(true);
        commandContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        commandContent.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        openInfoPopup("Available Commands - Level " + levelNumber, commandContent);
    }

    @FXML
    private void onBookclick() {
        ScrollPane syntaxContent = new ScrollPane(createSyntaxReference());
        syntaxContent.setFitToWidth(true);
        syntaxContent.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        syntaxContent.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        openInfoPopup("Useful Syntax", syntaxContent);
    }

    @FXML
    private void onInfoPopupCloseClicked() {
        infoPopup.setVisible(false);
        infoPopup.setManaged(false);
        infoPopupContent.getChildren().clear();
    }

    private void openInfoPopup(String title, Parent content) {
        if (!(content instanceof Region)) {
            throw new IllegalArgumentException("Popup content must have a resizable root.");
        }
        Region contentRegion = (Region) content;
        contentRegion.setMinSize(0, 0);
        contentRegion.setPrefSize(1080, 650);
        contentRegion.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        infoPopupTitle.setText(title);
        infoPopupContent.getChildren().setAll(content);
        infoPopup.setManaged(true);
        infoPopup.setVisible(true);
    }

    private VBox createCommandReference() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30, 40, 30, 40));
        content.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #ce1818, #c27305);");

        Label introduction = new Label(levelData.getCommandHelpIntroduction());
        introduction.setWrapText(true);
        introduction.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
        content.getChildren().add(introduction);

        for (GameLevelData.HelpSection section : levelData.getCommandHelpSections()) {
            content.getChildren().add(
                createSyntaxSection(
                    section.getHeading(),
                    section.getExplanation(),
                    section.getExample()
                )
            );
        }
        return content;
    }

    private VBox createSyntaxReference() {
        // Build the in-game Python help content without a separate FXML file.
        VBox content = new VBox(18);
        content.setPadding(new Insets(30, 40, 30, 40));
        content.setStyle("-fx-background-color: linear-gradient(from 0% 0% to 0% 100%, #ce1818, #c27305);");

        Label title = new Label("Useful Syntax for Python");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");
        content.getChildren().add(title);

        content.getChildren().addAll(
            createSyntaxSection(
                "Encapsulated Code",
                "A line ending with a colon starts a code block. Indent every line inside that block.",
                "if hero.can_move_right():\n    hero.move_right()\n    hero.move_right()\nhero.move_left()"
            ),
            createSyntaxSection(
                "If",
                "Use if to run code only when a condition is True.",
                "if hero.can_move_right():\n    hero.move_right()"
            ),
            createSyntaxSection(
                "If / Else",
                "Use else to run alternate code when the if condition is False.",
                "if hero.can_move_up():\n    hero.move_up()\nelse:\n    hero.move_right()"
            ),
            createSyntaxSection(
                "While Loop",
                "Repeat a block while its condition remains True.",
                "while hero.can_move_right():\n    hero.move_right()"
            ),
            createSyntaxSection(
                "For Loop",
                "Use range to repeat a block a specific number of times.",
                "for i in range(3):\n    hero.move_right()"
            ),
            createSyntaxSection(
                "Functions",
                "Define reusable commands with def, then call the function by name.",
                "def go_right_twice():\n    hero.move_right()\n    hero.move_right()\n\ngo_right_twice()"
            ),
            createSyntaxSection(
                "Variables",
                "Store a value in a variable and use it later.",
                "steps = 3\nfor i in range(steps):\n    hero.move_right()"
            ),
            createSyntaxSection(
                "Lists",
                "A list stores multiple values in one variable.",
                "directions = [\"up\", \"right\", \"down\"]\nfor direction in directions:\n    hero.move(direction)"
            ),
            createSyntaxSection(
                "Boolean Values",
                "A boolean value is either True or False.",
                "door_open = True\nif door_open:\n    hero.move_right()"
            ),
            createSyntaxSection(
                "Comparison Operators",
                "Compare values with ==, !=, >, <, >=, and <=.",
                "coins = 5\nif coins >= 3:\n    hero.open_chest()"
            )
        );
        return content;
    }

    private VBox createSyntaxSection(String heading, String explanation, String example) {
        VBox section = new VBox(8);
        Label headingLabel = new Label(heading);
        headingLabel.setStyle("-fx-text-fill: #67262f; -fx-font-size: 24px; -fx-font-weight: bold;");

        Label explanationLabel = new Label(explanation);
        explanationLabel.setWrapText(true);
        explanationLabel.setStyle("-fx-text-fill: white; -fx-font-size: 17px;");

        Label exampleLabel = new Label(example);
        exampleLabel.setWrapText(true);
        exampleLabel.setStyle(
            "-fx-text-fill: black; -fx-font-family: 'Monospaced'; -fx-font-size: 15px; " +
            "-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 14px;"
        );
        section.getChildren().addAll(headingLabel, explanationLabel, exampleLabel);
        return section;
    }
}
