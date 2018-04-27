package zaar;

import zaar.customer.Customer;
import zaar.product.Product;

import java.util.ArrayList;

public class Data {
    private static Data ourInstance = new Data();
    private Customer loggedInUser;
    private ArrayList<Product> cart;

    public static Data getInstance() {
        return ourInstance;
    }

    private Data() {
        this.loggedInUser = new Customer();
        cart = new ArrayList<>();
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

    public Customer getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Customer loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public ArrayList<Product> getCart() {
        return cart;
    }

    public void setCart(ArrayList<Product> cart) {
        this.cart = cart;
    }
}
