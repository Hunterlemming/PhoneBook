package Controller;

import Model.Database.DBC;
import Model.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="FXML">
    @FXML
    TableView table;
    @FXML
    TextField inputLastName;
    @FXML
    TextField inputFirstName;
    @FXML
    TextField inputEmail;
    @FXML
    Button addNewContract;
    @FXML
    StackPane menuPane;
    @FXML
    Pane contactPane;
    @FXML
    Pane exportPane;
    @FXML
    TextField inputExportName;
    @FXML
    Button exportButton;
    @FXML
    AnchorPane anchor;
    @FXML
    SplitPane mainSplit;
    //</editor-fold>

    private final String MENU_CONTACTS = "Kontaktok";
    private final String MENU_LIST = "Lista";
    private final String MENU_EXPORT = "Exportálás";
    private final String MENU_EXIT = "Kilépés";

    private final ObservableList<Person> data = FXCollections.observableArrayList();
    DBC database;

    private void setTableData(){

        //<editor-fold defaultstate="collapsed" desc="Last name column">
        TableColumn lastNameCol = new TableColumn("Vezetéknév");
        lastNameCol.setMinWidth(100);
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Person,String>("lastName"));

        lastNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person,String>>(){
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> event) {
                        Person contact = (Person)event.getTableView().getItems().get(event.getTablePosition().getRow());
                        contact.setLastName(event.getNewValue());
                        database.updateContact(contact);
                    }
                }
        );
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="First name column">
        TableColumn firstNameCol = new TableColumn("Keresztnév");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));

        firstNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person,String>>(){
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> event) {
                        Person contact = (Person)event.getTableView().getItems().get(event.getTablePosition().getRow());
                        contact.setFirstName(event.getNewValue());
                        database.updateContact(contact);
                    }
                }
        );
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Email column">
        TableColumn emailCol = new TableColumn("Email cím");
        emailCol.setMinWidth(200);
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));

        emailCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Person,String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Person, String> event) {
                        Person contact = (Person)event.getTableView().getItems().get(event.getTablePosition().getRow());
                        contact.setEmail(event.getNewValue());
                        database.updateContact(contact);
                    }
                }
        );
        //</editor-fold>

        table.getColumns().addAll(lastNameCol,firstNameCol,emailCol);

        data.addAll(database.getAllContacts());

        table.setItems(data );

    }

    private void setMenuData() {
        TreeItem<String> treeItemRoot1 = new TreeItem<>("Menü");
        TreeView<String> treeView = new TreeView<>(treeItemRoot1);
        treeView.setShowRoot(false);

        TreeItem<String> nodeItemA = new TreeItem<>(MENU_CONTACTS);
        TreeItem<String> nodeItemB = new TreeItem<>(MENU_EXIT);

        nodeItemA.setExpanded(true);

        Node contactsNode = new ImageView(new Image(getClass().getResourceAsStream("/imageSources/menuItem.png")));
        Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/imageSources/menuItem.png")));
        TreeItem<String> nodeItemA1 = new TreeItem<>(MENU_LIST, contactsNode);
        TreeItem<String> nodeItemA2 = new TreeItem<>(MENU_EXPORT, exportNode);

        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
        treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);

        menuPane.getChildren().add(treeView);

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                String selectedMenu;
                selectedMenu = selectedItem.getValue();

                if (null != selectedMenu) {
                    switch (selectedMenu) {
                        case MENU_CONTACTS:
                            selectedItem.setExpanded(true);
                            break;
                        case MENU_LIST:
                            contactPane.setVisible(true);
                            exportPane.setVisible(false);
                            break;
                        case MENU_EXPORT:
                            contactPane.setVisible(false);
                            exportPane.setVisible(true);
                            break;
                        case MENU_EXIT:
                            System.exit(0);
                            break;
                    }
                }

            }
        });

    }

    @FXML
    private void addContact(ActionEvent event) {
        String email = inputEmail.getText();
        if (email.length()>3 && email.contains("@") && email.contains(".")) {
            Person contact = new Person();
            contact.setLastName(inputLastName.getText());
            contact.setFirstName(inputFirstName.getText());
            contact.setEmail(inputEmail.getText());

            data.add(contact);
            database.addContact(contact);

            inputLastName.clear();
            inputFirstName.clear();
            inputEmail.clear();
        } else {
            alert("Adj meg egy valódi email címet!");
        }
    }

    @FXML
    private void exportList(ActionEvent event){
        String fileName = inputExportName.getText();
        fileName = fileName.replaceAll("\\s+","");
        if(fileName != null && !fileName.equals("")) {
            PdfGeneration pdfCreator = new PdfGeneration();
            pdfCreator.pdfGeneration(fileName, data);
        } else {
            alert("Adj meg egy file nevet!");
        }
    }

    private void alert(String text) {
        mainSplit.setDisable(true);
        mainSplit.setOpacity(0.4);

        Label label = new Label(text);
        Button alertButton = new Button("OK");
        VBox vbox = new VBox(label, alertButton);
        vbox.setAlignment(Pos.CENTER);

        alertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                mainSplit.setDisable(false);
                mainSplit.setOpacity(1);
                vbox.setVisible(false);
            }
        });

        anchor.getChildren().add(vbox);
        anchor.setTopAnchor(vbox, 300.0);
        anchor.setLeftAnchor(vbox, 300.0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        database = new DBC();
        setTableData();
        setMenuData();
    }

}
