package zaar.product;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.UpdateCaller;

import java.util.ArrayList;
import java.util.function.Predicate;

public class SelectManufacturerPopUp {

    public void popUp(Manufacturer manufacturer, Double x, Double y, UpdateCaller updateCaller){
        Database dB = Database.getInstance();
        ArrayList<Manufacturer> list = dB.getManufacturers();
        if(list!=null) {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Select Manufacturer");

            ObservableList<Manufacturer> list2 = FXCollections.observableList(list);
            FilteredList<Manufacturer> filteredData = new FilteredList<>(list2, s -> true);

            TextField filterTxtField = new TextField();
            filterTxtField.setPromptText("Contains");
            filterTxtField.textProperty().addListener((obs,ov,nv)->{
                filteredData.setPredicate(predicate(nv));
            });

            ListView<Manufacturer> listView = new ListView(filteredData);

            VBox vboxListView = new VBox(filterTxtField,listView);


            HBox hbox = new HBox();
            Button selectBtn = new Button("Accept selected");

            Insets insets = new Insets(5,5,5,5);
            VBox.setMargin(filterTxtField, insets);
            VBox.setMargin(listView, insets);
            HBox.setMargin(selectBtn, insets);

            hbox.getChildren().addAll(vboxListView, selectBtn);
            selectBtn.setOnAction(event -> {
                Manufacturer selectedIndices = listView.getSelectionModel().getSelectedItem();//getSelectedIndices();
                if(selectedIndices!=null) {
                    manufacturer.setId(selectedIndices.getId());
                    manufacturer.setName(selectedIndices.getName());
                    if(updateCaller!=null){
                        updateCaller.update();
                    }
                    stage.close();
                }
            });

            Scene scene = new Scene(hbox, -1, 120);
            stage.setX(x);
            stage.setY(y);
            stage.setScene(scene);
            stage.show();

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    selectBtn.requestFocus();
                }
            });
        }
    }
    private Predicate<Manufacturer> predicate(String filter){
        return Predicate->{
            if(filter == null || filter.length() == 0) {
                return true;
            }
            else {
                return Predicate.getName().toLowerCase().contains(filter.toLowerCase());//Search for manufacturer case insensitive
            }
        };
    }
}
