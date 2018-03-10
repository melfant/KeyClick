import javafx.animation.KeyFrame;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;


public class Controller {
    Settings settings = new Settings(KeyCode.SPACE, 3);

    private Integer keyClickCount = 0;
    private boolean counterInWorkFlag = false;
    private boolean counterReadyToStart = true;


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
    Button bnCounterReset;

    @FXML
    public void keyPressed(KeyEvent keyEvent) {
        if (tabKeyClick.isSelected() == true) {
            if (keyEvent.getCode() == settings.getKeyCode()) {
                if (counterInWorkFlag == false && counterReadyToStart == true) {
                    counterInWorkFlag = true;
                    counterReadyToStart = false;

                    lbPressKeyToStart.setVisible(false);


                    Timeline timer = new Timeline(
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

                    timer.play();
                } else if (counterInWorkFlag == true) {
                    keyClickCount++;
                    lbCounter.setText("Count: " + keyClickCount.toString());
                }
            }
        }
    }

    @FXML
    public void counterReset(ActionEvent actionEvent) {
        counterReadyToStart = true;
        lbPressKeyToStart.setVisible(true);
        bnCounterReset.setVisible(false);
        keyClickCount = 0;
        lbCounter.setText("Count: " + keyClickCount.toString());
        tabPane.requestFocus();
        pbCounter.setVisible(false);
    }
}
