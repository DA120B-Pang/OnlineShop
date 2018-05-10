package zaar.helperclasses;

import com.sun.xml.internal.ws.developer.MemberSubmissionEndpointReference;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import zaar.Database.Database;
import zaar.admin.EditFilters;
import zaar.admin.FilterInterfaces;
import zaar.admin.FilterShowHide;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;
import zaar.product.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Singleton class for creating objects for screen changing
 */
public class ScreenSingleton {
    private static ScreenSingleton ourInstance = new ScreenSingleton();
    private DataSingleton dS = DataSingleton.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private EditFilters ePF = EditFilters.getInstance();

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
            activateScreen(e,"../admin/AddProd.fxml");
        }
    }
    public class OpenEditProductScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"../admin/EditProd.fxml");
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
//    //****************Filter popups***********************************************************************
//
//    /**
//     * Filter popup for manufaturer
//     */
//    public class PopUpFilterManufacturer extends FilterShowHide{
//        private Button button;
//        private ArrayList<Manufacturer> list;
//        private ObservableList<Manufacturer> list2;
//        private FilteredList<Manufacturer> filteredData = null;
//        private FilteredList<Product> productFilteredList;
//        private ObservableList<Manufacturer>listUnchanged;
//        private Database dB = Database.getInstance();
//        private Stage stage;
//        private TextField filterTxtField = new TextField();
//        private ComboBox<Manufacturer> listView;
//        private boolean listIsCollected = false;
//        private SimpleIntegerProperty manufaturerid = new SimpleIntegerProperty(0);
//
//
//
//        public PopUpFilterManufacturer(FilterInterfaces.SetFilter setFilter, Button button, FilteredList<Product> productFilteredList){
//            super(setFilter);
//            this.button = button;
//            this.productFilteredList = productFilteredList;
//            popUp();
//        }
//        private void popUp(){
//            EditFilters.IntegerPropertyEquals prodManufacturer = ePF.new IntegerPropertyEquals(EditFilters.PropertyCompare.MANUFACTURER);
//            prodManufacturer.setId(manufaturerid);
//            prodManufacturer.setFilter(super.getSetFilter());
//
//            stage = new Stage();
//            stage.setResizable(false);
//            stage.initStyle(StageStyle.UNDECORATED);
//
//            filterTxtField.setPromptText("Contains");
//
//            listView = new ComboBox<>();
//
//            VBox vboxListView = new VBox(filterTxtField,listView);
//
//
//            VBox vbox = new VBox();
//            Button selectBtn = new Button("Accept selected");
//            Button resetFilterBtn = new Button("Reset filter");
//
//            Insets insets = new Insets(5,5,5,5);
//            VBox.setMargin(filterTxtField, insets);
//            VBox.setMargin(listView, insets);
//            HBox.setMargin(selectBtn, insets);
//            insets = new Insets(5,5,5,20);
//            HBox.setMargin(resetFilterBtn, insets);
//
//
//            HBox hbox = new HBox(selectBtn,resetFilterBtn);
//            vbox.getChildren().addAll(vboxListView, hbox);
//            selectBtn.setOnAction(event -> {
//                Manufacturer selectedIndices = listView.getSelectionModel().getSelectedItem();//getSelectedIndices();
//                if(selectedIndices!=null) {
//                    manufaturerid.set(selectedIndices.getId());
//                    dS.setFilterButtonYellow(button);
//                    hide();
//                }
//            });
//
//            resetFilterBtn.setOnAction((Event)->{
//                manufaturerid.set(0);
//                dS.setFilterButtonGreen(button);
//                hide();
//            });
//            tS.setRoundBorder(vbox);
//            Scene scene = new Scene(vbox, -1, 120);
//            stage.setScene(scene);
//
//            stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(!stage.isFocused()){
//                        stage.hide();
//                    }
//                }
//            });
//
//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    selectBtn.requestFocus();
//                }
//            });
//
//        }
//
//        @Override
//        public void hide() {
//            if(stage.isShowing()){
//                stage.hide();
//            }
//        }
//
//        @Override
//        public void show(double x, double y) {
//            if(!listIsCollected){
//                collectList();
//            }
//            if(listIsCollected){
//                stage.setX(x);
//                stage.setY(y);
//                stage.show();
//            }
//        }
//
//        @Override
//        public void close() {
//            stage.close();
//        }
//
//        @Override
//        public boolean getIsShowing() {
//            return false;
//        }
//
//        private void collectList(){
//            list = dB.getManufacturers();
//            if(list!=null) {
//                listUnchanged = FXCollections.observableList(list);
//                filteredData = new FilteredList<>(listUnchanged, s -> true);
//                listView.setItems(filteredData);
//                filterTxtField.textProperty().addListener((obs,ov,nv)->{
//                    setPredicate();
//                });
//                listIsCollected = true;
//            }
//        }
//
//        public void setPredicate(){
//            filteredData.setPredicate(predicate());
//        }
//        private Predicate<Manufacturer> predicate(){
//            return Predicate->{
//                String filter = filterTxtField.getText();
//                boolean retval1;
//                boolean retVal2 = false;
//                if(filter == null || filter.length() == 0) {
//                    retval1  = true;
//                }
//                else {
//                    retval1 = Predicate.getName().getValue().toLowerCase().contains(filter.toLowerCase());//Search for manufacturer case insensitive
//                }
//                for (Product p : productFilteredList) {
//                    if (Predicate.getId() == p.getManufacturerId()) {
//                        retVal2 = true;
//                    }
//                }
//                return retval1 && retVal2;
//            };
//        }
//    }
//    public class PopUpFilterString extends FilterShowHide{
//        private Stage stage;
//        private TextField filterText;
//        private Button button;
//        private boolean isButtonGreen;
//
//        public PopUpFilterString(FilterInterfaces.SetFilter setFilter,Button button){
//            super(setFilter);
//            stage = new Stage();
//            filterText = new TextField();
//            isButtonGreen = true;
//            this.button = button;
//            popUp();
//        }
//        private void popUp() {
//            stage.setResizable(false);
//
//            filterText.setPrefWidth(200);
//            stage.initStyle(StageStyle.UNDECORATED);
//
//            EditFilters.ProdName prodName = ePF.new ProdName();
//            prodName.setFilter(super.getSetFilter());
//
//            prodName.setName(filterText);
//            filterText.textProperty().addListener(l->{
//                if(filterText.getText().length()==0 && !isButtonGreen){
//                    dS.setFilterButtonGreen(button);
//                    isButtonGreen = true;
//                }
//                else if(isButtonGreen){
//                    dS.setFilterButtonYellow(button);
//                    isButtonGreen = false;
//                }
//            });
//            VBox.setMargin(filterText,new Insets(5,5,5,5));
//            VBox vBox = new VBox(filterText);
//
//            tS.setRoundBorder(vBox);
//
//            stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(!stage.isFocused()){
//                        stage.hide();
//                    }
//                }
//            });
//
//
//            Scene scene = new Scene(vBox, -1, -1);
//            stage.setScene(scene);
//        }
//
//        public void show(double x,double y){
//            if(!stage.isShowing()){
//                stage.setX(x);
//                stage.setY(y);
//                stage.show();
//            }
//        }
//
//        public void hide(){
//            if(!stage.isShowing()){
//                stage.show();
//            }
//        }
//
//        public boolean getIsShowing(){
//            return stage.isShowing();
//        }
//
//        public void close(){
//            stage.close();
//        }
//
//    }
//    public class PopUpFilterCategory extends FilterShowHide{
//        private Stage stage;
//        private FilterInterfaces.SetFilter setFilter;
//        private Button button;
//        private MenuButton menuButton = new MenuButton("Choose");
//        private SimpleIntegerProperty categoryID = new SimpleIntegerProperty(0);
//
//        public PopUpFilterCategory(FilterInterfaces.SetFilter setFilter, Button button){
//            super(setFilter);
//            this.button = button;
//        }
//
//        public void popUp(){
//            EditFilters.IntegerPropertyEquals prodCategory = ePF.new IntegerPropertyEquals(EditFilters.PropertyCompare.CATEGORY);
//            prodCategory.setId(categoryID);
//            prodCategory.setFilter(super.getSetFilter());
//
//            stage = new Stage();
//            stage.setResizable(false);
//            stage.initStyle(StageStyle.UNDECORATED);
//
////            VBox vboxListView = new VBox(filterTxtField,listView);
////
////
////            VBox vbox = new VBox();
////            vbox.setBorder(new Border(new BorderStroke(Color.BLACK,
////                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
////            Button selectBtn = new Button("Accept selected");
////            Button resetFilterBtn = new Button("Reset filter");
////
////            Insets insets = new Insets(5,5,5,5);
////            VBox.setMargin(filterTxtField, insets);
////            VBox.setMargin(listView, insets);
////            HBox.setMargin(selectBtn, insets);
////            insets = new Insets(5,5,5,20);
////            HBox.setMargin(resetFilterBtn, insets);
////
////
////            HBox hbox = new HBox(selectBtn,resetFilterBtn);
////            vbox.getChildren().addAll(vboxListView, hbox);
////            selectBtn.setOnAction(event -> {
////                Manufacturer selectedIndices = listView.getSelectionModel().getSelectedItem();//getSelectedIndices();
////                if(selectedIndices!=null) {
////                    manufaturerid.set(selectedIndices.getId());
////                    dS.setFilterButtonYellow(button);
////                    hide();
////                }
////            });
////
////            resetFilterBtn.setOnAction((Event)->{
////                manufaturerid.set(0);
////                dS.setFilterButtonGreen(button);
////                hide();
////            });
////            tS.setRoundBorder(vbox);
////            Scene scene = new Scene(vbox, -1, 120);
////            stage.setScene(scene);
//
//            stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
//                @Override
//                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                    if(!stage.isFocused()){
//                        stage.hide();
//                    }
//                }
//            });
//        }
//
//
//
//        public void getMenu(){
//            //MenuButton.getBuildMenu().getMenu();
//        }
//
//        @Override
//        public void hide() {
//
//        }
//
//        @Override
//        public void show(double x, double y) {
//
//        }
//
//        @Override
//        public void close() {
//
//        }
//
//        @Override
//        public boolean getIsShowing() {
//            return false;
//        }
//        private class AddMenuAction implements MenuAction{
//
//            @Override
//            public void action(Menus menu) {
//                //nothing
//            }
//        }
//
//        private class AddMenuItemAction implements MenuItemAction{
//
//            @Override
//            public void action(VBox vbox, Category cat) {
//                categoryID.set(cat.getCategoryId());
//            }
//        }
//    }
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
                        manufacturer.getName().setValue(selectedIndices.getName().getValue());
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
                    return Predicate.getName().getValue().toLowerCase().contains(filter.toLowerCase());//Search for manufacturer case insensitive
                }
            };
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
                    tS.getBuildMenu().getMenu(menuBtn,new VBox(),new AddMenuAction(), new AddMenuItemAction(),null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);
                }
            });

            tS.getBuildMenu().getMenu(menuBtn,new VBox(),new AddMenuAction(), new AddMenuItemAction(),null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);//Get menu

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
                    tS.getBuildMenu().getMenu(menuBtn,new VBox(),new AddMenuAction(), new AddMenuItemAction(),null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);//Rebuild menu to add new
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
