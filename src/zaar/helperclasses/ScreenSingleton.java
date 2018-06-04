package zaar.helperclasses;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import zaar.Database.BooleanMethodIntString;
import zaar.Database.BooleanMethodString;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.product.Manufacturer;
import zaar.product.Menu.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * Singleton class for creating objects for screen changing
 */
public class ScreenSingleton {
    private static ScreenSingleton ourInstance = new ScreenSingleton();

    public static ScreenSingleton getInstance() {
        return ourInstance;
    }

    private ScreenSingleton() {
    }
    //****************Navigation***********************************************************************
    public interface ScreenChange{
        public void screenChange(ActionEvent e);
    }

    public class OpenProductScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/product/Product.fxml");
        }
    }
    public class OpenAddProductScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/admin/add/AddProd.fxml");
        }
    }
    public class OpenEditScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/admin/edit/Edit.fxml");
        }
    }
    public class OpenEditUserScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/admin/edit/EditUser.fxml");
        }
    }

    public class OpenEditOrderScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/admin/edit/EditOrder.fxml");
        }
    }

    public class OpenManageDatabase implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/admin/AdminTools.fxml");
        }
    }
    public class OpenLoginScreen implements ScreenChange {
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/Login.fxml");
        }
    }
    public class OpenMyAccount implements ScreenChange{
        @Override
        public void screenChange(ActionEvent e) {
            activateScreen(e,"/zaar/customer/MyAccount.fxml");
        }
    }

    private void activateScreen(ActionEvent e, String url){
        try {
            Node node = (Node) e.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            Parent root = loader.load();

            Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
            stage.setScene(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
