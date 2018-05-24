package zaar.customer;

public class PaymentMethods {
    private int paymentId;
    private int userId;
    private String cardNbr;

    public PaymentMethods(int paymentId,int userId, String cardNbr) {
        this.paymentId = paymentId;
        this.userId = userId;
        this.cardNbr = cardNbr;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCardNbr() {
        return cardNbr;
    }

    public void setCardNbr(String cardNbr) {
        this.cardNbr = cardNbr;
    }

    public int getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public String toString() {
        return "Card: "+getCardNbr();
    }
}
