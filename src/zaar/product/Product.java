package zaar.product;

public class Product {
    private int productId;
    private int productCategory;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String technicalDetail;

    public Product(int productId, int productCategory, String name, double price, int quantity, String description, String technicalDetail) {
        this.productId = productId;
        this.productCategory = productCategory;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.technicalDetail = technicalDetail;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(int productCategory) {
        this.productCategory = productCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnicalDetail() {
        return technicalDetail;
    }

    public void setTechnicalDetail(String technicalDetail) {
        this.technicalDetail = technicalDetail;
    }
}
