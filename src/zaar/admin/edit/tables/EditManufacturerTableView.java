package zaar.admin.edit.tables;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.category.EditFiltersCat;
import zaar.admin.edit.PredicateFilters.manufacturer.EditFiltersMan;
import zaar.admin.edit.filterPopUps.*;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Manufacturer;
import zaar.product.Menu.Category;

public class EditManufacturerTableView {
    private Database dB = Database.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private EditFiltersMan eFMan = EditFiltersMan.getInstance();
    private EditFiltersMan.EditFilterObject editManFilterObject;
    private PopUpFilterString popUpFilterName;
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private final String TOOLTIP_FILTER_EMPTY = "Click to set filter";


    public TableView<Manufacturer> getManufacturerTableView(FilteredList<Manufacturer> list){

        editManFilterObject = eFMan.new EditFilterObject(list);//Filter for list in listview sets Predicate

        //************************name column*****************************
        TableColumn<Manufacturer,String> nameColumn  = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        Button nameColumnFilterBtn = createColumnBtn();
        nameColumn.setGraphic(nameColumnFilterBtn);

        nameColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterName==null) {
                popUpFilterName = new PopUpFilterString(nameColumnFilterBtn);//Creates popup to show filter
                EditFiltersMan.ManName manName = eFMan.new ManName();//Creates filter for Product
                manName.setFilter(editManFilterObject);//Register master Filter control
                manName.setName(popUpFilterName.getFilterTextField());//Set object to listen for change
            }
            showHideFilter(Event,nameColumnFilterBtn,popUpFilterName,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        TableView<Manufacturer> tableView = new TableView();//Create table
        tableView.setPrefHeight(-1);
        tableView.setPrefWidth(-1);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getColumns().addAll(
                nameColumn);

        SortedList<Manufacturer> sortedData = new SortedList<>(list);//Activates sorting ability in table
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
    }

    public EditFiltersMan.EditFilterObject getMasterFilter(){
        return editManFilterObject;
    }
    private Button createColumnBtn(){
        Button button = new Button();
        dS.setFilterButtonGreen(button);
        button.setTooltip(new Tooltip(TOOLTIP_FILTER_EMPTY));
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
}
