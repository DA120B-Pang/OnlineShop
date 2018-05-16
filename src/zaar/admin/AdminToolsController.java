package zaar.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminToolsController implements Initializable{

    @FXML
    private HBox hBox;

    @FXML
    private VBox vBox;

    ToolsSingleton tS = ToolsSingleton.getInstance();
    ScreenSingleton sS = ScreenSingleton.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getTopHBox(hBox);
        vBox.getChildren().add(getProdTools());

    }

    public VBox getProdTools(){

        Button addProductBtn = new Button("Add products/menu/category");
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
    public void getTopHBox(HBox hBox){
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
    }

}
