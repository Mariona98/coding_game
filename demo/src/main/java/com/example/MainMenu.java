package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import java.io.File;

/**
 * Controller for the main menu, save selection/creation, settings, and audio.
 * Its methods are connected to controls in MainMenu.fxml via {@code onAction}.
 */
public class MainMenu {

    @FXML
    private Button NewGame;

    @FXML
    private Button LoadGame;

    @FXML
    private Button ContinueGame;

    @FXML
    private Button Settings;

    @FXML
    private Button About;

    @FXML
    private ToggleButton MusicToggle;

    @FXML
    private ToggleButton SoundToggle;

    @FXML
    private Pane savePane;

    @FXML
    private TextField usernameField;

    @FXML
    private Button loadSelectedGame;

    @FXML
    private Pane loadPane;

    @FXML
    private ListView<String> saveList;

    @FXML
    private Pane settingsPane;

    @FXML
    private Pane aboutPane;

    @FXML
    private Slider volumeSlider;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> resolutionComboBox;

    @FXML
    private ComboBox<String> fullscreenComboBox;

    private AudioClip clickSound;
    private MediaPlayer backgroundMusic;

    private static Scene scene;
    private static Stage stage;
     private final File saveDirectory =new File("saves");
    private String continueSaveFilename;
    private int continueLevelNumber = 1;
    private boolean continueToMap;

    public void setContinueGame(String saveFilename, int levelNumber) {
        continueSaveFilename = saveFilename;
        continueLevelNumber = levelNumber;
        continueToMap = false;
        setContinueVisible(saveFilename);
    }

    public void setContinueMap(String saveFilename) {
        continueSaveFilename = saveFilename;
        continueToMap = true;
        setContinueVisible(saveFilename);
    }

    private void setContinueVisible(String saveFilename) {
        boolean canContinue = saveFilename != null && !saveFilename.isEmpty();
        ContinueGame.setVisible(canContinue);
        ContinueGame.setManaged(canContinue);
    }

    @FXML
    public void initialize() {

        // Populate the current placeholder settings options and default values.
        difficultyComboBox.getItems().setAll("Easy", "Normal", "Hard");
        languageComboBox.getItems().setAll("English");
        resolutionComboBox.getItems().setAll("1280x720", "1920x1080");
        fullscreenComboBox.getItems().setAll("Off", "On");
        resetSettings();

        clickSound = new AudioClip(
            getClass().getResource("click.wav").toExternalForm()
        );

        Media bgMusic = new Media(
            getClass().getResource("background.mp3").toExternalForm()
        );

        backgroundMusic = new MediaPlayer(bgMusic);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusic.play();

        MusicToggle.selectedProperty().addListener((obs, wasOn, isOn) -> {
            MusicToggle.setText(isOn ? "❌" : "🎵");

            if (isOn) {
                backgroundMusic.pause();
            } else {
                backgroundMusic.play();
            }
        });

        SoundToggle.selectedProperty().addListener((obs, wasOn, isOn) -> {
            SoundToggle.setText(isOn ? "🔇" : "🔊");

            if (isOn) {
                clickSound.setVolume(0);
            } else {
                clickSound.setVolume(1);
            }
        });
    }

    @FXML
private void onCancelLoadClicked() {

    clickSound.play();

    loadPane.setVisible(false);

    saveList.getSelectionModel().clearSelection();
}

    // =========================
    // NEW GAME
    // =========================

    @FXML
    private void onNewGameClicked() {

        clickSound.play();

        System.out.println("New Game clicked!");

        // Show username pane
        savePane.setVisible(true);

        usernameField.clear();
        usernameField.requestFocus();
    }


    @FXML
private void onLoadSelectedGameClicked() throws IOException {

    clickSound.play();

    String selectedSave =
        saveList.getSelectionModel().getSelectedItem();

    if (selectedSave == null ||
        selectedSave.equals("No saved games found.")) {

        Alert alert = new Alert(
            Alert.AlertType.WARNING
        );

        alert.setTitle("Load Game");
        alert.setHeaderText(null);
        alert.setContentText(
            "Please select a saved game."
        );

        alert.show();

        return;
    }

    System.out.println(
        "Loading save: " + selectedSave
    );

    FXMLLoader loader = new FXMLLoader(
        getClass().getResource(
            "/com/example/fxml/game_map.fxml"
        )
    );

    Parent root = loader.load();

    GameMap gameMap =
        loader.getController();

    // Pass save filename to GameMap
    gameMap.setSaveFilename(
        new File(saveDirectory, selectedSave).getPath()
    );

    stage =
        (Stage) LoadGame.getScene().getWindow();

    scene =
        new Scene(root);

    stage.setScene(scene);
    stage.centerOnScreen();
    stage.show();
}
    // =========================
    // SAVE NEW GAME
    // =========================

