package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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

    @FXML
    public void initialize() {
        
        clickSound = new AudioClip(getClass().getResource("click.mp3").toExternalForm());

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
        });
    }

    @FXML
    private void onNewGameClicked() {
        clickSound.play();
        System.out.println("New Game clicked!");
        
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
}
