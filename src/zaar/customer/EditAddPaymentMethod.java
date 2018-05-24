package zaar.customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.SetPredicate;


public class EditAddPaymentMethod {
    Database dB  =Database.getInstance();
    private Label cardNumberLbl = new Label("CardNumber: ");
    private TextField cardNumberTextField = new TextField();
    private Button editAddBtn = new Button("Edit");
    private boolean isUpdate = false;
    private int id;
    Stage stage;
    SetPredicate updateCaller;

    public EditAddPaymentMethod(boolean isUpdate, int id){
        this.isUpdate  = isUpdate;
        this.id = id;
    }
    public void setUpdateCaller(SetPredicate setCaller){
        updateCaller = setCaller;
    }

    public void popUp() {
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
                updateCard();
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
            updateCaller.setPredicate();
            stage.close();
        }
    }

    private void updateCard(){

    }
}
