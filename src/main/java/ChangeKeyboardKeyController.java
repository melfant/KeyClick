import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ChangeKeyboardKeyController {
    Settings settings = Settings.getInstance();

    private KeyCode keyCode;

    @FXML
    private Label lbEnterKey;
    @FXML
    private Button bnSave;

    public void keyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.ESCAPE){
            lbEnterKey.setText("'Escape' cannot be used.");
            keyCode = null;
        }
        else{
            lbEnterKey.setText("You choose: '" + keyEvent.getCode().getName() + "'");
            keyCode = keyEvent.getCode();

            settings.setKeyCode(keyCode);

            Timeline closeTimer = new Timeline(
                    new KeyFrame(Duration.seconds(0.5), ae -> {
                        //on finish
                        Node source = (Node)  keyEvent.getSource();
                        Stage stage  = (Stage) source.getScene().getWindow();
                        stage.close();

                    })
            );

            closeTimer.play();
        }
    }
}
