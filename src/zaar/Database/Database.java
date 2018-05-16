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

import static zaar.Database.Database.DeleteRecord.PRODUCT;

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

    public boolean isDBConnected(){
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkConnection(){
        if(connection!=null){
            try {
                if (!connection.isClosed()) {
                    if(connection.isValid(10)) {
                        return true;
                    }
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

    public enum GetString{
        GET_MANUFACTURER,
        GET_CATEGORY,
        GET_MENU;
    }

    public String getStringFromTable(int id, GetString getString){
        String Query;
        if(getString == GetString.GET_CATEGORY){
            Query = "SELECT product_catagory_name FROM shopit.category where product_catagory_id = ?;";
        }
        else if(getString == GetString.GET_MANUFACTURER){
            Query = "SELECT manufacturer_name FROM shopit.manufacturer where idmanufacturer = ?;";
        }
        else{
            Query = "select menu.menuName from menu where menu.idMenu = ?;";
        }
        String string = null;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(Query)) {
                if(id==0){
                    string ="root";
                }
                else {
                    pst.setInt(1, id);
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        string = rs.getString(1);
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }
        return string;
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

    public enum GetProd{
        PROD_SINGLE,
        PROD_CATEGORY;
    }

    public ArrayList<Product> getProduct(int id, GetProd getProd) {
        String Query;
        if(getProd == GetProd.PROD_SINGLE){
            Query = "SELECT * FROM shopit.products where product_id = ? order by product_name;";
        }
        else{
            Query = "SELECT * FROM shopit.products WHERE product_catagory_id = ? order by product_name;";
        }
        ArrayList<Product> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(Query)) {
                pst.setInt(1, id);
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

    public ArrayList<Category> getAllCategories(){
        ArrayList<Category> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.category;")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(new Category(rs.getInt(1), rs.getInt(2), rs.getString(3)));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    };

    public ArrayList<Menus> getAllMenus(){
        ArrayList<Menus> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.menu;")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(new Menus(rs.getInt(1), rs.getInt(2), rs.getString(3)));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    };

    public ArrayList<Manufacturer> getAllManufacturers(){
        ArrayList<Manufacturer> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.manufacturer;")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(new Manufacturer(rs.getInt(1), rs.getString(2)));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    };

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT product_id, product_catagory_id, manufacturer_idmanufacturer, product_name, product_price, product_quantity, product_description FROM shopit.products order by product_name;")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(new Product(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), rs.getInt(6), rs.getString(7), null, null));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }
        return list;
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
        public boolean method(int intNum, String string) {
            return dbInsertString("INSERT INTO `shopit`.`manufacturer` (`manufacturer_name`) VALUES (?);",string);
        }
    }

    public class InsertCategory implements BooleanMethodIntString{
        @Override
        public boolean method(int intNum, int intNum2, String string) {
            return dbInsertIntString("INSERT INTO `shopit`.`category` (`Menu_idMenu`, `product_catagory_name`) VALUES (?, ?);",intNum2,string);
        }
    }

    public class InsertMenu implements BooleanMethodIntString{
        @Override
        public boolean method(int intNum, int intNum2, String string) {
            return dbInsertIntString("INSERT INTO `shopit`.`menu` (`idParentMenu`, `menuName`) VALUES (?, ?);",intNum2,string);
        }
    }


    public boolean update2intString(String statement, int intNum, String string , int intNum2){

        String query = statement;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                if(intNum2 == 0) {
                    pst.setNull(1, Types.INTEGER);
                }
                else {
                    pst.setInt(1, intNum2);
                }
                pst.setString(2, string);

                pst.setInt(3, intNum);

                pst.executeUpdate();
                return true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }
        }
        return false;
    }

    public class UpdateMenu implements BooleanMethodIntString{
        @Override
        public boolean method(int intNum, int intNum2, String string) {
            return update2intString("UPDATE `shopit`.`menu` SET `idParentMenu`= ?, `menuName`= ? WHERE `idMenu`= ?;",intNum,string,intNum2);
        }
    }

    public class UpdateCategory implements BooleanMethodIntString{
        @Override
        public boolean method(int intNum, int intNum2, String string) {
            return update2intString("UPDATE `shopit`.`category` SET `Menu_idMenu`= ?, `product_catagory_name`= ? WHERE `product_catagory_id`= ? ;",intNum,string,intNum2);
        }
    }

    public boolean updateintString(String statement, int intNum, String string){

        String query = statement;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(query)) {

                pst.setString(1, string);

                pst.setInt(2, intNum);

                pst.executeUpdate();
                return true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }
        }
        return false;
    }
    public class UpdateManufacturer implements BooleanMethodString{
        @Override
        public boolean method(int intNum, String string) {
            return updateintString("UPDATE `shopit`.`manufacturer` SET `manufacturer_name`=? WHERE `idmanufacturer`=?;",intNum,string);
        }
    }

    public enum DeleteRecord{
        PRODUCT,
        MANUFACTURER,
        CATEGORY,
        MENU;
    }

    public boolean deleteRecord(DeleteRecord deleteRecord, int id) {
        String[] query = new String[2];
        String warning = "";
        switch (deleteRecord){
            case MENU:
                query = new String[3];
                query[0] = "SELECT * FROM shopit.category where category.Menu_idMenu = ?;";
                query[1] = "SELECT * FROM shopit.menu where idParentMenu = ?;";
                query[2] = "delete from shopit.menu where idMenu = ?";
                warning = "Child menus & categories must be removed first";
                break;
            case PRODUCT:
                query[1] = "delete from products where product_id = ?;";
                break;
            case CATEGORY:
                query[0] = "SELECT * FROM shopit.products where product_catagory_id = ?;";
                query[1] = "delete from shopit.category where product_catagory_id = ?;";
                warning = "Child products must be removed first";
                break;
            case MANUFACTURER:
                query[0] = "SELECT * FROM shopit.products where products.manufacturer_idmanufacturer = ?;";
                query[1] = "delete from shopit.manufacturer where idmanufacturer = ?;";
                warning = "Child products must be removed first";
        }

        if (checkConnection()) {
            for (int i = 0; i <query.length ; i++) {
                if(deleteRecord!=PRODUCT || i != 0) {
                    try (PreparedStatement pst = connection.prepareStatement(query[i])) {
                        pst.setInt(1, id);
                        if (i == query.length - 1) {
                            pst.executeUpdate();
                            return true;
                        } else {
                            if (pst.executeQuery().next()) {
                                Alert alert = new Alert(Alert.AlertType.WARNING, warning);
                                alert.show();
                                return false;
                            }
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();

                    }
                }
            }
        }
        return false;
    }
}


