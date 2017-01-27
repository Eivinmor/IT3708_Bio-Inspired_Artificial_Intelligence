package common;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

import javafx.util.Duration;
import task1.*;


public class GUI extends Application{

    private GridPane mapPane;
    private HBox configRow;
    private HashMap<Character, String> iconPathArray;
    private int trial, step, renderInterval;
    private KeyFrame keyframe;
    private Timeline timeline;
    private char[][][][] gridStorage;
    private Button playButton, applyRenderIntervalButton;
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
        renderInterval = 1000;

        GridPane rootPane = new GridPane();

        mapPane = new GridPane();
        mapPane.setAlignment(Pos.CENTER);
        mapPane.setPadding(new Insets(2, 1, 1, 2));
        mapPane.setStyle("-fx-background-color: #161616;");
        for (int i = 0; i < 10; i++) {
            mapPane.getColumnConstraints().add(new ColumnConstraints(50));
            mapPane.getRowConstraints().add(new RowConstraints(50));
        }

        configRow = new HBox(20);
        configRow.setMinHeight(50);
        configRow.setPadding(new Insets(10, 10, 10, 10));


        // PLAY BUTTON
        playButton = new Button("Play");
        playButton.setMinWidth(50);
        playButton.setOnAction(event -> {
            if (playButton.getText() == "Play"){
                newRenderInterval(renderInterval);
                playButton.setText("Pause");
            }
            else {
                timeline.stop();
                playButton.setText("Play");
            }
        });
        configRow.getChildren().add(playButton);


        // APPLY RENDER INTERVAL BUTTON
        applyRenderIntervalButton = new Button("Apply");
        applyRenderIntervalButton.setMinWidth(40);
        applyRenderIntervalButton.setOnAction(event -> {
            renderInterval = (Integer.parseInt(renderIntervalField.getText()));
            newRenderInterval(renderInterval);
        });
        configRow.getChildren().add(applyRenderIntervalButton);

        // RENDER INTERVAL FIELD
        renderIntervalField = new TextField(Integer.toString(renderInterval));
        renderIntervalField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                renderIntervalField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });


        configRow.getChildren().add(renderIntervalField);



        rootPane.add(configRow, 0, 0);
        rootPane.add(mapPane, 0, 1);
        Scene scene = new Scene(rootPane);
        primaryStage.setTitle("Flatland world");
        primaryStage.setScene(scene);
        primaryStage.show();

        trial = 0;
        step = 0;
        
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
        keyframe = new KeyFrame(Duration.millis(renderIntervalMillis), event -> {
            drawGrid(gridStorage[trial][step]);
            if (step < gridStorage[0].length-1) step++;
            else{
                step = 0;
                trial++;
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}