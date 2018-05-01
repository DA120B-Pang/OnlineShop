package zaar.product;

import java.util.Comparator;

public class MenuSort implements Comparator<MenuSort>{
    public int index;
    public String name;

    public MenuSort(int index, String name){
        this.name=name;
        this.index=index;
    }

    @Override
    public int compare(MenuSort o1, MenuSort o2) {
        return o1.name.compareTo(o2.name);
    }
}
