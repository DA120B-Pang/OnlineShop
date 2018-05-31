package zaar.customer;

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
import zaar.admin.edit.filterPopUps.FilterShowHide;
import zaar.helperclasses.DataSingleton;

public class ViewOrderTableView {
    private DataSingleton dS = DataSingleton.getInstance();
    private final String TOOLTIP_FILTER_EMPTY = "Click to set filter";


    public TableView<Order> getUserTableView(FilteredList<Order> list){


        //************************ first name column*****************************
        TableColumn<Order,String> dateColumn  = new TableColumn<>("Date");
        dateColumn.setMinWidth(100);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        //************************ lastname column*****************************
        TableColumn<Order,OrderStatus> statusColumn  = new TableColumn<>("Status");
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("shipmentStatus"));


        TableView<Order> tableView = new TableView();//Create table
        tableView.setPrefHeight(-1);
        tableView.setPrefWidth(-1);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getColumns().addAll(
                dateColumn,
                statusColumn);

        SortedList<Order> sortedData = new SortedList<>(list);//Activates sorting ability in table
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
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
