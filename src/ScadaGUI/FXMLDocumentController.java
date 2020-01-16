package ScadaGUI;

import JDBCmodule.mySQLsend;
import databaseUnit.SSHDB;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import korabelscada.KorabelSCADA;

public class FXMLDocumentController {

    @FXML
    private ImageView logo;

    @FXML
    private Button settings;

    @FXML
    private Button exitButt;

    @FXML
    private Button connect;

    @FXML
    private TableView<MachineSessions> workTable;

    @FXML
    private TableColumn<MachineSessions, String> date;

    @FXML
    private TableColumn<MachineSessions, String> benchName;

    @FXML
    private TableColumn<MachineSessions, String> sessionTime;

    @FXML
    private TableColumn<MachineSessions, String> colTimeStart;

    @FXML
    private CheckBox chkBxIdles;

    @FXML
    private TableColumn<MachineSessions, String> counter;

    @FXML
    private Button saveToFile;

    @FXML
    private Label labelTime;

    @FXML
    private Label labelUpTimes;

    @FXML
    private Label labelStatus;

    @FXML
    private Label labelPercent;

    @FXML
    private ComboBox machineList;

    @FXML
    private DatePicker dateTo;

    @FXML
    private DatePicker dateFrom;

    @FXML
    private CheckBox chkBxDetailed;

    public static ObservableList<MachineSessions> tableFills = FXCollections.observableArrayList();
    public ObservableList machines = FXCollections.observableArrayList();

    @FXML
    void initialize() throws Exception {
        SSHDB.connectSSH();
        initData();
        dateFrom.setValue(LocalDate.now());
        dateTo.setValue(LocalDate.now());
    }

    @FXML
    void fillTable() throws Exception {

//        getRequestData();
        workTable.setEditable(false);
        date.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
        benchName.setCellValueFactory(new PropertyValueFactory<>("name"));
        counter.setCellValueFactory(new PropertyValueFactory<>("sessions"));
        sessionTime.setCellValueFactory(new PropertyValueFactory<>("sessionLong"));
        colTimeStart.setCellValueFactory(new PropertyValueFactory<>("timeStart"));
        workTable.setItems(tableFills);
    }

    public void getRequestData() throws ParseException {

        String chosenMachine = String.valueOf(machineList.getValue());
        String startdate = String.valueOf(dateFrom.getValue());
        String enddate = String.valueOf(dateTo.getValue());
        if (chosenMachine.equals("Все")) {
            for (int i = 0; i < machines.size(); i++) {
                counter.setVisible(false);
                mySQLsend.getSummaryPerPeriod((String) machines.get(i), startdate, enddate);
            }
        } else if (chkBxDetailed.isSelected() == true) {
            counter.setVisible(false);
            mySQLsend.getDataForPeriod(chosenMachine, startdate, enddate);
        } else {
            counter.setVisible(true);
            mySQLsend.getSummaryPerPeriod(chosenMachine, startdate, enddate);
        }

    }
    int clickedTimes = 0;

    @FXML
    void logoEasternEgg() {
//        Object source = event.getSource();
//        System.out.println("you clicked " + source + " " + clickedTimes);
        clickedTimes++;
        if(clickedTimes == 5){
            modalityWindow();
            clickedTimes = 0;
        }
    }

    @FXML
    void clickedBtn(ActionEvent event) throws IOException, Exception {
        Object source = event.getSource();
        if (!(source instanceof Button)) {
            return;
        }

        Button clickedButton = (Button) source;
        Window parentWindow = ((Node) event.getSource()).getScene().getWindow();
//        System.out.println("You`ve pressed " + clickedButton.getText());
        switch (clickedButton.getId()) {
            case "connect":
                tableFills.clear();
                getRequestData();
                break;
            case "settings":
//                modalityWindow();
                break;
            case "exitButt":
                System.exit(0);
                break;
            case "saveToFile":
                ReportWriter.writeReportToXLS();
                break;

        }
    }

    public void modalityWindow() {
        Label secondLabel = new Label("Система контроля работы\n оборудования. \n Автор SancturbI4");

        StackPane secondaryLayout = new StackPane();
        secondaryLayout.getChildren().add(secondLabel);
        Scene secondScene = new Scene(secondaryLayout, 230, 100);
        Stage newWindow = new Stage();
        newWindow.setTitle("Настройки");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.initOwner(settings.getScene().getWindow());
        newWindow.setX(800);
        newWindow.setY(600);
        newWindow.show();
    }

    private void initData() throws Exception {
        getMachineList();
        fillTable();
    }

    public void getMachineList() {
        String[] machinelist = mySQLsend.getMachines();
        for (int i = 0; i < machinelist.length; i++) {
            machines.add(machinelist[i]);
        }
        machineList.getItems().add("Все");
        machineList.getItems().addAll(machines);
        machineList.getSelectionModel().selectFirst();
    }
}
