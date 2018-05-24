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
import zaar.admin.edit.PredicateFilters.menus.EditFiltersMenus;
import zaar.admin.edit.filterPopUps.*;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Menu.Category;
import zaar.product.Menu.Menus;

public class EditMenusTableView {
    private DataSingleton dS = DataSingleton.getInstance();
    private EditFiltersMenus eFM = EditFiltersMenus.getInstance();
    private EditFiltersMenus.EditFilterObject editProdFilterObject;
    private PopUpFilterString popUpFilterName;
    private PopUpFilterCategoryMenu popUpParentMenuId;
    private final String TOOLTIP_FILTER_EMPTY = "Click to set filter";


    public TableView<Menus> getMenusTableView(FilteredList<Menus> list){

        editProdFilterObject = eFM.new EditFilterObject(list);//Filter for list in listview sets Predicate

        //************************name column*****************************
        TableColumn<Menus,String> nameColumn  = new TableColumn<>("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        Button nameColumnFilterBtn = createColumnBtn();
        nameColumn.setGraphic(nameColumnFilterBtn);

        nameColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterName==null) {
                popUpFilterName = new PopUpFilterString(nameColumnFilterBtn);//Creates popup to show filter
                EditFiltersMenus.MenuName catName = eFM.new MenuName();//Creates filter for Product
                catName.setFilter(editProdFilterObject);//Register master Filter control
                catName.setName(popUpFilterName.getFilterTextField());//Set object to listen for change
            }
            showHideFilter(Event,nameColumnFilterBtn,popUpFilterName,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************Category column*****************************
        TableColumn<Menus,Integer> parentMenuIdColumn  = new TableColumn<>("Parent menu Id");
        parentMenuIdColumn.setMinWidth(150);
        parentMenuIdColumn.setCellValueFactory(new PropertyValueFactory<>("parentMenuId"));
        Button menuIdFilterBtn = createColumnBtn();
        parentMenuIdColumn.setGraphic(menuIdFilterBtn);

        menuIdFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpParentMenuId ==null) {
                popUpParentMenuId = new PopUpFilterCategoryMenu(menuIdFilterBtn,list, FilterObjectType.MENU);//Creates popup to show filter
                EditFiltersMenus.parentMenuId menuParent = eFM.new parentMenuId();//Creates filter for Category
                menuParent.setId(popUpParentMenuId.getId());//Set object to listen for change
                menuParent.setFilter(editProdFilterObject);//Register master Filter control
            }
            showHideFilter(Event,menuIdFilterBtn, popUpParentMenuId,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        TableView<Menus> tableView = new TableView();//Create table
        tableView.setPrefHeight(-1);
        tableView.setPrefWidth(-1);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getColumns().addAll(
                nameColumn,
                parentMenuIdColumn);

        SortedList<Menus> sortedData = new SortedList<>(list);//Activates sorting ability in table
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
    }

    public EditFiltersMenus.EditFilterObject getMasterFilter(){
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
