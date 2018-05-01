package zaar.product;

import java.util.Comparator;

public class Category implements Comparator<Category> {
    private int productCategory;
    private int parentGroupCategory;
    private String typeDescription;
    private String name;

    public Category(int productCategory, int parentGroupCategory, String typeDescription, String name) {
        this.productCategory = productCategory;
        this.parentGroupCategory = parentGroupCategory;
        this.typeDescription = typeDescription;
        this.name = name;
    }

    public int getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(int productCategory) {
        this.productCategory = productCategory;
    }

    public int getParentGroupCategory() {
        return parentGroupCategory;
    }

    public void setParentGroupCategory(int parentGroupCategory) {
        this.parentGroupCategory = parentGroupCategory;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compare(Category o1, Category o2) {
        return o1.name.compareTo(o2.name);
    }
}
