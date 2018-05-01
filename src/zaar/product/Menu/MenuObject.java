package zaar.product.Menu;

import java.util.Comparator;

public class MenuObject implements Comparator<MenuObject> {

    private String name;
    private int parentMenuId;

    public MenuObject() {
        this("",0);
    }
    public MenuObject(String name, int parentMenuId) {
        this.name = name;
        this.parentMenuId = parentMenuId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(int parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    @Override
    public int compare(MenuObject o1, MenuObject o2) {
        return o1.name.compareTo(o2.name);
    }
}
