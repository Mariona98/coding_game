package com.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
// aka scroll pane for including the specific codes that are available in that level
public class InfoPane {
    
    @FXML
    private ScrollPane scrollPane;
    
    @FXML
    private VBox contentContainer;
    
    @FXML
    private Label titleLabel;
    
    @FXML
    private VBox introSection;
    
    @FXML
    private Text introText;
    
    @FXML
    private VBox section1;
    
    @FXML
    private Text section1Text;
    
    @FXML
    private VBox bulletSection;
    
    @FXML
    private VBox bulletPoints;
    
    @FXML
    private VBox section2;
    
    @FXML
    private Text section2Text;
    
    @FXML
    private VBox notesSection;
    
    @FXML
    private VBox notesList;
    
    @FXML
    private VBox conclusionSection;
    
    @FXML
    private Text conclusionText;
    
    @FXML
    private Button closeButton;
    
    @FXML
    public void initialize() {
       
        System.out.println("Scroll popup initialized");
    }
    
    @FXML
    private void onCloseClicked() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
    
    
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    public void setIntroText(String text) {
        introText.setText(text);
    }
    
    public void setSection1Text(String text) {
        section1Text.setText(text);
    }
    
    public void setSection2Text(String text) {
        section2Text.setText(text);
    }
    
    public void setConclusionText(String text) {
        conclusionText.setText(text);
    }
    
    public void addBulletPoint(String point) {
        Text bullet = new Text("• " + point);
        bullet.setStyle("-fx-fill: #2c2418; -fx-font-size: 13px; -fx-font-family: 'Georgia';");
        bulletPoints.getChildren().add(bullet);
    }
    
    public void clearBulletPoints() {
        bulletPoints.getChildren().clear();
    }
    
    public void addNote(String note) {
        Text noteText = new Text("📌 " + note);
        noteText.setStyle("-fx-fill: #8b4513; -fx-font-size: 13px; -fx-font-family: 'Georgia';");
        notesList.getChildren().add(noteText);
    }
    
    public void clearNotes() {
        notesList.getChildren().clear();
    }
    
    public void setAllContent(String intro, String section1, String section2, String conclusion) {
        this.introText.setText(intro);
        this.section1Text.setText(section1);
        this.section2Text.setText(section2);
        this.conclusionText.setText(conclusion);
    }
    
    public VBox getContentContainer() {
        return contentContainer;
    }
    
   
    public void hideSection1() {
        section1.setVisible(false);
        section1.setManaged(false);
    }
    
    public void hideSection2() {
        section2.setVisible(false);
        section2.setManaged(false);
    }
    
    public void hideBulletPoints() {
        bulletSection.setVisible(false);
        bulletSection.setManaged(false);
    }
    
    public void hideNotes() {
        notesSection.setVisible(false);
        notesSection.setManaged(false);
    }
}