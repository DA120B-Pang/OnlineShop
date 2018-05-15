package zaar.product;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zaar.helperclasses.ToolsSingleton;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductController implements Initializable {

    @FXML private Button searchBtn;
    @FXML private MenuButton menuBtn;
    @FXML private TextField searchField;
    @FXML private ImageView logo;
    @FXML private VBox prodVbox;
    @FXML private HBox hBox;
    private ProductModel productModel = new ProductModel();
    private ToolsSingleton tS = ToolsSingleton.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tS.buttonSetImage(searchBtn,"src/img/icons/search.png",15d,15d); //Set image to buttons
        tS.buttonSetImage(menuBtn, "src/img/icons/menu.png", 15d,15d);

        productModel.getMenu(menuBtn, prodVbox);//Get menu to menubutton

        menuBtn.setOnMouseClicked((Event)->{
            if(menuBtn.getItems().size()==0){
                productModel.getMenu(menuBtn, prodVbox);
            }
        });
        //<*><*><*><*><*><*><*><*><*><*><*><*><*>TEST<*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*>Start
        ArrayList<Product> products = new ArrayList<>();//För test
        for (int i = 0; i < 20 ; i++) {
            products.add(new Product(1, 2,1, "Namn på produkt",32.68, 1,"Tetsed dsfdsa dafdag adsfdasf sdfasdfadf fadsfasdf adsfdasf asdfd asd sdad sdad asf sadsa sadsd sadsa dsad fdda eaf dafa", "", new ImageView()));
        }
        productModel.populateProductVbox(prodVbox,products);
        //<*><*><*><*><*><*><*><*><*><*><*><*><*>TEST<*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*>End


        productModel.getTopHBox(hBox);//Lägger till navigeringsknappar
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