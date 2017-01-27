package common;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

import javafx.util.Duration;
import task1.*;


public class GUI extends Application{

    private GridPane mapPane;
    private HashMap<Character, String> iconPathArray;
    private int trainingRound, trial, step, renderInterval;
    private Timeline timeline;
    private char[][][][][] gridStorage;
    private Button playButton;
    private TextField renderIntervalField;

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

        // CONFIG ROW
        HBox configRow = new HBox(20);
        configRow.setMinHeight(50);
        configRow.setPadding(new Insets(10, 10, 10, 10));
        configRow.setAlignment(Pos.CENTER_LEFT);

        // PLAY BUTTON
        playButton = new Button("Play");
        playButton.setMinWidth(56);
        playButton.setPadding(new Insets(10, 10, 10, 10));
        playButton.setOnAction(event -> {
            if (playButton.getText() == "Play"){
                playButton.setText("Pause");
                newRenderInterval(renderInterval);
            }
            else {
                timeline.stop();
                playButton.setText("Play");
            }
        });
        configRow.getChildren().add(playButton);


        // RENDER SETTINGS HBOX
            // APPLY RENDER INTERVAL BUTTON
            Button applyRenderIntervalButton = new Button("Apply");
            applyRenderIntervalButton.setMinWidth(40);
            applyRenderIntervalButton.setPadding(new Insets(4, 10, 4, 10));
            applyRenderIntervalButton.setOnAction(event -> {
                renderInterval = (Integer.parseInt(renderIntervalField.getText()));
                newRenderInterval(renderInterval);
            });

            // RENDER INTERVAL FIELD
            renderIntervalField = new TextField(Integer.toString(renderInterval));
            renderIntervalField.setPrefWidth(50);
            renderIntervalField.setAlignment(Pos.BASELINE_RIGHT);
            renderIntervalField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    renderIntervalField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            HBox renderSettingsHbox = new HBox();
            renderSettingsHbox.setAlignment(Pos.BOTTOM_LEFT);
            renderSettingsHbox.getChildren().addAll(applyRenderIntervalButton, renderIntervalField);


        // ITERATION SETTINGS HBOX
            // TRIALS FIELD
            TextField trainingRoundsField = new TextField(Integer.toString(trainingRound));

            // TRIALS FIELD
            TextField trialsField = new TextField(Integer.toString(trial));


            HBox iterationSettingsHbox = new HBox();
            iterationSettingsHbox.setAlignment(Pos.BOTTOM_LEFT);
            iterationSettingsHbox.getChildren().addAll(trainingRoundsField, trialsField);

        configRow.getChildren().add(renderSettingsHbox);


        rootPane.add(configRow, 0, 0);
        rootPane.add(mapPane, 0, 1);
        Scene scene = new Scene(rootPane);
        primaryStage.setTitle("Flatland world");
        primaryStage.setScene(scene);
        primaryStage.show();

        drawGrid(gridStorage[0][0][0]);
        
    }



    private void drawGrid(char[][]charGrid){
        mapPane.getChildren().clear();
        mapPane.setGridLinesVisible(true);
        for (int i = 0; i < charGrid.length; i++) {
            for (int j = 0; j < charGrid[i].length; j++) {
                Image image = new Image(iconPathArray.get(charGrid[i][j]), 48, 48, false, false);
                ImageView imageView = new ImageView(image);
                mapPane.add(imageView, j, i);
            }
        }
    }

    private void newRenderInterval(int renderIntervalMillis){
        timeline.stop();
        timeline.getKeyFrames().setAll(
        new KeyFrame(Duration.millis(renderIntervalMillis), event -> {
            drawGrid(gridStorage[trainingRound][trial][step]);
            if (step < gridStorage[0].length) step++;
            else if (trial >= gridStorage.length) timeline.stop();
            else {
                step = 1;
                trial++;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        if (playButton.getText() == "Pause") timeline.play();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}