package zaar.customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.product.Product;

import java.util.ArrayList;

import static javafx.geometry.Pos.BASELINE_CENTER;
import static javafx.geometry.Pos.BASELINE_RIGHT;


public class ViewOrderDetails {
    private Database dB = Database.getInstance();
    private GridPane gridPane = new GridPane();


    public void popUp(int orderId) {


        ArrayList<OrderItem> orderItems = dB.getOrderItems(orderId);
        if(orderItems.size()>0) {
            //Populate with items
            double total = 0;
            for (int i = 0; i < orderItems.size() ; i++) {
                itemView(orderItems.get(i), gridPane, i);
                total += orderItems.get(i).getPrice()*orderItems.get(i).getQuantity();
                if(i+1 >= orderItems.size()){
                    Label sumLbl = new Label(String.format("%.2f sek",total));
                    sumLbl.setAlignment(BASELINE_CENTER);//Align right more pretty this way
                    sumLbl.setMaxWidth(5000);
                    gridPane.add(sumLbl, 3, i + 2, 1, 1);

                }

            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            VBox.setMargin(gridPane, new Insets(10,10,10,10));

            Scene scene = new Scene(new VBox(gridPane));
            stage.setScene(scene);
            stage.show();
        }

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
