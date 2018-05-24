package zaar.customer;

public class OrderItem {
    private int prodId;
    private int orderId;
    private double price;
    private int quantity;

    public OrderItem(int prodId, int orderId, double price, int quantity) {
        this.prodId = prodId;
        this.orderId = orderId;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
