package com.example;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import java.io.File;

public class MainMenu {

    @FXML
    private Button NewGame;

    @FXML
    private Button LoadGame;

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

    private AudioClip clickSound;
    private MediaPlayer backgroundMusic;

    private static Scene scene;
    private static Stage stage;
     private final File saveDirectory =new File("saves");

    @FXML
    public void initialize() {

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
        selectedSave
    );

    stage =
        (Stage) LoadGame.getScene().getWindow();

    scene =
        new Scene(root, 1260, 810);

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

        // Save the game
        if (!saveDirectory.exists()) {
    saveDirectory.mkdirs();
}

saveData.saveGame(
    new File(
        saveDirectory,
        username + ".txt"
    ).getPath()
);

        System.out.println("New game created for: " + username);
       

        // Now open the game map
        Parent root = FXMLLoader.load(
            getClass().getResource("/com/example/fxml/game_map.fxml")
        );

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

    // =========================
    // SETTINGS
    // =========================

    @FXML
    private void onSettingsClicked() throws IOException {

        clickSound.play();

        System.out.println("Settings clicked!");

        Parent root = FXMLLoader.load(
            getClass().getResource("/com/example/Settings.fxml")
        );

        stage = (Stage) NewGame.getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    // =========================
    // ABOUT
    // =========================

    @FXML
    private void onAboutClicked() throws IOException {

        clickSound.play();

        Font irishGrover = Font.loadFont(
            getClass().getResourceAsStream(
                "/com/example/fonts/IrishGrover-Regular.ttf"
            ),
            48
        );

        if (irishGrover == null) {
            throw new IllegalStateException(
                "Could not load Irish Grover font"
            );
        }

        Parent root = FXMLLoader.load(
            getClass().getResource("/com/example/About.fxml")
        );

        stage = (Stage) NewGame.getScene().getWindow();
        scene = new Scene(root);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        System.out.println("About clicked!");
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