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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import javafx.util.Duration;
import task1.*;
import task2.*;
import task3.*;
import task4.*;


public class GUI extends Application{

    private GridPane mapPane;
    private HashMap<Character, String> iconPathArray;
    private int trainingRound, trial, step, renderInterval, agentX, agentY;
    private Timeline timeline;
    private Button playButton;
    private TextField renderIntervalField, trainingRoundsField, trialsField, stepsField;
    private char[][][][] initialGrids;
    private ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> positionStorage;

    public GUI(){
        iconPathArray = new HashMap<>(8);
        iconPathArray.put(' ', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\blank.png");
        iconPathArray.put('F', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\food.png");
        iconPathArray.put('P', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\poison.png");
        iconPathArray.put('X', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\bang.png");
        iconPathArray.put('⇑', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentNorth.png");
        iconPathArray.put('⇒', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentEast.png");
        iconPathArray.put('⇓', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentSouth.png");
        iconPathArray.put('⇐', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentWest.png");
        timeline = new Timeline();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Simulator2 sim = new Simulator2();
        sim.runSimulation();
        initialGrids = sim.getInitialGrids();
        positionStorage = sim.getPositionStorage();
        renderInterval = 300;
        trainingRound = 1;
        trial = 1;
        step = 0;

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
            step = 0;
            stepsField.setText(Integer.toString(step));
            drawGrid(trainingRound-1, trial-1);
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
            else if (Integer.parseInt(trainingRoundsField.getText()) > positionStorage.size()) {
                trainingRoundsField.setText(Integer.toString(positionStorage.size()));
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
            else if (Integer.parseInt(trialsField.getText()) > positionStorage.get(trainingRound-1).size()) {
                trialsField.setText(Integer.toString(positionStorage.get(trainingRound-1).size()));
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
            else if (Integer.parseInt(stepsField.getText()) > positionStorage.get(trainingRound-1).get(trial-1).size()-1) {
                stepsField.setText(Integer.toString(positionStorage.get(trainingRound-1).get(trial-1).size()));
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
        drawGrid(0, 0);

    }

    private void drawGrid(int trainingRound, int trial){
        mapPane.getChildren().clear();
        char[][] specificGrid = initialGrids[trainingRound][trial];
        for (int i = 0; i < specificGrid.length; i++) {
            for (int j = 0; j < specificGrid[i].length; j++) {
                char icon = specificGrid[i][j];
                if (icon == '⇑' || icon ==  '⇒' || icon ==  '⇓' || icon ==  '⇐' ){
                    agentX = i;
                    agentY = j;
                }
                Image image = new Image(iconPathArray.get(specificGrid[i][j]), 48, 48, false, false);
                ImageView imageView = new ImageView(image);
                mapPane.add(imageView, j, i);
            }
        }
    }

    private void updateGrid(int newX, int newY){
        char dir = 'X';
        if ((newY >= 10 || newY < 0) && (newX >= 10 || newX < 0) ) {
            newX = agentX;
            newY = agentY;
        }
        else if (newY > agentY) dir = '⇒';
        else if (newX > agentX) dir = '⇓';
        else if (newY < agentY) dir = '⇐';
        else if (newX < agentX) dir = '⇑';

        Image oldImage = new Image(iconPathArray.get(' '), 48, 48, false, false);
        ImageView oldImageView = new ImageView(oldImage);

        Image agentImage = new Image(iconPathArray.get(dir), 48, 48, false, false);
        ImageView agentImageView = new ImageView(agentImage);

        mapPane.add(oldImageView, agentY, agentX);
        mapPane.add(agentImageView, newY, newX);
        agentX = newX;
        agentY = newY;
    }

    private void newRenderInterval(int renderIntervalMillis){
        timeline.stop();
        timeline.getKeyFrames().setAll(
                new KeyFrame(Duration.millis(renderIntervalMillis), event -> {
                    if (step < positionStorage.get(trainingRound-1).get(trial-1).size()-1) step++;
                    else if (trial < positionStorage.get(trainingRound-1).size()) {
                        step = 0;
                        trial++;
                        trialsField.setText(Integer.toString(trial));
                        drawGrid(trainingRound-1, trial-1);
                    }
                    else if (trainingRound < positionStorage.size()) {
                        step = 0;
                        trial = 1;
                        trainingRound++;
                        trainingRoundsField.setText(Integer.toString(trainingRound));
                        drawGrid(trainingRound-1, trial-1);
                    }
                    else timeline.stop();
                    stepsField.setText(Integer.toString(step));
                    ArrayList<Integer> coords = positionStorage.get(trainingRound-1).get(trial-1).get(step);
                    updateGrid(coords.get(1), coords.get(0));
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