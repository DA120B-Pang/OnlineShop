package zaar.product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {

    public static Connection getConnection () throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://den1.mysql5.gear.host:3306/shopit?useSSL=false&user=shopit&password=ShopIt1234!");
        return connection;
    }
}