    @FXML
    private void onSaveGameClicked() throws IOException {

        clickSound.play();

        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            usernameField.setPromptText("Please enter a username!");
            usernameField.requestFocus();
            return;
        }

        // Create new save data
        Levels_Info saveData = new Levels_Info();

        saveData.setMap(1);
        saveData.setLevels(0);
        saveData.setSteps(0);

        // Create the save folder on first use, then write a new save file.
        if (!saveDirectory.exists()) {
    saveDirectory.mkdirs();
}

File saveFile = new File(saveDirectory, username + ".txt");
saveData.saveGame(saveFile.getPath());

        System.out.println("New game created for: " + username);
       

        // Now open the game map
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/fxml/game_map.fxml")
        );
        Parent root = loader.load();

        GameMap gameMap = loader.getController();
        gameMap.setSaveFilename(saveFile.getPath());

        stage = (Stage) NewGame.getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    // =========================
    // CANCEL
    // =========================

    @FXML
    private void onCancelSaveClicked() {

        clickSound.play();

        savePane.setVisible(false);
        usernameField.clear();
    }

    // =========================
    // LOAD GAME
    // =========================

 @FXML
private void onLoadGameClicked() {

    clickSound.play();

    System.out.println("Load Game clicked!");

    loadSaveFiles();

    loadPane.setVisible(true);
}

    @FXML
    private void onContinueGameClicked() throws IOException {
        if (continueSaveFilename == null || continueSaveFilename.isEmpty()) {
            return;
        }

        if (continueToMap) {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/fxml/game_map.fxml")
            );
            Parent root = loader.load();

            GameMap gameMap = loader.getController();
            gameMap.setSaveFilename(continueSaveFilename);

            stage = (Stage) ContinueGame.getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            return;
        }

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/example/fxml/GameScene.fxml")
        );
        Parent root = loader.load();

        GameScreen gameScreen = loader.getController();
        gameScreen.setLevelNumber(continueLevelNumber);
        gameScreen.setSaveFilename(continueSaveFilename);

        stage = (Stage) ContinueGame.getScene().getWindow();
        scene = new Scene(root, 1260, 810);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    // =========================
    // SETTINGS
    // =========================

    @FXML
    private void onSettingsClicked() {

        clickSound.play();
        settingsPane.setVisible(true);
    }

    @FXML
    private void onSettingsSaveClicked() {
        System.out.println("Settings saved:");
        System.out.println("  Volume: " + volumeSlider.getValue());
        System.out.println("  Difficulty: " + difficultyComboBox.getValue());
        System.out.println("  Language: " + languageComboBox.getValue());
        System.out.println("  Resolution: " + resolutionComboBox.getValue());
        System.out.println("  Fullscreen: " + fullscreenComboBox.getValue());
        settingsPane.setVisible(false);
    }

    @FXML
    private void onSettingsResetClicked() {
        resetSettings();
        System.out.println("Settings reset to default");
    }

    @FXML
    private void onSettingsCancelClicked() {
        settingsPane.setVisible(false);
    }

    private void resetSettings() {
        volumeSlider.setValue(0.75);
        difficultyComboBox.setValue("Normal");
        languageComboBox.setValue("English");
        resolutionComboBox.setValue("1280x720");
        fullscreenComboBox.setValue("Off");
    }

    // =========================
    // ABOUT
    // =========================

    @FXML
    private void onAboutClicked() {

        clickSound.play();
        aboutPane.setVisible(true);
    }

    @FXML
    private void onAboutCloseClicked() {
        aboutPane.setVisible(false);
    }

    // =========================
    // GENERAL CLICK SOUND
    // =========================

    @FXML
    private void clickSound() {
        clickSound.play();
    }
    private void loadSaveFiles() {

    saveList.getItems().clear();

    if (!saveDirectory.exists()) {
        saveDirectory.mkdirs();
    }

    File[] files = saveDirectory.listFiles(
        file -> file.isFile()
            && file.getName().toLowerCase().endsWith(".txt")
    );

    if (files == null || files.length == 0) {

        saveList.getItems().add(
            "No saved games found."
        );

        return;
    }

    for (File file : files) {

        saveList.getItems().add(
            file.getName()
        );
    }
}
}