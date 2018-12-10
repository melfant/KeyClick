
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class SaveResultController {
    Settings settings = Settings.getInstance();
    Results results = Results.getInstance();


    public void setLastResult(Result lastResult) {
        this.lastResult = lastResult;
    }

    private Result lastResult;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private MainController mainController;

    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfMiddleName;





    @FXML
    public void addResultToTable(ActionEvent actionEvent) {
        lastResult.setLastName(tfLastName.getText());
        lastResult.setFirstName(tfFirstName.getText());
        lastResult.setMiddleName(tfMiddleName.getText());

        results.getResultList().add(lastResult);

        mainController.refreshResultsTable();

        Node source = (Node)  actionEvent.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }


}
