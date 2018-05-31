package zaar.helperclasses;

import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import zaar.customer.User;
import zaar.product.Product;

import java.io.FileInputStream;
import java.util.ArrayList;
/**
 * Singleton class for data used by various classes in application.
 */
public class DataSingleton {
    private static DataSingleton ourInstance = new DataSingleton();
    private User loggedInUser;
    private ArrayList<Product> cart;
    private ImageView okImgView;
    private ImageView notOkImgView;
    private Image plusGreenImg;
    private Image plusYellowImg;
    private Image backwardIcon;
    private Image forwardIcon;
    private Image upIcon;
    private Image downIcon;
    private Image roundYellow;
    private Image plusBlack;
    private Image minusBlack;
    private Image removeX;
    private Label cartLabel = new Label();
    private SimpleIntegerProperty cartSum = new SimpleIntegerProperty(0);

    public static DataSingleton getInstance() {
        return ourInstance;
    }

    private DataSingleton() {
        this.loggedInUser = null;
        cart = new ArrayList<>();
        createImageViews();
        cartSum.addListener((oB,oV,nV)->{
            if(nV.intValue()>0){
                cartLabel.setText(Integer.toString(nV.intValue()));
                ImageView cartYellow = new ImageView(roundYellow);
                cartYellow.setFitHeight(20);
                cartYellow.setFitWidth(20);
                cartLabel.setLayoutX(50);
                cartLabel.setLayoutY(0);
                cartLabel.setGraphic(cartYellow);
                cartLabel.setContentDisplay(ContentDisplay.CENTER);
            }
            else{
                cartLabel.setGraphic(null);
                cartLabel.setText("");
            }
        });
    }

    private final void createImageViews(){
        try(FileInputStream input1 = new FileInputStream("src/img/button/redcheck.png");
            FileInputStream input2 = new FileInputStream("src/img/button/greencheck.png");
            FileInputStream input3 = new FileInputStream("src/img/button/plus.png");
            FileInputStream input4 = new FileInputStream("src/img/button/plusyellow.png");
            FileInputStream input5 = new FileInputStream("src/img/button/arrowback.png");
            FileInputStream input6 = new FileInputStream("src/img/button/arrowforward.png");
            FileInputStream input7 = new FileInputStream("src/img/button/arrowup.png");
            FileInputStream input8 = new FileInputStream("src/img/button/arrowdown.png");
            FileInputStream input9 = new FileInputStream("src/img/button/roundyellow.png");
            FileInputStream input10 = new FileInputStream("src/img/button/minusblack.png");
            FileInputStream input11 = new FileInputStream("src/img/button/plusblack.png");
            FileInputStream input12 = new FileInputStream("src/img/button/xremove.png");
            ) {
            //input = new FileInputStream("src/img/button/redcheck.png");
            Image image = new Image(input1);
            notOkImgView = new ImageView(image);
            notOkImgView.setFitWidth(15);
            notOkImgView.setFitHeight(15);

            //input = new FileInputStream("src/img/button/greencheck.png");
            image = new Image(input2);
            okImgView = new ImageView(image);
            okImgView.setFitWidth(15);
            okImgView.setFitHeight(15);

            //input = new FileInputStream("src/img/button/plus.png");
            plusGreenImg = new Image(input3);
            //input = new FileInputStream("src/img/button/plusyellow.png");
            plusYellowImg = new Image(input4);

            backwardIcon = new Image(input5);
            forwardIcon = new Image(input6);

            upIcon = new Image(input7);
            downIcon = new Image(input8);

            roundYellow = new Image(input9);

            minusBlack = new Image(input10);
            plusBlack = new Image(input11);
            removeX = new Image(input12);
        }
        catch (Exception e){
            ButtonType OK = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            Alert alert = new Alert(Alert.AlertType.WARNING,"Files are missing.. Shutting down.",OK);
            alert.showAndWait();
            System.exit(0);
        }
    }

    public Image getPlusBlack() {
        return plusBlack;
    }

    public Image getMinusBlack() {
        return minusBlack;
    }

    public Image getRemoveX() {
        return removeX;
    }

    public Label getCartLabel(){
        return cartLabel;
    }

    public ImageView getUpIconImageView() {
        ImageView imageView = new ImageView(upIcon);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    public ImageView getDownIconImageView() {
        ImageView imageView = new ImageView(downIcon);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    public void addToCart(Product cartProduct){
        boolean found = false;
        for(Product p : cart) {
            if(p.getProductId()==cartProduct.getProductId()){
                p.setQuantity(p.getQuantity()+cartProduct.getQuantity());
                found = true;
                break;
            }
        }
        if(!found){
            cart.add(cartProduct);
        }
        cartSum.set(cartSum.getValue()+cartProduct.getQuantity());
    }

    public void uppdateCartLogoQuantity(){
        cartSum.set(0);
        for(Product p : cart){
            cartSum.set(cartSum.getValue()+p.getQuantity());
        }
    }

    public Double getCartTotal(){
        double sum = 0;
        for(Product p : cart){
            sum += p.getQuantity()*p.getPrice();
        }
        return sum;
    }

    public ImageView getOkImgView() {
        return okImgView;
    }

    public ImageView getNotOkImgView() {
        return notOkImgView;
    }

    public Image getBackwardIcon() {
        return backwardIcon;
    }

    public Image getForwardIcon() {
        return forwardIcon;
    }

    public void setFilterButtonGreen(Button button) {
        ImageView imageView = new ImageView(plusGreenImg);
        imageView.setFitWidth(10);
        imageView.setFitHeight(10);
        button.setGraphic(imageView);
    }

    public void setFilterButtonYellow(Button button) {
        ImageView imageView = new ImageView(plusYellowImg);
        imageView.setFitWidth(10);
        imageView.setFitHeight(10);
        button.setGraphic(imageView);
    }
}
