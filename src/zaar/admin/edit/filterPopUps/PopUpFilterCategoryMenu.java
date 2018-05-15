package zaar.admin.edit.filterPopUps;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.product.EditFiltersProd;
import zaar.admin.edit.PredicateFilters.product.FilterInterfacesProd;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ToolsSingleton;
import zaar.product.Menu.*;
import zaar.product.Product;

import java.util.ArrayList;
import java.util.function.Predicate;

import static zaar.admin.edit.filterPopUps.FilterPopUpCommons.TOOLTIP_FILTER_EMPTY;

/**
 * Filter for Category
 */
public class PopUpFilterCategoryMenu implements FilterShowHide {
    private EditFiltersProd eF = EditFiltersProd.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private Stage stage;
    private FilterInterfacesProd.SetFilterProduct setFilter;
    private Button filterBtn;
    private MenuButton menuButton = new MenuButton("Choose");
    private SimpleIntegerProperty iD = new SimpleIntegerProperty(0);
    boolean listIsCollected = false;
    private ArrayList<ArrayList<MenuObject>> filtermodeList = null;
    private FilteredList<Product> filteredDataProduct = null;
    private FilteredList<Product> productFilteredList;
    private FilteredList<Category> filteredDataCategory = null;
    private FilteredList<Category> categoryFilteredList;
    private FilteredList<Menus> filteredDataMenus = null;
    private FilteredList<Menus> menusFilteredList;
    private Button resetFilterBtn = new Button("Reset filter");
    private FilterObjectType filterObjectType;

    public PopUpFilterCategoryMenu(Button filterBtn, FilteredList<?> filteredList, FilterObjectType filterObjectType ){
        this.filterBtn = filterBtn;
        this.filterObjectType = filterObjectType;
        if(filterObjectType == FilterObjectType.PRODUCT) {
            this.productFilteredList = (FilteredList<Product>)filteredList;
        }
        else if(filterObjectType == FilterObjectType.CATEGORY){
            this.categoryFilteredList = (FilteredList<Category>)filteredList;
        }
        else{
            this.menusFilteredList = (FilteredList<Menus>)filteredList;
        }
        popUp();
    }
    public SimpleIntegerProperty getId(){
        return iD;
    }

    public void popUp(){

        stage = new Stage();
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);

        Insets insets = new Insets(5,5,5,5);
        HBox.setMargin(menuButton, insets);
        HBox.setMargin(resetFilterBtn, insets);
        HBox hbox = new HBox(menuButton,resetFilterBtn);

        resetFilterBtn.setOnAction((Event)->{//Button resets filter to nothing
            dS.setFilterButtonGreen(filterBtn);//Change Filter button icon
            iD.set(0);
            filterBtn.setTooltip(new Tooltip(TOOLTIP_FILTER_EMPTY));
            hide();//Hide Window
        });

        tS.setBorder(hbox);//Sets border to round
        Scene scene = new Scene(hbox, -1, -1);
        stage.setScene(scene);

        stage.focusedProperty().addListener(new ChangeListener<Boolean>() {//Hide window if clicked outside
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!stage.isFocused()){
                    stage.hide();//Hide window
                }
            }
        });
    }


    /**
     * Builds the menu for selecting category
     */
    public void getMenu(){
        ArrayList<ArrayList<MenuObject>> tmp = new ArrayList<>(filtermodeList);

        if(filterObjectType==FilterObjectType.PRODUCT) {
            tS.getBuildMenu().getMenu(
                    menuButton,
                    new CatFilterMenuAction(),
                    new CatFilterMenuItemAction(),
                    filteredDataProduct,
                    FilterObjectType.PRODUCT,
                    BuildMenu.MenuBuildMode.FILTER,
                    tmp);
        }
        else if(filterObjectType==FilterObjectType.CATEGORY){
            tS.getBuildMenu().getMenu(
                    menuButton,
                    new MenuFilterMenuAction(),
                    new MenuFilterMenuItemAction(),
                    filteredDataCategory,
                    FilterObjectType.CATEGORY,
                    BuildMenu.MenuBuildMode.FILTER,
                    tmp);
        }
        else{
            tS.getBuildMenu().getMenu(
                    menuButton,
                    new MenuFilterMenuAction(),
                    new MenuFilterMenuItemAction(),
                    filteredDataMenus,
                    FilterObjectType.MENU,
                    BuildMenu.MenuBuildMode.CHOOSE_MENU,
                    tmp);
        }
    }

    @Override
    public void hide() {
        if(stage.isShowing()){
            stage.hide();
        }
    }

    @Override
    public void show(double x, double y) {
        if(!listIsCollected){
            collectList();
        }
        else{
            setPredicate();
            getMenu();
            stage.setX(x);
            stage.setY(y);
            stage.show();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean getIsShowing() {
        return stage.isShowing();
    }

    /**
     * Gets list from database
     */
    private void collectList(){
        filtermodeList = dB.getMenu();
        if(filtermodeList!=null) {
            if(filterObjectType==FilterObjectType.PRODUCT) {
                ObservableList<Product> listUnchanged = FXCollections.observableList(productFilteredList);
                filteredDataProduct = new FilteredList<>(listUnchanged, s -> true);
            }
            else if(filterObjectType==FilterObjectType.CATEGORY){
                ObservableList<Category> listUnchanged = FXCollections.observableList(categoryFilteredList);
                filteredDataCategory = new FilteredList<>(listUnchanged, s -> true);
            }
            else{
                ObservableList<Menus> listUnchanged = FXCollections.observableList(menusFilteredList);
                filteredDataMenus = new FilteredList<>(listUnchanged, s -> true);
            }
            listIsCollected = true;
        }
    }
    private void setPredicate(){
        if(filterObjectType==FilterObjectType.PRODUCT) {
            filteredDataProduct.setPredicate(predicateProduct());
        }
        else if(filterObjectType==FilterObjectType.CATEGORY){
            filteredDataCategory.setPredicate(predicateCategory());
        }
        else {
            filteredDataMenus.setPredicate(predicateMenus());
        }
    }

    private Predicate<Product> predicateProduct() {
        return Predicate -> {
            for (Product p : productFilteredList) {
                if (Predicate.getProductCategory() == p.getProductCategory()) {
                    return  true;
                }
            }
            return false;
        };
    }
    private Predicate<Category> predicateCategory() {
        return Predicate -> {
            for (Category c : categoryFilteredList) {
                if (Predicate.getParentMenuId() == c.getParentMenuId()) {
                    return  true;
                }
            }
            return false;
        };
    }
    private Predicate<Menus> predicateMenus() {
        return Predicate -> {
            for (Menus m : menusFilteredList) {
                if (Predicate.getParentMenuId() == m.getParentMenuId()) {
                    return  true;
                }
            }
            return false;
        };
    }

    private class CatFilterMenuAction implements MenuAction {

        @Override
        public void action(Menus menu) {
            //nothing
        }
    }

    private class CatFilterMenuItemAction implements MenuItemAction {

        @Override
        public void action(Category cat) {
            iD.set(cat.getCategoryId());
            filterBtn.setTooltip(new Tooltip(cat.getName()));
            dS.setFilterButtonYellow(filterBtn);
            hide();
        }
    }

    private class MenuFilterMenuAction implements MenuAction {

        @Override
        public void action(Menus menu) {
            iD.set(menu.getMenuId());
            filterBtn.setTooltip(new Tooltip(menu.getName()));
            dS.setFilterButtonYellow(filterBtn);
            hide();
        }
    }

    private class MenuFilterMenuItemAction implements MenuItemAction {

        @Override
        public void action(Category cat) {
            //Nothing
        }
    }
}
