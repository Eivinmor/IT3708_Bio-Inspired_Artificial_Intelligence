package common;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;


public class GUI extends Application{

    private GridPane grid;
    private HashMap<Character, String> iconPathArray;

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
        primaryStage.setTitle("Flatland world");
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setGridLinesVisible(true);

        for (int i = 0; i < 10; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(50));
            grid.getRowConstraints().add(new RowConstraints(50));
        }
        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void drawGrid(char[][]charGrid){
        System.out.println("DRAWING GRID");
        grid.getChildren().clear();
        for (int i = 0; i < charGrid.length; i++) {
            for (int j = 0; j < charGrid[i].length; j++) {
                Image image = new Image(iconPathArray.get(charGrid[i][j]), 50, 50, false, false);
                ImageView imageView = new ImageView(image);
                HBox hbox = new HBox(50);
                hbox.getChildren().add(imageView);
                grid.add(imageView, j, i);
            }
        }
    }
}
