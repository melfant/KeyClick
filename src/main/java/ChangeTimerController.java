import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ChangeTimerController {
    Settings settings = Settings.getInstance();

    @FXML
    private TextField tfTimer;
    @FXML
    private Button bnSave;

    @FXML
    public void initialize(){

        tfTimer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfTimer.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(tfTimer.getText().equals("") || tfTimer.getText().startsWith("0")){
                bnSave.setDisable(true);
            }
            else{
                bnSave.setDisable(false);
            }
        });
    }

    @FXML
    public void saveTimer(ActionEvent actionEvent) {
        settings.setTimeInSeconds(Integer.valueOf(tfTimer.getText()));

        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
