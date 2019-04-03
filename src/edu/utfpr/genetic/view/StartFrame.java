/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.genetic.view;

import edu.utfpr.genetic.Session;
import edu.utfpr.genetic.SessionListener;
import edu.utfpr.genetic.data.Chromosome;
import edu.utfpr.genetic.data.imp.BackPackBinaryChromossome;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.FormatStringConverter;

/**
 *
 * @author henrique
 */
public class StartFrame extends Application implements SessionListener{
    private BooleanProperty running = new SimpleBooleanProperty(false);
    private boolean simpleMode = false;
    private Session currentSession;
    
    private GridPane customizePane;
    private BorderPane basicRunPane;
    
    // CustomizeRun Controllers
    
    // Pool Size
    private Slider poolSize;
    // Child generation
    private Slider childPerGeneration;
    // Stop condition type
    private ChoiceBox<String> stopConditionType;
    // Stop condition parameter
    private Slider conditionA;
    private Slider conditionB;
    // Infactibility treatment type
    private ChoiceBox<String> infactTreatment;
    // Mutation chance
    private Slider mutaChanceSlider;
    private TextField mutaChanceField;
    // Cross over chance
    private Slider cOverSlider;
    private TextField cOverField;
    // Codification type
    private ChoiceBox<Chromosome> codification;
    // progress bar
    private ProgressBar progressBar;
    // Seed
    private TextField seedField;
    // Output
    private TextArea outputArea;
    // Start/Stop Button
    private Button startPauseB;
    
    @Override
    public void start(Stage primaryStage) {
        
        poolSize = new Slider(5, 1000, 50);
        poolSize.setBlockIncrement(5);
        poolSize.setMinorTickCount(5);
        poolSize.setMajorTickUnit(25);
        poolSize.setSnapToTicks(true);
        
        childPerGeneration = new Slider(5, 1000, 50);
        childPerGeneration.setBlockIncrement(5);
        childPerGeneration.setMinorTickCount(5);
        childPerGeneration.setMajorTickUnit(25);
        childPerGeneration.setSnapToTicks(true);
        
        stopConditionType = new ChoiceBox<>();
        stopConditionType.getItems().addAll("Generations count", "Fitness evaluations");
        
        conditionA = new Slider(1, 500, 100);
        conditionA.setBlockIncrement(10);
        childPerGeneration.setMinorTickCount(10);
        childPerGeneration.setMajorTickUnit(50);
        childPerGeneration.setSnapToTicks(true);
        
        conditionB = new Slider(1, 1000000, 10000);
        conditionB.setBlockIncrement(100);
        childPerGeneration.setMinorTickCount(10);
        childPerGeneration.setMajorTickUnit(1000);
        childPerGeneration.setSnapToTicks(true);
        
        infactTreatment = new ChoiceBox<>();
        infactTreatment.getItems().addAll("Repair chromossomes", "Eliminate chromossomes");
        
        mutaChanceSlider = new Slider(0, 100, 5);
        mutaChanceSlider.setBlockIncrement(0.5);
        mutaChanceSlider.setMinorTickCount(10);
        mutaChanceSlider.setMajorTickUnit(5);
        mutaChanceSlider.setSnapToTicks(true);
        
        mutaChanceField = new TextField();
        mutaChanceField.textProperty().bind(mutaChanceSlider.valueProperty().asString());
        
        cOverSlider = new Slider(0, 100, 80);
        cOverSlider.setBlockIncrement(0.5);
        cOverSlider.setMinorTickCount(10);
        cOverSlider.setMajorTickUnit(5);
        cOverSlider.setSnapToTicks(true);
        
        cOverField = new TextField();
        cOverField.textProperty().bind(cOverSlider.valueProperty().asString());
        
        customizePane =  new GridPane();
        
        GridPane.setConstraints(poolSize, 0, 0);
        GridPane.setConstraints(childPerGeneration, 0, 1);
        GridPane.setConstraints(stopConditionType, 1, 0);
        GridPane.setConstraints(infactTreatment, 1, 1);
        GridPane.setConstraints(cOverSlider, 2, 0);
        GridPane.setConstraints(mutaChanceSlider, 2, 1);
        
        customizePane.getChildren().addAll(poolSize, childPerGeneration, stopConditionType, infactTreatment, cOverSlider, mutaChanceSlider);
        
        seedField = new TextField();
        NumberFormat seedFormat = NumberFormat.getIntegerInstance();
        TextFormatter<Number> formatter = new TextFormatter<>(new FormatStringConverter(seedFormat));
        seedField.setTextFormatter(formatter);
        
        outputArea = new TextArea();
        
        progressBar = new ProgressBar(0.0);
        
        startPauseB = new Button("Start");
        running.addListener((observable) -> {
            if(running.getValue()){
                startPauseB.setText("Stop");
            }else{
                startPauseB.setText("Start");
            }
        });
        
        startPauseB.setOnAction(((event) ->{
            if(running.getValue()){
                stopGA();
            }else{
                runGA();
            }
        }));
        
        Button modeB = new Button("Change mode");
        modeB.setOnAction((event) -> {
            if(simpleMode){
                ((BorderPane)primaryStage.getScene().getRoot()).setTop(customizePane);
                simpleMode = false;
            }else{
                ((BorderPane)primaryStage.getScene().getRoot()).setTop(null);
                simpleMode = true;
            }
        });
        
        basicRunPane = new BorderPane(outputArea);
        basicRunPane.setTop(progressBar);
        basicRunPane.setBottom(new BorderPane(null, null, startPauseB, null, modeB));
        
        BorderPane root = new BorderPane(basicRunPane);
        root.setTop(customizePane);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Run Genetic Algorithm");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void runGA(){
        try {
//            currentSession = new Session<Byte[]>(16350543086l, 0.05, 0.8, 50, 50, BackPackBinaryChromossome.class, 'I', 500, 'r');
            currentSession = new Session<Byte[]>(1636845245286l, 0.05, 0.3, 300, 300, BackPackBinaryChromossome.class, 'I', 500, 'r');
            currentSession.addSessionListner(this);
            Platform.runLater(() -> {
                currentSession.execute();
            });
        } catch (Exception ex) {
            Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        running.setValue(true);
    }
    
    private void stopGA(){
        currentSession.stopExecution();
        currentSession.removeSessionListner(this);
        currentSession = null;
        running.setValue(false);
    }

    @Override
    public void receiveLogInfo(String... logLine) {
        for(String s : logLine){
            outputArea.appendText(s + "\n");
        }
    }

    @Override
    public void receiveStopStatus(char stopReason) {
        currentSession = null;
        running.setValue(false);
    }
}
