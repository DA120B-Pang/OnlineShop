package zaar.product;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import zaar.Database;

import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

public class ProductModel {
    private Connection connection;
    private Database db;
    private boolean toggleColor;

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
            try {
                FileInputStream input = new FileInputStream("src/img/product/menu.png");
                Image image = new Image(input);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(20);
                imageView.setFitHeight(20);
                button.setGraphic(imageView);
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return button;
    }

    /**
     * Creates menus and menuitems
     * @param item Menu or MenuItem
     * @param queue Queue of Category
     * @param <T>
     * @return Menu or MenuItem
     */
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

    public void populateProductVbox(VBox vBox, ArrayList<Product> products){
        for (Product p: products){
            vBox.getChildren().add(productIconView(p));
        }
    }
    private AnchorPane productIconView(Product product){
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("Ersätt med hoppa till produkt info");//OBS ska ersätts
            }
        });
        try {
            FileInputStream input = new FileInputStream("src/img/product/cart.png");//Adding image
            Image image = new Image(input);//Adding image
            ImageView imageView = new ImageView(image);//Adding image
            imageView.setFitWidth(100);//Adding image
            imageView.setFitHeight(100);//Adding image

            Label productName = new Label(product.getName());
            productName.setFont(Font.font(null,FontWeight.BOLD,14));
            productName.setLayoutX(126);
            productName.setLayoutY(14);

            anchorPane.getChildren().addAll(imageView,productName);//Add to layout
            System.out.println(anchorPane.getStyle().toString());
            if(toggleColor) {
                anchorPane.setStyle("-fx-background-color: #e4e2e2;");
                toggleColor = false;
            }
            else{
                toggleColor = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return anchorPane;
    }



}

