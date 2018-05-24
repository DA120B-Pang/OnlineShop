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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Payments implements Initializable {
    @FXML
private TextField skrivCVC;
    @FXML
    private TextField skrivsiffror;
    @FXML
    private TextField mm;
    @FXML
    private Label kortnummer;
    @FXML
    private Label expire;
    @FXML
    private Label cvv;
    @FXML
    private Label betalning;
    @FXML
    private Label visa;
    @FXML
    private Button betala;
    @FXML
    private ImageView shop;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane pane = new AnchorPane();
        Image image = new Image("File:img/ShopITsmall.png");
        pane.getChildren().add(new ImageView(image));
    }
    @FXML
   public void usePaymentButton(ActionEvent event) throws IOException {
        Node node = (Node)event.getSource();
        Stage stage = (Stage)node.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sampleAfterPayments.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}