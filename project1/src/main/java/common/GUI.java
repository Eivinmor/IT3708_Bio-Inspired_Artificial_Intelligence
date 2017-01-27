package common;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javafx.util.Duration;
import org.jfree.util.ArrayUtilities;
import task1.*;
import task2.*;
import task3.*;
import task4.*;


public class GUI extends Application{

    private GridPane mapPane;
    private HashMap<Character, String> iconPathArray;
    private int trainingRound, trial, step, renderInterval;
    private Timeline timeline;
    private ArrayList<ArrayList<ArrayList<ArrayList<ArrayList<Character>>>>> gridStorage;
    private Button playButton;
    private TextField renderIntervalField, trainingRoundsField, trialsField, stepsField;

    public GUI(){
        iconPathArray = new HashMap<>(7);
        iconPathArray.put(' ', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\blank.png");
        iconPathArray.put('F', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\food.png");
        iconPathArray.put('P', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\poison.png");
        iconPathArray.put('⇑', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentNorth.png");
        iconPathArray.put('⇒', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentEast.png");
        iconPathArray.put('⇓', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentSouth.png");
        iconPathArray.put('⇐', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentWest.png");
        timeline = new Timeline();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Simulator1 sim = new Simulator1();
        gridStorage = sim.runSimulation();
        renderInterval = 300;
        trainingRound = 1;
        trial = 1;
        step = 1;

        GridPane rootPane = new GridPane();

        mapPane = new GridPane();
        mapPane.setAlignment(Pos.CENTER);
        mapPane.setPadding(new Insets(2, 1, 1, 2));
        mapPane.setStyle("-fx-background-color: #161616;");
        for (int i = 0; i < 10; i++) {
            mapPane.getColumnConstraints().add(new ColumnConstraints(50));
            mapPane.getRowConstraints().add(new RowConstraints(50));
        }

    // BUTTON ROW

        HBox buttonRow = new HBox(20);
        buttonRow.setMinHeight(50);
        buttonRow.setPadding(new Insets(10, 10, 10, 10));
        buttonRow.setAlignment(Pos.BOTTOM_LEFT);

        // PLAY BUTTON
        playButton = new Button("Play");
        playButton.setMinWidth(56);
        playButton.setPadding(new Insets(10, 10, 10, 10));
        playButton.setOnAction(event -> {
            if (Objects.equals(playButton.getText(), "Play")){
                playButton.setText("Pause");
                newRenderInterval(renderInterval);
            }
            else {
                timeline.stop();
                playButton.setText("Play");
            }
        });

        // APPLY RENDER INTERVAL BUTTON
        Button appySettingsButton = new Button("Apply settings");
        appySettingsButton.setMinWidth(40);
        appySettingsButton.setPadding(new Insets(4, 10, 4, 10));
        appySettingsButton.setOnAction(event -> {
            renderInterval = (Integer.parseInt(renderIntervalField.getText()));
            newRenderInterval(renderInterval);
            trainingRound = Integer.parseInt(trainingRoundsField.getText());
            trial = Integer.parseInt(trialsField.getText());
            step = Integer.parseInt(stepsField.getText());
        });

        buttonRow.getChildren().addAll(playButton, appySettingsButton);

    // SETTINGS ROW

        HBox settingsRow = new HBox(20);
        settingsRow.setMinHeight(50);
        settingsRow.setPadding(new Insets(10, 10, 10, 10));
        settingsRow.setAlignment(Pos.CENTER_LEFT);

        // RENDER INTERVAL HBOX
        renderIntervalField = new TextField(Integer.toString(renderInterval));
        renderIntervalField.setAlignment(Pos.BASELINE_RIGHT);
        renderIntervalField.setPrefWidth(50);
        renderIntervalField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) renderIntervalField.setText(newValue.replaceAll("[^\\d]", ""));
        });
        HBox renderIntervalHbox = new HBox(2);
        renderIntervalHbox.setAlignment(Pos.CENTER_LEFT);
        renderIntervalHbox.getChildren().addAll(new Label("Render interval: "), renderIntervalField);


        // TRAINING ROUNDS HBOX
        trainingRoundsField = new TextField(Integer.toString(trainingRound));
        trainingRoundsField.setAlignment(Pos.BASELINE_RIGHT);
        trainingRoundsField.setPrefWidth(50);
        trainingRoundsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) trainingRoundsField.setText(newValue.replaceAll("[^\\d]", ""));
            else if (Integer.parseInt(trainingRoundsField.getText()) > gridStorage.size()) {
                trainingRoundsField.setText(Integer.toString(gridStorage.size()));
            }
        });
        HBox trainingRoundsHbox = new HBox(2);
        trainingRoundsHbox.setAlignment(Pos.CENTER_LEFT);
        trainingRoundsHbox.getChildren().addAll(new Label("Training round: "), trainingRoundsField);

        // TRIALS HBOX
        trialsField = new TextField(Integer.toString(trial));
        trialsField.setAlignment(Pos.BASELINE_RIGHT);
        trialsField.setPrefWidth(50);
        trialsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) trialsField.setText(newValue.replaceAll("[^\\d]", ""));
            else if (Integer.parseInt(trialsField.getText()) > gridStorage.get(0).size()) {
                trialsField.setText(Integer.toString(gridStorage.get(0).size()));
            }
        });
        HBox trialsHbox = new HBox(2);
        trialsHbox.setAlignment(Pos.CENTER_LEFT);
        trialsHbox.getChildren().addAll(new Label("Trial: "), trialsField);

        // STEPS HBOX
        stepsField = new TextField(Integer.toString(step));
        stepsField.setAlignment(Pos.BASELINE_RIGHT);
        stepsField.setPrefWidth(35);
        stepsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) stepsField.setText(newValue.replaceAll("[^\\d]", ""));
            else if (Integer.parseInt(stepsField.getText()) > gridStorage.get(0).get(0).size()) {
                stepsField.setText(Integer.toString(gridStorage.get(0).get(0).size()));
            }
        });
        HBox stepsHbox = new HBox(2);
        stepsHbox.setAlignment(Pos.CENTER_LEFT);
        stepsHbox.getChildren().addAll(new Label("Step: "), stepsField);

        settingsRow.getChildren().addAll(renderIntervalHbox, trainingRoundsHbox, trialsHbox, stepsHbox);
        settingsRow.setAlignment(Pos.BOTTOM_LEFT);


        rootPane.add(buttonRow, 0, 0);
        rootPane.add(settingsRow, 0, 1);
        rootPane.add(mapPane, 0, 2);
        Scene scene = new Scene(rootPane);
        primaryStage.setTitle("Flatland world");
        primaryStage.setScene(scene);
        primaryStage.show();

        drawGrid(gridStorage.get(0).get(0).get(0));
        
    }



