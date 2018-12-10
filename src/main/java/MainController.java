import javafx.animation.KeyFrame;

import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class MainController {
    Settings settings = Settings.getInstance();
    Results resultsSingleton = Results.getInstance();

    private Integer totalKeyClickCount = 0;
    private boolean counterInWorkFlag = false;
    private boolean counterReadyToStart = true;
    private Timeline timer;
    private Integer[] results;
    private Integer currentPeriod;
    private String previousPeriodKeyClickCountText;

    private SaveResultController saveResultController;

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
    private HBox hBoxResultButtons;
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
    private TableView<Result> tblResults;


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

        //results table
        tblResults.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        tblResults.setOnKeyPressed(event -> {
            if (keyCodeCopy.match(event)) {
                copySelectionToClipboard(tblResults);
            }
        });


        MenuItem item = new MenuItem("Copy");
        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                copySelectionToClipboard(tblResults);
            }
        });
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        tblResults.setContextMenu(menu);

        refreshResultsTable();


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
                                    lbPressToStop.setVisible(false);
                                    hBoxResultButtons.setVisible(true);
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
                    ChangePeriodsNumberController.class.getResource("ChangePeriodsNumber.fxml"));
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

    public void saveResult(ActionEvent actionEvent) throws MalformedURLException {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(SaveResultController.class.getResource("SaveResults.fxml"));

        Parent root = null;
        try {
            root = loader.load();
            saveResultController = loader.getController();
            saveResultController.setMainController(this);

            String periods = settings.getPeriodsNumber().toString()
                    .concat("\\")
                    .concat(settings.getTimeInSeconds().toString());

            String resultsString = "";
            for(Integer i = 0; i < results.length; i++){
                resultsString = resultsString.concat(results[i].toString()).concat(";");
            }

            saveResultController.setLastResult(new Result(null, null, null,
                    periods,
                    resultsString));

        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));


        stage.setTitle("Save results");
        stage.getIcons().add(new Image("mainIcon64.png"));
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
        hBoxResultButtons.setVisible(false);
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

    @FXML
    private TableColumn<Result, String> tblColLastName;
    @FXML
    private TableColumn<Result, String> tblColFirstName;
    @FXML
    private TableColumn<Result, String> tblColMiddleName;
    @FXML
    private TableColumn<Result, String> tblColPeriods;
    @FXML
    private TableColumn<Result, String> tblColResult;

    private ObservableList<Result> resultObservableList = FXCollections.observableArrayList();


    public void refreshResultsTable(){
        resultObservableList = FXCollections.observableArrayList(resultsSingleton.getResultList());

        tblColLastName.setCellValueFactory(new PropertyValueFactory<Result, String>("lastName"));
        tblColFirstName.setCellValueFactory(new PropertyValueFactory<Result, String>("firstName"));
        tblColMiddleName.setCellValueFactory(new PropertyValueFactory<Result, String>("middleName"));
        tblColPeriods.setCellValueFactory(new PropertyValueFactory<Result, String>("periods"));
        tblColResult.setCellValueFactory(new PropertyValueFactory<Result, String>("result"));


        tblResults.setItems(resultObservableList);



        tblResults.refresh();
        refresh_table(tblResults);

    }

    public static void refresh_table(TableView table)
    {
        for (int i = 0; i < table.getColumns().size(); i++) {
            ((TableColumn)(table.getColumns().get(i))).setVisible(false);
            ((TableColumn)(table.getColumns().get(i))).setVisible(true);
        }
    }

    @SuppressWarnings("rawtypes")
    public void copySelectionToClipboard(final TableView<?> table) {
        final Set<Integer> rows = new TreeSet<>();
        for (final TablePosition tablePosition : table.getSelectionModel().getSelectedCells()) {
            rows.add(tablePosition.getRow());
        }
        final StringBuilder strb = new StringBuilder();
        boolean firstRow = true;
        for (final Integer row : rows) {
            if (!firstRow) {
                strb.append('\n');
            }
            firstRow = false;
            boolean firstCol = true;
            for (final TableColumn<?, ?> column : table.getColumns()) {
                if (!firstCol) {
                    strb.append('\t');
                }
                firstCol = false;
                final Object cellData = column.getCellData(row);
                strb.append(cellData == null ? "" : cellData.toString());
            }
        }
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(strb.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
    }


    public void loadTableToExcel() throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet spreadsheet = workbook.createSheet("sample");

        Row row = spreadsheet.createRow(0);

        for (int j = 0; j < tblResults.getColumns().size(); j++) {
            row.createCell(j).setCellValue(tblResults.getColumns().get(j).getText());
        }

        for (int i = 0; i < tblResults.getItems().size(); i++) {
            row = spreadsheet.createRow(i + 1);
            for (int j = 0; j < tblResults.getColumns().size(); j++) {
                if(tblResults.getColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(tblResults.getColumns().get(j).getCellData(i).toString());
                }
                else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        String filename = "results "
                .concat(String.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss"))))
                .concat(".xls");


        FileOutputStream fileOut = new FileOutputStream(filename);
        workbook.write(fileOut);
        fileOut.close();
    }

}
