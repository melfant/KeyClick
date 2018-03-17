import javafx.animation.KeyFrame;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class MainController {
    Settings settings = Settings.getInstance();

    private Integer totalKeyClickCount = 0;
    private boolean counterInWorkFlag = false;
    private boolean counterReadyToStart = true;
    private Timeline timer;
    private Integer[] results;
    private Integer currentPeriod;
    private String previousPeriodKeyClickCountText;

    @FXML
    private Label lbCounter;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabKeyClick;
    @FXML
    private ProgressBar pbCounter;
    @FXML
    private Label lbPressKeyToStart;
    @FXML
    private Button bnCounterReset;
    @FXML
    private Label lbKeyboardKey;
    @FXML
    private Label lbTimer;
    @FXML
    private Label lbPressToStop;
    @FXML
    private Label lbPeriodsNumber;
    @FXML
    private Label lbPeriod;

    @FXML
    public void initialize() throws NoSuchMethodException {
        lbKeyboardKey.textProperty().bind(
                (
                        new JavaBeanObjectPropertyBuilder<KeyCode>()
                                .bean(settings)
                                .name("keyCode")
                                .build()
                ).asString()
        );

        lbTimer.textProperty().bind(
                (
                        new JavaBeanIntegerPropertyBuilder()
                                .bean(settings)
                                .name("timeInSeconds")
                                .build()
                ).asString().concat(" seconds")
        );

        lbPeriodsNumber.textProperty().bind(
                (
                        new JavaBeanIntegerPropertyBuilder()
                                .bean(settings)
                                .name("periodsNumber")
                                .build()
                ).asString().concat(" periods")
        );

    }

    @FXML
    public void keyReleased(KeyEvent keyEvent) {
        if (tabKeyClick.isSelected() == true) {
            if (keyEvent.getCode() == settings.getKeyCode()) {
                if (counterInWorkFlag == false && counterReadyToStart == true) {
                    counterInWorkFlag = true;
                    counterReadyToStart = false;

                    lbPressKeyToStart.setVisible(false);
                    lbPeriod.setText("Period: 1/" + settings.getPeriodsNumber().toString());
                    lbPeriod.setVisible(true);

                    results = new Integer[settings.getPeriodsNumber()];
                    for(int i = 0; i < settings.getPeriodsNumber(); i++){
                        results[i] = 0;
                    }

                    currentPeriod = 1;
                    previousPeriodKeyClickCountText = "0";

                    timer = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(pbCounter.progressProperty(), 0)),
                            new KeyFrame(Duration.seconds(settings.getTimeInSeconds()), ae -> {
                                //on finish
                                if(currentPeriod < settings.getPeriodsNumber()){
                                    currentPeriod++;
                                    lbPeriod.setText("Period: " + currentPeriod.toString() + "/" + settings.getPeriodsNumber().toString());
                                    if(currentPeriod > 1){
                                        previousPeriodKeyClickCountText = results[currentPeriod - 2].toString();
                                    }
                                    lbCounter.setText("Count (current/previous): " + results[currentPeriod - 1].toString() + "/" + previousPeriodKeyClickCountText);
                                }
                                else {
                                    counterInWorkFlag = false;
                                    bnCounterReset.setVisible(true);
                                    timer.stop();
                                }

                            }, new KeyValue(pbCounter.progressProperty(), 1))
                    );

                    results[currentPeriod - 1]++;
                    lbCounter.setText("Count (current/previous): " + results[currentPeriod - 1].toString() + "/" + previousPeriodKeyClickCountText);

                    pbCounter.setVisible(true);
                    lbPressToStop.setVisible(true);

                    timer.setCycleCount(settings.getPeriodsNumber());
                    timer.play();
                } else if (counterInWorkFlag == true) {
                    results[currentPeriod - 1]++;
                    totalKeyClickCount++;
                    lbCounter.setText("Count (current/previous): " + results[currentPeriod - 1].toString() + "/" + previousPeriodKeyClickCountText);
                }
            } else if (keyEvent.getCode() == KeyCode.ESCAPE && counterInWorkFlag == true) {

                counterResetService();
            }
        }
    }

    @FXML
    public void counterReset(ActionEvent actionEvent) {
        counterResetService();
    }

    @FXML
    public void changeKeyboardKey(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    ChangeKeyboardKeyController.class.getResource("ChangeKeyboardKey.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.setTitle("Change key");
        stage.getIcons().add(new Image("settingsIcon64.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
        root.requestFocus();
    }

    @FXML
    public void changeTimer(ActionEvent actionEvent) {

        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    ChangeTimerController.class.getResource("ChangeTimer.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));

        stage.setTitle("Change timer");
        stage.getIcons().add(new Image("settingsIcon64.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }
    public void changePeriodsNumber(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    ChangeTimerController.class.getResource("ChangePeriodsNumber.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));

        stage.setTitle("Change periods number");
        stage.getIcons().add(new Image("settingsIcon64.png"));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(
                ((Node) actionEvent.getSource()).getScene().getWindow());
        stage.show();
    }


    //need refactoring into other layer
    private void counterResetService() {
        counterReadyToStart = true;
        counterInWorkFlag = false;
        lbPressKeyToStart.setVisible(true);
        bnCounterReset.setVisible(false);
        totalKeyClickCount = 0;
        lbCounter.setText("Count: " + totalKeyClickCount.toString());
        tabPane.requestFocus();
        pbCounter.setProgress(0);
        pbCounter.setVisible(false);
        lbPressToStop.setVisible(false);
        lbPeriod.setVisible(false);
        if (timer != null) {
            timer.stop();
        }
    }


}
