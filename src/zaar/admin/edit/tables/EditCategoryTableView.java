package zaar.admin.edit.tables;

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
import zaar.admin.edit.filterPopUps.*;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Menu.Category;

public class EditCategoryTableView {
    private DataSingleton dS = DataSingleton.getInstance();
    private EditFiltersCat eFC = EditFiltersCat.getInstance();
    private EditFiltersCat.EditFilterObject editCatFilterObject;
    private PopUpFilterString popUpFilterName;
    private PopUpFilterCategoryMenu popUpParentMenuId;
    private final String TOOLTIP_FILTER_EMPTY = "Click to set filter";


    public TableView<Category> getCategoryTableView(FilteredList<Category> list){

        editCatFilterObject = eFC.new EditFilterObject(list);//Filter for list in listview sets Predicate

        //************************name column*****************************
        TableColumn<Category,String> nameColumn  = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        Button nameColumnFilterBtn = createColumnBtn();
        nameColumn.setGraphic(nameColumnFilterBtn);

        nameColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterName==null) {
                popUpFilterName = new PopUpFilterString(nameColumnFilterBtn);//Creates popup to show filter
                EditFiltersCat.CatName catName = eFC.new CatName();//Creates filter for Product
                catName.setFilter(editCatFilterObject);//Register master Filter control
                catName.setName(popUpFilterName.getFilterTextField());//Set object to listen for change
            }
            showHideFilter(Event,nameColumnFilterBtn,popUpFilterName,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************Category column*****************************
        TableColumn<Category,Integer> parentMenuIdColumn  = new TableColumn<>("Parent menu Id");
        parentMenuIdColumn.setMinWidth(150);
        parentMenuIdColumn.setCellValueFactory(new PropertyValueFactory<>("parentMenuId"));
        Button categoryIdFilterBtn = createColumnBtn();
        parentMenuIdColumn.setGraphic(categoryIdFilterBtn);

        categoryIdFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpParentMenuId ==null) {
                popUpParentMenuId = new PopUpFilterCategoryMenu(categoryIdFilterBtn,list, FilterObjectType.CATEGORY);//Creates popup to show filter
                EditFiltersCat.parentMenuId prodCategory = eFC.new parentMenuId();//Creates filter for Category
                prodCategory.setId(popUpParentMenuId.getId());//Set object to listen for change
                prodCategory.setFilter(editCatFilterObject);//Register master Filter control
            }
            showHideFilter(Event,categoryIdFilterBtn, popUpParentMenuId,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        TableView<Category> tableView = new TableView();//Create table
        tableView.setPrefHeight(-1);
        tableView.setPrefWidth(-1);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getColumns().addAll(
                nameColumn,
                parentMenuIdColumn);

        SortedList<Category> sortedData = new SortedList<>(list);//Activates sorting ability in table
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
    }

    public EditFiltersCat.EditFilterObject getMasterFilter(){
        return editCatFilterObject;
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
