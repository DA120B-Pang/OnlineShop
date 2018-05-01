package zaar.product;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import zaar.product.Menu.Category;
import zaar.product.Menu.Menus;
import zaar.product.Menu.MenuObject;
import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

public class ProductModel {
    private Connection connection;
    private Database db;
    private boolean toggleColor;

    /**
     * Gets the menubutton from Database
     * @param button
     * @return
     */
    public MenuButton getMenu(MenuButton button, VBox vBox) {
        db = Database.getInstance();//Get Menus and categories from database
        ArrayList<ArrayList<MenuObject>> list = db.getMenu();//Get menus and categories from db
        HashMap<Integer,Menu> menuHash = new HashMap<>();//All Menu that exists.
        HashMap<Integer,ArrayList<MenuObject>> toParentHash = new HashMap<>();//Items sorted by to wich parent they belong
        ArrayList<Menus> rootMenus = new ArrayList<>();//Lowest level of Menu

        for(MenuObject o:list.get(0)){//Get Menus and sort
            if (o.getParentMenuId()==0){//Is menu root menu?
                rootMenus.add((Menus)o);
            }
            else{//Menu is higher than root
                addMenuObject(toParentHash,o);//Add menu hashtable for wich parent MenuObject belongs
            }
            Menu menu = new Menu(o.getName());//Create Menu
            menuHash.put(((Menus)o).getMenuId(), menu);//And add to hashMap that contains all Menus
        }

        for(MenuObject o:list.get(1)){// Get Categories and sort
            addMenuObject(toParentHash,o);//Add menu hashtable for wich parent MenuObject belongs
        }

        for(Map.Entry<Integer, ArrayList<MenuObject>> Entry: toParentHash.entrySet()){//Loop all Menuobjects that belongs to a parentMenuObject
            Entry.getValue().sort(new MenuObject());//Sort list alphabetically
            for (MenuObject m:Entry.getValue()){//Loop MenuObjects that belong to parentMenuObject
                if(m instanceof Menus){//Is MenuObject Menu or menuitem?
                    menuHash.get(Entry.getKey()).getItems().add(menuHash.get(((Menus) m).getMenuId()));//Get Menu from hashmap that contains all menus add to parent Menu
                }
                else{
                    MenuItem menuItem = new MenuItem(m.getName());
                    menuItem.setOnAction((event -> {
                        ArrayList<Product> prodList = db.getCategory(((Category)m).getCategoryId());
                        populateProductVbox(vBox,prodList);
                    }));
                    menuHash.get(Entry.getKey()).getItems().add(menuItem);//Add MenuItem to parent Menu
                }
            }
        }
        rootMenus.sort(new MenuObject());//Sort list alphabetically
        for(Menus m:rootMenus){//Loop all root Menus
            button.getItems().add(menuHash.get(m.getMenuId()));//Add root Menus to button
        }
        try {
            //Adding image to button
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



    public void outpuyt(int num, String name){
        System.out.println(num +" "+name);
    }


    public void populateProductVbox(VBox vBox, ArrayList<Product> products){
        AnchorPane anchorPane;
        if(vBox.getChildren().size()>0) {
            vBox.getChildren().remove(0, vBox.getChildren().size());
        }
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
//            FileInputStream input = new FileInputStream("src/img/product/cart.png");//Adding image
//            Image image = new Image(input);//Adding image
            ImageView imageView = product.getImageView();
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


            FileInputStream input = new FileInputStream("src/img/product/cart.png");//Adding image
            Image image = new Image(input);//Adding image
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

