package zaar.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageDatabaseController implements Initializable{

    private ManageDatabaseModel mDM= new ManageDatabaseModel();

    @FXML
    private HBox hBox;

    @FXML
    private VBox vBox;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mDM.getTopHBox(hBox);
        vBox.getChildren().add(mDM.getProdTools());

    }
}
