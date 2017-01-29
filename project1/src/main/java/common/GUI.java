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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javafx.util.Duration;


public class GUI extends Application{

    private GridPane mapPane;
    private HashMap<Character, String> iconPathArray;
    private int trainingRound, trial, step, renderInterval;
    private Timeline timeline;
    private ArrayList<ArrayList<ArrayList<ArrayList<Character>>>> roundGridStorage;
    private ArrayList<ArrayList<Integer>> roundScoreStorage;
    private Button playButton;
    private TextField renderIntervalField, trainingRoundsField, trialsField, stepsField;
    private File gridStorageFile;
    private ArrayList<ArrayList<Integer>> roundIndexes;
    private Text scoreText;

    @Override
    public void start(Stage primaryStage) throws Exception {

        String filePathRoot = System.getProperty("user.dir");

        iconPathArray = new HashMap<>(8);
        iconPathArray.put(' ', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\blank.png");
        iconPathArray.put('F', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\food.png");
        iconPathArray.put('P', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\poison.png");
        iconPathArray.put('X', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\bang.png");
        iconPathArray.put('⇑', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\agentNorth.png");
        iconPathArray.put('⇒', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\agentEast.png");
        iconPathArray.put('⇓', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\agentSouth.png");
        iconPathArray.put('⇐', "File:" + filePathRoot + "\\src\\main\\java\\common\\icons\\agentWest.png");

        renderInterval = 300;
        trainingRound = 1;
        trial = 1;
        step = 0;

        timeline = new Timeline();
        gridStorageFile = new File(filePathRoot + "\\src\\main\\java\\common\\gridStorageFile.txt");

        roundIndexes = indexRoundsInFile();
        readRoundData(trainingRound-1);


        GridPane rootPane = new GridPane();

        mapPane = new GridPane();
        mapPane.setAlignment(Pos.CENTER);
        mapPane.setPadding(new Insets(2, 1, 1, 2));
        mapPane.setStyle("-fx-background-color: #161616;");
        for (int i = 0; i < 10; i++) {
            mapPane.getColumnConstraints().add(new ColumnConstraints(50));
            mapPane.getRowConstraints().add(new RowConstraints(50));
        }

// --- BUTTON ROW ---

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
            try {
                readRoundData(trainingRound-1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            step = Integer.parseInt(stepsField.getText());
            if (step >= roundGridStorage.get(trial-1).size()) { // Checks that steps is not higher than number of steps for the chosen trial
                step = roundGridStorage.get(trial-1).size() - 1;
                stepsField.setText(Integer.toString(step));
            }
            scoreText.setText(Integer.toString(roundScoreStorage.get(trial-1).get(step)));
            drawGrid(roundGridStorage.get(trial-1).get(step));

        });

    // SCORE HBOX
        scoreText = new Text("0");
        scoreText.setFont(Font.font("Verdana", 20));
        scoreText.setTextAlignment(TextAlignment.RIGHT);
        scoreText.setWrappingWidth(50);

        HBox scoreHbox = new HBox(2);
        scoreHbox.setPadding(new Insets(0, 0, 0, 150));
        scoreHbox.setAlignment(Pos.CENTER_LEFT);
        Label scoreLabel = new Label("Score: ");
        scoreLabel.setFont(Font.font("Verdana", 20));
        scoreHbox.getChildren().addAll(scoreLabel, scoreText);


        buttonRow.getChildren().addAll(playButton, appySettingsButton, scoreHbox);



// --- SETTINGS ROW ---

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
            else if (trainingRoundsField.getText().length() > 0 && Integer.parseInt(trainingRoundsField.getText()) > roundIndexes.size()) {
                trainingRoundsField.setText(Integer.toString(roundIndexes.size()));
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
            else if (trialsField.getText().length() > 0 && Integer.parseInt(trialsField.getText()) > roundGridStorage.size()) {
                trialsField.setText(Integer.toString(roundGridStorage.size()));
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
            else if (stepsField.getText().length() > 0 && Integer.parseInt(stepsField.getText()) > roundGridStorage.get(trial-1).size()-1) {
                stepsField.setText(Integer.toString(roundGridStorage.get(trial-1).size()-1));
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

        drawGrid(roundGridStorage.get(0).get(0));

    }

    private void drawGrid(ArrayList<ArrayList<Character>> charGrid){
        mapPane.getChildren().clear();
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
        timeline.getKeyFrames().setAll(new KeyFrame(Duration.millis(renderIntervalMillis), event -> renderNextStep()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        if (Objects.equals(playButton.getText(), "Pause")) timeline.play();
    }

    private void renderNextStep() {
        if (step < roundGridStorage.get(trial-1).size()-1) step++;
        else if (trial < roundGridStorage.size()) {
            step = 0;
            trial++;
            trialsField.setText(Integer.toString(trial));
        }
        else if (trainingRound < roundIndexes.size()) {
            step = 0;
            trial = 1;
            trainingRound++;
            trainingRoundsField.setText(Integer.toString(trainingRound));
            try {
                readRoundData(trainingRound-1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else timeline.stop();
        stepsField.setText(Integer.toString(step));
        scoreText.setText(Integer.toString(roundScoreStorage.get(trial-1).get(step)));
        drawGrid(roundGridStorage.get(trial-1).get(step));
    }

    private ArrayList<ArrayList<Integer>> indexRoundsInFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(gridStorageFile));
        ArrayList<ArrayList<Integer>> indexes = new ArrayList<>();
        int lineNumber = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("Round")) {
                indexes.add(new ArrayList<>(2));
                indexes.get(indexes.size()-1).add(lineNumber+1);
            }
            else if (line.equals("End of round")) {
                indexes.get(indexes.size()-1).add(lineNumber-1);
            }
            lineNumber++;
        }
        reader.close();
        return indexes;
    }

    private void readRoundData(int trainingRound) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(gridStorageFile));
        ArrayList<ArrayList<ArrayList<ArrayList<Character>>>> roundGridData = new ArrayList<>();
        ArrayList<ArrayList<Integer>> roundScoreData = new ArrayList<>();
        int indexMin = roundIndexes.get(trainingRound).get(0);
        int indexMax = roundIndexes.get(trainingRound).get(1);
        ArrayList<Character> tempRow = new ArrayList<>();
        int index = 0;

        String line;
        while ((line = reader.readLine()) != null) {
            if (index >= indexMin && index <= indexMax) {
                if (line.equals("Trial")) {
                    roundGridData.add(new ArrayList<>());
                    roundScoreData.add(new ArrayList<>());
                }
                else if (!line.equals("Round") && !line.equals("End of round")){
                    roundGridData.get(roundGridData.size()-1).add(new ArrayList<>());
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        if (c == ',') {
                            roundGridData.get(roundGridData.size()-1).get(roundGridData.get(roundGridData.size()-1).size()-1).add(tempRow);
                            tempRow = new ArrayList<>();
                        }
                        else if (c == ':') {
                            StringBuilder stepScoreStringBuilder = new StringBuilder();
                            for (int j = i+1; j < line.length(); j++) {
//                                System.out.println(Character.getNumericValue(c));
                                stepScoreStringBuilder.append(line.charAt(j));
                            }
                            roundScoreData.get(roundScoreData.size()-1).add(Integer.parseInt(stepScoreStringBuilder.toString()));
                            break;
                        }
                        else {
                            tempRow.add(c);
                        }
                    }
                }
            }
            index++;
        }
        reader.close();
        roundScoreStorage = roundScoreData;
        roundGridStorage = roundGridData;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}