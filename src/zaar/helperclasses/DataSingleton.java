package zaar.helperclasses;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private SimpleBooleanProperty menuChanged;

    public static DataSingleton getInstance() {
        return ourInstance;
    }

    private DataSingleton() {
        this.loggedInUser = new User();
        cart = new ArrayList<>();
        createImageViews();
        menuChanged = new SimpleBooleanProperty(false);
    }

    private final void createImageViews(){
        try {
            FileInputStream input = new FileInputStream("src/img/button/redcheck.png");
            Image image = new Image(input);
            notOkImgView = new ImageView(image);
            notOkImgView.setFitWidth(15);
            notOkImgView.setFitHeight(15);

            input = new FileInputStream("src/img/button/greencheck.png");
            image = new Image(input);
            okImgView = new ImageView(image);
            okImgView.setFitWidth(15);
            okImgView.setFitHeight(15);
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

}
