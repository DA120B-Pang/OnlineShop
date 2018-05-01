package zaar;

import com.mysql.jdbc.Connection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import zaar.product.Menu.Category;
import zaar.product.Menu.Menus;
import zaar.product.Menu.MenuObject;
import zaar.product.Product;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

public class Database {
    private static Database ourInstance = new Database();;
    private Connection connection;

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        connector();
    }

    private void connector(){
        try{
            //Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://den1.mysql5.gear.host:3306/shopit?useSSL=false&user=shopit&password=ShopIt1234!");
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private boolean checkConnection(){
        if(connection!=null){
            try {
                if (!connection.isClosed()) {
                    return true;
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        ButtonType Yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType No = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.WARNING, "Connection to database is lost. Try to reconnect?", Yes, No);
        alert.setTitle("Database connection is down.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == Yes) {
            connector();
        }
        return false;
    }

    /**
     * Gets menus and Categories from database
     * @return
     */
    public ArrayList<ArrayList<MenuObject>> getMenu() {
        ArrayList<ArrayList<MenuObject>> list = new ArrayList<>();
        if (checkConnection()) {
            list.add(new ArrayList<MenuObject>());
            list.add(new ArrayList<MenuObject>());
            try (Statement statement = connection.createStatement()){
                ResultSet rs = statement.executeQuery("SELECT * FROM shopit.menu;");
                while (rs.next()) {
//                    //index1 = menu ID, index2 = parent menu ID, index 3 = menu name
                    list.get(0).add(new Menus(rs.getInt(1), rs.getInt(2), rs.getString(3)));
                }
                //statement = connection.createStatement();
                rs = statement.executeQuery("SELECT * FROM shopit.category;");
                while (rs.next()) {
//                    //index1 = category ID, index2 = parent menu ID, index 3 = category name
                    list.get(1).add(new Category(rs.getInt(1), rs.getInt(2), rs.getString(3)));
                }
                return list;
            }

             catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
                //menu = null;
            }
        }
        return list;
    }

    public ArrayList<Product> getCategory(int categoryId){
        ArrayList<Product> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.products WHERE product_catagory_id = ?;")) {
                pst.setInt(1, categoryId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    ImageView imageView = new ImageView();
                    if (rs.getBinaryStream(9) != null) {
                        try {
                            InputStream is = rs.getBinaryStream(9);
                            Image image = new Image(is);
                            imageView = new ImageView(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            FileInputStream input = new FileInputStream("src/img/product/noimage.png");
                            Image image = new Image(input);
                            imageView = new ImageView(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    list.add(new Product(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getInt(6), rs.getString(7), rs.getString(8), imageView));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }
        return list;
    }

    public ArrayList<Product> getMenuProducts(int Category){
        if (connection != null){
            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM shopit.products where parent_product_catagory_id is not null order by parent_product_catagory_id DESC, product_catagory_name DESC");

            }catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
                //menu = null;
            }

        }
        return null;
    }
}


