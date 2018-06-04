package zaar.admin.add;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.AddManufacturer;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;
import zaar.product.SelectManufacturerPopUp;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class AddProdController implements Initializable {


    @FXML
    private AnchorPane anchorPane;

    @FXML
    private HBox hBox;

    @FXML
    private VBox vBox;

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
    private Button writeToDbBtn;

    @FXML
    private CheckBox retainCheckbox;

    @FXML
    private MenuButton chooseCatBtn;

    @FXML
    private Button chooseManBtn;

    @FXML
    private Button addManBtn;

    @FXML
    private Button addCatBtn;

    @FXML
    private Button addMenuBtn;

    @FXML
    private Button choosePicBtn;

    @FXML
    private TextField pictureTxtFld;

    @FXML
    private TableView<DataSingleton> cartTable;

    @FXML
    private TableColumn<DataSingleton, String> prodCol;

    @FXML
    private TableColumn<DataSingleton, Double> priceCol;

    @FXML
    private TableColumn<DataSingleton, Integer> amountCol;

    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private Category category;
    private Manufacturer manufacturer = new Manufacturer();
    private final FileChooser fileChooser = new FileChooser();
    private File file;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        getTopHBox(hBox);//Get navigation buttons for top container

        setChangelisteners();//Set changelisteners for textfields

        writeToDbBtn.setOnAction((Event)->{
            executeAddProduct();//Add product to database
        });

        chooseManBtn.setOnAction((Event)->{//Button for selecting manufacturer for product dialog
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            new SelectManufacturerPopUp().popUp(manufacturer, stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50, new UpdateSelectedManufacturer());
        });

        addManBtn.setOnAction((Event)->{//Add manufacturer to database dialog
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            new AddManufacturer().popUp("Add manufacturer", dB.new InsertManufacturer(), stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50,null);
        });

        chooseCatBtn.setOnMouseClicked((Event)->{//Builds category menu if error has occured in previous attempt
            if(chooseCatBtn.getItems().size()==0){
                tS.getBuildMenu().getMenu(chooseCatBtn,new AddProdMenuAction(), new AddProdMenuItemAction(),null,null, BuildMenu.MenuBuildMode.STANDARD,null);
            }
        });

        addCatBtn.setOnAction((Event)->{//Opens dialog for choosing category for product
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            new AddMenuCategory().popUp("Add category", dB.new InsertCategory(),false,"Id",stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50,new UpdateCategories());

        });

        addMenuBtn.setOnAction((Event)->{//Opens dialog for choosing manufacturer for product
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            new AddMenuCategory().popUp("Add menu", dB.new InsertMenu(),true,"Empty means root",stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50, null);
        });

        choosePicBtn.setOnAction((Event)->{//Opens dialog for choosing picture for product
            file = tS.openFileChooser(fileChooser, Event, pictureTxtFld);
            if(file!=null && file.length()>64000){
                file = null;
                pictureTxtFld.setText("maximum file size 64kb");
            }
        });
        tS.setFileChooser(fileChooser);
        tS.getBuildMenu().getMenu(chooseCatBtn,new AddProdMenuAction(), new AddProdMenuItemAction(),null, null, BuildMenu.MenuBuildMode.STANDARD,null);
    }

    /**
     * Adds product to database if successful
     */
    private void executeAddProduct(){
        boolean retVal = false;
        Image image;
        int categoryId;
        int manufacturerId;
        String name;
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
            retVal = dB.insertProduct(categoryId, manufacturerId, name, price, quantity, desc, other, file);
        }
        catch (Exception e){
            //e.printStackTrace();
        }
        if(retVal) {
            image = DataSingleton.getInstance().getOkImgView().getImage();
            emptyFields();
        }
        else{
            image = DataSingleton.getInstance().getNotOkImgView().getImage();
        }
        tS.getButtonAnimation(anchorPane,writeToDbBtn,image);//Show result icon for button

    }

    private void emptyFields(){
        if(!retainCheckbox.isSelected()) {
            categoryTxtFld.setText("");
            manufacturerTxtFld.setText("");
            nameTxtFld.setText("");
            priceTxtFld.setText("");
            quantityTxtFld.setText("");
            desqTxtArea.setText("");
            detailsTxtArea.setText("");
        }

    }

    private void setChangelisteners(){
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


    public class AddProdMenuAction implements MenuAction{
        @Override
        public void action(Menus menu) {

        }
    }

    public class AddProdMenuItemAction implements MenuItemAction{

        @Override
        public void action(Category cat) {
            categoryTxtFld.setText(String.valueOf(cat.getName()));
            category = cat;
        }
    }

    public void getTopHBox(HBox hBox){
        ToolsSingleton tS = ToolsSingleton.getInstance();
        ScreenSingleton sS = ScreenSingleton.getInstance();
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
        tS.setButtonTopHBox(hBox, "My account", sS.new OpenMyAccount());//Adds button to top container
        tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());
    }
    private class UpdateCategories implements UpdateCaller{

        @Override
        public void update() {
            tS.getBuildMenu().getMenu(chooseCatBtn,new AddProdMenuAction(), new AddProdMenuItemAction(),null,null, BuildMenu.MenuBuildMode.STANDARD,null);
        }
    }

    private class UpdateSelectedManufacturer implements UpdateCaller{

        @Override
        public void update() {
            manufacturerTxtFld.setText(manufacturer.getName());
        }
    }

}
