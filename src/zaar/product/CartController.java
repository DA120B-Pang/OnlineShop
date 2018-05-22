package zaar.product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.customer.Order;
import zaar.helperclasses.DataSingleton;
import zaar.product.Menu.Menus;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CartController implements Initializable {

    private DataSingleton dS = DataSingleton.getInstance();
    @FXML private TableView <Order> table;
    @FXML private TableColumn<Order, String> nameCol;
    @FXML private TableColumn<Order, Double> priceCol;
    @FXML private TableColumn<Order, Integer> quantityAmount;

        // Går tillbaka till produktskärmen
    @FXML
    void fortsattHandla (ActionEvent e) throws IOException {
        Node node = (Node)e.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Product.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    ObservableList<Order> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            Connection con = SqlConnection.getConnection();
            ResultSet rs = con.createStatement().executeQuery("select * from products");

            while (rs.next()) {
                oblist.add(new Order(rs.getString("product_name"), rs.getInt("product_price"), rs.getDouble("product_quantity")));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
            nameCol.setCellValueFactory(new PropertyValueFactory<>("product_name"));
            priceCol.setCellValueFactory(new PropertyValueFactory<>("product_price"));
         //   quantityAmount.setCellValueFactory(new PropertyValueFactory<>("product_quantity"));

            table.setItems(oblist);

    }
    @FXML
    public void tömKundvagnen (ActionEvent e) throws IOException {
        try {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("tomVarukorgen.fxml"));
           Parent root1 = (Parent) loader.load();

           Stage stage = new Stage();
           stage.setScene(new Scene(root1));

           stage.setTitle("Varningsmeddelande");
           stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    public void taBortProdukt (ActionEvent e) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("taBortProdukt.fxml"));
            Parent root1 = (Parent) loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root1));

            stage.setTitle("Varningsmeddelande");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

