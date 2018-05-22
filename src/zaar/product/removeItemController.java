package zaar.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import zaar.customer.Order;

import java.io.IOException;

public class removeItemController {

    @FXML
    private Button yesButton;
    @FXML
    private Button noButton;
    private TableView<Order> table = new TableView<>();


    @FXML
    public void handleYesButton(ActionEvent ae) throws IOException {
        Order selectedItem = table.getSelectionModel().getSelectedItem();
        table.getItems().remove(selectedItem);
    }
    @FXML
    public void handleNoButton(ActionEvent ae) throws IOException {
        Stage stage = (Stage) noButton.getScene().getWindow();
        stage.close();

    }
}
