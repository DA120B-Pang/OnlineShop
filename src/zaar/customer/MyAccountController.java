package zaar.customer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

import java.net.URL;
import java.util.ResourceBundle;

public class MyAccountController implements Initializable{

    @FXML
    private HBox hBox;

    @FXML
    private VBox vBox;

    ToolsSingleton tS = ToolsSingleton.getInstance();
    ScreenSingleton sS = ScreenSingleton.getInstance();
    DataSingleton dS = DataSingleton.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTopHBox(hBox);

        vBox.getChildren().addAll(getAccountTools(), getOrderTools());

    }
    /**
     * Creates button menu for product tools
     * @return
     */
    public VBox getAccountTools(){

        Button editAccountBtn = new Button("Edit account");
        editAccountBtn.setPrefWidth(180);
        editAccountBtn.setOnAction(E->{
                EditAddUser editaccount = new EditAddUser("Edit account");
                editaccount.setUserData(dS.getLoggedInUser(), true, null);
                editaccount.popUp();
        });

        Button editPaymentBtn = new Button("Edit payment methods");
        editPaymentBtn.setPrefWidth(180);
        editPaymentBtn.setOnAction(E->{
            EditPaymentMethods editPaymentMethods = new EditPaymentMethods("Edit cards");
            editPaymentMethods.popUp();
        });

        VBox vBox = tS.makeButtonMenuModel("Account tools", editAccountBtn, editPaymentBtn);

        VBox.setMargin(vBox,new Insets(20,20,20,20));
        return vBox;
    }

    /**
     * Creates button menu for User tools
     * @return
     */
    public VBox getOrderTools() {


        Button viewOrdersBtn = new Button("View orders");
        viewOrdersBtn.setPrefWidth(180);
        viewOrdersBtn.setOnAction(E->{
            new ViewOrders(dS.getLoggedInUser().getCustomerID()).popUp();
        });

        VBox vBox = tS.makeButtonMenuModel("Order tools", viewOrdersBtn);

        VBox.setMargin(vBox,new Insets(20,20,20,20));
        return vBox;
    }

    public void getTopHBox(HBox hBox){
        tS.setButtonTopHBox(hBox, "View product", sS.new OpenProductScreen());
        //Button cartBtn = tS.setButtonTopHBox(hBox, "Cart", sS.new OpenCartScreen());//Adds button to top container
        if(dS.getLoggedInUser() != null && dS.getLoggedInUser().getRole() == 1 ) {
            tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());//Adds button to top container
        }
    }

}
