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
import zaar.UpdateCaller;
import zaar.customer.*;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

import java.net.URL;
import java.util.ResourceBundle;

public class EditOrderController implements Initializable, UpdateCaller {

    private enum TypeEdited{
        NEW,
        OLD;
    }

    @FXML
    private HBox hBox;

    @FXML
    private VBox tableVBox;

    @FXML
    private Label editMainLbl;


    @FXML
    private VBox vBoxSide;

    private ViewOrderTableView vOTV;
    private TypeEdited typeEdited = TypeEdited.NEW;
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private ScreenSingleton sS = ScreenSingleton.getInstance();
    private Database dB = Database.getInstance();
    private Button editNewBtn = new Button("New orders");
    private Button viewOldtBtn = new Button("Old orders");
    private Button actionUpdateBtn = new Button("Update");
    private ObservableList<Order> listOrder;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTopHBox(hBox);
        buildSideMenu();
        setOnActionsSideMenu();
        setTypeEdited(TypeEdited.NEW);
        getOrders();
    }
    /**
     * Sets the buttons in topbox
     * @param hBox
     */
    private void getTopHBox(HBox hBox){
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
        tS.setButtonTopHBox(hBox, "My account", sS.new OpenMyAccount());//Adds button to top container
        tS.setButtonTopHBox(hBox, "Admin tools", sS.new OpenManageDatabase());
    }

    /**
     * Creates side menu on Edit page
     */
    private void buildSideMenu(){

        VBox menuEditVBox = tS.makeButtonMenuModel("Select type to edit", editNewBtn, viewOldtBtn);
        VBox menuActionVBox = tS.makeButtonMenuModel("Select action", actionUpdateBtn);

        VBox.setMargin(menuEditVBox, new Insets(5,5,5,5));
        VBox.setMargin(menuActionVBox, new Insets(5,5,5,5));
        tS.removeVboxChildren(vBoxSide);
        vBoxSide.getChildren().addAll(menuEditVBox,menuActionVBox);
    }

    private void setOnActionsSideMenu(){
        editNewBtn.setOnAction((Event)->{
            setTypeEdited(TypeEdited.NEW);
            getOrders();
        });
        viewOldtBtn.setOnAction((Event)->{
            setTypeEdited(TypeEdited.OLD);
            getOrders();

        });

        actionUpdateBtn.setOnAction((Event)->{
            switch (typeEdited) {

                case NEW:
                    Order order = ((TableView<Order>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if (order != null) {
                        ViewOrderDetails viewOrderDetails = new ViewOrderDetails();
                        viewOrderDetails.popUpAdmin(order,this, false);
                    }
                    break;
                case OLD:
                    order = ((TableView<Order>) tableVBox.getChildren().get(0)).getSelectionModel().getSelectedItem();
                    if (order != null) {
                        ViewOrderDetails viewOrderDetails = new ViewOrderDetails();
                        viewOrderDetails.popUpAdmin(order,this, true);
                    }
                    break;
                }
        });
    }

    /**
     * Creates Tableview with filters
     * @return
     */
    private void getOrders() {
        if (typeEdited == TypeEdited.NEW) {
            listOrder = FXCollections.observableList(dB.getOrders(true));
        }
        else{
            listOrder = FXCollections.observableList(dB.getOrders(false));
        }
        FilteredList<Order> filteredData = new FilteredList<>(listOrder, l -> true);
        tS.removeVboxChildren(tableVBox);
        vOTV = new ViewOrderTableView();
        tableVBox.getChildren().add(vOTV.getOrderTableView(filteredData,true));
    }

    @Override
    public void update() {
        getOrders();
    }

    /**
     * Sets title to edit page and changes control enum so that correct code i run according to wich table is shown
     * @param typeEdited
     */
    private void setTypeEdited(TypeEdited typeEdited){
        String title = "";
        this.typeEdited = typeEdited;
        switch (typeEdited){
            case NEW:
                title="New Orders";
                actionUpdateBtn.setText("Update");
                break;
            case OLD:
                title="Shipped orders";
                actionUpdateBtn.setText("View");
                break;
        }
        editMainLbl.setText(title);
    }

}
