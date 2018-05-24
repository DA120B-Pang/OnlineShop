package zaar.product;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.SetPredicate;
import zaar.customer.EditAddPaymentMethod;
import zaar.customer.OrderItem;
import zaar.customer.PaymentMethods;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ToolsSingleton;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javafx.geometry.Pos.BASELINE_RIGHT;
import static javafx.geometry.Pos.CENTER;

public class Cart {
    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private VBox vBox;
    private ArrayList<Product> listCart;
    private Label sumLbl = new Label();
    private PaymentMethods paymentMethod;
    private final ComboBox<PaymentMethods> choosePaymentComBox =  new ComboBox<>();;
    private Button confirmOrderBtn;
    private Label orderLbl;


    public void makeCart(VBox vBox, boolean thankYouForOrdering){
        this.vBox = vBox;
        listCart = dS.getCart();
        tS.removeVboxChildren(vBox);
        GridPane gridPane = new GridPane();

        Label brand = new Label("Brand");
        Label name = new Label("Name");
        Label quantity = new Label("Quantity");
        Label price = new Label("Price");

        brand.setMaxWidth(5000);
        name.setMaxWidth(5000);
        quantity.setMaxWidth(5000);
        price.setMaxWidth(5000);

        brand.setAlignment(CENTER);
        name.setAlignment(CENTER);
        quantity.setAlignment(CENTER);
        price.setAlignment(CENTER);

        GridPane.setVgrow(brand, Priority.ALWAYS);
        GridPane.setVgrow(name, Priority.ALWAYS);
        GridPane.setVgrow(quantity, Priority.ALWAYS);
        GridPane.setVgrow(price, Priority.ALWAYS);

        gridPane.add(brand,0,0);
        gridPane.add(name,1,0);
        gridPane.add(quantity,2,0);
        gridPane.add(price,3,0);

        //Populate with items
        for (int i = 0; i < listCart.size() ; i++) {
            itemView(listCart.get(i), gridPane, i);
        }

        gridPane.add(new Label(" "),0, gridPane.getRowCount());

        Label totalLbl = new Label("Total");
        totalLbl.setMaxWidth(5000);//Go how big u need
        totalLbl.setAlignment(CENTER);//Be centered
        gridPane.add(totalLbl,3,gridPane.getRowCount());


        sumLbl.setText(String.format("%.2f sek",dS.getCartTotal()));
        sumLbl.setAlignment(BASELINE_RIGHT);//Align right more pretty this way
        sumLbl.setMaxWidth(5000);

        GridPane.setMargin(sumLbl,new Insets(5,5,5,5));
        gridPane.add(sumLbl,3,gridPane.getRowCount());

        orderLbl = new Label("Cart items");
        VBox.setMargin(orderLbl, new Insets(20,0,30,0));
        orderLbl.setFont(Font.font(null, FontWeight.BOLD,16));
        orderLbl.setMaxWidth(5000);
        orderLbl.setAlignment(CENTER);

        gridPane.setAlignment(CENTER);

        HBox selectPaymentMethodHBox = new HBox();
        selectPaymentMethodHBox.setAlignment(CENTER);
        VBox.setMargin(selectPaymentMethodHBox, new Insets(30,0,0,0));

        if(listCart.size()<1){
            Label cartEmpty;
            if(thankYouForOrdering){
                cartEmpty = new Label(String.format("Thank you for ordering. Confirmation has been sent to %s",dS.getLoggedInUser().getEmail()));
            }
            else{
                cartEmpty = new Label("Cart is empty");
            }
            cartEmpty.setMaxWidth(5000);
            cartEmpty.setAlignment(CENTER);

            vBox.getChildren().addAll(orderLbl, cartEmpty);
        }
        else {
            if(dS.getLoggedInUser()!=null){
                //Select payment
                Label selectPaymentMethodLbl = new Label("Select paymentmethod");
                HBox.setMargin(selectPaymentMethodLbl, new Insets(0,15,0,0));


                choosePaymentComBox.setOnMouseClicked(E-> {
                    if(choosePaymentComBox.getItems().size()<1){
                        updateComboBox();
                    }
                });
                updateComboBox();

                Button selectionOkBtn = new Button("Ok");
                selectionOkBtn.setOnAction(E->{

                    PaymentMethods tmp = choosePaymentComBox.getSelectionModel().getSelectedItem();
                    if(tmp!=null) {
                        if(paymentMethod!=null){
                            vBox.getChildren().remove(vBox.getChildren().size()-2,vBox.getChildren().size());//Remove old details
                        }
                        paymentMethod = tmp;
                        showDetails();
                    }
                });

                Button addPaymentMethodBtn = new Button("Add card");
                addPaymentMethodBtn.setOnAction(E->{
                    EditAddPaymentMethod editAddPaymentMethod = new EditAddPaymentMethod(false, dS.getLoggedInUser().getCustomerID());
                    editAddPaymentMethod.setUpdateCaller(new UpdateCombobox());
                    editAddPaymentMethod.popUp();
                });
                Insets insets = new Insets(0,10,0,10);
                HBox.setMargin(choosePaymentComBox,insets);
                HBox.setMargin(selectionOkBtn, insets);
                HBox.setMargin(addPaymentMethodBtn,insets);

                selectPaymentMethodHBox.getChildren().addAll(selectPaymentMethodLbl,choosePaymentComBox, selectionOkBtn, addPaymentMethodBtn);
            }
            else{
                Label loginToProceed = new Label("To register order you need to login.");
                selectPaymentMethodHBox.getChildren().add(loginToProceed);
            }

            vBox.getChildren().addAll(orderLbl, gridPane, selectPaymentMethodHBox);
        }

    }

