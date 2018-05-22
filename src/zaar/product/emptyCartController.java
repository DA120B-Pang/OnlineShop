package zaar.product;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import zaar.customer.Order;

public class emptyCartController {

    @FXML
    private Button noButton;
    @FXML
    private Button yesButton;
    TableView <Order> table = new TableView<>();

    @FXML
    public void handleNoButton () {
        Stage stage = (Stage) noButton.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void handleYesButton () {

    }
}
