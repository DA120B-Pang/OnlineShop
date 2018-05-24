package zaar.helperclasses;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.product.EditFiltersProd;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Singleton class for creating objects for screen changing
 */
public class ScreenSingleton {
    private static ScreenSingleton ourInstance = new ScreenSingleton();
    private DataSingleton dS = DataSingleton.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private EditFiltersProd ePF = EditFiltersProd.getInstance();

    public static ScreenSingleton getInstance() {
        return ourInstance;
    }

    private ScreenSingleton() {
    }
    //****************Navigation***********************************************************************
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
            activateScreen(e,"../admin/add/AddProd.fxml");
        }
    }
    public class OpenEditScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../admin/edit/Edit.fxml");
        }
    }
    public class OpenEditUserScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../admin/edit/EditUser.fxml");
        }
    }
    public class OpenManageDatabase implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../admin/AdminTools.fxml");
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
    //****************Adding products/menus/categories popups*********************************************
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
                filterTxtField.setPromptText("Contains");
                filterTxtField.textProperty().addListener((obs,ov,nv)->{
                    filteredData.setPredicate(predicate(nv));
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
                    if(selectedIndices!=null) {
                        manufacturer.setId(selectedIndices.getId());
                        manufacturer.setName(selectedIndices.getName());
                        dS.toggleManChanged();
                        stage.close();
                    }
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
        private Predicate<Manufacturer> predicate(String filter){
            return Predicate->{
                if(filter == null || filter.length() == 0) {
                    return true;
                }
                else {
                    return Predicate.getName().toLowerCase().contains(filter.toLowerCase());//Search for manufacturer case insensitive
                }
            };
        }
    }
    public class InsertStringToDbPopUp {
        private boolean retVal;
        private Manufacturer manufacturer = new Manufacturer();
        private TextField nameTxtField = new TextField();

        public void setId(Manufacturer manufacturer){
            this.manufacturer = manufacturer;
            nameTxtField.setText(manufacturer.getName());
        }
        public void popUp(String titleAddButton, BooleanMethodString dbQuery, Double x, Double y){
            Database dB = Database.getInstance();
            ToolsSingleton tS = ToolsSingleton.getInstance();
            DataSingleton dS = DataSingleton.getInstance();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(titleAddButton);

            HBox hbox = new HBox();

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
                    retVal = dbQuery.method(manufacturer.getId(), nameTxtField.getText());//dB.addManufacturer(nameTxtField.getText());
                }
                if(retVal){
                    tS.getButtonAnimation(anchorPane,addBtn,dS.getOkImgView().getImage());
                    manufacturer.setName(nameTxtField.getText());
                    nameTxtField.setText("");
                    dS.toggleManChanged();
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

    /**
     * Add Category and Add Menu
     */
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
        private int parentId;
        private int iD;
        private boolean menuChanged;//Tells if menu was changed when closing window
        private Category cat;
        private Menus menu;
        private boolean clearText = true;
        private boolean useMenuActionForMenu = false;
        private boolean useRootBtn = false;
        private Button rootBtn;
        private MenuAction menuAction;

        /**
         * Sets parameters when editing an existing Category
         * @param cat
         * @param parentName
         */
        public void setParametersCategory(Category cat, String parentName){
            this.iD = cat.getCategoryId();
            this.parentId = cat.getParentMenuId();
            this.menuIdTxtField.setText(parentName);
            this.nameTxtField.setText(cat.getName());
            this.cat = cat;
            clearText = false;
        }

        /**
         * Sets parameters when editing an existing Menu
         * @param menu
         * @param parentName
         */
        public void setParametersMenu(Menus menu, String parentName){
            this.iD = menu.getMenuId();
            this.parentId = menu.getParentMenuId();
            this.menuIdTxtField.setText(parentName);
            this.nameTxtField.setText(menu.getName());
            this.menu = menu;
            useMenuActionForMenu = true;
            useRootBtn = true;
        }

        public void popUp(String titleAddButton, BooleanMethodIntString dbQuery, boolean idNullOk,String idPrompt, Double x, Double y){
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(titleAddButton);
            stage.setOnCloseRequest(new EventHandler< WindowEvent>(){//Inform main window if Change to menu was made
                @Override
                public void handle(WindowEvent event) {
                    event.consume();
                    if(menuChanged){
                        dS.toggleMenuChanged();//Announce that change has been made (if something should be updated)
                    }
                    stage.close();
                }
            });
            if(useMenuActionForMenu){
                menuAction = new AddMenuActionForMenu();
            }
            else{
                menuAction = new AddMenuAction();
            }
            menuIdTxtField.setEditable(false);
            menuIdTxtField.setPromptText(idPrompt);

            menuBtn.setOnMouseClicked((Event)->{//If database error occured then Get menu on click
                if(menuBtn.getItems().size()==0){
                    tS.getBuildMenu().getMenu(menuBtn,menuAction, new AddMenuItemAction(),null,null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);
                }
            });

            if(useRootBtn){//For setting root as parent for menu in edit screen
                rootBtn = new Button("Set root as parent");
                rootBtn.setOnAction((Event)->{
                    menuIdTxtField.setText("root");
                    parentId = 0;
                });
            }

            tS.getBuildMenu().getMenu(menuBtn,menuAction, new AddMenuItemAction(),null,null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);//Get menu

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
                if(nameTxtField.getText().equalsIgnoreCase("") || parentId ==0 && !idNullOk){//Check so that not nullable field are ok (Menus can have parentId = 0 -> root. Categories must have parentId>0
                    retVal = false;
                }
                else{
                    try {
                        retVal = dbQuery.method(iD,parentId, nameTxtField.getText());//Write to database
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                if(retVal){//If writing to database was successful
                    tS.getButtonAnimation(anchorPane,addBtn,dS.getOkImgView().getImage());//Set animation for success

                    tS.getBuildMenu().getMenu(menuBtn, menuAction, new AddMenuItemAction(),null,null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);//Rebuild menu to add new
                    if(cat!=null){
                        cat.setParentMenuId(parentId);
                        cat.setName(nameTxtField.getText());
                    }
                    else if(menu!=null){
                        menu.setParentMenuId(parentId);
                        menu.setName(nameTxtField.getText());
                    }
                    menuChanged = true;//Set that menu has been changed(for when exiting window to tell main window about change)
                    if(clearText) {
                        nameTxtField.setText("");//remove info from fields
                        menuIdTxtField.setText("");//remove info from fields
                    }
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
        private void setGridPane(){//Setting layout on popUp
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

            if(useRootBtn){
                GridPane.setMargin(rootBtn, insets);
                gridPane.add(rootBtn,2,1,1,1);
            }
        }

        private class AddMenuAction implements MenuAction{

            @Override
            public void action(Menus menu) {
                menuIdTxtField.setText(menu.getName());
                parentId = menu.getMenuId();
            }
        }
        private class AddMenuActionForMenu implements MenuAction{

            @Override
            public void action(Menus menu) {
                if(menu.getMenuId()!=iD) {
                    menuIdTxtField.setText(menu.getName());
                    parentId = menu.getMenuId();
                }
            }
        }
        private class AddMenuItemAction implements MenuItemAction{

            @Override
            public void action(Category cat) {
                //Nothing
            }
        }
    }
}
