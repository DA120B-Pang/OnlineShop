package zaar.customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.helperclasses.DataSingleton;


public class ViewOrders {
    private Database dB  =Database.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private Stage stage;
    Button viewOrderBtn = new Button("View");
    private int id;

    public ViewOrders(int id){
        this.id = id;
    }

    public void popUp() {



        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("View Orders");
        VBox.setMargin(viewOrderBtn, new Insets(5,5, 5,5));
        ViewOrderTableView viewOrderTableView = new ViewOrderTableView();

        ObservableList<Order> observableList = FXCollections.observableList(dB.getOrders(id));
        FilteredList<Order> filteredData = new FilteredList<>(observableList, l -> true);
        TableView<Order> tableView = viewOrderTableView.getUserTableView(filteredData);

        viewOrderBtn.setOnAction(E->{
            Order order = tableView.getSelectionModel().getSelectedItem();
            if(order != null) {
                new ViewOrderDetails().popUp(order.getOrderId());
            }
        });

        VBox vBox = new VBox(tableView,viewOrderBtn);

        Scene scene = new Scene(vBox);
        stage.setScene(scene);
        stage.show();

    }


}