    private void updateComboBox(){
        if(choosePaymentComBox.getItems().size()>0){
            choosePaymentComBox.getItems().remove(0,choosePaymentComBox.getItems().size());
        }
        choosePaymentComBox.getItems().addAll(getPaymentMethods(dS.getLoggedInUser().getCustomerID()));
        choosePaymentComBox.getSelectionModel().selectFirst();
    }

    private void itemView(Product p, GridPane gridPane, int index){
        ImageView imageView = new ImageView(p.getImageView().getImage());
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        AnchorPane.setLeftAnchor(imageView,5.0);
        AnchorPane.setTopAnchor(imageView,5.0);
        AnchorPane.setBottomAnchor(imageView,5.0);


        Label manufacturerLbl = new Label(dB.getStringFromTable(p.getManufacturerId(),"",Database.GetString.GET_MANUFACTURER));
        manufacturerLbl.setMaxWidth(70);

        Label nameLbl = new Label(p.getName());
        nameLbl.setMaxWidth(200);

        //Quantity
        TextField quantityTextField = new TextField(Integer.toString(p.getQuantity()));
        quantityTextField.setPrefWidth(35);

        quantityTextField.textProperty().addListener((oB,oV,nV)->{
            try {

                int check = Integer.parseInt(nV);
                if (check>199){
                    Platform.runLater(()-> {
                        quantityTextField.setText("199");
                    });
                }
                else if(check<1){
                    Platform.runLater(()-> {
                        quantityTextField.setText("1");
                    });
                }
                else{
                    p.setQuantity(Integer.parseInt(quantityTextField.getText()));
                    dS.uppdateCartLogoQuantity();
                    sumLbl.setText(String.format("%.2f sek",dS.getCartTotal()));
                }
            } catch (Exception e) {
                quantityTextField.setText(oV);
            }
        });

        //Minus quantity
        Button minusBtn = new Button();
        ImageView minusImageView = new ImageView(dS.getMinusBlack());
        minusImageView.setFitHeight(10);
        minusImageView.setFitWidth(10);
        minusBtn.setGraphic(minusImageView);

        minusBtn.setOnAction(E->{
            if(p.getQuantity()>0){
                p.setQuantity(p.getQuantity()-1);
                quantityTextField.setText(Integer.toString(p.getQuantity()));
                dS.uppdateCartLogoQuantity();
                sumLbl.setText(String.format("%.2f sek",dS.getCartTotal()));
            }
        });

        //Plus quantity
        Button plusBtn = new Button();
        ImageView plusImageView = new ImageView(dS.getPlusBlack());
        plusImageView.setFitHeight(10);
        plusImageView.setFitWidth(10);
        plusBtn.setGraphic(plusImageView);

        plusBtn.setOnAction(E->{
            if(p.getQuantity()<199) {
                p.setQuantity(p.getQuantity()+1);
                quantityTextField.setText(Integer.toString(p.getQuantity()));
                dS.uppdateCartLogoQuantity();
                sumLbl.setText(String.format("%.2f sek",dS.getCartTotal()));
            }
        });

        //Remove from cart
        Button removeX = new Button();
        ImageView removeImageView = new ImageView(dS.getRemoveX());
        removeImageView.setFitHeight(10);
        removeImageView.setFitWidth(10);
        removeX.setGraphic(removeImageView);

        removeX.setOnAction(E->{
            listCart.remove(p);
            dS.uppdateCartLogoQuantity();
            makeCart(vBox,false);
        });

        Insets insets = new Insets(0,5,0,5);
        HBox.setMargin(minusBtn,insets);
        HBox.setMargin(plusBtn,insets);
        HBox.setMargin(quantityTextField,insets);
        insets = new Insets(0,15,0,5);
        HBox.setMargin(removeX,insets);
        HBox quantityBox = new HBox(minusBtn,quantityTextField,plusBtn,removeX);

        Label priceLbl = new Label(String.format("%.2f sek",p.getPrice()));
        priceLbl.setAlignment(BASELINE_RIGHT);
        priceLbl.setMaxWidth(5000);
        GridPane.setVgrow(priceLbl, Priority.ALWAYS);
//        priceLbl.setContentDisplay(ContentDisplay.LEFT);

        insets = new Insets(2,10,2,10);
        GridPane.setMargin(manufacturerLbl,insets);
        GridPane.setMargin(nameLbl,insets);
        GridPane.setMargin(quantityBox,insets);
        GridPane.setMargin(priceLbl,insets);

        gridPane.setGridLinesVisible(true);

        gridPane.add(manufacturerLbl,0,index+1,1,1);
        gridPane.add(nameLbl,1,index+1,1,1);
        gridPane.add(quantityBox,2,index+1,1,1);
        gridPane.add(priceLbl,3,index+1,1,1);
    }

