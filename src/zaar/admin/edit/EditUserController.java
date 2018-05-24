package zaar.admin.edit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.SetPredicate;
import zaar.admin.edit.tables.EditPaymentMethodsTableView;
import zaar.admin.edit.tables.EditUserTableView;
import zaar.customer.EditAddUser;
import zaar.customer.PaymentMethods;
import zaar.customer.User;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {
    private enum TypeEdited{
        USER,
        PAYMENT_METHODS;
    }

    @FXML
    private HBox hBox;

    @FXML
    private VBox tableVBox;

    @FXML
    private Label editMainLbl;


    @FXML
    private VBox vBoxSide;

    private EditUserTableView eUTV;
    private EditPaymentMethodsTableView ePTV;
    private TypeEdited typeEdited = TypeEdited.USER;
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private Database dB = Database.getInstance();
    private Button editUserBtn = new Button("User");
    private Button editPaymentBtn = new Button("Payment methods");
    private Button actionUpdateBtn = new Button("Update");
    private Button actionDeleteBtn = new Button("Delete");
    private ObservableList<User> listUser;
    private ObservableList<PaymentMethods> listPaymentMethod;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTopHBox(hBox);
        buildSideMenu();
        setOnActionsSideMenu();
        getEditUser();
        setTypeEdited(TypeEdited.USER);
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

        VBox menuEditVBox = tS.makeButtonMenuModel("Select type to edit", editUserBtn, editPaymentBtn);
        VBox menuActionVBox = tS.makeButtonMenuModel("Select action", actionUpdateBtn,actionDeleteBtn);

        VBox.setMargin(menuEditVBox, new Insets(5,5,5,5));
        VBox.setMargin(menuActionVBox, new Insets(5,5,5,5));
        tS.removeVboxChildren(vBoxSide);
        vBoxSide.getChildren().addAll(menuEditVBox,menuActionVBox);
    }

    private void setOnActionsSideMenu(){
        editUserBtn.setOnAction((Event)->{
            getEditUser();
            setTypeEdited(TypeEdited.USER);
        });
        editPaymentBtn.setOnAction((Event)->{
            getEditPaymentMethods();
            setTypeEdited(TypeEdited.PAYMENT_METHODS);
        });

        actionUpdateBtn.setOnAction((Event)->{
            Node node = (Node) Event.getSource();
            Stage callingStage = (Stage) node.getScene().getWindow();

            switch (typeEdited) {

                case USER:
                    User user = ((TableView<User>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if (user != null) {
                        EditAddUser editAddUser = new EditAddUser("Update");
                        editAddUser.setUserData(user,new SetPredicateUser());
                        editAddUser.popUp();
                    }
                    break;
                case PAYMENT_METHODS:

                    PaymentMethods paymentMethods = ((TableView<PaymentMethods>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if (paymentMethods != null) {
//                        ScreenSingleton.InsertIntStringToDbPopUp categoryUpdate =  sS.new InsertIntStringToDbPopUp();
//                        categoryUpdate.setParametersCategory(category, menuName);
//                        categoryUpdate.popUp("Update", dB.new UpdateCategory(), false,"Id",callingStage.getX()+actionUpdateBtn.getLayoutX(),callingStage.getY()+actionUpdateBtn.getLayoutY()+50);
                    }
                    break;
                }



        });
        actionDeleteBtn.setOnAction((Event)->{
            switch (typeEdited){

                case USER://Delete action for products
                User user = ((TableView<User>)tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                if(user != null) {
                    if(dB.deleteRecord(Database.DeleteRecord.USER, user.getCustomerID())){
                        listUser.remove(user);
                        eUTV.getMasterFilter().setPredicate();
                    }

                }
                break;

                case PAYMENT_METHODS://Delete action for categories
                    PaymentMethods paymentMethods = ((TableView<PaymentMethods>)tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if(paymentMethods != null) {
                        if(dB.deleteRecord(Database.DeleteRecord.PAYMENT_METHOD, paymentMethods.getUserId())){
                            listPaymentMethod.remove(paymentMethods);
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
    private void getEditUser(){
        listUser = FXCollections.observableList(dB.getAllUsers());
        FilteredList<User> filteredData = new FilteredList<>(listUser, l -> true);
        tS.removeVboxChildren(tableVBox);
        eUTV = new EditUserTableView();
        tableVBox.getChildren().add(eUTV.getUserTableView(filteredData));
    }
    /**
     * Creates Tableview with filters
     * @return
     */
    private void getEditPaymentMethods() {
        listPaymentMethod = FXCollections.observableList(dB.getPayment(0,false));
        FilteredList<PaymentMethods> filteredData = new FilteredList<>(listPaymentMethod, l -> true);
        tS.removeVboxChildren(tableVBox);
        ePTV = new EditPaymentMethodsTableView();
        tableVBox.getChildren().add(ePTV.getPaymentMethodsTableView(filteredData));
    }

    /**
     * Sets title to edit page and changes control enum so that correct code i run according to wich table is shown
     * @param typeEdited
     */
    private void setTypeEdited(TypeEdited typeEdited){
        String title = "";
        this.typeEdited = typeEdited;
        switch (typeEdited){
            case USER:
                title="Edit user";
                break;
            case PAYMENT_METHODS:
                title="Edit payment methods";
                break;
        }
        editMainLbl.setText(title);
    }
    private class SetPredicateUser implements SetPredicate{
        @Override
        public void setPredicate() {
            eUTV.getMasterFilter().setPredicate();
        }
    }
    private class SetPredicatePayment implements SetPredicate{

        @Override
        public void setPredicate() {

        }
    }
}
