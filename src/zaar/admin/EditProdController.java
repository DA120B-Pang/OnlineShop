package zaar.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import zaar.Database.Database;
import zaar.product.Product;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProdController implements Initializable {
    @FXML
    private HBox hBox;

    @FXML
    private VBox tableVBox;

    @FXML
    private Button editProdBtn;

    @FXML
    private Button editManBtn;

    @FXML
    private Button editCatBtn;

    @FXML
    private Button editMenuBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private Button deleteBtn;

    private EditProdModel ePM = new EditProdModel();
    private Database dB = Database.getInstance();
    private TableView<Product> tableView;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ePM.getTopHBox(hBox);
        tableView = ePM.getProductTableview(getProductList());

        tableVBox.getChildren().remove(0,tableVBox.getChildren().size());
        tableVBox.getChildren().addAll(tableView);
    }

    private FilteredList<Product> getProductList(){
        ObservableList<Product> list = FXCollections.observableList(dB.getAllProducts());
        FilteredList<Product> filteredData = new FilteredList<>(list, l -> true);
        return  filteredData;
    }

}
