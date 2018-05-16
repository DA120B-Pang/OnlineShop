package zaar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zaar.Database.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML private Button backButton;
    @FXML private Button loginButton;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Label dbStatus;
    @FXML private Label loginStatus;

    public LoginModel loginModel = new LoginModel();
    private Database dB = Database.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        if(loginModel.isDBConnected()){
//            dbStatus.setText("Ansluten till DB.");
//        }else {
//            dbStatus.setText("Ej ansluten till DB. ");
//        }
    }

    public void login(ActionEvent event){
        try{
            if (dB.isLogin(username.getText(), password.getText())){
                loginStatus.setText("Användarnamn och lösenord är korrekt!");
            }else {
                loginStatus.setText("Användarnamn eller lösenord är inkorrekt!");
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
    }

    // byter scen från loginskärmen till startsidan
    public void useBackButton(ActionEvent e) throws IOException {
        Node node = (Node)e.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("product/Product.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
