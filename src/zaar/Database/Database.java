package zaar.Database;

import com.mysql.jdbc.Connection;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import zaar.customer.PaymentMethods;
import zaar.customer.User;
import zaar.helperclasses.BooleanMethodIntString;
import zaar.helperclasses.BooleanMethodString;
import zaar.helperclasses.DataSingleton;
import zaar.product.Manufacturer;
import zaar.product.Menu.Category;
import zaar.product.Menu.Menus;
import zaar.product.Menu.MenuObject;
import zaar.product.Product;

import java.io.*;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

import static zaar.Database.Database.DeleteRecord.PAYMENT_METHOD;
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

    public String getStringFromTable(int id, GetString getString) {
        String query;
        if (getString == GetString.GET_CATEGORY) {
            query = "SELECT product_catagory_name FROM shopit.category where product_catagory_id = ?;";
        } else if (getString == GetString.GET_MANUFACTURER) {
            query = "SELECT manufacturer_name FROM shopit.manufacturer where idmanufacturer = ?;";
        } else{
            query = "select menu.menuName from menu where menu.idMenu = ?;";
        }
        String string = null;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                if(id == 0){
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

    public boolean checkLoginName(String loginName){
        String query = "SELECT login_name FROM shopit.users where login_name = ?;";
        boolean  retVal = false;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(query)) {

                pst.setString(1, loginName);
                if (!pst.executeQuery().next()){
                    retVal = true;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }
        return retVal;

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

    public ArrayList<User> getAllUsers(){
        ArrayList<User> list = new ArrayList<>();
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.users;")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(new User(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getString(10),
                            rs.getString(11)));
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return list;
    }

    public  ArrayList<PaymentMethods> getPayment(int id, boolean singlePayment){
        ArrayList<PaymentMethods> list = new ArrayList<>();
        String query;
        if (!singlePayment){
            query = "SELECT * FROM shopit.user_payment_methods;";
        }
        else{
            query = "SELECT * FROM shopit.user_payment_methods;";
        }
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("SELECT * FROM shopit.user_payment_methods;")) {
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    list.add(new PaymentMethods(rs.getInt(1),rs.getInt(2), rs.getString(3)));
                }

            } catch (SQLException ex) {
                list = null;
                ex.printStackTrace();
            }
        }
        return list;
    }

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

    public boolean insertPaymentMethod(int id, String card){
        boolean retVal = false;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("INSERT INTO `shopit`.`user_payment_methods` (`users_user_id`, `credit_card_number`) VALUES (?, ?);")) {
                pst.setInt(1, id);
                pst.setString(2, card);

                pst.execute();
                retVal = true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }

        }
        return retVal;
    }

    public int insertPayment(String amount) {
        int retVal = -1;

        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("INSERT INTO `shopit`.`payments` (`payment_date`, `payment_amount`) VALUES (now(), ?);",Statement.RETURN_GENERATED_KEYS)) {
                pst.setString(1, amount);
                pst.execute();

                if(pst.getGeneratedKeys().next()){
                    System.out.println(pst.getGeneratedKeys());
                }
                retVal = 0;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }

        }
        return retVal;
    }

