package zaar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import zaar.Database.Database;
import zaar.customer.EditAddUser;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable{
    @FXML private Button backButton;
    @FXML private Button loginButton;
    @FXML private TextField username;
    @FXML private TextField password;
    @FXML private Label loginStatus;
    @FXML private Label accountLbl;
    @FXML private Label passwdLbl;

    private DataSingleton dS = DataSingleton.getInstance();
    private Database dB = Database.getInstance();
    private ScreenSingleton sS = ScreenSingleton.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ImageView backIconImageView = new ImageView(dS.getBackwardIcon());
        backIconImageView.setFitWidth(15);
        backIconImageView.setFitHeight(15);
        backButton.setGraphic(backIconImageView);
        VBox.setMargin(backButton, new Insets(5,0,0,5));

        accountLbl.setOnMouseEntered(E->{
            accountLbl.setStyle("-fx-text-fill: BLUE");
        });
        accountLbl.setOnMouseExited(E->{
            accountLbl.setStyle("-fx-text-fill: BLACK");
        });
        accountLbl.setOnMouseClicked(E->{
            new EditAddUser("Add user").popUp();
        });
        passwdLbl.setOnMouseEntered(E->{
            passwdLbl.setStyle("-fx-text-fill: BLUE");
        });
        passwdLbl.setOnMouseExited(E->{
            passwdLbl.setStyle("-fx-text-fill: BLACK");
        });
    }
    public void login(ActionEvent event){
        try{
            dS.setLoggedInUser(dB.isLogin(username.getText(), password.getText()));
            if (dS.getLoggedInUser()!=null){
                loginStatus.setText("Welcome "+dS.getLoggedInUser().getLoginName());
                sS.new OpenProductScreen().screenChange(event);
            }else {
                loginStatus.setText("Incorrect username or password!");
            }
        }catch (SQLException e){
            e.printStackTrace();

        }
    }

    // byter scen från loginskärmen till startsidan
    public void useBackButton(ActionEvent e){
        sS.new OpenProductScreen().screenChange(e);
    }


}
