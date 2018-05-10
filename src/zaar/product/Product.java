package zaar.product;


import javafx.scene.image.ImageView;

public class Product {
    private int productId;
    private int productCategory;
    private int manufacturerId;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String technicalDetail;
    private ImageView imageView;

    public Product(int productId, int productCategory,int manufacturerId, String name, double price, int quantity, String description, String technicalDetail, ImageView imageView) {
        this.productId = productId;
        this.productCategory = productCategory;
        this.manufacturerId = manufacturerId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.technicalDetail = technicalDetail;
        this.imageView = imageView;
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

    public int getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.manufacturerId = manufacturerId;
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

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
