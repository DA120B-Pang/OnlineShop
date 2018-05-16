package zaar.customer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import zaar.Database.Database;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {

    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField userNameTextField;
    @FXML private TextField passwordTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private TextField adressLineTextField;
    @FXML private TextField cityTextField;
    @FXML private TextField countryTextField;

    private Database dB = Database.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void toLastScene(ActionEvent e) throws IOException {
        Node node = (Node)e.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        // TODO kan inte ladda in den gamla scenen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("product/sampleLogin.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void createAccount(ActionEvent e) throws SQLException {
        dB.insertAccountInfoToDB(firstNameTextField.getText(), lastNameTextField.getText(), emailTextField.getText(), userNameTextField.getText(),
                passwordTextField.getText(), phoneNumberTextField.getText(), adressLineTextField.getText(), cityTextField.getText(), countryTextField.getText());

    }
}
