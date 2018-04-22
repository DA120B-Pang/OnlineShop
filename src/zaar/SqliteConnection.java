package zaar;

import java.sql.*;

public class SqliteConnection {

    public static Connection connector(){

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/OnlineShopDatabase?verifyServerCertificate=false&useSSL=true",
                    "root","root");
            return conn;
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
}
