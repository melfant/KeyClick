import javafx.animation.KeyFrame;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class MainController {
    Settings settings = Settings.getInstance();

    private Integer keyClickCount = 0;
    private boolean counterInWorkFlag = false;
    private boolean counterReadyToStart = true;
    private Timeline timer;

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
    private Button btnSetKeyboardKey;
    @FXML
    private Label lbTimer;
    @FXML
    private Button btnSetTimer;
    @FXML
    private Label lbPressToStop;

    @FXML
    public void initialize(){

    }

    @FXML
    public void keyReleased(KeyEvent keyEvent) {
        if (tabKeyClick.isSelected() == true) {
            if (keyEvent.getCode() == settings.getKeyCode()) {
                if (counterInWorkFlag == false && counterReadyToStart == true) {
                    counterInWorkFlag = true;
                    counterReadyToStart = false;

                    lbPressKeyToStart.setVisible(false);

                    if(timer != null){
                        timer.stop();
                    }

                    timer = new Timeline(
                            new KeyFrame(Duration.ZERO, new KeyValue(pbCounter.progressProperty(), 0)),
                            new KeyFrame(Duration.seconds(settings.getTimeInSeconds()), ae -> {
                                //on finish
                                counterInWorkFlag = false;
                                bnCounterReset.setVisible(true);

                            }, new KeyValue(pbCounter.progressProperty(), 1))
                    );

                    keyClickCount++;
                    lbCounter.setText("Count: " + keyClickCount.toString());

                    pbCounter.setVisible(true);
                    lbPressToStop.setVisible(true);

                    timer.play();
                } else if (counterInWorkFlag == true) {
                    keyClickCount++;
                    lbCounter.setText("Count: " + keyClickCount.toString());
                }
            }
            else if (keyEvent.getCode() == KeyCode.ESCAPE && counterInWorkFlag == true){

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
                ((Node)actionEvent.getSource()).getScene().getWindow() );
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
                ((Node)actionEvent.getSource()).getScene().getWindow() );
        stage.show();
    }

    //need refactoring into other layer
    private void counterResetService(){
        counterReadyToStart = true;
        counterInWorkFlag = false;
        lbPressKeyToStart.setVisible(true);
        bnCounterReset.setVisible(false);
        keyClickCount = 0;
        lbCounter.setText("Count: " + keyClickCount.toString());
        tabPane.requestFocus();
        pbCounter.setProgress(0);
        pbCounter.setVisible(false);
        lbPressToStop.setVisible(false);
    }

}
