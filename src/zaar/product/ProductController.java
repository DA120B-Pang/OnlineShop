package zaar.product;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zaar.Database.Database;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Menu.*;

import java.net.URL;
import java.util.*;

import static javafx.scene.paint.Color.BLACK;

public class ProductController implements Initializable {

    private enum ProdFilter{
        PRICE_UP,
        PRICE_DOWN,
        NAME_UP,
        NAME_DOWN,
        NONE;
    }

    @FXML private Button searchBtn;
    @FXML private MenuButton menuBtn;
    @FXML private TextField searchField;
    @FXML private ImageView logo;
    @FXML private VBox prodVbox;
    @FXML private HBox hBox;
    @FXML private ScrollPane scrollPane;
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private Database dB = Database.getInstance();
    private boolean toggleColor;
    private int viewIndex = 0;
    private ArrayList<Product> prodList;
    private ProdFilter sort;
    private HashMap<Integer, Manufacturer> manufacturersHash = new HashMap<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tS.buttonSetImage(searchBtn,"/icons/search.png",15d,15d); //Set image to buttons
        tS.buttonSetImage(menuBtn, "/icons/menu.png", 15d,15d);

        getMenu(menuBtn, prodVbox);//Get menu to menubutton

        searchBtn.setOnAction(E->{
            if(!searchField.getText().isEmpty()){
                prodList = dB.searchProducts(searchField.getText());
                getManufacturerHash();
                sort = ProdFilter.NONE;
                viewIndex = 0;
                populateProductVbox(prodVbox, prodList);
            }
        });

        menuBtn.setOnMouseClicked((Event)->{
            if(menuBtn.getItems().size()==0){
                getMenu(menuBtn, prodVbox);
            }
        });

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
    private void getMenu(MenuButton button, VBox vBox){
        tS.getBuildMenu().getMenu(button,new ProdMenuAction(), new ProdMenuItemAction(),null,null, BuildMenu.MenuBuildMode.STANDARD,null);//Call build menu
    }

