package zaar.admin.edit.filterPopUps;


import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.product.EditFiltersProd;
import zaar.admin.edit.PredicateFilters.product.FilterInterfacesProd;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Manufacturer;
import zaar.product.Product;
import java.util.ArrayList;
import java.util.function.Predicate;

import static zaar.admin.edit.filterPopUps.FilterPopUpCommons.TOOLTIP_FILTER_EMPTY;

/**
 * Filter popup for manufaturer
 */
public class PopUpFilterManufacturer implements FilterShowHide {
    private EditFiltersProd eF = EditFiltersProd.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private Button filterBtn;
    private ArrayList<Manufacturer> list;
    private FilteredList<Manufacturer> filteredData = null;
    private FilteredList<Product> productFilteredList;
    private Stage stage;
    private TextField filterTxtField = new TextField();
    private ComboBox<Manufacturer> listView;
    private boolean listIsCollected = false;
    private SimpleIntegerProperty manufaturerid = new SimpleIntegerProperty(0);
    private Manufacturer selectedIndices;

    public PopUpFilterManufacturer(Button filterBtn, FilteredList<Product> productFilteredList){
        this.filterBtn = filterBtn;
        this.productFilteredList = productFilteredList;
        popUp();
    }


    public SimpleIntegerProperty getManufaturerid(){
        return manufaturerid;
    }

    /**
     * Creates popUp window
     */
    private void popUp(){

        stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        filterTxtField.setPromptText("Contains");

        listView = new ComboBox<>();

        Button selectBtn = new Button("Accept selected"); //Button accept selected filter
        Button resetFilterBtn = new Button("Reset filter");//Button resets filter to nothing
        //**************Layout
        Insets insets = new Insets(5,5,5,5);
        VBox.setMargin(filterTxtField, insets);
        VBox.setMargin(listView, insets);
        HBox.setMargin(selectBtn, insets);
        insets = new Insets(5,5,5,20);
        HBox.setMargin(resetFilterBtn, insets);
        HBox hbox = new HBox(selectBtn,resetFilterBtn);
        VBox vbox = new VBox();
        VBox vboxListView = new VBox(filterTxtField,listView);
        vbox.getChildren().addAll(vboxListView, hbox);

        selectBtn.setOnAction(event -> {//Select chosen item
            selectedIndices = listView.getSelectionModel().getSelectedItem();//getSelectedIndices();
            if(selectedIndices!=null) {//No item selected
                manufaturerid.set(selectedIndices.getId());//Set id
                filterBtn.setTooltip(new Tooltip(selectedIndices.getName()));//Change filterbuttonTooltip
                dS.setFilterButtonYellow(filterBtn);//Change Filter button icon
                hide();//Hide Window
            }
        });

        resetFilterBtn.setOnAction((Event)->{//Reset filter
            manufaturerid.set(0);
            selectedIndices = null;
            filterBtn.setTooltip(new Tooltip(TOOLTIP_FILTER_EMPTY));//Change filterbuttonTooltip
            dS.setFilterButtonGreen(filterBtn);//Change Filter button icon
            hide();
        });
        tS.setBorder(vbox);//Sets border to round
        Scene scene = new Scene(vbox, -1, 120);
        stage.setScene(scene);

        stage.focusedProperty().addListener(new ChangeListener<Boolean>() {//Hide window if clicked outside
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
                selectBtn.requestFocus();//Set focus on select button
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
            setPredicate();//Filter list
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

    /**
     * Collects list from database
     */
    private void collectList(){
        list = dB.getManufacturers();//Gets list from DB
        if(list!=null) {
            ObservableList<Manufacturer> listUnchanged = FXCollections.observableList(list);
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
        if(selectedIndices!=null){//Write back selection after updating list (Otherwise empty after update)
            listView.getSelectionModel().select(selectedIndices);
        }
    }
    private Predicate<Manufacturer> predicate(){
        return Predicate->{
            String filter = filterTxtField.getText();
            boolean retVal1;
            boolean retVal2 = false;
            if(filter == null || filter.length() == 0) {//Filters locally
                retVal1  = true;
            }
            else {
                retVal1 = Predicate.getName().toLowerCase().contains(filter.toLowerCase());//Search for manufacturer case insensitive

            }
            for (Product p : productFilteredList) {//Check to master list(FilteredList for products shown) Determine which manufactorers should can be shown in local filter list
                if (Predicate.getId() == p.getManufacturerId()) {
                    retVal2 = true;
                }
            }
            return retVal1 && retVal2;//Both must be true
        };
    }
}
