package zaar.customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.helperclasses.DataSingleton;



public class EditPaymentMethods implements UpdateCaller {
    private String title;
    private final ComboBox<PaymentMethods> choosePaymentComBox =  new ComboBox<>();
    private Database dB = Database.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private Button editBtn = new Button("Edit");
    private Button deleteBtn = new Button("Delete");
    private Button addBtn = new Button("Add");


    public EditPaymentMethods(String title) {
        this.title = title;
    }

    public void popUp() {

        choosePaymentComBox.setOnMouseClicked(E-> {
            if(choosePaymentComBox.getItems().size()<1){
                updateComboBox();
            }
        });
        addBtn.setOnAction(E->{
            EditAddPaymentMethod editAddPaymentMethod = new EditAddPaymentMethod(false, dS.getLoggedInUser().getCustomerID());
            editAddPaymentMethod.setUpdateCaller(this);
            editAddPaymentMethod.popUp();
        });

        editBtn.setOnAction(E->{
            PaymentMethods selected = choosePaymentComBox.getSelectionModel().getSelectedItem();
            if(selected!=null) {
                EditAddPaymentMethod editAddPaymentMethod = new EditAddPaymentMethod(true, selected.getPaymentId());
                editAddPaymentMethod.setUpdateCaller(this);
                editAddPaymentMethod.popUp();
            }
        });

        deleteBtn.setOnAction(E->{
            PaymentMethods selected = choosePaymentComBox.getSelectionModel().getSelectedItem();
            if(selected!=null){
                dB.deleteRecord(Database.DeleteRecord.PAYMENT_METHOD, selected.getPaymentId());
                updateComboBox();
            }
        });
        choosePaymentComBox.setPrefWidth(150);
        choosePaymentComBox.setMaxWidth(150);
        Insets insets = new Insets(5,5,5,5);
        HBox.setMargin(choosePaymentComBox, insets);
        HBox.setMargin(editBtn, insets);
        HBox.setMargin(deleteBtn, insets);
        HBox.setMargin(addBtn, insets);
        HBox hBox = new HBox(choosePaymentComBox,addBtn,editBtn, deleteBtn);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        Scene scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();

    }
    private void updateComboBox(){
        if(choosePaymentComBox.getItems().size()>0){
            choosePaymentComBox.getItems().remove(0,choosePaymentComBox.getItems().size());
        }
        choosePaymentComBox.getItems().addAll(getPaymentMethods(dS.getLoggedInUser().getCustomerID()));
        choosePaymentComBox.getSelectionModel().selectFirst();
    }

    private ObservableList<PaymentMethods> getPaymentMethods(int id){
        return FXCollections.observableArrayList(dB.getPayment(id, true));
    }

    @Override
    public void update() {
        updateComboBox();
    }
}
