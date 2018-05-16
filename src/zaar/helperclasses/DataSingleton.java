package zaar.helperclasses;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private SimpleBooleanProperty menuChanged = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty manChanged = new SimpleBooleanProperty(false);

    public static DataSingleton getInstance() {
        return ourInstance;
    }

    private DataSingleton() {
        this.loggedInUser = new User();
        cart = new ArrayList<>();
        createImageViews();
    }

    private final void createImageViews(){
        try(FileInputStream input1 = new FileInputStream("src/img/button/redcheck.png");
            FileInputStream input2 = new FileInputStream("src/img/button/greencheck.png");
            FileInputStream input3 = new FileInputStream("src/img/button/plus.png");
            FileInputStream input4 = new FileInputStream("src/img/button/plusyellow.png");
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
        }
        catch (Exception e){
            okImgView = new ImageView();
            notOkImgView = new ImageView();
        }
    }

    /**
     * If user is logged in returns true
     * @return boolean
     */
    public boolean isUserLoggedIn(){
        if (loggedInUser.getCustomerID()>0){
            return true;
        }
        return false;
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

    public void setCart(ArrayList<Product> cart) {
        this.cart = cart;
    }

    public ImageView getOkImgView() {
        return okImgView;
    }

    public ImageView getNotOkImgView() {
        return notOkImgView;
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

    public SimpleBooleanProperty menuChangedProperty() {
        return menuChanged;
    }

    public void toggleMenuChanged(){
        if(menuChanged.getValue()){
            menuChanged.setValue(false);
        }
        else{
            menuChanged.setValue(true);
        }
    }
    public SimpleBooleanProperty manChangedProperty() {
        return manChanged;
    }

    public void toggleManChanged() {
        if (manChanged.getValue()) {
            manChanged.setValue(false);
        } else {
            manChanged.setValue(true);
        }
    }

}
