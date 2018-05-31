package zaar.customer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.helperclasses.ToolsSingleton;


public class ForgotPassword {
    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private boolean error;

    public void popUp() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Forgot password");

        TextField enterLoginNameTxtFiled = new TextField();
        enterLoginNameTxtFiled.setPromptText("Enter login name");
        Button okBtn = new Button("Ok");

        enterLoginNameTxtFiled.textProperty().addListener((oB,oV,nV)->{//Max 45 char in db
            if(error){
                enterLoginNameTxtFiled.setStyle("-fx-text-fill: black;");
                error = false;
            }
            if(nV.length()>45){
                Platform.runLater(()->{
                    enterLoginNameTxtFiled.setText(oV);
                });
            }
        });

        okBtn.setOnAction(E->{
            if(!enterLoginNameTxtFiled.getText().isEmpty()) {
                String email = dB.getStringFromTable(0, enterLoginNameTxtFiled.getText(), Database.GetString.GET_EMAIL);
                String password = dB.getStringFromTable(0, enterLoginNameTxtFiled.getText(), Database.GetString.GET_PASSWORD);
                if (email != null && password != null) {
                    tS.sendPassword(email, password);
                    enterLoginNameTxtFiled.setText("Check your email");
                }
                else {
                    enterLoginNameTxtFiled.setText("Not a valid login name.");
                    enterLoginNameTxtFiled.setStyle("-fx-text-fill: red;");
                    error = true;
                }
            }
        });

        Insets insets = new Insets(10,10,10,10);
        HBox.setMargin(enterLoginNameTxtFiled, insets);
        HBox.setMargin(okBtn,insets);

        HBox hBox = new HBox(enterLoginNameTxtFiled,okBtn);

        Platform.runLater(()->{
            okBtn.requestFocus();
        });
        Scene scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();

    }
}
