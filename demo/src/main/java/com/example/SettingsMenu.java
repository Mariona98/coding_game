package com.example;

//default settings, to be changed later 
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

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