    /**
     * Adds products to product View
     * @param vBox
     * @param products
     */
    private void populateProductVbox(VBox vBox, ArrayList<Product> products){
        ToolsSingleton.getInstance().removeVboxChildren(vBox);
        List<Product> subList = new ArrayList<>();


        if(products.size()>1){//Create sort buttons

            Product p = new Product();
            Button priceSortBtn = new Button("Price");
            priceSortBtn.setGraphic(dS.getDownIconImageView());


            //Get graphic for sortpricebtn
            if(sort == ProdFilter.PRICE_UP){
                priceSortBtn.setGraphic(dS.getUpIconImageView());
            }
            else{
                priceSortBtn.setGraphic(dS.getDownIconImageView());
            }

            //Action for price sort
            priceSortBtn.setOnAction(E->{
                if(sort == ProdFilter.PRICE_UP){
                    products.sort(p.new ComparePriceAsc());
                    sort = ProdFilter.PRICE_DOWN;
                }
                else{
                    products.sort(p.new ComparePriceAsc().reversed());
                    sort = ProdFilter.PRICE_UP;
                }
                populateProductVbox(vBox,products);
            });


            Button nameSortBtn = new Button("Name");
            nameSortBtn.setGraphic(dS.getDownIconImageView());

            //Get graphic for sort name btn
            if(sort == ProdFilter.NAME_UP){
                nameSortBtn.setGraphic(dS.getUpIconImageView());
            }
            else{
                nameSortBtn.setGraphic(dS.getDownIconImageView());
            }

            nameSortBtn.setOnAction(E->{

                if(sort == ProdFilter.NAME_UP){
                    products.sort(p.new CompareNameAsc());
                    sort = ProdFilter.NAME_DOWN;
                }
                else{
                    products.sort(p.new CompareNameAsc().reversed());
                    sort = ProdFilter.NAME_UP;
                }
                populateProductVbox(vBox,products);
            });

            //Set choosen buttons border to show that it is selected
            if(sort == ProdFilter.PRICE_DOWN || sort == ProdFilter.PRICE_UP){
                priceSortBtn.setBorder(new Border(new BorderStroke( new Color(0.043,0.105,0.122,1),new BorderStrokeStyle(StrokeType.OUTSIDE,null,null,1,0,null),new CornerRadii(5),new BorderWidths(5))));
            }
            else if(sort == ProdFilter.NAME_UP || sort == ProdFilter.NAME_DOWN){
                nameSortBtn.setBorder(new Border(new BorderStroke( new Color(0.043,0.105,0.122,1),new BorderStrokeStyle(StrokeType.OUTSIDE,null,null,1,0,null),new CornerRadii(5),new BorderWidths(5))));
            }

            Insets insets  =new Insets(10,50,10,50);
            HBox.setMargin(priceSortBtn,insets);
            HBox.setMargin(nameSortBtn,insets);
            HBox hboxBottom = new HBox(nameSortBtn,priceSortBtn);
            hboxBottom.setAlignment(Pos.BASELINE_CENTER);
            vBox.getChildren().add(hboxBottom);
        }

        if (products.size()>=viewIndex*10+10) {//Show only 10 products per page
            subList = products.subList(viewIndex * 10, viewIndex * 10 + 10);
        }
        else{
            subList = products.subList(viewIndex * 10, products.size());
        }
        for (Product p: subList){
            vBox.getChildren().add(productIconView(p));
        }
        //Check if Back and forward button should be used
        if(products.size()>10){
            Button backBtn = new Button();
            ImageView backImageView = new ImageView(dS.getBackwardIcon());
            backImageView.setFitWidth(20);
            backImageView.setFitHeight(20);
            backBtn.setGraphic(backImageView);
            if(viewIndex > 0 ){
                backBtn.setOnAction(E->{
                    viewIndex -= 1;
                    populateProductVbox(vBox,products);
                    scrollPane.setVvalue(0);
                });
            }
            else{
                backBtn.setDisable(true);
            }

            Label indexLabel = new Label(Integer.toString(viewIndex+1));

            Button fwdBtn = new Button();
            ImageView fwdImageView = new ImageView(dS.getForwardIcon());
            fwdImageView.setFitHeight(20);
            fwdImageView.setFitWidth(20);
            fwdBtn.setGraphic(fwdImageView);
            if(products.size() > viewIndex*10+10 ){
                fwdBtn.setOnAction(E->{
                    viewIndex += 1;
                    populateProductVbox(vBox,products);
                    scrollPane.setVvalue(0);
                });
            }
            else{
                fwdBtn.setDisable(true);
            }
            Insets insets  =new Insets(10,50,10,50);
            HBox.setMargin(backBtn,insets);
            HBox.setMargin(indexLabel,insets);
            HBox.setMargin(fwdBtn,insets);
            HBox hboxBottom = new HBox(backBtn,indexLabel,fwdBtn);
            hboxBottom.setAlignment(Pos.BASELINE_CENTER);
            vBox.getChildren().add(hboxBottom);

        }
    }

