package zaar.customer;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class Order {
    private String name;
    private int price;
    private double quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Order(String name, int price, double quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
