package zaar.admin.edit.filterPopUps;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zaar.admin.edit.PredicateFilters.product.EditFiltersProd;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ToolsSingleton;

import static zaar.admin.edit.filterPopUps.FilterPopUpCommons.TOOLTIP_FILTER_EMPTY;

/**
 * Filter for string
 */
public class PopUpFilterString implements FilterShowHide {
    private DataSingleton dS = DataSingleton.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private EditFiltersProd eF = EditFiltersProd.getInstance();
    private Stage stage = new Stage();;
    private TextField filterText = new TextField();;
    private Button filterBtn;
    private boolean isButtonGreen = true;;

    public PopUpFilterString(Button filterBtn){
        this.filterBtn = filterBtn;
        popUp();
    }

    /**
     * Textfield to use in predicate filter
     * @return
     */
    public TextField getFilterTextField(){
        return filterText;
    }

    private void popUp() {

        stage.setResizable(false);
        filterText.setPrefWidth(200);
        stage.initStyle(StageStyle.UNDECORATED);

        filterText.textProperty().addListener((oBV,oV,nV)->{//If filter text changes then
            if(nV.length()==0 && !isButtonGreen){
                dS.setFilterButtonGreen(filterBtn);//Change Filter button icon
                filterBtn.setTooltip(new Tooltip(TOOLTIP_FILTER_EMPTY));//Change filterbuttonTooltip
                isButtonGreen = true;
            }
            else if(isButtonGreen){
                dS.setFilterButtonYellow(filterBtn);//Change Filter button icon
                isButtonGreen = false;
            }
            if(nV.length()>0){
                filterBtn.setTooltip(new Tooltip(nV));//Change filterbuttonTooltip
            }
        });

        VBox.setMargin(filterText,new Insets(5,5,5,5));
        VBox vBox = new VBox(filterText);
        tS.setBorder(vBox);//Sets border to round

        stage.focusedProperty().addListener(new ChangeListener<Boolean>() {//Hide window if clicked outside
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!stage.isFocused()){
                    stage.hide();//Hide window
                }
            }
        });

        Scene scene = new Scene(vBox, -1, -1);
        stage.setScene(scene);
    }

    public void show(double x,double y){
        if(!stage.isShowing()){
            stage.setX(x);
            stage.setY(y);
            stage.show();
        }
    }

    public void hide(){
        if(!stage.isShowing()){
            stage.show();
        }
    }

    public boolean getIsShowing(){
        return stage.isShowing();
    }

    public void close(){
        stage.close();
    }

}