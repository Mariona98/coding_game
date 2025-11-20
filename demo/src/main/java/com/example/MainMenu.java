package com.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
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

    private AudioClip clickSound;
    private MediaPlayer backgroundMusic;
    private static Scene scene;
    private static Stage stage;

    @FXML
    public void initialize() {
        
        clickSound = new AudioClip(getClass().getResource("click.wav").toExternalForm());

        Media bgMusic = new Media(getClass().getResource("background.mp3").toExternalForm());
        backgroundMusic = new MediaPlayer(bgMusic);
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundMusic.play();

        
        MusicToggle.selectedProperty().addListener((obs, wasOn, isOn) -> {
            MusicToggle.setText(isOn ? "❌" : "🎵");
            if (isOn) backgroundMusic.pause();
            else backgroundMusic.play();
        });

        
        SoundToggle.selectedProperty().addListener((obs, wasOn, isOn) -> {
            SoundToggle.setText(isOn ? "🔇" : "🔊");
            if (isOn) clickSound.setVolume(0);
            else clickSound.setVolume(1);
        });
    }

    @FXML
    private void onNewGameClicked() throws IOException {
        clickSound.play();
        System.out.println("New Game clicked!");
       Parent root=FXMLLoader.load(getClass().getResource("/com/example/game_map.fxml"));
       stage=(Stage)NewGame.getScene().getWindow();
       scene=new Scene(root);
         stage.setScene(scene);
            stage.show();
        
    }

    @FXML
    private void onLoadGameClicked() {
        clickSound.play();
        System.out.println("Load Game clicked!");
       
    }

    @FXML
    private void onSettingsClicked() {
        clickSound.play();
        System.out.println("Settings clicked!");
        
    }

    @FXML
    private void onAboutClicked() {
        clickSound.play();
        System.out.println("About clicked!");
        
    }
    
    @FXML
    private void clickSound() {
        clickSound.play();
    }
    
}
