package zaar;

import java.sql.*;
import com.mysql.jdbc.Connection;

public class SqlConnection {

    public static Connection connector(){

        try{


            //Class.forName("com.mysql.jdbc.Driver");
            Connection conn = (Connection)DriverManager.getConnection("jdbc:mysql://den1.mysql5.gear.host:3306/shopit?useSSL=false&user=shopit&password=ShopIt1234!");
            return conn;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
