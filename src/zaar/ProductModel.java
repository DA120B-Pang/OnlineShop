package zaar;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

import java.sql.*;
import java.util.*;

public class ProductModel {
    private Connection connection;
    private Database db;
//    public ProductModel() {
//        this.connection = SqlConnection.connector();
//    }

    public MenuButton getMenu(MenuButton button) {
        HashMap<Integer, Queue <Category> > menu = new HashMap<>();
        db = Database.getInstance();
        menu = db.getMenu(menu);
        for (Map.Entry<Integer, Queue<Category>> entry : menu.entrySet()){
            Queue queue = entry.getValue();
            Stack<Object> menuItems = new Stack<>();
            int oldParent = ((Category)queue.peek()).getParentGroupCategory();
            while (!queue.isEmpty()) {
                if(queue.size()==1 && menuItems.isEmpty()){
                    menuItems.push(createMenuitem(new MenuItem(), queue));
                }
                else if(queue.size()==1){

                    Menu item = createMenuitem(new Menu(), queue);
                    while(!menuItems.isEmpty()){
                        if( menuItems.peek() instanceof Menu) {
                            item.getItems().add((Menu) menuItems.pop());
                        }
                        else{
                            item.getItems().add((MenuItem) menuItems.pop());
                        }
                    }
                    menuItems.push(item);
                }
                else{
                    if(oldParent != ((Category)queue.peek()).getParentGroupCategory()){
                        oldParent=((Category)queue.peek()).getParentGroupCategory();
                        Menu item = createMenuitem(new Menu(), queue);
                        while(!menuItems.isEmpty()){//Empty Stack to menu and then push menu to stack
                            if( menuItems.peek() instanceof Menu) {
                                item.getItems().add((Menu) menuItems.pop());
                            }
                            else{
                                item.getItems().add((MenuItem) menuItems.pop());
                            }
                        }
                        menuItems.push(item);
                    }
                    else{
                        MenuItem item = createMenuitem(new MenuItem(), queue);
                        menuItems.push(item);
                    }
                }

            }
            while(!menuItems.isEmpty()){
                if(menuItems.peek() instanceof Menu) {
                    button.getItems().add((Menu)menuItems.pop());
                }
                else{
                    button.getItems().add((MenuItem) menuItems.pop());
                }
            }
        }


            return button;
    }

    private <T extends javafx.scene.control.MenuItem > T createMenuitem(T item, Queue<Category> queue){
        final Category category = (Category)queue.poll();
        item.setText(category.getName());
        if(item instanceof Menu) {
            //Nothing only for menuitems
        }
        else{
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    outpuyt(category.getProductCategory(), category.getName());
                }
            });
        }
        return item;
    }


    public void outpuyt(int num, String name){
        System.out.println(num +" "+name);
    }

}

