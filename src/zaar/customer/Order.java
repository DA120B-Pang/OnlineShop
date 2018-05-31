package zaar.customer;

public class Order {
    private int orderId;
    private int userId;
    private int paymentId;
    private OrderStatus shipmentStatus;
    private String date;
    private String details;

    public Order(){
        this(0,0,0,OrderStatus.PROCESSING,"","");
    }

    public Order(int orderId, int userId, int paymentId, OrderStatus shipmentStatus, String date, String details) {
        this.orderId = orderId;
        this.userId = userId;
        this.paymentId = paymentId;
        this.shipmentStatus = shipmentStatus;
        this.date = date;
        this.details = details;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public OrderStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(OrderStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
