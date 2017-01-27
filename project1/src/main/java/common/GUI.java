package common;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;

import javafx.util.Duration;
import task1.*;


public class GUI extends Application{

    private GridPane gridPane;
    private HashMap<Character, String> iconPathArray;
    private int trial, step;

    public GUI(){
        iconPathArray = new HashMap<>(7);
        iconPathArray.put(' ', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\blank.png");
        iconPathArray.put('F', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\food.png");
        iconPathArray.put('P', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\poison.png");
        iconPathArray.put('⇑', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentNorth.png");
        iconPathArray.put('⇒', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentEast.png");
        iconPathArray.put('⇓', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentSouth.png");
        iconPathArray.put('⇐', "File:C:\\Users\\Eivind\\IdeaProjects\\IT3708_Bio-Inspired_Artificial_Intelligence\\project1\\src\\main\\java\\common\\icons\\agentWest.png");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Simulator1 sim = new Simulator1();
        char[][][][] gridStorage = sim.runSimulation();


        primaryStage.setTitle("Flatland world");
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        gridPane.setGridLinesVisible(true);

        for (int i = 0; i < 10; i++) {
            gridPane.getColumnConstraints().add(new ColumnConstraints(50));
            gridPane.getRowConstraints().add(new RowConstraints(50));
        }

        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();

        trial = 0;
        step = 0;

//        gridPane.setOnMousePressed(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                if (event.getButton() == MouseButton.PRIMARY){
//                    drawGrid(gridStorage[trial][step]);
//                    if (step < 49) step++;
//                    else{
//                        step = 0;
//                        trial++;
//                    }
//                }
//
//            }
//        });

        KeyFrame keyframe = new KeyFrame(Duration.millis(100), event -> {
            drawGrid(gridStorage[trial][step]);
            if (step < gridStorage[0].length-1) step++;
            else{
                step = 0;
                trial++;
            }
        });

        Timeline timeline = new Timeline(keyframe);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }



    private void drawGrid(char[][]charGrid){
        gridPane.getChildren().clear();
        for (int i = 0; i < charGrid.length; i++) {
            for (int j = 0; j < charGrid[i].length; j++) {
                Image image = new Image(iconPathArray.get(charGrid[i][j]), 40, 40, false, false);
                ImageView imageView = new ImageView(image);
                gridPane.add(imageView, j, i);
            }
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}