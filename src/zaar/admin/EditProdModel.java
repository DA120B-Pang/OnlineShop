package zaar.admin;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zaar.Database.Database;
import zaar.admin.EditFilters.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;
import zaar.product.Product;

import java.util.ArrayList;
import java.util.function.Predicate;

public class EditProdModel {
    private Database dB = Database.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private EditFilters ePF = EditFilters.getInstance();
    private EditFilterObject editProdFilterObject;
    private PopUpFilterString popUpFilterName;
    private PopUpFilterManufacturer popUpFilterManufacturer;
    private PopUpFilterCategory popUpFilterCategory;

    public void getTopHBox(HBox hBox){
        ToolsSingleton tS = ToolsSingleton.getInstance();
        ScreenSingleton sS = ScreenSingleton.getInstance();
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
        tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());
    }

    public TableView<Product> getProductTableview(FilteredList<Product> list){
        editProdFilterObject = ePF.new EditFilterObject(list);

        TableColumn<Product,String> nameColumn  = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        Button nameColumnFilterBtn = createColumnBtn();
        nameColumn.setGraphic(nameColumnFilterBtn);

        nameColumnFilterBtn.setOnAction((Event)->{
            if(popUpFilterName==null) {
                popUpFilterName = new PopUpFilterString(editProdFilterObject,nameColumnFilterBtn);
            }
            showHideFilter(Event,nameColumnFilterBtn,popUpFilterName,0,-10);
        });


        TableColumn<Product,Integer> categoryIdColumn  = new TableColumn<>("Category Id");
        categoryIdColumn.setMinWidth(100);
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        Button categoryIdFilterBtn = createColumnBtn();
        categoryIdColumn.setGraphic(categoryIdFilterBtn);

        categoryIdFilterBtn.setOnAction((Event)->{
            if(popUpFilterCategory==null) {
                popUpFilterCategory = new PopUpFilterCategory(editProdFilterObject,categoryIdFilterBtn,list);
            }
            showHideFilter(Event,categoryIdFilterBtn,popUpFilterCategory,0,-93);
        });

        TableColumn<Product,Integer> manufacturerIdColumn  = new TableColumn<>("Manufacturer Id");
        manufacturerIdColumn.setMinWidth(100);
        manufacturerIdColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturerId"));
        Button manufacturerIdColumnFilterBtn = createColumnBtn();
        manufacturerIdColumn.setGraphic(manufacturerIdColumnFilterBtn);

        manufacturerIdColumnFilterBtn.setOnAction((Event)->{
            if(popUpFilterManufacturer==null) {
                popUpFilterManufacturer = new PopUpFilterManufacturer(editProdFilterObject,manufacturerIdColumnFilterBtn,list);
            }
            showHideFilter(Event,manufacturerIdColumnFilterBtn,popUpFilterManufacturer,0,-93);
        });



        TableColumn<Product,Double> priceColumn  = new TableColumn<>("Price");
        priceColumn.setSortable(true);
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product,Integer> quantityColumn  = new TableColumn<>("Quantity");
        quantityColumn.setSortable(true);
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Product,String> descriptionColumn  = new TableColumn<>("Description");
        descriptionColumn.setSortable(true);
        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));


        TableView<Product> tableView = new TableView();
        tableView.setPrefHeight(-1);
        tableView.setPrefWidth(-1);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getColumns().addAll(
                nameColumn,
                categoryIdColumn,
                manufacturerIdColumn,
                priceColumn,
                quantityColumn,
                descriptionColumn);

        SortedList<Product> sortedData = new SortedList<>(list);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
    }

    public Button createColumnBtn(){
        Button button = new Button();
        dS.setFilterButtonGreen(button);
        return button;
    }

    private void showHideFilter(Event e, Button button, FilterShowHide fSH, double offsetX, double offsetY){
        Node node = (Node)e.getSource();
        Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
        if(fSH.getIsShowing()) {
            fSH.hide();
        }
        else{
            fSH.show(
                    stage.getX()+ button.getLocalToSceneTransform().getTx()+offsetX,
                    stage.getY()+button.getLocalToSceneTransform().getTy()+offsetY);
        }
    }
    //****************Filter popups***********************************************************************

    /**
     * Filter popup for manufaturer
     */
    public class PopUpFilterManufacturer extends FilterShowHide{
        private Button button;
        private ArrayList<Manufacturer> list;
        private FilteredList<Manufacturer> filteredData = null;
        private FilteredList<Product> productFilteredList;
        private Stage stage;
        private TextField filterTxtField = new TextField();
        private ComboBox<Manufacturer> listView;
        private boolean listIsCollected = false;
        private SimpleIntegerProperty manufaturerid = new SimpleIntegerProperty(0);
        Manufacturer selectedIndices;



        public PopUpFilterManufacturer(FilterInterfaces.SetFilter setFilter, Button button, FilteredList<Product> productFilteredList){
            super(setFilter);
            this.button = button;
            this.productFilteredList = productFilteredList;
            popUp();
        }
        private void popUp(){
            EditFilters.IntegerPropertyEquals prodManufacturer = ePF.new IntegerPropertyEquals(EditFilters.PropertyCompare.MANUFACTURER);
            prodManufacturer.setId(manufaturerid);
            prodManufacturer.setFilter(super.getSetFilter());

            stage = new Stage();
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            filterTxtField.setPromptText("Contains");

            listView = new ComboBox<>();

            VBox vboxListView = new VBox(filterTxtField,listView);


            VBox vbox = new VBox();
            Button selectBtn = new Button("Accept selected");
            Button resetFilterBtn = new Button("Reset filter");

            Insets insets = new Insets(5,5,5,5);
            VBox.setMargin(filterTxtField, insets);
            VBox.setMargin(listView, insets);
            HBox.setMargin(selectBtn, insets);
            insets = new Insets(5,5,5,20);
            HBox.setMargin(resetFilterBtn, insets);


            HBox hbox = new HBox(selectBtn,resetFilterBtn);
            vbox.getChildren().addAll(vboxListView, hbox);
            selectBtn.setOnAction(event -> {
                selectedIndices = listView.getSelectionModel().getSelectedItem();//getSelectedIndices();
                if(selectedIndices!=null) {
                    manufaturerid.set(selectedIndices.getId());
                    dS.setFilterButtonYellow(button);
                    hide();
                }
            });

            resetFilterBtn.setOnAction((Event)->{
                manufaturerid.set(0);
                selectedIndices = null;
                dS.setFilterButtonGreen(button);
                hide();
            });
            tS.setRoundBorder(vbox);
            Scene scene = new Scene(vbox, -1, 120);
            stage.setScene(scene);

            stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(!stage.isFocused()){
                        stage.hide();
                    }
                }
            });

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    selectBtn.requestFocus();
                }
            });

        }

        @Override
        public void hide() {
            if(stage.isShowing()){
                stage.hide();
            }
        }

        @Override
        public void show(double x, double y) {
            if(!listIsCollected){
                collectList();
            }
            if(listIsCollected){
                stage.setX(x);
                stage.setY(y);
                setPredicate();
                stage.show();
            }
        }

        @Override
        public void close() {
            stage.close();
        }

        @Override
        public boolean getIsShowing() {
            return stage.isShowing();
        }

        private void collectList(){
            list = dB.getManufacturers();
            if(list!=null) {
                ObservableList<Manufacturer>listUnchanged = FXCollections.observableList(list);
                filteredData = new FilteredList<>(listUnchanged, s -> true);
                listView.setItems(filteredData);
                filterTxtField.textProperty().addListener((obs,ov,nv)->{
                    setPredicate();
                });
                listIsCollected = true;
            }
        }

        private void setPredicate(){
            filteredData.setPredicate(predicate());
            if(selectedIndices!=null){
                listView.getSelectionModel().select(selectedIndices);
            }
        }
        private Predicate<Manufacturer> predicate(){
            return Predicate->{
                String filter = filterTxtField.getText();
                boolean retVal1;
                boolean retVal2 = false;
                if(filter == null || filter.length() == 0) {
                    retVal1  = true;
                }
                else {
                    retVal1 = Predicate.getName().getValue().toLowerCase().contains(filter.toLowerCase());//Search for manufacturer case insensitive

                }
                for (Product p : productFilteredList) {
                    if (Predicate.getId() == p.getManufacturerId()) {
                        retVal2 = true;
                    }
                }
                return retVal1 && retVal2;
            };
        }
    }

    /**
     * Filter for string
     */
    public class PopUpFilterString extends FilterShowHide{
        private Stage stage;
        private TextField filterText;
        private Button button;
        private boolean isButtonGreen;

        public PopUpFilterString(FilterInterfaces.SetFilter setFilter,Button button){
            super(setFilter);
            stage = new Stage();
            filterText = new TextField();
            isButtonGreen = true;
            this.button = button;
            popUp();
        }
        private void popUp() {
            stage.setResizable(false);

            filterText.setPrefWidth(200);
            stage.initStyle(StageStyle.UNDECORATED);

            EditFilters.ProdName prodName = ePF.new ProdName();
            prodName.setFilter(super.getSetFilter());

            prodName.setName(filterText);
            filterText.textProperty().addListener(l->{
                if(filterText.getText().length()==0 && !isButtonGreen){
                    dS.setFilterButtonGreen(button);
                    isButtonGreen = true;
                }
                else if(isButtonGreen){
                    dS.setFilterButtonYellow(button);
                    isButtonGreen = false;
                }
            });
            VBox.setMargin(filterText,new Insets(5,5,5,5));
            VBox vBox = new VBox(filterText);

            tS.setRoundBorder(vBox);

            stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(!stage.isFocused()){
                        stage.hide();
                    }
                }
            });


            Scene scene = new Scene(vBox, -1, -1);
            stage.setScene(scene);
        }

        public void show(double x,double y){
            if(!stage.isShowing()){
                stage.setX(x);
                stage.setY(y);
                stage.show();
            }
        }

        public void hide(){
            if(!stage.isShowing()){
                stage.show();
            }
        }

        public boolean getIsShowing(){
            return stage.isShowing();
        }

        public void close(){
            stage.close();
        }

    }

    /**
     * Filter for Category
     */
    public class PopUpFilterCategory extends FilterShowHide{
        private Stage stage;
        private FilterInterfaces.SetFilter setFilter;
        private Button button;
        private MenuButton menuButton = new MenuButton("Choose");
        private SimpleIntegerProperty categoryID = new SimpleIntegerProperty(0);
        boolean listIsCollected = false;
        private ArrayList<ArrayList<MenuObject>> filtermodeList = null;
        private FilteredList<Product> filteredData = null;
        private FilteredList<Product> productFilteredList;
        private Button resetFilterBtn = new Button("Reset filter");

        public PopUpFilterCategory(FilterInterfaces.SetFilter setFilter, Button button,FilteredList<Product> productFilteredList ){
            super(setFilter);
            this.button = button;
            this.productFilteredList = productFilteredList;
            popUp();
        }

        public void popUp(){
            EditFilters.IntegerPropertyEquals prodCategory = ePF.new IntegerPropertyEquals(EditFilters.PropertyCompare.CATEGORY);
            prodCategory.setId(categoryID);
            prodCategory.setFilter(super.getSetFilter());

            stage = new Stage();
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);

            Insets insets = new Insets(5,5,5,5);
            HBox.setMargin(menuButton, insets);
            HBox.setMargin(resetFilterBtn, insets);
            HBox hbox = new HBox(menuButton,resetFilterBtn);

            resetFilterBtn.setOnAction((Event)->{
                dS.setFilterButtonGreen(button);
                categoryID.set(0);
                hide();
            });
            tS.setRoundBorder(hbox);
            Scene scene = new Scene(hbox, -1, -1);
            stage.setScene(scene);

            stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if(!stage.isFocused()){
                        stage.hide();
                    }
                }
            });
        }



        public void getMenu(){
            ArrayList<ArrayList<MenuObject>> tmp = new ArrayList<>(filtermodeList);
            tS.getBuildMenu().getMenu(menuButton,new VBox(), new FilterMenuAction(), new FilterMenuItemAction(),filteredData, BuildMenu.MenuBuildMode.FILTER,tmp);
        }

        @Override
        public void hide() {
            if(stage.isShowing()){
                stage.hide();
            }
        }

        @Override
        public void show(double x, double y) {
            if(!listIsCollected){
                collectList();
            }
            if(listIsCollected){
                setPredicate();
                getMenu();
                stage.setX(x);
                stage.setY(y);
                stage.show();
            }
        }

        @Override
        public void close() {

        }

        @Override
        public boolean getIsShowing() {
            return stage.isShowing();
        }

        private void collectList(){
            filtermodeList = dB.getMenu();
            if(filtermodeList!=null) {
                ObservableList<Product>listUnchanged = FXCollections.observableList(productFilteredList);
                filteredData = new FilteredList<>(listUnchanged, s -> true);
                listIsCollected = true;
            }
        }
        private void setPredicate(){
            filteredData.setPredicate(predicate());
        }
        private Predicate<Product> predicate() {
            return Predicate -> {
                for (Product p : productFilteredList) {
                    if (Predicate.getProductCategory() == p.getProductCategory()) {
                        return  true;
                    }
                }
                return false;
            };
        }

        private class FilterMenuAction implements MenuAction {

            @Override
            public void action(Menus menu) {
                //nothing
            }
        }

        private class FilterMenuItemAction implements MenuItemAction {

            @Override
            public void action(VBox vbox, Category cat) {
                categoryID.set(cat.getCategoryId());
                dS.setFilterButtonYellow(button);
            }
        }
    }

}
