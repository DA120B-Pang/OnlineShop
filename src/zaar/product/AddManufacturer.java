package zaar.product;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.BooleanMethodString;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ToolsSingleton;

public class AddManufacturer {

    private boolean retVal;
    private Manufacturer manufacturer = new Manufacturer();
    private TextField nameTxtField = new TextField();
    private UpdateCaller updateCaller = null;

    public void setId(Manufacturer manufacturer){
        this.manufacturer = manufacturer;
        nameTxtField.setText(manufacturer.getName());
    }
    public void popUp(String titleAddButton, BooleanMethodString dbQuery, Double x, Double y, UpdateCaller updateCaller){
        this.updateCaller = updateCaller;
        Database dB = Database.getInstance();
        ToolsSingleton tS = ToolsSingleton.getInstance();
        DataSingleton dS = DataSingleton.getInstance();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(titleAddButton);

        HBox hbox = new HBox();

        nameTxtField.setPromptText("Name");
        Insets insets = new Insets(75,20,20,20);
        HBox.setMargin(nameTxtField, insets);
        nameTxtField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()>45){
                    nameTxtField.setText(oldValue);
                }
            }
        });

        Button addBtn = new Button(titleAddButton);

        HBox.setMargin(addBtn, insets);

        hbox.getChildren().addAll(nameTxtField, addBtn);
        AnchorPane anchorPane = new AnchorPane(hbox);
        anchorPane.prefHeight(300);
        anchorPane.prefWidth(100);



        addBtn.setOnAction(event -> {
            if(nameTxtField.getText().equalsIgnoreCase("")) {
                retVal = false;
            }
            else{
                retVal = dbQuery.method(manufacturer.getId(), nameTxtField.getText());//dB.addManufacturer(nameTxtField.getText());
            }
            if(retVal){
                tS.getButtonAnimation(anchorPane,addBtn,dS.getOkImgView().getImage());
                manufacturer.setName(nameTxtField.getText());
                nameTxtField.setText("");
                if(updateCaller!=null){
                    updateCaller.update();
                }
//                    dS.toggleManChanged();
            }
            else{
                tS.getButtonAnimation(anchorPane,addBtn,dS.getNotOkImgView().getImage());
            }
        });

        Scene scene = new Scene(anchorPane, -1, 120);
        stage.setX(x);
        stage.setY(y);
        stage.setScene(scene);
        stage.show();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                addBtn.requestFocus();
            }
        });
    }
}
