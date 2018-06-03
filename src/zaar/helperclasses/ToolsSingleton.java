package zaar.helperclasses;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import zaar.product.Cart;
import zaar.product.Menu.BuildMenu;
import javax.mail.*;
import javax.mail.internet.*;
import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

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

    public File openFileChooser(FileChooser fileChooser, Event event, TextField pictureTxtFld) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();//Gets stage for positioning poup
        InputStream stream = null;
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            pictureTxtFld.setText(file.getAbsolutePath());
        }
        return file;
    }
    /**
     * Sets filechooser to png pictures
     * @param fileChooser
     */
    public void setFileChooser(FileChooser fileChooser){
        fileChooser.setTitle("Select png picture");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
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
        Insets insets = new Insets(0,5,0,5);
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
        try(InputStream input = this.getClass().getResourceAsStream(url);) {
            //Adding image to button
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

    /**
     * Sets round borders to Region
     * @param t
     * @param <T>
     */
    public <T extends Region> void setRoundBorder(T t){
        t.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, new CornerRadii(5), BorderWidths.DEFAULT)));
        t.setBackground(new Background(new BackgroundFill(null,new CornerRadii(5),null)));
    }
    /**
     * Sets borders to Region
     * @param t
     * @param <T>
     */
    public <T extends Region> void setBorder(T t){
        t.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT)));
        t.setBackground(new Background(new BackgroundFill(null,new CornerRadii(5),null)));
    }
    /**
     * Sets darkBackground to Region
     * @param t
     * @param <T>
     */
    public <T extends Region> void setBGDark(T t){
        t.setStyle("-fx-background-color:  #112731;-fx-text-fill: #FFFFFF;");
    }

    public <T extends Region> void setMenuDark(T t){
        t.setStyle("-fx-background-color:  #d3cdcd;");
    }

    /**
     * Produces a VBox with with titleLabel parameter on top and buttons below.
     * @param titleLabel Label
     * @param buttons   Button ... (multiple) all that shall be in menu
     * @return
     */
    public VBox makeButtonMenuModel(String titleLabel,Button...buttons){
        VBox vBox = new VBox();
        setRoundBorder(vBox);

        Label titleLbl = new Label(titleLabel);
        titleLbl.setMaxHeight(5000);
        titleLbl.setMaxWidth(5000);
        titleLbl.paddingProperty().setValue(new Insets(0,5,0,5));
        setBGDark(titleLbl);

        HBox hBox = new HBox();//HBox is for producing a black line between label and buttons
        hBox.setPrefHeight(2);
        hBox.setPrefWidth(-1);
        hBox.setBackground(new Background(new BackgroundFill(Color.BLACK,null,null)));
        vBox.getChildren().addAll(titleLbl,hBox);

        for(Button b: buttons){
            b.setMaxWidth(5000);
            VBox.setVgrow(b,Priority.ALWAYS);
            Insets insets = new Insets(5,5,5,5);
            VBox.setMargin(b,insets);
            vBox.getChildren().add(b);
        }
        return vBox;
    }

    public void sendRecipe(String reciverEmail){
        try {
            String from = "shopit.hkr@gmail.com";
            String pass = "1234qwerty%";
            Properties properties = System.getProperties();
            String host = "smtp.gmail.com";
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.user", from);
            properties.put("mail.smtp.password", pass);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            InternetAddress reciever = new InternetAddress(reciverEmail);

            message.addRecipient(Message.RecipientType.TO, reciever);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            message.setSubject(String.format("Recipe from Shop-IT %s",dateFormat.format(date)));

            // create and fill the first message part
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText("Recipe is attached");

            // create the second message part
            MimeBodyPart mbp2 = new MimeBodyPart();

            // attach the file to the message
            mbp2.attachFile("tmp.png");

            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);

            message.setContent(mp);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                Path path = Paths.get("tmp.png");
                Files.delete(path);
            }
            catch (Exception ex){

            }
        }

    }

    public void sendPassword(String reciverEmail, String password){
        try {
            String from = "shopit.hkr@gmail.com";
            String pass = "1234qwerty%";
            Properties properties = System.getProperties();
            String host = "smtp.gmail.com";
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.user", from);
            properties.put("mail.smtp.password", pass);
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));
            InternetAddress reciever = new InternetAddress(reciverEmail);

            message.addRecipient(Message.RecipientType.TO, reciever);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            message.setSubject(String.format("Password from Shop-IT %s", dateFormat.format(date)));
            message.setText(String.format("Your password is: %s",password));


            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {

        }
    }

}
