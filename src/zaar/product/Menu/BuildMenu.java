package zaar.product.Menu;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import zaar.Database.Database;
import zaar.admin.edit.filterPopUps.FilterObjectType;
import zaar.product.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static zaar.product.Menu.BuildMenu.MenuBuildMode.FILTER;
import static zaar.product.Menu.BuildMenu.MenuBuildMode.STANDARD;

public class BuildMenu {

    private Database db = Database.getInstance();

    /**
     * Build Menu for Menubutton passed as parameter.
     * @param button MenuButton
     * @param menuAction
     * @param menuItemAction
     * @param filteredList
     * @param menuBuildMode
     * @param filtermodeList
     */
    public void getMenu(MenuButton button, MenuAction menuAction, MenuItemAction menuItemAction, FilteredList<?> filteredList, FilterObjectType filterObjectType, MenuBuildMode menuBuildMode, ArrayList<ArrayList<MenuObject>> filtermodeList) {
        ArrayList<ArrayList<MenuObject>> list;
        FilteredList<Product> filteredListProduct = null;
        FilteredList<Category> filteredListCategory = null;
        FilteredList<Menus> filteredListMenus = null;

        if(menuBuildMode==FILTER){//Lista ska inte läsas från databas när meny i filter Editproduct används
            list = filtermodeList;
            if(filterObjectType == FilterObjectType.PRODUCT){
                filteredListProduct = (FilteredList<Product>) filteredList;
            }
            else if(filterObjectType == FilterObjectType.CATEGORY){
                filteredListCategory = (FilteredList<Category>) filteredList;
            }
            else{
                filteredListMenus = (FilteredList<Menus>) filteredList;
            }
        }
        else {
            db = Database.getInstance();//Get Menus and categories from database
            list = db.getMenu();//Get menus and categories from db
        }

        HashMap<Integer, Menu> menuHash = new HashMap<>();//All Menu that exists.
        HashMap<Integer, ArrayList<MenuObject>> toParentHash = new HashMap<>();//Items sorted by to wich parent they belong
        ArrayList<Menus> rootMenus = new ArrayList<>();//Lowest level of Menu



        if(button.getItems().size()>0){
            button.getItems().remove(0,button.getItems().size());
        }

        for (MenuObject o : list.get(1)) {// Get Categories
            if(menuBuildMode == FILTER){
                if(filterObjectType == FilterObjectType.PRODUCT) {
                    for (Product p : filteredListProduct) {
                        if (((Category) o).getCategoryId() == p.getProductCategory()) {
                            addMenuObject(toParentHash, o);
                            break;
                        }
                    }
                }
                else if(filterObjectType == FilterObjectType.CATEGORY){
                    for (Category c : filteredListCategory) {
                        if (((Category) o).getCategoryId() == c.getCategoryId()) {
                            addMenuObject(toParentHash, o);
                            break;
                        }
                    }
                }
            }
            else {
                addMenuObject(toParentHash, o);//Add menu hashtable for wich parent MenuObject belongs
            }
        }
        if(menuBuildMode==FILTER || menuBuildMode == STANDARD) {//Filters Menus in list(1) According to existing categories
            filterMenu(toParentHash, list);
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


        for (Map.Entry<Integer, ArrayList<MenuObject>> Entry : toParentHash.entrySet()) {//Loop all Menuobjects that belongs to a parentMenuObject
            Entry.getValue().sort(new MenuObject());//Sort list alphabetically
            for (MenuObject m : Entry.getValue()) {//Loop MenuObjects that belong to parentMenuObject
                if (m instanceof Menus) {//Is MenuObject Menu or menuitem?
                    menuHash.get(Entry.getKey()).getItems().add(menuHash.get(((Menus) m).getMenuId()));//Get Menu from hashmap that contains all menus add to parent Menu
                } else {
                    MenuItem menuItem = new MenuItem(m.getName());
                    menuItem.setOnAction((event -> {
                        menuItemAction.action(((Category) m));//Action from calling class
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
    private void filterMenu(HashMap<Integer, ArrayList<MenuObject>> categoriesToParentHash, ArrayList<ArrayList<MenuObject>> list){

        HashMap<Integer,MenuObject> filteredMenus = new HashMap<>();
        HashMap<Integer,MenuObject> allMenus = new HashMap<>();

        for(MenuObject m: list.get(0)){//Put all menus in hash
            allMenus.put(((Menus)m).getMenuId(),m);
        }

        for(Map.Entry<Integer, ArrayList<MenuObject>> entry : categoriesToParentHash.entrySet()){//Loop all categories and locate all menus connected to Category
        Category cat = (Category)entry.getValue().get(0);//Gets category
            boolean loop = true;
            int parentKey = cat.getParentMenuId();
            do{
                if(!filteredMenus.containsKey(parentKey) && parentKey!=0){//Check if menu already exists in filtered menus... If the parent exits. It and all its parent menus exits
                    filteredMenus.put(parentKey,allMenus.get(parentKey));//Add it
                    parentKey = allMenus.get(parentKey).getParentMenuId();//Get new parent
                }
                else {
                    loop = false;
                }
            }while(loop);
        }
        ArrayList<MenuObject> tmp = new ArrayList<>(); // replace arraylist containing menus
        for(Map.Entry<Integer,MenuObject> entry: filteredMenus.entrySet()){// replace arraylist containing menus
            tmp.add(entry.getValue());// replace arraylist containing menus
        }
        list.remove(0);// replace arraylist containing menus
        list.add(0,tmp);// replace arraylist containing menus
    }

    public enum MenuBuildMode{
        STANDARD,//Shows only menus containing categories
        FILTER,//Filtering menu is done for Editscreen
        CHOOSE_MENU;//Shows all menus even those not containing categories
    }
}
