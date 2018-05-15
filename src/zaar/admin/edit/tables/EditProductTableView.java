package zaar.admin.edit.tables;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.admin.edit.filterPopUps.FilterShowHide;
import zaar.admin.edit.PredicateFilters.product.EditFiltersProd;
import zaar.admin.edit.PredicateFilters.product.EditFiltersProd.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import zaar.admin.edit.filterPopUps.*;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Product;

/**
 * Contains methods for creating a tableview for Products. And filters for that tableview.
 * And method for setting the topBox
 */
public class EditProductTableView {
    private Database dB = Database.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private EditFiltersProd eFP = EditFiltersProd.getInstance();
    private EditFilterObject editProdFilterObject;
    private PopUpFilterString popUpFilterName;
    private PopUpFilterManufacturer popUpFilterManufacturer;
    private PopUpFilterCategoryMenu popUpFilterCategory;
    private PopUpFilterNumber<Double> popUpFilterDouble;
    private PopUpFilterNumber<Integer> popUpFilterInteger;
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private final String TOOLTIP_FILTER_EMPTY = "Click to set filter";


    public TableView<Product> getProductTableView(FilteredList<Product> list){

        editProdFilterObject = eFP.new EditFilterObject(list);//Filter for list in listview sets Predicate

        //************************name column*****************************
        TableColumn<Product,String> nameColumn  = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        Button nameColumnFilterBtn = createColumnBtn();
        nameColumn.setGraphic(nameColumnFilterBtn);

        nameColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterName==null) {
                popUpFilterName = new PopUpFilterString(nameColumnFilterBtn);//Creates popup to show filter
                EditFiltersProd.ProdName prodName = eFP.new ProdName();//Creates filter for Product
                prodName.setFilter(editProdFilterObject);//Register master Filter control
                prodName.setName(popUpFilterName.getFilterTextField());//Set object to listen for change
            }
            showHideFilter(Event,nameColumnFilterBtn,popUpFilterName,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************Category column*****************************
        TableColumn<Product,Integer> categoryIdColumn  = new TableColumn<>("Category Id");
        categoryIdColumn.setMinWidth(100);
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        Button categoryIdFilterBtn = createColumnBtn();
        categoryIdColumn.setGraphic(categoryIdFilterBtn);

        categoryIdFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterCategory==null) {
                popUpFilterCategory = new PopUpFilterCategoryMenu(categoryIdFilterBtn,list, FilterObjectType.PRODUCT);//Creates popup to show filter
                EditFiltersProd.IntegerPropertyEquals prodCategory = eFP.new IntegerPropertyEquals(EditFiltersProd.PropertyCompare.CATEGORY);//Creates filter for Category
                prodCategory.setId(popUpFilterCategory.getId());//Set object to listen for change
                prodCategory.setFilter(editProdFilterObject);//Register master Filter control
            }
            showHideFilter(Event,categoryIdFilterBtn,popUpFilterCategory,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************Manufacturer column*****************************
        TableColumn<Product,Integer> manufacturerIdColumn  = new TableColumn<>("Manufacturer Id");
        manufacturerIdColumn.setMinWidth(100);
        manufacturerIdColumn.setCellValueFactory(new PropertyValueFactory<>("manufacturerId"));
        Button manufacturerIdColumnFilterBtn = createColumnBtn();
        manufacturerIdColumn.setGraphic(manufacturerIdColumnFilterBtn);

        manufacturerIdColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterManufacturer==null) {
                popUpFilterManufacturer = new PopUpFilterManufacturer(manufacturerIdColumnFilterBtn,list);//Creates popup to show filter
                EditFiltersProd.IntegerPropertyEquals prodManufacturer = eFP.new IntegerPropertyEquals(EditFiltersProd.PropertyCompare.MANUFACTURER);//Creates filter for Manufactorer
                prodManufacturer.setId(popUpFilterManufacturer.getManufaturerid());//Set object to listen for change
                prodManufacturer.setFilter(editProdFilterObject); //Register master Filter control
            }

            showHideFilter(Event,manufacturerIdColumnFilterBtn,popUpFilterManufacturer,0,-93);//Shows filter popUp att buttons coordinate plus offset
        });


        //************************Price column*****************************
        TableColumn<Product,Double> priceColumn  = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        Button priceColumnFilterBtn = createColumnBtn();
        priceColumn.setGraphic(priceColumnFilterBtn);

        priceColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterDouble==null) {
                popUpFilterDouble = new PopUpFilterNumber<>(editProdFilterObject,priceColumnFilterBtn, new Double(0));//Creates popup to show filter
            }
            showHideFilter(Event,priceColumnFilterBtn,popUpFilterDouble,0,-93);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************Quantity column*****************************
        TableColumn<Product,Integer> quantityColumn  = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        Button quantityColumnFilterBtn = createColumnBtn();
        quantityColumn.setGraphic(quantityColumnFilterBtn);

        quantityColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterInteger==null) {
                popUpFilterInteger = new PopUpFilterNumber<>(editProdFilterObject,quantityColumnFilterBtn, new Integer(0));//Creates popup to show filter
            }
            showHideFilter(Event,quantityColumnFilterBtn,popUpFilterInteger,0,-93);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************Description column*****************************
        TableColumn<Product,String> descriptionColumn  = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));


        TableView<Product> tableView = new TableView();//Create table
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

        SortedList<Product> sortedData = new SortedList<>(list);//Activates sorting ability in table
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
    }

    public EditFilterObject getMasterFilter(){
        return editProdFilterObject;
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
