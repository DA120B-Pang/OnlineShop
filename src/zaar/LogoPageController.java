package zaar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogoPageController implements Initializable {
    @FXML
    private ImageView logo;
    @FXML
    private Button moveOn;
    @FXML
    private AnchorPane pane;

    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane pane = new AnchorPane();
        Image image = new Image("File:img/ShopIT.png");
        pane.getChildren().add(new ImageView(image));



    }

    public void useMoveOnButton(ActionEvent e) throws IOException {
        Node node = (Node) e.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sampleProduct.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);

    }
}
