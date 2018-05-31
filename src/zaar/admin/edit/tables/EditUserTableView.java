package zaar.admin.edit.tables;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zaar.admin.edit.PredicateFilters.User.EditFiltersUser;
import zaar.admin.edit.filterPopUps.*;
import zaar.customer.User;
import zaar.helperclasses.DataSingleton;

public class EditUserTableView {
    private DataSingleton dS = DataSingleton.getInstance();
    private EditFiltersUser eFU = EditFiltersUser.getInstance();
    private EditFiltersUser.EditFilterObject editUserFilterObject;
    private PopUpFilterString popUpFilterFirstName;
    private PopUpFilterString popUpFilterLastName;
    private PopUpFilterString popUpFilterLoginName;
    private final String TOOLTIP_FILTER_EMPTY = "Click to set filter";


    public TableView<User> getUserTableView(FilteredList<User> list){

        editUserFilterObject = eFU.new EditFilterObject(list);//Filter for list in listview sets Predicate

        //************************ first name column*****************************
        TableColumn<User,String> firstNameColumn  = new TableColumn<>("First name");
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        Button firstNameColumnFilterBtn = createColumnBtn();
        firstNameColumn.setGraphic(firstNameColumnFilterBtn);

        firstNameColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterFirstName==null) {
                popUpFilterFirstName = new PopUpFilterString(firstNameColumnFilterBtn);//Creates popup to show filter
                EditFiltersUser.UserFirstName userFirstName = eFU.new UserFirstName();//Creates filter for Product
                userFirstName.setFilter(editUserFilterObject);//Register master Filter control
                userFirstName.setName(popUpFilterFirstName.getFilterTextField());//Set object to listen for change
            }
            showHideFilter(Event,firstNameColumnFilterBtn,popUpFilterFirstName,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************ lastname column*****************************
        TableColumn<User,String> lastNameColumn  = new TableColumn<>("Last name");
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        Button lastNameColumnFilterBtn = createColumnBtn();
        lastNameColumn.setGraphic(lastNameColumnFilterBtn);

        lastNameColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterLastName==null) {
                popUpFilterLastName = new PopUpFilterString(lastNameColumnFilterBtn);//Creates popup to show filter
                EditFiltersUser.UserLastName userLastName = eFU.new UserLastName();//Creates filter for Product
                userLastName.setFilter(editUserFilterObject);//Register master Filter control
                userLastName.setName(popUpFilterLastName.getFilterTextField());//Set object to listen for change
            }
            showHideFilter(Event,lastNameColumnFilterBtn,popUpFilterLastName,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************ email column*****************************
        TableColumn<User,String> emailColumn  = new TableColumn<>("Email");
        emailColumn.setMinWidth(100);
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        //************************ login name*****************************
        TableColumn<User,String> loginNameColumn  = new TableColumn<>("Login name");
        loginNameColumn.setMinWidth(100);
        loginNameColumn.setCellValueFactory(new PropertyValueFactory<>("loginName"));
        Button loginNameColumnFilterBtn = createColumnBtn();
        loginNameColumn.setGraphic(loginNameColumnFilterBtn);

        loginNameColumnFilterBtn.setOnAction((Event)->{//Set Event for button in top of column
            if(popUpFilterLoginName==null) {
                popUpFilterLoginName = new PopUpFilterString(loginNameColumnFilterBtn);//Creates popup to show filter
                EditFiltersUser.LoginName loginName = eFU.new LoginName();//Creates filter for Product
                loginName.setFilter(editUserFilterObject);//Register master Filter control
                loginName.setName(popUpFilterLoginName.getFilterTextField());//Set object to listen for change
            }
            showHideFilter(Event,loginNameColumnFilterBtn,popUpFilterLoginName,0,-10);//Shows filter popUp att buttons coordinate plus offset
        });

        //************************ password*****************************
        TableColumn<User,String> passwordColumn  = new TableColumn<>("Password");
        passwordColumn.setMinWidth(100);
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        //************************ phone*****************************
        TableColumn<User,String> phoneColumn  = new TableColumn<>("Phone number");
        phoneColumn.setMinWidth(100);
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        //************************ Role*****************************
        TableColumn<User,Integer> roleColumn  = new TableColumn<>("Role");
        roleColumn.setMinWidth(100);
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        //************************ Role*****************************
        TableColumn<User,Integer> userIdColumn  = new TableColumn<>("Uid");
        userIdColumn.setMinWidth(100);
        userIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));




        TableView<User> tableView = new TableView();//Create table
        tableView.setPrefHeight(-1);
        tableView.setPrefWidth(-1);
        VBox.setVgrow(tableView, Priority.ALWAYS);

        tableView.getColumns().addAll(
                firstNameColumn,
                lastNameColumn,
                emailColumn,
                loginNameColumn,
                passwordColumn,
                phoneColumn,
                roleColumn,
                userIdColumn);

        SortedList<User> sortedData = new SortedList<>(list);//Activates sorting ability in table
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
        return tableView;
    }

    public EditFiltersUser.EditFilterObject getMasterFilter(){
        return editUserFilterObject;
    }
    private Button createColumnBtn(){
        Button button = new Button();
        dS.setFilterButtonGreen(button);
        button.setTooltip(new Tooltip(TOOLTIP_FILTER_EMPTY));
        return button;
    }

    private void showHideFilter(Event e, Button button, FilterShowHide fSH, double offsetX, double offsetY){
        Node node = (Node)e.getSource();
        Stage stage = (Stage)node.getScene().getWindow();//Gets stage for positioning poup
        if(fSH.getIsShowing()) {
            fSH.hide();
        }
        else{
            fSH.show(
                    stage.getX()+ button.getLocalToSceneTransform().getTx()+offsetX,
                    stage.getY()+button.getLocalToSceneTransform().getTy()+offsetY);
        }
    }
}
