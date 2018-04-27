package zaar.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML private Button loginButton;
    @FXML private Button kundVButton;
    @FXML private Button searchButton;
    @FXML private MenuButton menuBtn;
    @FXML private TextField searchField;
    @FXML private ImageView logo;
    @FXML private VBox prodVbox;
    private ProductModel productModel = new ProductModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            FileInputStream input = new FileInputStream("src/img/product/cart.png");
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            kundVButton.setGraphic(imageView);

            input = new FileInputStream("src/img/product/login.png");
            image = new Image(input);
            imageView = new ImageView(image);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            loginButton.setGraphic(imageView);

            input = new FileInputStream("src/img/product/search.png");
            image = new Image(input);
            imageView = new ImageView(image);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            searchButton.setGraphic(imageView);


        }
        catch (Exception e){
            e.printStackTrace();
        }

        productModel.getMenu(menuBtn);
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 0; i < 20 ; i++) {
            products.add(new Product(1, 2, "Namn på produkt",32.68, 4,"", ""));
        }
        prodVbox.setPrefWidth(1000);
        productModel.populateProductVbox(prodVbox,products);
    }

    @FXML
    void searchButtonOnAction(ActionEvent event) {

    }

    // Byter scen från startsidan till loginskärmen
    @FXML
    void useLoginButton(ActionEvent e) throws IOException {
        Node node = (Node)e.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../sampleLogin.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

}