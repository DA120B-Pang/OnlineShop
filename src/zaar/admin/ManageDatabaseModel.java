package zaar.admin;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

public class ManageDatabaseModel {
    ToolsSingleton tS = ToolsSingleton.getInstance();
    ScreenSingleton sS = ScreenSingleton.getInstance();

    public AnchorPane getAdminTools(VBox vbox){
        Double layoutX = 100.0;
        Double layoutY = 100.0;
        AnchorPane anchorPane = new AnchorPane();
        Label titleLbl = new Label("Admin tools");
        titleLbl.setStyle("-fx-background-color: #f4f4f4");
        titleLbl.setLayoutX(layoutX);
        titleLbl.setLayoutY(layoutY+25);
        Rectangle rectangle = new Rectangle(layoutX-10,layoutY+35,120,150);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.BLACK);
        Double buttonWidth = 100.0;
        Button editOrdersBtn = new Button("Edit orders");
        editOrdersBtn.setPrefWidth(buttonWidth);
        editOrdersBtn.setLayoutX(layoutX);
        editOrdersBtn.setLayoutY(layoutY+50);
        Button addProductBtn = new Button("Add products");
        addProductBtn.setPrefWidth(buttonWidth);
        addProductBtn.setLayoutX(layoutX);
        addProductBtn.setLayoutY(layoutY+100);
        Button manageUsersBtn = new Button("Manage users");
        manageUsersBtn.setPrefWidth(buttonWidth);
        manageUsersBtn.setLayoutX(layoutX);
        manageUsersBtn.setLayoutY(layoutY+150);

        addProductBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                sS.new OpenAddProductScreen().screenChange(e);//Open add productScreen
            }
        });

        editOrdersBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        manageUsersBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

                anchorPane.getChildren().addAll(rectangle, titleLbl, editOrdersBtn, addProductBtn, manageUsersBtn);
        return anchorPane;
    }

    public void getTopHBox(HBox hBox){
        tS.setButtonTopHBox(hBox, "View products", sS.new OpenProductScreen());
    }



}
