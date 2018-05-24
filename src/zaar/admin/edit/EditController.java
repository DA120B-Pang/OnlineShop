package zaar.admin.edit;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.admin.edit.tables.EditCategoryTableView;
import zaar.admin.edit.tables.EditManufacturerTableView;
import zaar.admin.edit.tables.EditMenusTableView;
import zaar.admin.edit.tables.EditProductTableView;
import zaar.admin.edit.update.UpdateProdController;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Manufacturer;
import zaar.product.Menu.Category;
import zaar.product.Menu.Menus;
import zaar.product.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    private enum TypeEdited{
        PRODUCT,
        MANUFACTURER,
        CATEGORY,
        MENU;
    }

    @FXML
    private HBox hBox;

    @FXML
    private VBox tableVBox;

    @FXML
    private Label editMainLbl;


    @FXML
    private VBox vBoxSide;

    private DataSingleton dS = DataSingleton.getInstance();
    private TypeEdited typeEdited = TypeEdited.PRODUCT;
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private EditProductTableView ePTV;
    private EditCategoryTableView eCTV;
    private EditMenusTableView eMTV;
    private EditManufacturerTableView eManTV;
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private Database dB = Database.getInstance();
    private Button editProductBtn = new Button("Product");
    private Button editManufacturerBtn = new Button("Manufacturer");
    private Button editCategoryBtn = new Button("Category");
    private Button editMenuBtn = new Button("Menu");
    private Button actionUpdateBtn = new Button("Update");
    private Button actionDeleteBtn = new Button("Delete");
    private ObservableList<Product> listProduct;
    private ObservableList<Category> listCategory;
    private ObservableList<Menus> listMenus;
    private ObservableList<Manufacturer> listManufacturer;
    private SimpleBooleanProperty menuOrCategoryTableChanged;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTopHBox(hBox);
        buildSideMenu();
        setOnActionsSideMenu();
        getEditProduct();
        setTypeEdited(TypeEdited.PRODUCT);
        menuOrCategoryTableChanged = dS.menuChangedProperty();

        menuOrCategoryTableChanged.addListener((o)->{//Update filtered list
            if(typeEdited == TypeEdited.CATEGORY){
                eCTV.getMasterFilter().setPredicate();
           }
           else if(typeEdited == TypeEdited.MENU){
                eMTV.getMasterFilter().setPredicate();
            }
        });

        dS.manChangedProperty().addListener(l->{
            if(eManTV!=null) {
                eManTV.getMasterFilter().setPredicate();
            }
        });

    }
    /**
     * Sets the buttons in topbox
     * @param hBox
     */
    private void getTopHBox(HBox hBox){
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
        tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());
    }

    /**
     * Creates side menu on Edit page
     */
    private void buildSideMenu(){

        VBox menuEditVBox = tS.makeButtonMenuModel("Select type to edit", editProductBtn, editCategoryBtn, editMenuBtn, editManufacturerBtn);
        VBox menuActionVBox = tS.makeButtonMenuModel("Select action", actionUpdateBtn,actionDeleteBtn);

        VBox.setMargin(menuEditVBox, new Insets(5,5,5,5));
        VBox.setMargin(menuActionVBox, new Insets(5,5,5,5));
        tS.removeVboxChildren(vBoxSide);
        vBoxSide.getChildren().addAll(menuEditVBox,menuActionVBox);
    }

    private void setOnActionsSideMenu(){
        editProductBtn.setOnAction((Event)->{
            getEditProduct();
            setTypeEdited(TypeEdited.PRODUCT);
        });
        editCategoryBtn.setOnAction((Event)->{
            getEditCategory();
            setTypeEdited(TypeEdited.CATEGORY);
        });
        editMenuBtn.setOnAction((Event)->{
            getEditMenu();
            setTypeEdited(TypeEdited.MENU);
        });
        editManufacturerBtn.setOnAction((Event)->{
            getEditManufacturer();
            setTypeEdited(TypeEdited.MANUFACTURER);
        });

        actionUpdateBtn.setOnAction((Event)->{
            Node node = (Node) Event.getSource();
            Stage callingStage = (Stage) node.getScene().getWindow();

            switch (typeEdited) {

                case PRODUCT:

                    Product product = ((TableView<Product>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if (product != null) {
                        ArrayList<Product> productFromDb = dB.getProduct(product.getProductId(), Database.GetProd.PROD_SINGLE);//Get full product from database
                        String man = dB.getStringFromTable(product.getManufacturerId(), Database.GetString.GET_MANUFACTURER);// Get manuf. name from db
                        String cat = dB.getStringFromTable(product.getProductCategory(), Database.GetString.GET_CATEGORY);// Get Cat. name from db
                        if (man != null && cat != null && !productFromDb.isEmpty()) {
                            try {
                                Stage stage = new Stage();
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.setTitle("Update product");
                                stage.setOnCloseRequest((closeEvent) -> {
                                    closeEvent.consume();
                                    ePTV.getMasterFilter().setPredicate();//Update listProduct changes
                                    stage.close();
                                });
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("update/UpdateProd.fxml"));
                                Parent root = loader.load();
                                UpdateProdController updateProdController = loader.getController();
                                updateProdController.setProduct(productFromDb.get(0), man, cat, product);
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case CATEGORY:

                    Category category = ((TableView<Category>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    String menuName = dB.getStringFromTable(category.getParentMenuId(), Database.GetString.GET_MENU);
                    if (category != null && menuName != null) {
                        ScreenSingleton.InsertIntStringToDbPopUp categoryUpdate =  sS.new InsertIntStringToDbPopUp();
                        categoryUpdate.setParametersCategory(category, menuName);
                        categoryUpdate.popUp("Update", dB.new UpdateCategory(), false,"Id",callingStage.getX()+actionUpdateBtn.getLayoutX(),callingStage.getY()+actionUpdateBtn.getLayoutY()+50);
                    }
                    break;

                case MENU:
                    Menus menu = ((TableView<Menus>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    menuName = dB.getStringFromTable(menu.getParentMenuId(), Database.GetString.GET_MENU);
                    if (menu != null && menuName != null) {
                        ScreenSingleton.InsertIntStringToDbPopUp menuUpdate =  sS.new InsertIntStringToDbPopUp();
                        menuUpdate.setParametersMenu(menu, menuName);
                        menuUpdate.popUp("Update", dB.new UpdateMenu(), true,"Id",callingStage.getX()+actionUpdateBtn.getLayoutX(),callingStage.getY()+actionUpdateBtn.getLayoutY()+50);
                    }

                    break;

                case MANUFACTURER:

                    Manufacturer manufacturer = ((TableView<Manufacturer>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if (manufacturer != null) {
                        ScreenSingleton.InsertStringToDbPopUp manufacturerUpdate =  sS.new InsertStringToDbPopUp();
                        manufacturerUpdate.setId(manufacturer);
                        manufacturerUpdate.popUp("Update", dB.new UpdateManufacturer(),callingStage.getX()+actionUpdateBtn.getLayoutX(),callingStage.getY()+actionUpdateBtn.getLayoutY()+50);
                    }
                    break;
                }



        });
        actionDeleteBtn.setOnAction((Event)->{
            switch (typeEdited){

                case PRODUCT://Delete action for products
                Product product = ((TableView<Product>)tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                if(product != null) {
                    if(dB.deleteRecord(Database.DeleteRecord.PRODUCT, product.getProductId())){
                        listProduct.remove(product);
                        ePTV.getMasterFilter().setPredicate();
                    }

                }
                break;

                case CATEGORY://Delete action for categories
                    Category category = ((TableView<Category>)tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if(category != null) {
                        if(dB.deleteRecord(Database.DeleteRecord.CATEGORY, category.getCategoryId())){
                            listCategory.remove(category);
                            eCTV.getMasterFilter().setPredicate();
                        }

                    }
                    break;

                case MENU://Delete action for menus
                    Menus menu = ((TableView<Menus>)tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if(menu != null) {
                        if(dB.deleteRecord(Database.DeleteRecord.MENU, menu.getMenuId())){
                            listMenus.remove(menu);
                            eMTV.getMasterFilter().setPredicate();
                        }
                    }

                    break;

                case MANUFACTURER://Delete action for Manufacturers
                    Manufacturer manufacturer = ((TableView<Manufacturer>)tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if(manufacturer != null) {
                        if(dB.deleteRecord(Database.DeleteRecord.MANUFACTURER, manufacturer.getId())){
                            listManufacturer.remove(manufacturer);
                            eManTV.getMasterFilter().setPredicate();
                        }

                    }
                    break;
            }
        });
    }

    /**
     * Creates Tableview with filters
     * @return
     */
    private void getEditProduct(){
        listProduct = FXCollections.observableList(dB.getAllProducts());
        FilteredList<Product> filteredData = new FilteredList<>(listProduct, l -> true);
        tS.removeVboxChildren(tableVBox);
        ePTV = new EditProductTableView();
        tableVBox.getChildren().add(ePTV.getProductTableView(filteredData));
    }
    /**
     * Creates Tableview with filters
     * @return
     */
    private void getEditCategory(){
        listCategory = FXCollections.observableList(dB.getAllCategories());
        FilteredList<Category> filteredData = new FilteredList<>(listCategory, l -> true);
        tS.removeVboxChildren(tableVBox);
        eCTV = new EditCategoryTableView();
        tableVBox.getChildren().add(eCTV.getCategoryTableView(filteredData));
    }

    /**
     * Creates Tableview with filters
     */
    private void getEditMenu(){
        listMenus = FXCollections.observableList(dB.getAllMenus());
        FilteredList<Menus> filteredData = new FilteredList<>(listMenus, l -> true);
        tS.removeVboxChildren(tableVBox);
        eMTV = new EditMenusTableView();
        tableVBox.getChildren().add(eMTV.getMenusTableView(filteredData));
    }

    private void getEditManufacturer(){
        listManufacturer = FXCollections.observableList(dB.getAllManufacturers());
        FilteredList<Manufacturer> filteredData = new FilteredList<>(listManufacturer, l -> true);
        tS.removeVboxChildren(tableVBox);
        eManTV = new EditManufacturerTableView();
        tableVBox.getChildren().add(eManTV.getManufacturerTableView(filteredData));
    }

    /**
     * Sets title to edit page and changes control enum so that correct code i run according to wich table is shown
     * @param typeEdited
     */
    private void setTypeEdited(TypeEdited typeEdited){
        String title = "";
        this.typeEdited = typeEdited;
        switch (typeEdited){
            case MANUFACTURER:
                title="Edit manufacturer";
                break;
            case MENU:
                title="Edit menu";
                break;
            case CATEGORY:
                title="Edit category";
                break;
            case PRODUCT:
                title="Edit product";
                break;
        }
        editMainLbl.setText(title);
    }

}
