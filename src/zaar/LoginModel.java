package zaar;

import zaar.Database.Database;

import java.sql.*;

public class LoginModel {

    Connection connection;

    //public LoginModel(){
    //connection = Database.connector();
    //if (connection == null) System.exit(0);
    //}



    public boolean isLogin(String user, String pass) throws SQLException{
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "Select * from Customers where login_name = ? and login_password = ?";

        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,user);
            preparedStatement.setString(2,pass);

            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else {
                return false;
            }

        }catch (Exception e ){
            return false;
        }finally {
            preparedStatement.close();
            resultSet.close();
        }
    }
}
