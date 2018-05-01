package zaar.product.Menu;

public class Menus extends MenuObject {
    private int menuId;

    public Menus(int menuId, int parentMenuId, String name) {
        super(name, parentMenuId);
        this.menuId = menuId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }
}
