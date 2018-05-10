package zaar.Database;

import com.mysql.jdbc.Connection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import zaar.helperclasses.BooleanMethodIntString;
import zaar.helperclasses.BooleanMethodString;
import zaar.product.Manufacturer;
import zaar.product.Menu.Category;
import zaar.product.Menu.Menus;
import zaar.product.Menu.MenuObject;
import zaar.product.Product;

import java.io.*;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

public class Database {
    private static Database ourInstance = new Database();
    private boolean tryReconnect;
    private Connection connection;

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        connector();
    }

    private void connector() {
        try {
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://den1.mysql5.gear.host:3306/shopit?useSSL=false&user=shopit&password=ShopIt1234!");
            if(connection!=null) {
                connection.setAutoReconnect(true);
                tryReconnect = true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private boolean checkConnection(){
        if(connection!=null){
            try {
                if (!connection.isClosed()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(tryReconnect){
            tryReconnect = false;
            connector();
            if(checkConnection()){
                return true;
            }
        }
        else {
            ButtonType Yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType No = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Connection to database is lost. Try to reconnect?", Yes, No);
            alert.setTitle("Database connection is down.");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == Yes) {
                connector();
            }
        }
        return false;
    }

    /**
     * Gets menus and Categories from database
     *
     * @return
     */
    public ArrayList<ArrayList<MenuObject>> getMenu() {
        ArrayList<ArrayList<MenuObject>> list = new ArrayList<>();
        if (checkConnection()) {
            list.add(new ArrayList<MenuObject>());
            list.add(new ArrayList<MenuObject>());
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM shopit.menu;");
                while (rs.next()) {
//                    //index1 = menu ID, index2 = parent menu ID, index 3 = menu name
                    list.get(0).add(new Menus(rs.getInt(1), rs.getInt(2), rs.getString(3)));
                }
                rs = statement.executeQuery("SELECT * FROM shopit.category;");
                while (rs.next()) {
//                    //index1 = category ID, index2 = parent menu ID, index 3 = category name
                    list.get(1).add(new Category(rs.getInt(1), rs.getInt(2), rs.getString(3)));
                }
                return list;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }
        }
        return list;
    }

    public ArrayList<Product> getCategory(int categoryId) {
        ArrayList<Product> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.products WHERE product_catagory_id = ?  order by product_name;")) {
                pst.setInt(1, categoryId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    ImageView imageView = new ImageView();
                    if (rs.getBinaryStream(9) != null) {
                        try (InputStream is = rs.getBinaryStream(9);){
                            Image image = new Image(is);
                            imageView = new ImageView(image);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try(FileInputStream input = new FileInputStream("src/img/icons/noimage.png");) {
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

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.products order by product_name;")) {
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
                            FileInputStream input = new FileInputStream("src/img/icons/noimage.png");
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

    public String[] getManufCatName(int manufacturerId, int categoryId){
        String[] retVal = new String[2];
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT manufacturer_name,product_catagory_name FROM shopit.manufacturer,shopit.category where idmanufacturer = ? AND product_catagory_id = ?;")) {
                pst.setInt(1, manufacturerId);
                pst.setInt(2, categoryId);

                ResultSet rs = pst.executeQuery();
                if(rs.next()) {
                    retVal[0] = rs.getString(1);
                    retVal[1] = rs.getString(2);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                retVal = null;
            }
        }
        return retVal;
    }

    public boolean updateProduct(int prodId, int categoryId, int manufacturerId, String name, Double price, int Quantity, String desc, String other, File file){
        boolean retVal = false;
        InputStream is = null;
        String query = "UPDATE `shopit`.`products` SET `product_catagory_id`=?, `manufacturer_idmanufacturer`=?, `product_name`=?, `product_price`=?, `product_quantity`=?, `product_description`=?, `other_product_details`=? WHERE `product_id`=?;";
        if(file!=null) {
            query = "UPDATE `shopit`.`products` SET `product_catagory_id`=?, `manufacturer_idmanufacturer`=?, `product_name`=?, `product_price`=?, `product_quantity`=?, `product_description`=?, `other_product_details`=?,`product_picture`=? WHERE `product_id`=?;";
        }
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setInt(1, categoryId);
                pst.setInt(2, manufacturerId);
                pst.setString(3, name);
                pst.setDouble(4, price);
                pst.setInt(5, Quantity);
                pst.setString(6, desc);
                pst.setString(7, other);
                if(file!=null) {
                    is = new FileInputStream(file);
                    pst.setBinaryStream(8, is);
                    pst.setInt(9,prodId);
                }
                else {
                    pst.setInt(8,prodId);
                }
                pst.executeUpdate();
                retVal = true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }
            catch(IOException e){

            }
            finally {
                if(is!=null){
                    try{
                        is.close();
                    }
                    catch (IOException e){

                    }
                }
            }

        }
        return retVal;
    }

    public boolean insertProduct(int categoryId, int manufacturerId, String name, Double price, int Quantity, String desc, String other, File file) {
        boolean retVal = false;
        InputStream is = null;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("INSERT INTO `shopit`.`products` (`product_catagory_id`,`manufacturer_idmanufacturer`, `product_name`, `product_price`, `product_quantity`, `product_description`, `other_product_details`,`product_picture`) VALUES (?,?, ?, ?, ?, ?, ?, ?);");) {
                pst.setInt(1, categoryId);
                pst.setInt(2, manufacturerId);
                pst.setString(3, name);
                pst.setDouble(4, price);
                pst.setInt(5, Quantity);
                pst.setString(6, desc);
                pst.setString(7, other);
                if(file==null) {
                    pst.setNull(8,Types.BLOB);
                }
                else {
                    is = new FileInputStream(file);
                    pst.setBinaryStream(8, is);

                }
                pst.execute();
                retVal = true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }
            catch (IOException e){

            }
            finally {
                if(is!=null) {
                    try {
                        is.close();
                    } catch (IOException e) {

                    }
                }
            }

        }
        return retVal;
        //INSERT INTO `shopit`.`products` (`manufacturer_idmanufacturer`, `product_name`, `product_price`, `product_quantity`, `product_description`, `other_product_details`) VALUES ('2', 'Test', '100', '2', 'testa', 'testa');
    }

    public ArrayList<Manufacturer> getManufacturers() {
        ArrayList<Manufacturer> list = new ArrayList<>();
        if (checkConnection()) {
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery("SELECT * FROM shopit.manufacturer order by manufacturer_name;");
                while (rs.next()) {
//                    //index1 = manufacturer ID, index2 = name
                    list.add(new Manufacturer(rs.getInt(1), rs.getString(2)));
                }
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
                list = null;
            }

        }else{
            list = null;
        }
        return list;
    }
    private boolean dbInsertIntString(String insertStatement ,int intNumber, String string) {
        boolean retVal = false;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(insertStatement)) {
                if(intNumber==0){
                    pst.setNull(1, intNumber);
                }
                else {
                    pst.setInt(1, intNumber);
                }
                pst.setString(2, string);
                pst.execute();
                retVal = true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
                //menu = null;
            }

        }
        return retVal;
    }

    private boolean dbInsertString(String insertStatement ,String string) {
        boolean retVal = false;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(insertStatement)) {
                pst.setString(1, string);
                pst.execute();
                retVal = true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
                //menu = null;
            }

        }
        return retVal;
    }

    public class InsertManufacturer implements BooleanMethodString{
        @Override
        public boolean method(String string) {
            return dbInsertString("INSERT INTO `shopit`.`manufacturer` (`manufacturer_name`) VALUES (?);",string);
        }
    }

    public class InsertCategory implements BooleanMethodIntString{
        @Override
        public boolean method(int intNum, String string) {
            return dbInsertIntString("INSERT INTO `shopit`.`category` (`Menu_idMenu`, `product_catagory_name`) VALUES (?, ?);",intNum,string);
        }
    }

    public class InsertMenu implements BooleanMethodIntString{
        @Override
        public boolean method(int intNum, String string) {
            return dbInsertIntString("INSERT INTO `shopit`.`menu` (`idParentMenu`, `menuName`) VALUES (?, ?);",intNum,string);
        }
    }

}


