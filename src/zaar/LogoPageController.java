package zaar;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class LogoPageController implements Initializable {
    @FXML
    private ImageView logo;
    @FXML
    private Label moveOn;
    @FXML
    private AnchorPane pane;

    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane pane = new AnchorPane();
        Image image = new Image("File:img/ShopITsmall.png");
        pane.getChildren().add(new ImageView(image));

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1.2), moveOn);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.play();


    }

    @FXML
    void useMoveOnButton(MouseEvent Event) {
        try {
            Node node = (Node) Event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("product/Product.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            System.out.println("go");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Switched scene");
        }
    }
}
