package zaar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.input.KeyCode.T;

public class ProductController implements Initializable {

    @FXML private Button loginButton;
    @FXML private Button kundVButton;
    @FXML private Button searchButton;
    @FXML private MenuButton menuBtn;
    @FXML private TextField searchField;
    @FXML private ImageView logo;
    private ProductModel productModel = new ProductModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane pane = new AnchorPane();
        Image image = new Image("File:img/ShopIT.png");
        pane.getChildren().add(new ImageView(image));


        productModel.getMenu(menuBtn);
    }

    // Byter scen från startsidan till loginskärmen
    public void useLoginButton(ActionEvent e) throws IOException {
        Node node = (Node)e.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sampleLogin.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }



}