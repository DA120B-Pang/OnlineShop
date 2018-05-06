package zaar.helperclasses;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import zaar.Database.Database;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Singleton class for creating objects for screen changing
 */
public class ScreenSingleton {
    private static ScreenSingleton ourInstance = new ScreenSingleton();
    private DataSingleton dS = DataSingleton.getInstance();

    public static ScreenSingleton getInstance() {
        return ourInstance;
    }

    private ScreenSingleton() {
    }

    public interface ScreenChange{
        public void screenChange(ActionEvent e);
    }

    public class OpenProductScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../product/Product.fxml");
        }
    }
    public class OpenAddProductScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../admin/AddProd.fxml");
        }
    }
    public class OpenManageDatabase implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../admin/ManageDatabase.fxml");
        }
    }
    public class OpenLoginScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../sampleLogin.fxml");
        }
    }
    public class OpenCartScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            //activateScreen(e,"../sampleLogin.fxml");
        }
    }
    private void activateScreen(ActionEvent e, String url){
        try {
            Node node = (Node) e.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            Parent root = loader.load();

            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public class SelectManufacturerPopUp {
            public void popUp(Manufacturer manufacturer, Double x, Double y){
                Database dB = Database.getInstance();
                ArrayList<Manufacturer> list = dB.getManufacturers();
                if(list!=null) {
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Select Manufacturer");

                    ObservableList<Manufacturer> list2 = FXCollections.observableList(list);
                    FilteredList<Manufacturer> filteredData = new FilteredList<>(list2, s -> true);

                    TextField filterTxtField = new TextField();
                    filterTxtField.setPromptText("Search");
                    filterTxtField.textProperty().addListener(observ->{
                        String filter = filterTxtField.getText();
                        if(filter == null || filter.length() == 0) {
                            filteredData.setPredicate(s -> true);
                        }
                        else {
                            filteredData.setPredicate(s -> s.getName().getValue().toLowerCase().contains(filter.toLowerCase()));//Search for manufacturer case insensitive
                        }
                    });

                    ListView<Manufacturer> listView = new ListView(filteredData);

                    VBox vboxListView = new VBox(filterTxtField,listView);


                    HBox hbox = new HBox();
                    Button selectBtn = new Button("Accept selected");

                    Insets insets = new Insets(5,5,5,5);
                    VBox.setMargin(filterTxtField, insets);
                    VBox.setMargin(listView, insets);
                    HBox.setMargin(selectBtn, insets);

                    hbox.getChildren().addAll(vboxListView, selectBtn);
                    selectBtn.setOnAction(event -> {
                        Manufacturer selectedIndices = listView.getSelectionModel().getSelectedItem();//getSelectedIndices();
                        manufacturer.setId(selectedIndices.getId());
                        manufacturer.getName().setValue(selectedIndices.getName().getValue());
                        stage.close();
                    });

                    Scene scene = new Scene(hbox, -1, 120);
                    stage.setX(x);
                    stage.setY(y);
                    stage.setScene(scene);
                    stage.show();

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            selectBtn.requestFocus();
                        }
                    });
                }
        }
    }
    public class InsertStringToDbPopUp {
        Boolean retVal;
        public void popUp(String titleAddButton, BooleanMethodString dbQuery, Double x, Double y){
            Database dB = Database.getInstance();
            ToolsSingleton tS = ToolsSingleton.getInstance();
            DataSingleton dS = DataSingleton.getInstance();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(titleAddButton);

            HBox hbox = new HBox();

            TextField nameTxtField = new TextField();

            nameTxtField.setPromptText("Name");
            Insets insets = new Insets(75,20,20,20);
            HBox.setMargin(nameTxtField, insets);
            nameTxtField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(newValue.length()>45){
                        nameTxtField.setText(oldValue);
                    }
                }
            });

            Button addBtn = new Button(titleAddButton);

            HBox.setMargin(addBtn, insets);

            hbox.getChildren().addAll(nameTxtField, addBtn);
            AnchorPane anchorPane = new AnchorPane(hbox);
            anchorPane.prefHeight(300);
            anchorPane.prefWidth(100);



            addBtn.setOnAction(event -> {
                if(nameTxtField.getText().equalsIgnoreCase("")) {
                    retVal = false;
                }
                else{
                    retVal = dbQuery.method(nameTxtField.getText());//dB.addManufacturer(nameTxtField.getText());
                }
                if(retVal){
                    tS.getButtonAnimation(anchorPane,addBtn,dS.getOkImgView().getImage());
                    nameTxtField.setText("");
                }
                else{
                    tS.getButtonAnimation(anchorPane,addBtn,dS.getNotOkImgView().getImage());
                }
            });

            Scene scene = new Scene(anchorPane, -1, 120);
            stage.setX(x);
            stage.setY(y);
            stage.setScene(scene);
            stage.show();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addBtn.requestFocus();
                }
            });
        }
    }
    public class InsertIntStringToDbPopUp {
        private TextField menuIdTxtField = new TextField();
        private Boolean retVal;
        private Database dB = Database.getInstance();
        private ToolsSingleton tS = ToolsSingleton.getInstance();
        private DataSingleton dS = DataSingleton.getInstance();
        private MenuButton menuBtn = new MenuButton("Select parent menu");//Button for selecting parent menu
        private TextField nameTxtField = new TextField();
        private Label parentMenuIdLbl = new Label("Parent menu ID");
        private Label nameLbl = new Label("Name");
        private Button addBtn = new Button();
        private GridPane gridPane = new GridPane();
        private int id;
        private boolean menuChanged;//Tells if menu was changed when closing window

        public void popUp(String titleAddButton, BooleanMethodIntString dbQuery, boolean idNullOk,String idPrompt, Double x, Double y){
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(titleAddButton);
            stage.setOnCloseRequest(new EventHandler< WindowEvent>(){//Inform main window if Change to menu was made
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                    if(menuChanged){
                        dS.toggleMenuChanged();
                    }
                    stage.close();
                }
            });

            menuIdTxtField.setEditable(false);
            menuIdTxtField.setPromptText(idPrompt);

            menuBtn.setOnMouseClicked((Event)->{//If database error occured then Get menu on click
                if(menuBtn.getItems().size()==0){
                    tS.getBuildMenu().getMenu(menuBtn,new VBox(),new AddMenuAction(), new AddMenuItemAction());
                }
            });

            tS.getBuildMenu().getMenu(menuBtn,new VBox(),new AddMenuAction(), new AddMenuItemAction());//Get menu

            nameTxtField.setPromptText("Name");
            nameTxtField.textProperty().addListener(new ChangeListener<String>() {//Check length(limit in database)
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(newValue.length()>45){
                        nameTxtField.setText(oldValue);
                    }
                }
            });
            setGridPane();
            AnchorPane anchorPane = new AnchorPane(gridPane);
            anchorPane.prefHeight(300);
            anchorPane.prefWidth(100);


            addBtn.setText(titleAddButton);

            addBtn.setOnAction(event -> {//Write to database
                if(nameTxtField.getText().equalsIgnoreCase("") || id==0 && !idNullOk){//Check so that not nullable field are ok
                    retVal = false;
                }
                else{
                    try {
                        retVal = dbQuery.method(id, nameTxtField.getText());//Write to database
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(retVal){//If writing to database was successful
                    tS.getButtonAnimation(anchorPane,addBtn,dS.getOkImgView().getImage());//Set animation for success
                    tS.getBuildMenu().getMenu(menuBtn,new VBox(),new AddMenuAction(), new AddMenuItemAction());//Rebuild menu to add new
                    menuChanged = true;//Set that menu has been changed(for when exiting window to tell main window about change)
                    nameTxtField.setText("");//remove info from fields
                    menuIdTxtField.setText("");//remove info from fields
                }
                else{
                    tS.getButtonAnimation(anchorPane,addBtn,dS.getNotOkImgView().getImage());//Set animation for fail
                }
            });

            Scene scene = new Scene(anchorPane);
            stage.setX(x);
            stage.setY(y);
            stage.setScene(scene);
            stage.show();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    addBtn.requestFocus();
                }
            });
        }
        private void setGridPane(){
            Insets insets = new Insets(5,5,5,5);
            gridPane.setFillWidth(addBtn,true);
            gridPane.setFillWidth(menuBtn,true);

            GridPane.setMargin(addBtn, insets);
            GridPane.setMargin(parentMenuIdLbl, insets);
            GridPane.setMargin(nameLbl, insets);
            GridPane.setMargin(parentMenuIdLbl, insets);
            GridPane.setMargin(menuIdTxtField, insets);
            GridPane.setMargin(nameTxtField, insets);
            GridPane.setMargin(menuBtn, insets);


            gridPane.add(parentMenuIdLbl,0,0,1,1);
            gridPane.add(nameLbl,0,1,1,1);
            gridPane.add(menuIdTxtField,1,0,1,1);
            gridPane.add(nameTxtField,1,1,1,1);
            gridPane.add(menuBtn,2,0,1,1);
            gridPane.add(addBtn,2,2,1,1);
        }

        private class AddMenuAction implements MenuAction{

            @Override
            public void action(Menus menu) {
                menuIdTxtField.setText(menu.getName());
                id = menu.getMenuId();
            }
        }

        private class AddMenuItemAction implements MenuItemAction{

            @Override
            public void action(VBox vbox, Category cat) {
                //Nothing
            }
        }
    }
}
