package zaar.product;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zaar.Database.Database;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Menu.*;

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
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private boolean toggleColor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tS.buttonSetImage(searchBtn,"src/img/icons/search.png",15d,15d); //Set image to buttons
        tS.buttonSetImage(menuBtn, "src/img/icons/menu.png", 15d,15d);

        getMenu(menuBtn, prodVbox);//Get menu to menubutton

        menuBtn.setOnMouseClicked((Event)->{
            if(menuBtn.getItems().size()==0){
                getMenu(menuBtn, prodVbox);
            }
        });
        //<*><*><*><*><*><*><*><*><*><*><*><*><*>TEST<*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*>Start
        ArrayList<Product> products = new ArrayList<>();//För test
        for (int i = 0; i < 20 ; i++) {
            products.add(new Product(1, 2,1, "Namn på produkt",32.68, 1,"Tetsed dsfdsa dafdag adsfdasf sdfasdfadf fadsfasdf adsfdasf asdfd asd sdad sdad asf sadsa sadsd sadsa dsad fdda eaf dafa", "", new ImageView()));
        }
        populateProductVbox(prodVbox,products);
        //<*><*><*><*><*><*><*><*><*><*><*><*><*>TEST<*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*><*>End


        getTopHBox(hBox);//Lägger till navigeringsknappar
    }

    @FXML
    void searchButtonOnAction(ActionEvent event) {

    }

    /**
     * Calls method for building meny (this location because is home class for subclasses ProdMenuAction()& ProdMenuItemAction())
     * @param button MenuButton
     * @param vBox  VBox
     */
    public void getMenu(MenuButton button, VBox vBox){
        tS.getBuildMenu().getMenu(button,new ProdMenuAction(), new ProdMenuItemAction(),null,null, BuildMenu.MenuBuildMode.STANDARD,null);//Call build menu
    }

    /**
     * Adds products to product View
     * @param vBox
     * @param products
     */
    public void populateProductVbox(VBox vBox, ArrayList<Product> products){
        ToolsSingleton.getInstance().removeVboxChildren(vBox);
        for (Product p: products){
            vBox.getChildren().add(productIconView(p));
        }
    }

    /**
     * Returns Layout container with product view;
     * @param product product details
     * @return AnchorPane
     */
    private Pane productIconView(Product product){
        Pane pane = new AnchorPane();
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Ersätt med hoppa till produkt info");//OBS ska ersätts
            }
        });

        ImageView imageView = product.getImageView();//Adding product image
        imageView.setFitWidth(100);//Adding image
        imageView.setFitHeight(100);//Adding image
        AnchorPane.setTopAnchor(imageView,10.0);
        AnchorPane.setLeftAnchor(imageView,10.0);
        AnchorPane.setBottomAnchor(imageView,10.0);


        Label productName = new Label(product.getName());//Adding product name
        productName.setFont(Font.font(null, FontWeight.BOLD,14));//Adding product name
        productName.setLayoutX(126);//Adding product name
        productName.setLayoutY(14);//Adding product name

        Label productDesc = new Label(product.getDescription());//Adding product description
        productDesc.setFont(Font.font(null,12));//Adding product descriptio
        productDesc.setPrefWidth(450);//Adding product description
        productDesc.setPrefHeight(35);//Adding product description
        productDesc.wrapTextProperty().setValue(true);//Adding product description
        productDesc.setLayoutX(126);//Adding product descriptio
        productDesc.setLayoutY(33);//Adding product descriptio

        Rectangle inStockIndicator = new Rectangle(15,15);//Adding indicator for in stock
        inStockIndicator.setLayoutX(126);
        inStockIndicator.setLayoutY(78);
        if(product.getQuantity()>0) {
            inStockIndicator.setFill(Color.LIMEGREEN);
        }
        else {
            inStockIndicator.setFill(Color.RED);
        }

        Label inStockLbl = new Label(String.format("%d items in stock.", product.getQuantity()));//Adding quatity
        inStockLbl.setLayoutX(153);
        inStockLbl.setLayoutY(77);

        Button addToCartBtn = new Button("Add");//Adding add to cart button
        tS.buttonSetImage(addToCartBtn,"src/img/icons/cart.png",20d,20d);//Set image to button
        addToCartBtn.setLayoutX(600);
        addToCartBtn.setLayoutY(73);

        addToCartBtn.setOnAction(new EventHandler<ActionEvent>() {//Action for pressing add to Cart
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Add to cart");
            }
        });

        Label price = new Label(String.format("Price: %.2fkr", product.getPrice()));//Add price to Label
        price.setLayoutX(600);
        price.setLayoutY(33);


        pane.getChildren().addAll(imageView,productName,productDesc,inStockIndicator,inStockLbl,addToCartBtn,price);//Add to layout


        pane.setMaxWidth(5000);
        pane.setPrefWidth(-1);
        VBox.setVgrow(pane, Priority.ALWAYS);

        if(toggleColor) {//Background color for product view
            System.out.println(pane.getStyle());
            pane.setStyle("-fx-background-color: #e4e2e2");
            toggleColor = false;
        }
        else{
            toggleColor = true;
        }

        return pane;
    }

    /**
     * Creates top container elements
     * @param hBox
     */
    public void getTopHBox(HBox hBox){
        ToolsSingleton tS = ToolsSingleton.getInstance();
        ScreenSingleton sS = ScreenSingleton.getInstance();

        Button loginBtn = tS.setButtonTopHBox(hBox, "Login", sS.new OpenLoginScreen());//Adds button to top container
        Button cartBtn = tS.setButtonTopHBox(hBox, "Cart", sS.new OpenCartScreen());//Adds button to top container
        tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());//Adds button to top container

        tS.buttonSetImage(cartBtn,"src/img/icons/cart.png",15d,15d);//Set image to button
        tS.buttonSetImage(loginBtn,"src/img/icons/login.png",15d,15d);//Set image to button
    }

    /**
     * Action for menuitem in product view
     */
    public class ProdMenuItemAction implements MenuItemAction {
        Database db = Database.getInstance();

        @Override
        public void action(Category category) {
            ArrayList<Product> prodList = db.getProduct(category.getCategoryId(), Database.GetProd.PROD_CATEGORY);
            populateProductVbox(prodVbox, prodList);
        }
    }

    /**
     * Action for menu in product view
     */
    public class ProdMenuAction implements MenuAction {

        @Override
        public void action(Menus menu) {
            //Nothing;
        }
    }

}