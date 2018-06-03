package zaar.customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Product;

import java.util.ArrayList;

import static javafx.geometry.Pos.BASELINE_CENTER;
import static javafx.geometry.Pos.BASELINE_RIGHT;


public class ViewOrderDetails {
    private Database dB = Database.getInstance();
    private GridPane gridPane = new GridPane();
    private  GridPane adressGridPane = new GridPane();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private Insets insets = new Insets(10, 10, 10, 10);


    public void popUpAdmin(Order order, UpdateCaller updateCaller, boolean viewOnly){
        if(populateOrderItems(order.getOrderId())) {
            User user = dB.getUser(order.getUserId());
            if(user!=null) {
                Button updateBtn = new Button("Set order as Shipped");


                setAdress(user);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                Label itemsTitle = new Label("Items");
                itemsTitle.setFont(Font.font(null, FontWeight.BOLD,16));
                Label addressTitle = new Label("Address");
                addressTitle.setFont(Font.font(null, FontWeight.BOLD,16));
                VBox.setMargin(itemsTitle, insets);
                VBox.setMargin(addressTitle, insets);
                VBox.setMargin(updateBtn, insets);
                VBox.setMargin(gridPane, insets);
                VBox.setMargin(adressGridPane, insets);
                VBox.setMargin(adressGridPane, insets);
                Scene scene;
                if(viewOnly){
                    scene = new Scene(new VBox(itemsTitle,gridPane,addressTitle,adressGridPane));
                }
                else {
                    scene = new Scene(new VBox(itemsTitle, gridPane, addressTitle, adressGridPane, updateBtn));
                }
                stage.setTitle("Order details");
                stage.setScene(scene);
                stage.show();

                updateBtn.setOnAction(E->{
                    if(dB.setOrderToShipped(order.getOrderId())){
                        stage.close();
                        updateCaller.update();
                    }
                });
            }
        }
    }

    public void popUp(int orderId) {

        if(populateOrderItems(orderId)) {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            VBox.setMargin(gridPane, insets);
            Label itemsTitle = new Label("Items");
            itemsTitle.setFont(Font.font(null, FontWeight.BOLD,16));
            VBox.setMargin(itemsTitle, insets);

            Scene scene = new Scene(new VBox(itemsTitle,gridPane));
            stage.setTitle("Order details");
            stage.setScene(scene);
            stage.show();
        }
    }

    private boolean populateOrderItems(int orderId) {
        ArrayList<OrderItem> orderItems = dB.getOrderItems(orderId);
        if (orderItems.size() > 0) {
            //Populate with items
            double total = 0;
            for (int i = 0; i < orderItems.size(); i++) {
                itemView(orderItems.get(i), gridPane, i);
                total += orderItems.get(i).getPrice() * orderItems.get(i).getQuantity();
                if (i + 1 >= orderItems.size()) {
                    Label sumLbl = new Label(String.format("%.2f sek", total));
                    sumLbl.setAlignment(BASELINE_CENTER);//Align right more pretty this way
                    sumLbl.setMaxWidth(5000);
                    gridPane.add(sumLbl, 3, i + 2, 1, 1);
                }

            }
        }
        if (orderItems.size()>0){
            return true;
        }
        else{
            return false;
        }
    }

    private void setAdress(User user){
        Label nameTitleLbl = new Label("Name");
        Label nameLbl = new Label(user.getFirstName()+" "+user.getLastName());

        Label addressTitleLbl = new Label("Adress");
        Label addressLbl = new Label(user.getAddress());

        Label cityTitleLbl = new Label("City");
        Label cityLbl = new Label(user.getCity());

        Label countryTitleLbl = new Label("Country");
        Label countryLbl = new Label(user.getCountry());

        Insets insets = new Insets(2, 10, 2, 10);
        GridPane.setMargin(nameTitleLbl, insets);
        GridPane.setMargin(nameLbl, insets);

        GridPane.setMargin(addressTitleLbl, insets);
        GridPane.setMargin(addressLbl, insets);

        GridPane.setMargin(cityTitleLbl, insets);
        GridPane.setMargin(cityLbl, insets);

        GridPane.setMargin(countryTitleLbl, insets);
        GridPane.setMargin(countryLbl, insets);

        adressGridPane.add(nameTitleLbl, 1, 1, 1, 1);
        adressGridPane.add(nameLbl, 2, 1, 1, 1);
        adressGridPane.add(addressTitleLbl, 1, 2, 1, 1);
        adressGridPane.add(addressLbl, 2, 2, 1, 1);
        adressGridPane.add(cityTitleLbl, 1, 3, 1, 1);
        adressGridPane.add(cityLbl, 2, 3, 1, 1);
        adressGridPane.add(countryTitleLbl, 1, 4, 1, 1);
        adressGridPane.add(countryLbl, 2, 4, 1, 1);
        adressGridPane.setGridLinesVisible(true);
        adressGridPane.setMinWidth(gridPane.getWidth());

    }

    private void itemView(OrderItem item, GridPane gridPane, int index){
        ArrayList<Product> list =  dB.getProduct(item.getProdId(), Database.GetProd.PROD_SINGLE);
        if(list.size()>0) {
            Product p = list.get(0);
            p.setQuantity(item.getQuantity());
            p.setPrice(item.getPrice());
            Label manufacturerLbl = new Label(dB.getStringFromTable(p.getManufacturerId(), "", Database.GetString.GET_MANUFACTURER));
            manufacturerLbl.setMaxWidth(70);

            Label nameLbl = new Label(p.getName());
            nameLbl.setMaxWidth(200);

            //Quantity
            Label quantityLbl = new Label(Integer.toString(p.getQuantity()));
            quantityLbl.setPrefWidth(35);

            Insets insets = new Insets(0, 5, 0, 5);
            HBox.setMargin(quantityLbl, insets);
            insets = new Insets(0, 15, 0, 5);

            Label priceLbl = new Label(String.format("%.2f sek", p.getPrice()));
            priceLbl.setAlignment(BASELINE_RIGHT);
            priceLbl.setMaxWidth(5000);
            GridPane.setVgrow(priceLbl, Priority.ALWAYS);
//        priceLbl.setContentDisplay(ContentDisplay.LEFT);

            insets = new Insets(2, 10, 2, 10);
            GridPane.setMargin(manufacturerLbl, insets);
            GridPane.setMargin(nameLbl, insets);
            GridPane.setMargin(quantityLbl, insets);
            GridPane.setMargin(priceLbl, insets);

            gridPane.setGridLinesVisible(true);

            gridPane.add(manufacturerLbl, 0, index + 1, 1, 1);
            gridPane.add(nameLbl, 1, index + 1, 1, 1);
            gridPane.add(quantityLbl, 2, index + 1, 1, 1);
            gridPane.add(priceLbl, 3, index + 1, 1, 1);
        }
    }
}
