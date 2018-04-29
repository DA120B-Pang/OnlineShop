package zaar;

import com.mysql.jdbc.Connection;
import zaar.product.Category;
import zaar.product.MenuSort;
import zaar.product.Product;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://den1.mysql1.gear.host:3306/shopit?useSSL=false&user=shopit&password=ShopIt1234!");
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public HashMap<Integer, Queue <Category>> getMenu(HashMap<Integer, Queue <Category>> menu) {
        if (connection != null) {
            Queue<Category> list;
            Category cat;
            HashMap<Integer,String> forSortingMenu = new HashMap<>();
            try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT * FROM shopit.category where parent_product_catagory_id is not null order by parent_product_catagory_id DESC, product_catagory_name DESC");
                while (rs.next()) {
                    //index1 = category ID, index2 = parent Category ID, index 3 = category type desc, index 4 = category name
                    cat = new Category(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4));
                    System.out.println(cat.getName());
                    forSortingMenu.put(cat.getProductCategory(), cat.getName());
                    if (menu.containsKey(cat.getProductCategory())) {//Submenus exists
                        list = menu.get(cat.getProductCategory());//Read Qqueue
                        menu.remove(cat.getProductCategory());//Remove old Hashvalue
                        list.add(cat);//Add Category
                        if (cat.getParentGroupCategory() != 0) {
                            if (menu.containsKey(cat.getParentGroupCategory())) {
                                Queue<Category> list2 = menu.get(cat.getParentGroupCategory());
                                for (Category c: list2) {
                                    list.add(c);
                                    }
                            }
                            menu.put(cat.getParentGroupCategory(), list);//Insert new Hash
                        }
                        else {
                            menu.put(cat.getProductCategory(), list);//Insert new Hash
                        }
                    }
                    else if (menu.containsKey(cat.getParentGroupCategory())) {//Add to same menulevel
                        list = menu.get(cat.getParentGroupCategory());//Read Qqueue
                        list.add(cat);//Add Category
                    }
                    else {
                        list = new LinkedList<>();
                        list.add(cat);
                        if (cat.getParentGroupCategory()==0) {
                            menu.put(cat.getProductCategory(), list);//Insert new Hash
                        }
                        else{
                            menu.put(cat.getParentGroupCategory(), list);//Insert new Hash
                        }
                    }
                }
                ArrayList<MenuSort> menuSort = new ArrayList<>();
                for (Map.Entry<Integer, Queue<Category>> entry : menu.entrySet()){
                    menuSort.add(new MenuSort(entry.getKey(),forSortingMenu.get(entry.getKey())));
                }
                menuSort.sort(new MenuSort(1,""));

                HashMap<Integer, Queue <Category>> tmp = new HashMap<>();
                for (int i = 0; i < menuSort.size(); i++) {
                    tmp.put(i,menu.get(menuSort.get(i).index));
                }

                return tmp;

            } catch (SQLException ex) {
                System.out.println("error on executing the query" + ex);
                //menu = null;
            }
        }
        return menu;
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


