import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
        Image image = new Image("File:img/ShopIT.png");
        pane.getChildren().add(new ImageView(image));
    }
}
