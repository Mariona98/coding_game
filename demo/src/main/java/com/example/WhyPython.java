package com.example;
// simple pane for information about python , to be added in the menu section 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class WhyPython extends Application {

    public TextFlow createTextFlow() {
        TextFlow textFlow = new TextFlow();
        textFlow.setPrefWidth(580);  
        textFlow.setStyle("-fx-padding: 20; -fx-background-color: #ffffff; -fx-font-family: 'Segoe UI', Arial, sans-serif;");

        
        Text mainTitle = new Text("Python Introduction\n\n");
        mainTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-fill: #4CAF50;");

        
        Text whatIsTitle = new Text("What is Python?\n");
        whatIsTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #333333;");

        Text whatIsText = new Text("Python is a popular programming language. It was created by Guido van Rossum, and released in 1991.\n\n");
        whatIsText.setStyle("-fx-font-size: 14px; -fx-fill: #555555;");

        Text usedForTitle = new Text("It is used for:\n");
        usedForTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-fill: #666666;");

        
        Text bullet1 = new Text("• web development (server-side),\n");
        Text bullet2 = new Text("• software development,\n");
        Text bullet3 = new Text("• mathematics,\n");
        Text bullet4 = new Text("• system scripting.\n\n");
        String bulletStyle = "-fx-font-size: 14px; -fx-fill: #2b5b84;";
        bullet1.setStyle(bulletStyle);
        bullet2.setStyle(bulletStyle);
        bullet3.setStyle(bulletStyle);
        bullet4.setStyle(bulletStyle);

        
        Text canDoTitle = new Text("What can Python do?\n");
        canDoTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #333333;");

        Text canDo1 = new Text("• Python can be used on a server to create web applications.\n");
        Text canDo2 = new Text("• Python can be used alongside software to create workflows.\n");
        Text canDo3 = new Text("• Python can connect to database systems. It can also read and modify files.\n");
        Text canDo4 = new Text("• Python can be used to handle big data and perform complex mathematics.\n");
        Text canDo5 = new Text("• Python can be used for rapid prototyping, or for production-ready software development.\n\n");
        String canDoStyle = "-fx-font-size: 14px; -fx-fill: #2b5b84;";
        canDo1.setStyle(canDoStyle);
        canDo2.setStyle(canDoStyle);
        canDo3.setStyle(canDoStyle);
        canDo4.setStyle(canDoStyle);
        canDo5.setStyle(canDoStyle);

        
        Text whyTitle = new Text("Why Python?\n");
        whyTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #333333;");

        Text why1 = new Text("• Python works on different platforms (Windows, Mac, Linux, Raspberry Pi, etc).\n");
        Text why2 = new Text("• Python has a simple syntax similar to the English language.\n");
        Text why3 = new Text("• Python has syntax that allows developers to write programs with fewer lines than some other programming languages.\n");
        Text why4 = new Text("• Python runs on an interpreter system, meaning that code can be executed as soon as it is written. This means that prototyping can be very quick.\n");
        Text why5 = new Text("• Python can be treated in a procedural way, an object-oriented way or a functional way.\n\n");
        String whyStyle = "-fx-font-size: 14px; -fx-fill: #2b5b84;";
        why1.setStyle(whyStyle);
        why2.setStyle(whyStyle);
        why3.setStyle(whyStyle);
        why4.setStyle(whyStyle);
        why5.setStyle(whyStyle);

        
        Text goodToKnowTitle = new Text("Good to know\n");
        goodToKnowTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #333333;");

        Text good1 = new Text("• The most recent major version of Python is Python 3, which we shall be using in this tutorial.\n");
        Text good2 = new Text("• In this tutorial Python will be written in a text editor. It is possible to write Python in an Integrated Development Environment, such as Thonny, Pycharm, Netbeans or Eclipse which are particularly useful when managing larger collections of Python files.\n\n");
        String goodStyle = "-fx-font-size: 14px; -fx-fill: #2b5b84;";
        good1.setStyle(goodStyle);
        good2.setStyle(goodStyle);

        
        Text syntaxTitle = new Text("Python Syntax compared to other programming languages\n");
        syntaxTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-fill: #333333;");

        Text syntax1 = new Text("• Python was designed for readability, and has some similarities to the English language with influence from mathematics.\n");
        Text syntax2 = new Text("• Python uses new lines to complete a command, as opposed to other programming languages which often use semicolons or parentheses.\n");
        Text syntax3 = new Text("• Python relies on indentation, using whitespace, to define scope; such as the scope of loops, functions and classes. Other programming languages often use curly-brackets for this purpose.\n\n");
        String syntaxStyle = "-fx-font-size: 14px; -fx-fill: #2b5b84;";
        syntax1.setStyle(syntaxStyle);
        syntax2.setStyle(syntaxStyle);
        syntax3.setStyle(syntaxStyle);

        
        textFlow.getChildren().addAll(
            mainTitle,
            whatIsTitle, whatIsText, usedForTitle, bullet1, bullet2, bullet3, bullet4,
            canDoTitle, canDo1, canDo2, canDo3, canDo4, canDo5,
            whyTitle, why1, why2, why3, why4, why5,
            goodToKnowTitle, good1, good2,
            syntaxTitle, syntax1, syntax2, syntax3
        );

        return textFlow;
    }

    @Override
    public void start(Stage primaryStage) {
        TextFlow textFlow = createTextFlow();
        
       
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(textFlow);
        scrollPane.setFitToWidth(true);  // Makes content fit horizontally
        scrollPane.setFitToHeight(false); // Allows vertical scrolling
        scrollPane.setPrefViewportWidth(650);  // Standard width for the viewport
        scrollPane.setPrefViewportHeight(600); // Standard height for the viewport
        scrollPane.setStyle("-fx-background-color: #ffffff;");
        
        
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        
        Scene scene = new Scene(scrollPane, 680, 650);
        primaryStage.setTitle("W3Schools Python Introduction");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}