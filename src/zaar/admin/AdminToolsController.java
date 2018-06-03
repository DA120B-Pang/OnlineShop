package zaar.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import zaar.admin.edit.tables.EditCategoryTableView;
import zaar.customer.EditAddUser;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminToolsController implements Initializable{

    @FXML
    private HBox hBox;

    @FXML
    private VBox vBox;

    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTopHBox(hBox);

        vBox.getChildren().addAll(getProdTools(), getUserTools(), getOrderTools());

    }
    /**
     * Creates button menu for product tools
     * @return
     */
    public VBox getProdTools(){

        Button addProductBtn = new Button("Add products/menu/category");
        addProductBtn.setPrefWidth(180);
        addProductBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                sS.new OpenAddProductScreen().screenChange(e);//Open add productScreen
            }
        });

        Button editProdBtn = new Button("Edit products/menu/category");
        editProdBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                sS.new OpenEditScreen().screenChange(e);//Open add productScreen
            }
        });

        VBox vBox = tS.makeButtonMenuModel("Product tools", editProdBtn, addProductBtn);

        VBox.setMargin(vBox,new Insets(20,20,20,20));
        return vBox;
    }

    /**
     * Creates button menu for User tools
     * @return
     */
    public VBox getUserTools() {


        Button editUserBtn = new Button("Edit user");
        editUserBtn.setPrefWidth(180);
        editUserBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                sS.new OpenEditUserScreen().screenChange(e);//Open edit user screen
            }
        });

        Button addUserBtn = new Button("Add user");
        addUserBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                new EditAddUser("Add user").popUp();
            }
        });
        VBox vBox = tS.makeButtonMenuModel("User tools", editUserBtn, addUserBtn);

        VBox.setMargin(vBox,new Insets(20,20,20,20));
        return vBox;
    }

    public VBox getOrderTools() {


        Button editOrderBtn = new Button("Edit Orders");
        editOrderBtn.setPrefWidth(180);
        editOrderBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                sS.new OpenEditOrderScreen().screenChange(e);//Open edit user screen
            }
        });
        VBox vBox = tS.makeButtonMenuModel("Order tools", editOrderBtn);

        VBox.setMargin(vBox,new Insets(20,20,20,20));
        return vBox;
    }

    public void getTopHBox(HBox hBox){
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
        tS.setButtonTopHBox(hBox, "My account", sS.new OpenMyAccount());//Adds button to top container
    }

}
