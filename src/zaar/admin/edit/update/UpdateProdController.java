package zaar.admin.edit.update;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.admin.edit.filterPopUps.FilterObjectType;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;
import zaar.product.Product;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UpdateProdController implements Initializable, UpdateCaller {

    @FXML
    private VBox vBox;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField categoryTxtFld;

    @FXML
    private TextField manufacturerTxtFld;

    @FXML
    private TextField nameTxtFld;

    @FXML
    private TextField priceTxtFld;

    @FXML
    private TextField quantityTxtFld;

    @FXML
    private TextArea desqTxtArea;

    @FXML
    private TextArea detailsTxtArea;

    @FXML
    private Button chooseManBtn;

    @FXML
    private MenuButton menuBtn;

    @FXML
    private Button writeToDbBtn;

    @FXML
    private Button addManBtn;

    @FXML
    private Button addCatBtn;

    @FXML
    private Button addMenuBtn;

    @FXML
    private TextField pictureTxtFld;

    @FXML
    private Button choosePicBtn;

    @FXML
    private ImageView oldImageView;

    @FXML
    private GridPane gridPane;



    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private Category category = new Category();
    private Manufacturer manufacturer = new Manufacturer();
    private final FileChooser fileChooser = new FileChooser();
    private File file;
    private Product product;
    private Product listViewProduct;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setChangelisteners();//Set changelisteners for textfields

        writeToDbBtn.setOnAction((Event)->{
            executeUpdateProduct();//Add product to database
        });

        //Shows dialog for choosing manufacturer
        chooseManBtn.setOnAction((Event)->{
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new SelectManufacturerPopUp().popUp(manufacturer, stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50,this);
        });

        //Adds manufacturer
        addManBtn.setOnAction((Event)->{//Add manufacturer
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new InsertStringToDbPopUp().popUp("Add manufacturer", dB.new InsertManufacturer(), stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50,this);
        });

        //Shows available menus
        menuBtn.setOnMouseClicked((Event)->{
            if(menuBtn.getItems().size()==0){
                tS.getBuildMenu().getMenu(menuBtn,new UpdateProdMenuAction(), new UpdateProdMenuItemAction(),null, FilterObjectType.PRODUCT, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);
            }
        });

        //Add category
        addCatBtn.setOnAction((Event)->{
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new InsertIntStringToDbPopUp().popUp("Add category", dB.new InsertCategory(),false,"Id",stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50, this);
        });

        //Add menu
        addMenuBtn.setOnAction((Event)->{
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new InsertIntStringToDbPopUp().popUp("Add menu", dB.new InsertMenu(),true,"Empty means root",stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50,this);
        });

        //Choose picture
        choosePicBtn.setOnAction((Event)->{
            file = tS.openFileChooser(fileChooser, Event,pictureTxtFld);
            if(file!=null && file.length()>64000){
                file = null;
                pictureTxtFld.setText("maximum file size 64kb");
            }
        });
        tS.setFileChooser(fileChooser);
        tS.getBuildMenu().getMenu(menuBtn,new UpdateProdMenuAction(), new UpdateProdMenuItemAction(),null,null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);


    }

    /**
     * Adds product to database if successful
     */
    private void executeUpdateProduct(){
        boolean retVal = false;
        Image image;
        int categoryId;
        int manufacturerId = 0;
        String name = "";
        Double price;
        int quantity;
        String desc;
        String other;

        try{//Check all fields
            categoryId = category.getCategoryId();//Integer.parseInt(categoryTxtFld.getText());
            manufacturerId = manufacturer.getId();
            if(manufacturerId==0){
                throw new Exception("manufacturer is not choosen");
            }
            name = nameTxtFld.getText();
            price = Double.parseDouble(priceTxtFld.getText());
            quantity = Integer.parseInt(quantityTxtFld.getText());
            desc = desqTxtArea.getText();
            other = detailsTxtArea.getText();
            retVal = dB.updateProduct(product.getProductId(),categoryId, manufacturerId, name, price, quantity, desc, other, file);
            if(retVal){
                listViewProduct.setName(name);
                listViewProduct.setManufacturerId(manufacturerId);
                listViewProduct.setProductCategory(categoryId);
                listViewProduct.setPrice(price);
                listViewProduct.setQuantity(quantity);
                listViewProduct.setDescription(desc);
            }
        }
        catch (Exception e){
            //e.printStackTrace();
        }
        if(retVal) {
            image = DataSingleton.getInstance().getOkImgView().getImage();
        }
        else{
            image = DataSingleton.getInstance().getNotOkImgView().getImage();
        }
        tS.getButtonAnimation(anchorPane,writeToDbBtn,image);//Show result icon for button

    }


    private void setChangelisteners(){

        quantityTxtFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue){
                if(!newValue.equalsIgnoreCase("")) {
                    try {
                        Integer.parseInt(newValue);
                    } catch (Exception e) {
                        quantityTxtFld.setText(oldValue);
                    }
                }
            }
        });

        priceTxtFld.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.equalsIgnoreCase("")) {
                    try {
                        Double.parseDouble(newValue);
                    } catch (Exception e) {
                        priceTxtFld.setText(oldValue);
                    }
                }
            }
        });



        desqTxtArea.textProperty().addListener((oB,oV,nV)->{
            if(nV.length()>500){
                desqTxtArea.setText(oV);
            }
        });

        nameTxtFld.textProperty().addListener((oB,oV,nV)->{
            if(nV.length()>100){
                nameTxtFld.setText(oV);
            }
        });

        detailsTxtArea.textProperty().addListener((oB,oV,nV)->{
            if(nV.length()>500){
                detailsTxtArea.setText(oV);
            }
        });
    }

    /**
     *Sets selected product from edti screen listview
     *
     * @param product
     * @param nameManufacturer
     * @param nameCategory
     * @param listViewProd
     */
    public void setProduct(Product product, String nameManufacturer, String nameCategory, Product listViewProd) {
        this.listViewProduct = listViewProd;
        this.product = product;
        ArrayList<Product> productFromDb = dB.getProduct(product.getProductId(), Database.GetProd.PROD_SINGLE);
        manufacturer.setName(nameManufacturer);
        manufacturerTxtFld.setText(nameManufacturer);
        category.setName(nameCategory);
        categoryTxtFld.setText(nameCategory);
        product = productFromDb.get(0);
        nameTxtFld.setText(product.getName());
        manufacturer.setId(product.getManufacturerId());
        category.setCategoryId(product.getProductCategory());
        priceTxtFld.setText(String.valueOf(product.getPrice()));
        quantityTxtFld.setText(String.valueOf(product.getQuantity()));
        desqTxtArea.setText(String.valueOf(product.getDescription()));
        detailsTxtArea.setText(String.valueOf(product.getTechnicalDetail()));
        product.getImageView().setFitWidth(230);
        product.getImageView().setFitHeight(230);
        gridPane.add(product.getImageView(),1,8);
    }

    @Override
    public void update() {
        tS.getBuildMenu().getMenu(menuBtn,new UpdateProdMenuAction(), new UpdateProdMenuItemAction(),null,null, BuildMenu.MenuBuildMode.CHOOSE_MENU,null);
    }

    public class UpdateProdMenuAction implements MenuAction {
        @Override
        public void action(Menus menu) {

        }
    }

    public class UpdateProdMenuItemAction implements MenuItemAction {

        @Override
        public void action(Category cat) {
            categoryTxtFld.setText(String.valueOf(cat.getName()));
            category = cat;
        }
    }
}
