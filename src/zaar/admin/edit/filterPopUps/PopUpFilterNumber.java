package zaar.admin.edit.filterPopUps;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zaar.admin.edit.PredicateFilters.product.EditFiltersProd;
import zaar.admin.edit.PredicateFilters.product.FilterInterfacesProd;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ToolsSingleton;

import static zaar.admin.edit.filterPopUps.FilterPopUpCommons.TOOLTIP_FILTER_EMPTY;

/**
 * Filter for Category
 */
public class PopUpFilterNumber<T extends Number> implements FilterShowHide {
    private FilterInterfacesProd.SetFilterProduct setFilter;
    private DataSingleton dS = DataSingleton.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private EditFiltersProd eF = EditFiltersProd.getInstance();
    private Stage stage;
    private Button filterBtn;
    private Button setFilterBtn = new Button("Set filter");
    private Button resetFilterBtn = new Button("Reset filter");
    private TextField numberInputTxtField = new TextField();
    private boolean addRemoveFilter;
    private ComboBox<CompareOperator> chooseOpComboBox = new ComboBox<>();
    private CompareOperator operatorSelection;
    private T number;
    private EditFiltersProd.CompareNumber<T> prodNumber;
    private Property<T> test;


    public PopUpFilterNumber(FilterInterfacesProd.SetFilterProduct setFilter, Button filterBtn, T number ){
        this.setFilter = setFilter;
        this.filterBtn = filterBtn;
        this.number = number;
        popUp();
    }

    public void popUp(){
        prodNumber = eF.new CompareNumber();
        prodNumber.setFilter(setFilter);

        chooseOpComboBox.setItems(FXCollections.observableArrayList(CompareOperator.values()));
        chooseOpComboBox.setPromptText("Select operator");
        stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        Insets insets = new Insets(5,5,5,5);
        VBox.setMargin(numberInputTxtField, insets);
        VBox.setMargin(chooseOpComboBox, insets);
        HBox.setMargin(setFilterBtn, insets);
        HBox.setMargin(resetFilterBtn, insets);
        VBox vbox = new VBox(numberInputTxtField,chooseOpComboBox,new HBox(setFilterBtn,resetFilterBtn));
        numberInputTxtField.setPromptText("Enter value");
        numberInputTxtField.textProperty().addListener((oBV,oV,nV)->{
            try {
                if (!numberInputTxtField.getText().isEmpty()) {
                    if(number instanceof Integer){//Check if Integer
                        number = (T)Integer.valueOf(nV);
                    }
                    else{ //then it is double(Price)
                        number = (T)Double.valueOf(nV);
                    }
                }
            }
            catch (Exception e){
                numberInputTxtField.setText(oV);
            }
        });

        resetFilterBtn.setOnAction((Event)->{
            dS.setFilterButtonGreen(filterBtn);
            filterBtn.setTooltip(new Tooltip(TOOLTIP_FILTER_EMPTY));
            addToFilter(false);
            hide();
        });

        setFilterBtn.setOnAction((Event)->{
            operatorSelection = chooseOpComboBox.getSelectionModel().getSelectedItem();
            if(operatorSelection!=null && !numberInputTxtField.getText().isEmpty()) {
                filterBtn.setTooltip(new Tooltip(operatorSelection.getValue()+" "+number));
                dS.setFilterButtonYellow(filterBtn);
                prodNumber.setOperatorSelection(operatorSelection,number);
                addToFilter(true);
                hide();
            }
        });
        tS.setBorder(vbox);
        Scene scene = new Scene(vbox, -1, -1);
        stage.setScene(scene);

        stage.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!stage.isFocused()){
                    stage.hide();
                }
            }
        });

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setFilterBtn.requestFocus();
            }
        });
    }

    private void addToFilter(boolean b){
        addRemoveFilter = b;
        prodNumber.addRemoveFilter(addRemoveFilter);

    }

    @Override
    public void hide() {
        if(stage.isShowing()){
            stage.hide();
        }
    }

    @Override
    public void show(double x, double y) {
        if(addRemoveFilter && operatorSelection!=null){
            chooseOpComboBox.getSelectionModel().select(operatorSelection);
        }
        stage.setX(x);
        stage.setY(y);
        stage.show();
    }

    @Override
    public void close() {
        stage.close();
    }

    @Override
    public boolean getIsShowing() {
        return stage.isShowing();
    }
}