//    public boolean updatePaymentMethod(int id, String card){
//        boolean retVal = false;
//        if (checkConnection()) {
//            try (PreparedStatement pst = connection.prepareStatement("INSERT INTO `shopit`.`user_payment_methods` (`users_user_id`, `credit_card_number`) VALUES (?, ?);")) {
//                pst.setInt(1, id);
//                pst.setString(2, card);
//
//                pst.execute();
//                retVal = true;
//            } catch (SQLException ex) {
//                System.out.println("error on executing the query" + ex);
//            }
//
//        }
//        return retVal;
//    }

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

    public boolean insertUser(int role,
                              String firstName,
                              String lastName,
                              String email,
                              String loginName,
                              String password,
                              String phoneNumber,
                              String address,
                              String city,
                              String country) {
        boolean retVal = false;
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement("INSERT INTO `shopit`.`users` (`Role_idRole`, `first_name`, `last_name`, `email_adress`, `login_name`, `login_password`, `phone_number`, `address_line`, `town_city`, `country`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")){
                pst.setInt(1, role);
                pst.setString(2, firstName);
                pst.setString(3, lastName);
                pst.setString(4, email);
                pst.setString(5, loginName);
                pst.setString(6, password);
                pst.setString(7, phoneNumber);
                pst.setString(8, address);
                pst.setString(9, city);
                pst.setString(10, country);

                pst.executeUpdate();
                retVal = true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }
        }
        return retVal;
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

    public boolean updateUser(User user){

        String query = "UPDATE `shopit`.`users` SET `Role_idRole`= ?, `first_name`= ?, `last_name`= ?, `email_adress`= ?, `login_name`= ?, `login_password`=?, `phone_number`=?, `address_line`=?, `town_city`=?, `country`=? WHERE `user_id`=?;";
        if (checkConnection()) {
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setInt(1, user.getRole());
                pst.setString(2, user.getFirstName());
                pst.setString(3, user.getLastName());
                pst.setString(4, user.getEmail());
                pst.setString(5, user.getLoginName());
                pst.setString(6, user.getPassword());
                pst.setString(7, user.getPhoneNumber());
                pst.setString(8, user.getAddress());
                pst.setString(9, user.getCity());
                pst.setString(10, user.getCountry());
                pst.setInt(11, user.getCustomerID());

                pst.executeUpdate();
                return true;
            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
            }
        }
        return false;
    }

    public enum DeleteRecord{
        PRODUCT,
        MANUFACTURER,
        CATEGORY,
        MENU,
        USER,
        PAYMENT_METHOD;
    }

    public boolean deleteRecord(DeleteRecord deleteRecord, int id) {
        String[] query = new String[2];//adapt length to how many queries so that loop will know when delete statement is coming
        String[] warning = new String[5];
        switch (deleteRecord){
            case MENU:
                query  =new String[3];
                query[0] = "SELECT * FROM shopit.category where category.Menu_idMenu = ?;";
                warning[0] = "Child categories must be removed first";
                query[1] = "SELECT * FROM shopit.menu where idParentMenu = ?;";
                warning[1] = "Child menus must be removed first";
                query[2] = "delete from shopit.menu where idMenu = ?";
                break;
            case PRODUCT:
                query[1] = "delete from products where product_id = ?;";
                break;
            case CATEGORY:
                query[0] = "SELECT * FROM shopit.products where product_catagory_id = ?;";
                warning[0] = "Child products must be removed first";
                query[1] = "delete from shopit.category where product_catagory_id = ?;";
                break;
            case MANUFACTURER:
                query[0] = "SELECT * FROM shopit.products where products.manufacturer_idmanufacturer = ?;";
                warning[0] = "Products that belong to category must be removed first";
                query[1] = "delete from shopit.manufacturer where idmanufacturer = ?;";
            case USER:
                query  =new String[3];
                query[0] = "SELECT * FROM shopit.user_payment_methods where users_user_id = ?;";
                warning[0] = "Users payment methods must be removed first";
                query[1] = "SELECT * FROM shopit.orders where users_user_id = ?;";
                warning[1] = "Users orders must be removed first";
                query[2] = "delete from shopit.users where user_id = ?;";
                break;
            case PAYMENT_METHOD:
                query[1] = "delete from user_payment_methods where users_user_id = ?;";
                break;
        }

        if (checkConnection()) {
            for (int i = 0; i <query.length ; i++) {
                if(deleteRecord != PRODUCT && deleteRecord != PAYMENT_METHOD || i != 0) {
                    try (PreparedStatement pst = connection.prepareStatement(query[i])) {
                        pst.setInt(1, id);
                        if (i == query.length - 1) {
                            pst.executeUpdate();
                            return true;
                        } else {
                            if (pst.executeQuery().next()) {
                                Alert alert = new Alert(Alert.AlertType.WARNING, warning[i]);
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

    public User isLogin(String user, String pass) throws SQLException{

        ResultSet rs = null;
        String query = "Select * from users where BINARY login_name = ? and BINARY login_password = ?";
        User loggedInUser = null;
        if(checkConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, user);
                preparedStatement.setString(2, pass);

                rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return loggedInUser = new User(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getString(5),
                            rs.getString(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9),
                            rs.getString(10),
                            rs.getString(11));
                } else {
                    return null;
                }

            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}


