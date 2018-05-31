package zaar.product.Menu;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import zaar.Database.BooleanMethodIntString;
import zaar.Database.Database;
import zaar.UpdateCaller;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ScreenSingleton;
import zaar.helperclasses.ToolsSingleton;

public class AddMenuCategory {

    private TextField menuIdTxtField = new TextField();
    private Boolean retVal;
    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private MenuButton menuBtn = new MenuButton("Select parent menu");//Button for selecting parent menu
    private TextField nameTxtField = new TextField();
    private Label parentMenuIdLbl = new Label("Parent menu ID");
    private Label nameLbl = new Label("Name");
    private Button addBtn = new Button();
    private GridPane gridPane = new GridPane();
    private int parentId;
    private int iD;
    private boolean menuChanged;//Tells if menu was changed when closing window
    private Category cat;
    private Menus menu;
    private boolean clearText = true;
    private boolean useMenuActionForMenu = false;
    private boolean useRootBtn = false;
    private Button rootBtn;
    private MenuAction menuAction;

    /**
     * Sets parameters when editing an existing Category
     *
     * @param cat
     * @param parentName
     */
    public void setParametersCategory(Category cat, String parentName) {
        this.iD = cat.getCategoryId();
        this.parentId = cat.getParentMenuId();
        this.menuIdTxtField.setText(parentName);
        this.nameTxtField.setText(cat.getName());
        this.cat = cat;
        clearText = false;
    }

    /**
     * Sets parameters when editing an existing Menu
     *
     * @param menu
     * @param parentName
     */
    public void setParametersMenu(Menus menu, String parentName) {
        this.iD = menu.getMenuId();
        this.parentId = menu.getParentMenuId();
        this.menuIdTxtField.setText(parentName);
        this.nameTxtField.setText(menu.getName());
        this.menu = menu;
        useMenuActionForMenu = true;
        useRootBtn = true;
    }

    public void popUp(String titleAddButton, BooleanMethodIntString dbQuery, boolean idNullOk, String idPrompt, Double x, Double y, UpdateCaller updateCaller) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(titleAddButton);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {//Inform main window if Change to menu was made
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                if (updateCaller != null && menuChanged) {
                    updateCaller.update();
                }
                stage.close();
            }
        });
        if (useMenuActionForMenu) {
            menuAction = new AddMenuActionForMenu();
        } else {
            menuAction = new AddMenuAction();
        }
        menuIdTxtField.setEditable(false);
        menuIdTxtField.setPromptText(idPrompt);

        menuBtn.setOnMouseClicked((Event) -> {//If database error occured then Get menu on click
            if (menuBtn.getItems().size() == 0) {
                tS.getBuildMenu().getMenu(menuBtn, menuAction, new AddMenuItemAction(), null, null, BuildMenu.MenuBuildMode.CHOOSE_MENU, null);
            }
        });

        if (useRootBtn) {//For setting root as parent for menu in edit screen
            rootBtn = new Button("Set root as parent");
            rootBtn.setOnAction((Event) -> {
                menuIdTxtField.setText("root");
                parentId = 0;
            });
        }

        tS.getBuildMenu().getMenu(menuBtn, menuAction, new AddMenuItemAction(), null, null, BuildMenu.MenuBuildMode.CHOOSE_MENU, null);//Get menu

        nameTxtField.setPromptText("Name");
        nameTxtField.textProperty().addListener(new ChangeListener<String>() {//Check length(limit in database)
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.length() > 45) {
                    nameTxtField.setText(oldValue);
                }
            }
        });
        setGridPane();
        AnchorPane anchorPane = new AnchorPane(gridPane);
        anchorPane.prefHeight(300);
        anchorPane.prefWidth(100);


        addBtn.setText(titleAddButton);

        addBtn.setOnAction(event -> {//Write to database
            if (nameTxtField.getText().equalsIgnoreCase("") || parentId == 0 && !idNullOk) {//Check so that not nullable field are ok (Menus can have parentId = 0 -> root. Categories must have parentId>0
                retVal = false;
            } else {
                try {
                    retVal = dbQuery.method(iD, parentId, nameTxtField.getText());//Write to database
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (retVal) {//If writing to database was successful
                tS.getButtonAnimation(anchorPane, addBtn, dS.getOkImgView().getImage());//Set animation for success

                tS.getBuildMenu().getMenu(menuBtn, menuAction, new AddMenuItemAction(), null, null, BuildMenu.MenuBuildMode.CHOOSE_MENU, null);//Rebuild menu to add new
                if (cat != null) {
                    cat.setParentMenuId(parentId);
                    cat.setName(nameTxtField.getText());
                } else if (menu != null) {
                    menu.setParentMenuId(parentId);
                    menu.setName(nameTxtField.getText());
                }
                menuChanged = true;//Set that menu has been changed(for when exiting window to tell main window about change)
                if (clearText) {
                    nameTxtField.setText("");//remove info from fields
                    menuIdTxtField.setText("");//remove info from fields
                }
            } else {
                tS.getButtonAnimation(anchorPane, addBtn, dS.getNotOkImgView().getImage());//Set animation for fail
            }
        });
        Scene scene = new Scene(anchorPane);
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

    private void setGridPane() {//Setting layout on popUp
        Insets insets = new Insets(5, 5, 5, 5);
        gridPane.setFillWidth(addBtn, true);
        gridPane.setFillWidth(menuBtn, true);

        GridPane.setMargin(addBtn, insets);
        GridPane.setMargin(parentMenuIdLbl, insets);
        GridPane.setMargin(nameLbl, insets);
        GridPane.setMargin(parentMenuIdLbl, insets);
        GridPane.setMargin(menuIdTxtField, insets);
        GridPane.setMargin(nameTxtField, insets);
        GridPane.setMargin(menuBtn, insets);

        gridPane.add(parentMenuIdLbl, 0, 0, 1, 1);
        gridPane.add(nameLbl, 0, 1, 1, 1);
        gridPane.add(menuIdTxtField, 1, 0, 1, 1);
        gridPane.add(nameTxtField, 1, 1, 1, 1);
        gridPane.add(menuBtn, 2, 0, 1, 1);
        gridPane.add(addBtn, 2, 2, 1, 1);

        if (useRootBtn) {
            GridPane.setMargin(rootBtn, insets);
            gridPane.add(rootBtn, 2, 1, 1, 1);
        }
    }


    private class AddMenuAction implements MenuAction{

        @Override
        public void action(Menus menu) {
            menuIdTxtField.setText(menu.getName());
            parentId = menu.getMenuId();
        }
    }
    private class AddMenuActionForMenu implements MenuAction{

        @Override
        public void action(Menus menu) {
            if(menu.getMenuId()!=iD) {
                menuIdTxtField.setText(menu.getName());
                parentId = menu.getMenuId();
            }
        }
    }
    private class AddMenuItemAction implements MenuItemAction{

        @Override
        public void action(Category cat) {
            //Nothing
        }
    }
}
