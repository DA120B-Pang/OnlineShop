package zaar.helperclasses;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import zaar.product.Menu.BuildMenu;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * Singleton class for metods used by various classes in application.
 */
public class ToolsSingleton {
    private static ToolsSingleton ourInstance = new ToolsSingleton();
    private BuildMenu buildMenu;

    public static ToolsSingleton getInstance() {
        return ourInstance;
    }

    private ToolsSingleton() {
        buildMenu = new BuildMenu();
    }

    /**
     * Empties VBox
     * @param vBox  VBox
     */
    public void removeVboxChildren(VBox vBox){
        if (vBox.getChildren().size() > 0) {
            vBox.getChildren().remove(0, vBox.getChildren().size());
        }
    }

    
    /**
     * Gets instance of menu builder class
     * @return BuildMenu
     */
    public BuildMenu getBuildMenu() {
        return buildMenu;
    }

    /**
     * Creates button that changes screen in top container
     * @param hbox          HBox top container
     * @param name          String button label
     * @param screenChange  ScreenChange object with method for changing screen
     * @return              Button for further handling
     */
    public Button setButtonTopHBox(HBox hbox, String name, ScreenSingleton.ScreenChange screenChange){
        Insets insets = new Insets(0,10,0,10);
        Button button = new Button(name);
        HBox.setMargin(button, insets);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                screenChange.screenChange(event);
            }
        });
        hbox.getChildren().add(button);
        return button;
    }

    /**
     * Creates image on button
     * @param button        Button base Any button
     * @param url           String Image file location
     * @param fitWidth      Double Width of image on button
     * @param fitHeight     Double Height of image on button
     */
    public void buttonSetImage(ButtonBase button, String url, Double fitWidth, Double fitHeight){
        try {
            //Adding image to button
            FileInputStream input = new FileInputStream(url);
            Image image = new Image(input);
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(fitWidth);
            imageView.setFitHeight(fitHeight);
            button.setGraphic(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Animation for success or fail when writing to database
     * @param pane      Pane container of button
     * @param button    Button
     * @param image     Image
     */
    public void getButtonAnimation(Pane pane, Button button, Image image){

        Double x = button.getLayoutX()+button.getWidth()/2;
        Double y = button.getLayoutY();

        ImageView imageview = new ImageView(image);
        imageview.setFitHeight(10);
        imageview.setFitWidth(10);
        imageview.setLayoutX(x);//point.getX());
        imageview.setLayoutY(y);//point.getY()-100);
        pane.getChildren().add(imageview);


        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), imageview);
        fadeTransition.setFromValue(1.0f);
        fadeTransition.setToValue(0.0f);
        fadeTransition.setCycleCount(1);
        fadeTransition.setAutoReverse(false);
        fadeTransition.setDelay(Duration.millis(2500));
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(2000), imageview);
        translateTransition.setFromX(0);
        translateTransition.setToX(0);
        translateTransition.setFromY(0);
        translateTransition.setToY(-50);
        translateTransition.setCycleCount(1);
        translateTransition.setAutoReverse(false);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), imageview);
        scaleTransition.setToX(7);
        scaleTransition.setToY(7);
        scaleTransition.setCycleCount(1);
        scaleTransition.setAutoReverse(false);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(
                fadeTransition,
                translateTransition,
                scaleTransition
        );
        parallelTransition.setCycleCount(1);
        parallelTransition.play();
    }


}
