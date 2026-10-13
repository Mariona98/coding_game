package com.example;

// This controller is currently unused; defaults and settings actions are placeholders.
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * Separate settings-screen controller with placeholder defaults and console-only
 * save behavior. It is not currently referenced by the project's FXML or Java
 * navigation; the active settings controls are managed by {@link MainMenu}.
 * Keep this class only if a separate settings window is planned.
 */
public class SettingsMenu {
    
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
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button resetButton;
    
    @FXML
    public void initialize() {
        
        difficultyComboBox.getItems().setAll("Easy", "Normal", "Hard");
        languageComboBox.getItems().setAll("English");
        resolutionComboBox.getItems().setAll("1280x720", "1920x1080");
        fullscreenComboBox.getItems().setAll("Off", "On");

        loadSettings();
        
        
        setupListeners();
    }
    
    private void loadSettings() {
        
        volumeSlider.setValue(0.75);
        difficultyComboBox.setValue("Normal");
        languageComboBox.setValue("English");
        resolutionComboBox.setValue("1280x720");
        fullscreenComboBox.setValue("Off");
    }
    
    private void setupListeners() {
        
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            
            System.out.println("Volume changed to: " + newVal);
        });
    }
    
    @FXML
    private void onSaveClicked() {
        
        saveSettings();
        
        
        onCancelClicked();
    }
    
    private void saveSettings() {
        
        double volume = volumeSlider.getValue();
        String difficulty = difficultyComboBox.getValue();
        String language = languageComboBox.getValue();
        String resolution = resolutionComboBox.getValue();
        String fullscreen = fullscreenComboBox.getValue();
        
        System.out.println("Settings saved:");
        System.out.println("  Volume: " + volume);
        System.out.println("  Difficulty: " + difficulty);
        System.out.println("  Language: " + language);
        System.out.println("  Resolution: " + resolution);
        System.out.println("  Fullscreen: " + fullscreen);
        
        
    }
    
    @FXML
    private void onCancelClicked() {
        
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        
        // load main menu scene
       
    }
    
    @FXML
    private void onResetClicked() {
        
        volumeSlider.setValue(0.75);
        difficultyComboBox.setValue("Normal");
        languageComboBox.setValue("English");
        resolutionComboBox.setValue("1280x720");
        fullscreenComboBox.setValue("Off");
        
        System.out.println("Settings reset to default");
    }
}