    /**
     * Returns Layout container with product view;
     * @param product product details
     * @return AnchorPane
     */
    private Pane productIconView(Product product){
        Product prodData = product;
        Pane pane = new AnchorPane();
        pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                productSingleView(prodVbox,product);
            }
        });

        ImageView imageView = product.getImageView();//Adding product image
        imageView.setFitWidth(100);//Adding image
        imageView.setFitHeight(100);//Adding image
        AnchorPane.setTopAnchor(imageView,10.0);
        AnchorPane.setLeftAnchor(imageView,10.0);
        AnchorPane.setBottomAnchor(imageView,10.0);


        Label productName = new Label(manufacturersHash.get(product.getManufacturerId()).getName()+" "+product.getName()+" "+"art: "+product.getProductId());//Adding product name
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
        tS.buttonSetImage(addToCartBtn,"/icons/cart.png",20d,20d);//Set image to button
        addToCartBtn.setLayoutX(600);
        addToCartBtn.setLayoutY(73);

        TextField quantityToCart = new TextField();
        quantityToCart.setText("1");

        quantityToCart.setPrefWidth(40);
        quantityToCart.setLayoutX(550);
        quantityToCart.setLayoutY(73);

        quantityToCart.textProperty().addListener((oB,oV,nV)->{
            try{
                int check = Integer.parseInt(nV);
                if (check>199){
                    quantityToCart.setText("199");
                }
                else if(check<1){
                    Platform.runLater(()->{
                        quantityToCart.setText("1");
                    });

                }
            }
            catch (Exception e){
                quantityToCart.setText(oV);
            }
        });

        addToCartBtn.setOnAction(new EventHandler<ActionEvent>() {//Action for pressing add to Cart
            @Override
            public void handle(ActionEvent event) {
                dS.addToCart(new Product(
                        prodData.getProductId(),
                        prodData.getProductCategory(),
                        prodData.getManufacturerId(),
                        prodData.getName(),
                        prodData.getPrice(),
                        Integer.parseInt(quantityToCart.getText()),
                        prodData.getDescription(),
                        prodData.getTechnicalDetail(),
                        prodData.getImageView()));
            }
        });

        Label price = new Label(String.format("Price: %.2fkr", product.getPrice()));//Add price to Label
        price.setLayoutX(600);
        price.setLayoutY(33);


        pane.getChildren().addAll(imageView,productName,productDesc,inStockIndicator,inStockLbl,addToCartBtn,price,quantityToCart);//Add to layout


        pane.setMaxWidth(5000);
        pane.setPrefWidth(-1);

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
     * Returns Layout container with product view;
     * @param product product details
     * @return AnchorPane
     */
    private void productSingleView(VBox vbox, Product product){
        Product prodData = product;
        Pane pane = new AnchorPane();

        //BackButton top left
        Button backBtn = new Button();
        backBtn.setLayoutX(20);
        backBtn.setLayoutY(20);

        backBtn.setOnAction(E->{
            populateProductVbox(prodVbox, prodList);
        });

        //Product image
        ImageView imageBackBtn = new ImageView(dS.getBackwardIcon());
        imageBackBtn.setFitWidth(20);
        imageBackBtn.setFitHeight(20);
        backBtn.setGraphic(imageBackBtn);

        ImageView imageView = new ImageView(product.getImageView().getImage());//Adding product image
        imageView.setFitWidth(200);//Adding image
        imageView.setFitHeight(200);//Adding image
        AnchorPane.setTopAnchor(imageView,100.0);
        AnchorPane.setLeftAnchor(imageView,30.0);

        //Manufacturer label
        String manufacturerName  = dB.getStringFromTable(prodData.getManufacturerId(),"", Database.GetString.GET_MANUFACTURER);
        Label manufacturerLbl = new Label();
        manufacturerLbl.setFont(Font.font(null, FontWeight.BOLD,16));//Adding product name
        manufacturerLbl.setLayoutX(245);
        manufacturerLbl.setLayoutY(20);
        if (manufacturerName!= null){
            manufacturerLbl.setText(manufacturerName);
        }

        //Product name
        Label productName = new Label(product.getName());//Adding product name
        productName.setFont(Font.font(null, FontWeight.BOLD,14));//Adding product name
        productName.setLayoutX(245);//Adding product name
        productName.setLayoutY(55);//Adding product name

        VBox vBoxDesc = new VBox();
        Label titleDescLbl = new Label("Description");

        Label productDesc = new Label(product.getDescription());//Adding product description
        productDesc.setFont(Font.font(null,12));//Adding product descriptio
        productDesc.setPrefWidth(400);//Adding product description
        productDesc.setPrefHeight(-1);//Adding product description
        productDesc.wrapTextProperty().setValue(true);//Adding product description

        Label titleDetailLbl = new Label("Details");
        Label productDetail = new Label(product.getDescription());//Adding product description
        productDetail.setFont(Font.font(null,12));//Adding product descriptio
        productDetail.setPrefWidth(400);//Adding product description
        productDetail.setPrefHeight(-1);//Adding product description
        productDetail.wrapTextProperty().setValue(true);//Adding product description
        VBox.setMargin(titleDetailLbl, new Insets(30,0,0,0));
        vBoxDesc.getChildren().addAll(titleDescLbl,productDesc,titleDetailLbl,productDetail);

        vBoxDesc.setLayoutX(245);
        vBoxDesc.setLayoutY(160);

        Rectangle inStockIndicator = new Rectangle(15,15);//Adding indicator for in stock
        inStockIndicator.setLayoutX(245);
        inStockIndicator.setLayoutY(88);
        if(product.getQuantity()>0) {
            inStockIndicator.setFill(Color.LIMEGREEN);
        }
        else {
            inStockIndicator.setFill(Color.RED);
        }

        Label inStockLbl = new Label(String.format("%d items in stock.", product.getQuantity()));//Adding quatity
        inStockLbl.setLayoutX(275);
        inStockLbl.setLayoutY(87);

        Button addToCartBtn = new Button("Add");//Adding add to cart button
        tS.buttonSetImage(addToCartBtn,"/icons/cart.png",20d,20d);//Set image to button
        addToCartBtn.setLayoutX(600);
        addToCartBtn.setLayoutY(113);

        TextField quantityToCart = new TextField();
        quantityToCart.setText("1");

        quantityToCart.setPrefWidth(40);
        quantityToCart.setLayoutX(550);
        quantityToCart.setLayoutY(113);

        quantityToCart.textProperty().addListener((oB,oV,nV)->{
            try{
                int check = Integer.parseInt(nV);
                if (check>199){
                    quantityToCart.setText("199");
                }
                else if(check<1){
                    Platform.runLater(()-> {
                        quantityToCart.setText("1");
                    });
                }
            }
            catch (Exception e){
                quantityToCart.setText(oV);
            }
        });

        addToCartBtn.setOnAction(new EventHandler<ActionEvent>() {//Action for pressing add to Cart
            @Override
            public void handle(ActionEvent event) {
                dS.addToCart(new Product(
                        prodData.getProductId(),
                        prodData.getProductCategory(),
                        prodData.getManufacturerId(),
                        prodData.getName(),
                        prodData.getPrice(),
                        Integer.parseInt(quantityToCart.getText()),
                        prodData.getDescription(),
                        prodData.getTechnicalDetail(),
                        prodData.getImageView()));
            }
        });

        Label price = new Label(String.format("Price: %.2fkr", product.getPrice()));//Add price to Label
        price.setLayoutX(550);
        price.setLayoutY(87);


        pane.getChildren().addAll(backBtn,manufacturerLbl,imageView,productName,vBoxDesc,inStockIndicator,inStockLbl,addToCartBtn,price,quantityToCart);//Add to layout


        pane.setMaxWidth(5000);
        pane.setPrefWidth(-1);

        if(toggleColor) {//Background color for product view
            System.out.println(pane.getStyle());
            pane.setStyle("-fx-background-color: #e4e2e2");
            toggleColor = false;
        }
        else{
            toggleColor = true;
        }

        tS.removeVboxChildren(vbox);
        vbox.getChildren().add(pane);
    }
    /**
     * Creates top container elements
     * @param hBox
     */
    public void getTopHBox(HBox hBox){
        ToolsSingleton tS = ToolsSingleton.getInstance();
        ScreenSingleton sS = ScreenSingleton.getInstance();
        if(dS.getLoggedInUser()==null) {
            Button loginBtn = tS.setButtonTopHBox(hBox, "Login", sS.new OpenLoginScreen());//Adds button to top container
            tS.buttonSetImage(loginBtn,"/icons/login.png",15d,15d);//Set image to button
        }else{
            Button logoutBtn = tS.setButtonTopHBox(hBox, "Logout", null);//Adds button to top container
//            Button logoutBtn = new Button("Logout");
//            hBox.getChildren().add(logoutBtn);
            logoutBtn.setOnAction(E->{
                dS.setLoggedInUser(null);
                sS.new OpenProductScreen().screenChange(E);
            });
        }

        //Button cartBtn = tS.setButtonTopHBox(hBox, "Cart", sS.new OpenCartScreen());//Adds button to top container
        if(dS.getLoggedInUser() != null) {
            tS.setButtonTopHBox(hBox, "My account", sS.new OpenMyAccount());//Adds button to top container
        }

        //Button cartBtn = tS.setButtonTopHBox(hBox, "Cart", sS.new OpenCartScreen());//Adds button to top container
        if(dS.getLoggedInUser() != null && dS.getLoggedInUser().getRole() == 1 ) {
            tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());//Adds button to top container
        }

        Button cartBtn  = new Button("Cart");
        cartBtn.setLayoutY(2);
        tS.buttonSetImage(cartBtn,"/icons/cart.png",15d,15d);//Set image to button
        Group group = new Group();
        HBox.setMargin(group, new Insets(0,5,0,5));
        group.getChildren().addAll(cartBtn,dS.getCartLabel());
        cartBtn.setOnAction(E->{
            new Cart().makeCart(prodVbox,false);
        });
        hBox.getChildren().add(group);

    }

    private void getManufacturerHash(){
        if(manufacturersHash.isEmpty()) {
            ArrayList<Manufacturer> manufacturersList = dB.getManufacturers();
            for (Manufacturer m : manufacturersList) {
                manufacturersHash.put(m.getId(), m);
            }
        }
    }

    /**
     * Action for menuitem in product view
     */
    public class ProdMenuItemAction implements MenuItemAction {
        Database db = Database.getInstance();

        @Override
        public void action(Category category) {
            prodList = db.getProduct(category.getCategoryId(), Database.GetProd.PROD_CATEGORY);
            sort = ProdFilter.NONE;
            viewIndex = 0;
            getManufacturerHash();
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