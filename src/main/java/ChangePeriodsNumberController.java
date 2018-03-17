
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ChangePeriodsNumberController {
    Settings settings = Settings.getInstance();

    @FXML
    private TextField tfPeriodsNumber;
    @FXML
    private Button bnSave;

    @FXML
    public void initialize(){

        tfPeriodsNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfPeriodsNumber.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if(tfPeriodsNumber.getText().equals("") || tfPeriodsNumber.getText().startsWith("0")){
                bnSave.setDisable(true);
            }
            else{
                bnSave.setDisable(false);
            }
        });
    }

    @FXML
    public void savePeriodsNumber(ActionEvent actionEvent) {
        settings.setPeriodsNumber(Integer.valueOf(tfPeriodsNumber.getText()));

        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
