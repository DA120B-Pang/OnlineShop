package zaar.product.Menu;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import zaar.Database.Database;
import zaar.product.Product;
import zaar.product.ProductModel;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BuildMenu {
    Database db = Database.getInstance();
    ProductModel productModel = new ProductModel();

    public void getMenu(MenuButton button, VBox vBox, MenuAction menuAction, MenuItemAction menuItemAction) {
        db = Database.getInstance();//Get Menus and categories from database
        ArrayList<ArrayList<MenuObject>> list = db.getMenu();//Get menus and categories from db
        HashMap<Integer, Menu> menuHash = new HashMap<>();//All Menu that exists.
        HashMap<Integer, ArrayList<MenuObject>> toParentHash = new HashMap<>();//Items sorted by to wich parent they belong
        ArrayList<Menus> rootMenus = new ArrayList<>();//Lowest level of Menu

        if(button.getItems().size()>0){
            button.getItems().remove(0,button.getItems().size());
        }

        for (MenuObject o : list.get(0)) {//Get Menus and sort
            if (o.getParentMenuId() == 0) {//Is menu root menu?
                rootMenus.add((Menus) o);
            } else {//Menu is higher than root
                addMenuObject(toParentHash, o);//Add menu hashtable for wich parent MenuObject belongs
            }
            Menu menu = new Menu(o.getName());//Create Menu
            menu.setOnAction((Event)->{
                menuAction.action((Menus)o);
            });
            menuHash.put(((Menus) o).getMenuId(), menu);//And add to hashMap that contains all Menus
        }

        for (MenuObject o : list.get(1)) {// Get Categories and sort
            addMenuObject(toParentHash, o);//Add menu hashtable for wich parent MenuObject belongs
        }

        for (Map.Entry<Integer, ArrayList<MenuObject>> Entry : toParentHash.entrySet()) {//Loop all Menuobjects that belongs to a parentMenuObject
            Entry.getValue().sort(new MenuObject());//Sort list alphabetically
            for (MenuObject m : Entry.getValue()) {//Loop MenuObjects that belong to parentMenuObject
                if (m instanceof Menus) {//Is MenuObject Menu or menuitem?
                    menuHash.get(Entry.getKey()).getItems().add(menuHash.get(((Menus) m).getMenuId()));//Get Menu from hashmap that contains all menus add to parent Menu
                } else {
                    MenuItem menuItem = new MenuItem(m.getName());
                    menuItem.setOnAction((event -> {
                        menuItemAction.action(vBox,((Category) m));//Action from calling class
                    }));
                    menuHash.get(Entry.getKey()).getItems().add(menuItem);//Add MenuItem to parent Menu
                }
            }
        }

        rootMenus.sort(new MenuObject());//Sort list alphabetically
        for (Menus m : rootMenus) {//Loop all root Menus
            button.getItems().add(menuHash.get(m.getMenuId()));//Add root Menus to button
        }
    }

    /**
     * Adds Menuobject to hashMap
     * @param toParentHash HashMap<Integer,ArrayList<MenuObject>>
     * @param o MenuObject
     */
    private void addMenuObject(HashMap<Integer,ArrayList<MenuObject>> toParentHash, MenuObject o){
        ArrayList<MenuObject> tmpList;
        if(toParentHash.get(o.getParentMenuId())==null){//If list does not exist.. create
            tmpList = new ArrayList<>();
            toParentHash.put(o.getParentMenuId(),tmpList);//Insert list to hashmap
        }
        else{//List did exist.. then get it
            tmpList = toParentHash.get(o.getParentMenuId());
        }
        tmpList.add(o);//Add menu to list
    }


}
