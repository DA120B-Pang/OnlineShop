package zaar.product.Menu;

import java.util.Comparator;

/**
 * Used for storing product categories
 *  test
 */
public class Category extends MenuObject {
    private int categoryId;
    private int parentMenuId;

    public Category(int categoryId, int parentMenuId, String name) {
        super(name, parentMenuId);
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
