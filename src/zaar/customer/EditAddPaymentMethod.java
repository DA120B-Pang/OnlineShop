package zaar.customer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.UpdateCaller;


public class EditAddPaymentMethod {
    private Database dB  =Database.getInstance();
    private Label cardNumberLbl = new Label("CardNumber: ");
    private TextField cardNumberTextField = new TextField();
    private Button editAddBtn = new Button("Edit");
    private boolean isUpdate = false;
    private int id;
    private Stage stage;
    private UpdateCaller updateCaller;

    public EditAddPaymentMethod(boolean isUpdate, int id){
        this.isUpdate  = isUpdate;
        this.id = id;
    }
    public void setUpdateCaller(UpdateCaller setCaller){
        updateCaller = setCaller;
    }

    public void popUp() {
        cardNumberTextField.textProperty().addListener((oB,oV,nV)->{
            if(!nV.isEmpty()) {
                try {
                    Integer.parseInt(nV);
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        cardNumberTextField.setText(oV);
                    });
                }
            }
        });

        if(!isUpdate) {
            cardNumberTextField.setPromptText("Card number");
            editAddBtn.setText("Add");
            editAddBtn.setOnAction(e->{
                if(!cardNumberTextField.getText().isEmpty()) {
                    addCard();
                }
            });
        }
        else {
            editAddBtn.setOnAction(e->{
               if(!cardNumberTextField.getText().isEmpty()) {
                    updateCard();
                }
            });
        }
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Add");

        cardNumberTextField.textProperty().addListener((oB, oV, nV) -> {
            if(nV.length()>45){
                Platform.runLater(()->{
                    cardNumberTextField.setText(oV);
                });
            }
        });

        Insets insets = new Insets(10,10,10,10);
        HBox.setMargin(cardNumberLbl, insets);
        HBox.setMargin(cardNumberTextField,insets);
        HBox.setMargin(editAddBtn,insets);

        HBox hBox = new HBox(cardNumberLbl,cardNumberTextField, editAddBtn);

        Scene scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();

    }

    private void addCard(){

        if(dB.insertPaymentMethod(id, cardNumberTextField.getText())){
            updateCaller.update();
            stage.close();
        }
    }

    private void updateCard(){
        if(dB.updatePaymentMethod(id, cardNumberTextField.getText())){
            updateCaller.update();
            stage.close();
        }
    }
}
