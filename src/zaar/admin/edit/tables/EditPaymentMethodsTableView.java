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
import zaar.admin.edit.PredicateFilters.User.EditFiltersUser;
import zaar.admin.edit.filterPopUps.FilterShowHide;
import zaar.admin.edit.filterPopUps.PopUpFilterString;
import zaar.customer.PaymentMethods;
import zaar.customer.User;
import zaar.helperclasses.DataSingleton;

public class EditPaymentMethodsTableView {
    private DataSingleton dS = DataSingleton.getInstance();


    public TableView<PaymentMethods> getPaymentMethodsTableView(FilteredList<PaymentMethods> list){


        //************************ first name column*****************************
        TableColumn<PaymentMethods,Integer> userIdColumn  = new TableColumn<>("User id");
        userIdColumn.setMinWidth(100);
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));


        //************************ lastname column*****************************
        TableColumn<PaymentMethods,String> cardNbrColumn  = new TableColumn<>("CardNumber");
        cardNbrColumn.setMinWidth(100);
        cardNbrColumn.setCellValueFactory(new PropertyValueFactory<>("cardNbr"));


        TableView<PaymentMethods> tableView = new TableView();//Create table
        tableView.setPrefHeight(-1);
        tableView.setPrefWidth(-1);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getColumns().addAll(
                userIdColumn,
                cardNbrColumn);

        SortedList<PaymentMethods> sortedData = new SortedList<>(list);//Activates sorting ability in table
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
    }
}