    private ObservableList<PaymentMethods> getPaymentMethods(int id){
        return FXCollections.observableArrayList(dB.getPayment(id, true));
    }

    private void showDetails(){
        Label selectedPaymentMethod = new Label(String.format("Selected card: %s", paymentMethod.getCardNbr()));
        Label name = new Label(String.format("Name: %s %s",dS.getLoggedInUser().getFirstName(), dS.getLoggedInUser().getLastName()));
        Label adress = new Label(String.format("Address: %s",dS.getLoggedInUser().getAddress()));
        Label city = new Label(String.format("City: %s", dS.getLoggedInUser().getCity()));
        Label country = new Label(String.format("Country: %s", dS.getLoggedInUser().getCountry()));
        Label email = new Label(String.format("Confirmation will be send to: %s", dS.getLoggedInUser().getEmail()));
        TextArea orderMessageTxtArea = new TextArea();
        confirmOrderBtn = new Button("Confirm order");

        confirmOrderBtn.setOnAction(E->{

            int paymentId = dB.insertPayment(paymentMethod.getPaymentId(), Double.toString(dS.getCartTotal()));
            if (paymentId > 0) {
                int orderId = dB.insertOrder(dS.getLoggedInUser().getCustomerID(), paymentId, orderMessageTxtArea.getText());
                if (orderId > 0) {
                    OrderItem[] itemArray = new OrderItem[listCart.size()];
                    for (int i = 0; i < listCart.size(); i++) {
                        itemArray[i] = new OrderItem(listCart.get(i).getProductId(), orderId, listCart.get(i).getPrice(), listCart.get(i).getQuantity());
                    }
                    if (dB.insertOrderItems(itemArray)) {
                        confirmOrderBtn.setVisible(false);
                        orderLbl.setText("Recipe");
                        saveAsPng(vBox,"src/tmp/tmp");
                        listCart.removeAll(listCart);
                        dS.uppdateCartLogoQuantity();
                        makeCart(vBox, true);
                        tS.sendRecipe(dS.getLoggedInUser().getEmail());
                    } else {

                    }
                } else {

                }
            }
        });

        GridPane confirmationGridPane = new GridPane();

        Insets insets  = new Insets(5,5,5,5);
        GridPane.setMargin(selectedPaymentMethod, insets);
        GridPane.setMargin(name, insets);
        GridPane.setMargin(adress, insets);
        GridPane.setMargin(city, insets);
        GridPane.setMargin(country, insets);
        GridPane.setMargin(email, insets);


        confirmationGridPane.add(selectedPaymentMethod,0,0);
        confirmationGridPane.add(name,0,1);
        confirmationGridPane.add(adress,0,2);
        confirmationGridPane.add(city,0,3);
        confirmationGridPane.add(country,0,4);
        confirmationGridPane.add(email,0,5);

        confirmationGridPane.setGridLinesVisible(true);
        confirmationGridPane.setAlignment(CENTER);
        insets = new Insets(20,5,5,5);
        VBox.setMargin(confirmationGridPane,insets);

        orderMessageTxtArea.setWrapText(true);
        orderMessageTxtArea.setMaxWidth(250);
        orderMessageTxtArea.setMinHeight(100);
        orderMessageTxtArea.setPromptText("OrderItem message to Shop-IT. \rMax 255 characters.");

        orderMessageTxtArea.textProperty().addListener((oB,oV,nV)->{//Max char check
            if(nV.length()>255){
                Platform.runLater(()->{
                    orderMessageTxtArea.setText(oV);
                });
            }
        });

        VBox.setMargin(confirmOrderBtn, insets);
        VBox.setMargin(orderMessageTxtArea, insets);
        VBox confirmOrderBtnVBox = new VBox(orderMessageTxtArea, confirmOrderBtn);
        confirmOrderBtnVBox.setAlignment(CENTER);

        vBox.getChildren().addAll(confirmationGridPane, confirmOrderBtnVBox);
    }

    private class UpdateCombobox implements SetPredicate {

        @Override
        public void setPredicate() {
            updateComboBox();
        }
    }

    private void saveAsPng(final Node NODE, final String FILE_NAME) {//https://www.programcreek.com/java-api-examples/?class=javafx.scene.Node&method=snapshot
        final WritableImage SNAPSHOT = NODE.snapshot(new SnapshotParameters(), null);
        final String        NAME     = FILE_NAME.replace("\\.[a-zA-Z]{3,4}", "");
        final File FILE     = new File(NAME + ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(SNAPSHOT, null), "png", FILE);
        } catch (IOException exception) {
            // handle exception here
        }
    }

}