//    private void drawGrid(char[][] charGrid){
//        mapPane.getChildren().clear();
//        mapPane.setGridLinesVisible(true);
//        for (int i = 0; i < charGrid.length; i++) {
//            for (int j = 0; j < charGrid[i].length; j++) {
//                Image image = new Image(iconPathArray.get(charGrid[i][j]), 48, 48, false, false);
//                ImageView imageView = new ImageView(image);
//                mapPane.add(imageView, j, i);
//            }
//        }
//    }

    private void drawGrid(ArrayList<ArrayList<Character>> charGrid){
        mapPane.getChildren().clear();
        mapPane.setGridLinesVisible(true);
        for (int i = 0; i < charGrid.size(); i++) {
            for (int j = 0; j < charGrid.get(i).size(); j++) {
                Image image = new Image(iconPathArray.get(charGrid.get(i).get(j)), 48, 48, false, false);
                ImageView imageView = new ImageView(image);
                mapPane.add(imageView, j, i);
            }
        }
    }

    private void newRenderInterval(int renderIntervalMillis){
        timeline.stop();
        timeline.getKeyFrames().setAll(
        new KeyFrame(Duration.millis(renderIntervalMillis), event -> {
            drawGrid(gridStorage.get(trainingRound-1).get(trial-1).get(step-1));
//            drawGrid(gridStorage[trainingRound-1][trial-1][step-1]);
            if (step < gridStorage.get(0).get(0).size()) step++;
            else if (trial < gridStorage.get(0).size()) {
                step = 1;
                trial++;
                trialsField.setText(Integer.toString(trial));
            }
            else if (trainingRound < gridStorage.size()) {
                step = 1;
                trial = 1;
                trainingRound++;
                trainingRoundsField.setText(Integer.toString(trainingRound));
            }
            else timeline.stop();
            stepsField.setText(Integer.toString(step));
        }
        )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        if (Objects.equals(playButton.getText(), "Pause")) timeline.play();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}