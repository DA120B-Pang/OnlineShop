package zaar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AfterPayments implements Initializable {
    @FXML
    private Label ty;
    @FXML
    private Label confirmed;
    @FXML
    private ImageView check;
    @FXML
    private ImageView pic;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane pane = new AnchorPane();
        Image image = new Image("File:img/check.png");
        pane.getChildren().add(new ImageView(image));
    }
}
