package zaar.product;

import javafx.beans.value.WritableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
        AnchorPane anchorPane;
        for (Product p: products){
            vBox.getChildren().add(productIconView(p));

        }
    }

    /**
     * Returns Layout container with product view;
     * @param product product details
     * @return AnchorPane
     */
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
            AnchorPane.setTopAnchor(imageView,10.0);
            AnchorPane.setLeftAnchor(imageView,10.0);
            AnchorPane.setBottomAnchor(imageView,10.0);


            Label productName = new Label(product.getName());//Adding product name
            productName.setFont(Font.font(null,FontWeight.BOLD,14));//Adding product name
            productName.setLayoutX(126);//Adding product name
            productName.setLayoutY(14);//Adding product name

            Label productDesc = new Label(product.getDescription());//Adding product description
            productDesc.setFont(Font.font(null,12));//Adding product descriptio
            productDesc.setPrefWidth(300);//Adding product description
            productDesc.setPrefHeight(35);//Adding product description
            productDesc.wrapTextProperty().setValue(true);//Adding product description
            productDesc.setLayoutX(126);//Adding product descriptio
            productDesc.setLayoutY(33);//Adding product descriptio

            Rectangle inStockIndicator = new Rectangle(15,15);
            inStockIndicator.setLayoutX(126);
            inStockIndicator.setLayoutY(78);
            if(product.getQuantity()>0) {
                inStockIndicator.setFill(Color.LIMEGREEN);
            }
            else {
                inStockIndicator.setFill(Color.RED);
            }

            Label inStockLbl = new Label(String.format("%d items in stock.", product.getQuantity()));
            inStockLbl.setLayoutX(153);
            inStockLbl.setLayoutY(77);


            input = new FileInputStream("src/img/product/cart.png");//Adding image
            image = new Image(input);//Adding image
            ImageView imageView2 = new ImageView(image);//Adding image
            imageView2.setFitWidth(20);//Adding image
            imageView2.setFitHeight(20);//Adding image
            Button addToCartBtn = new Button("Add",imageView2);
            addToCartBtn.setLayoutX(456);
            addToCartBtn.setLayoutY(73);
            addToCartBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Add to cart");
                }
            });

            Label price = new Label(String.format("Price: %.2fkr", product.getPrice()));
            price.setLayoutX(456);
            price.setLayoutY(33);


            anchorPane.getChildren().addAll(imageView,productName,productDesc,inStockIndicator,inStockLbl,addToCartBtn,price);//Add to layout

            if(toggleColor) {
                System.out.println(anchorPane.getStyle());
                anchorPane.setStyle("-fx-background-color: #e4e2e2");
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

