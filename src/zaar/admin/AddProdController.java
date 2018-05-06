package zaar.admin;


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
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;

import java.net.URL;
import java.util.ResourceBundle;


public class AddProdController implements Initializable{


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
    private MenuButton menuBtn;

    @FXML
    private Button chooseManBtn;

    @FXML
    private Button addManBtn;

    @FXML
    private Button addCatBtn;

    @FXML
    private Button addMenuBtn;

    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private Category category;
    private Manufacturer manufacturer = new Manufacturer();
    private AddProdModel aPM = new AddProdModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        aPM.getTopHBox(hBox);//Get navigation buttons for top container

        setChangelisteners();//Set changelisteners for textfields

        writeToDbBtn.setOnAction((Event)->{
            executeAddProduct();//Add product to database
        });

        chooseManBtn.setOnAction((Event)->{
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new SelectManufacturerPopUp().popUp(manufacturer, stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50);
        });

        addManBtn.setOnAction((Event)->{//Add manufacturer
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new InsertStringToDbPopUp().popUp("Add manufacturer", dB.new InsertManufacturer(), stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50);
        });

        manufacturer.getName().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                manufacturerTxtFld.setText(manufacturer.getName().getValue());
            }
        });

        menuBtn.setOnMouseClicked((Event)->{
            if(menuBtn.getItems().size()==0){
                tS.getBuildMenu().getMenu(menuBtn,vBox,new AddProdMenuAction(), new AddProdMenuItemAction());
            }
        });

        addCatBtn.setOnAction((Event)->{
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new InsertIntStringToDbPopUp().popUp("Add category", dB.new InsertCategory(),false,"Id",stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50);
        });

        addMenuBtn.setOnAction((Event)->{
            Node node = (Node)Event.getSource();
            Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
            sS.new InsertIntStringToDbPopUp().popUp("Add menu", dB.new InsertMenu(),true,"Empty means root",stage.getX()+chooseManBtn.getLayoutX(),stage.getY()+chooseManBtn.getLayoutY()+50);
        });

        tS.getBuildMenu().getMenu(menuBtn,vBox,new AddProdMenuAction(), new AddProdMenuItemAction());
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
            retVal = dB.addProduct(categoryId, manufacturerId, name, price, quantity, desc, other);
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
                        System.out.println(Double.parseDouble(newValue));
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

        dS.menuChangedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                tS.getBuildMenu().getMenu(menuBtn,vBox,new AddProdMenuAction(), new AddProdMenuItemAction());
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
        public void action(VBox vbox, Category cat) {
            categoryTxtFld.setText(String.valueOf(cat.getName()));
            category = cat;
        }
    }
}
