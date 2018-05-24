package zaar.customer;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zaar.Database.Database;
import zaar.admin.edit.PredicateFilters.SetPredicate;
import zaar.helperclasses.DataSingleton;
import zaar.helperclasses.ToolsSingleton;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class EditAddUser {

    private enum Role {
        ADMIN(1),
        USER(2);

        private int value;
        Role(int value) {
            this.value = value;
        }

        public int getValue(){
            return value;
        }

    }
    private Label roleLbl = new Label("Role");
    private Label firstNameLbl = new Label("First name");
    private Label lastNameLbl= new Label("Last name");
    private Label emailLbl= new Label("Email");
    private Label loginNameLbl = new Label("Login name");
    private Label passwordLbl = new Label("Password");
    private Label phoneNumberLbl = new Label("Phone");
    private Label addressLbl = new Label("Address");
    private Label cityLbl= new Label("City");
    private Label countryLbl = new Label("Country");

    private ComboBox<Role> roleComboBox = new ComboBox<>();

    private int id = 0;
    private TextField firstNameTxtField = new TextField();
    private TextField lastNameTxtField = new TextField();
    private TextField emailTxtField = new TextField();
    private TextField loginNameTxtField = new TextField();
    private TextField passwordTxtField = new TextField();
    private TextField phoneNumberTxtField = new TextField();
    private TextField addressTxtField = new TextField();
    private TextField cityTxtField = new TextField();
    private TextField countryTxtField = new TextField();

    private Button writeBtn = new Button("Add");

    private GridPane gridPane = new GridPane();

    private boolean isUpdate = false;
    private String title;
    private SetPredicate setPredicate;
    private Database dB = Database.getInstance();
    private ToolsSingleton tS = ToolsSingleton.getInstance();
    private DataSingleton dS = DataSingleton.getInstance();
    private User user;
    Pane pane;

    public EditAddUser(String title){
        this.title = title;
    }


    public void setUserData(User user, SetPredicate setPredicate) {
        this.setPredicate = setPredicate;
        this.user = user;
        id = user.getCustomerID();
        Role tmp;
        if(user.getRole()==1){
            tmp = Role.ADMIN;
        }
        else{
             tmp = Role.USER;   
        }
        this.roleComboBox.getSelectionModel().select(tmp);
        this.firstNameTxtField.setText(user.getFirstName());
        this.lastNameTxtField.setText(user.getLastName());
        this.emailTxtField.setText(user.getEmail());
        this.loginNameTxtField.setText(user.getLoginName());
        this.passwordTxtField.setText(user.getPassword());
        this.phoneNumberTxtField.setText(user.getPhoneNumber());
        this.addressTxtField.setText(user.getAddress());
        this.cityTxtField.setText(user.getCity());
        this.countryTxtField.setText(user.getCountry());
        writeBtn.setText("Update");
        isUpdate = true;
    }

    public void popUp() {
        if(!isUpdate) {
            roleComboBox.setVisible(false);
            roleLbl.setVisible(false);
            writeBtn.setText("Add");
            writeBtn.setOnAction(e->{
                addUser();
            });
        }
        else {
            roleComboBox.setItems(FXCollections.observableArrayList(Role.values()));
            writeBtn.setOnAction(e->{
                updateUser();
            });
        }
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(title);

        ArrayList<TextField> listTxtFields = new ArrayList<>(Arrays.asList(
                                                            firstNameTxtField,
                                                            lastNameTxtField,
                                                            emailTxtField,
                                                            loginNameTxtField,
                                                            passwordTxtField,
                                                            phoneNumberTxtField,
                                                            addressTxtField,
                                                            cityTxtField,
                                                            countryTxtField));

        for (TextField t:listTxtFields) {//Limit input length
            setListener45(t);
        }

        setGridPane();
        pane= new Pane(gridPane); 
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

    }

    private void setGridPane(){//Setting layout on popUp

        Insets insets = new Insets(5,5,5,5);
        ArrayList<Node> listNodes = new ArrayList<>(Arrays.asList(firstNameTxtField,
                                                                    lastNameTxtField,
                                                                    emailTxtField,
                                                                    loginNameTxtField,
                                                                    passwordTxtField,
                                                                    phoneNumberTxtField,
                                                                    addressTxtField,
                                                                    cityTxtField,
                                                                    countryTxtField,
                                                                    roleLbl,
                                                                    firstNameLbl,
                                                                    lastNameLbl,
                                                                    emailLbl,
                                                                    loginNameLbl,
                                                                    phoneNumberLbl,
                                                                    passwordLbl,
                                                                    addressLbl,
                                                                    cityLbl,
                                                                    countryLbl,
                                                                    roleComboBox,
                                                                    writeBtn));
        for (Node n:listNodes) {
            gridPane.setFillWidth(n, true);
            GridPane.setMargin(n, insets);
        }

        //Row 1
        gridPane.add(roleLbl,0,0,1,1);
        gridPane.add(roleComboBox,1,0,1,1);
        //Row 2
        gridPane.add(firstNameLbl,0,1,1,1);
        gridPane.add(firstNameTxtField,1,1,1,1);
        //Row 3
        gridPane.add(lastNameLbl,0,2,1,1);
        gridPane.add(lastNameTxtField,1,2,1,1);
        //Row 4
        gridPane.add(emailLbl,0,3,1,1);
        gridPane.add(emailTxtField,1,3,1,1);
        //Row 5
        gridPane.add(loginNameLbl,0,4,1,1);
        gridPane.add(loginNameTxtField,1,4,1,1);
        //Row 6
        gridPane.add(passwordLbl,0,5,1,1);
        gridPane.add(passwordTxtField,1,5,1,1);
        //Row 7
        gridPane.add(phoneNumberLbl,0,6,1,1);
        gridPane.add(phoneNumberTxtField,1,6,1,1);
        //Row 8
        gridPane.add(addressLbl,0,7,1,1);
        gridPane.add(addressTxtField,1,7,1,1);
        //Row 9
        gridPane.add(cityLbl,0,8,1,1);
        gridPane.add(cityTxtField,1,8,1,1);
        //Row 10
        gridPane.add(countryLbl,0,9,1,1);
        gridPane.add(countryTxtField,1,9,1,1);
        //Row 11
        gridPane.add(writeBtn,3,10,1,1);
        }


        private void addUser(){

        boolean doIt = true;
            String message = "";
            int role = 2;
            if (firstNameTxtField.getText().isEmpty()){
                message = "First name cannot be empty.\n";
                doIt = false;
            }
            if (lastNameTxtField.getText().isEmpty()){
                message += "Last name cannot be empty.\n";
                doIt = false;
            }
            if (emailTxtField.getText().isEmpty() || !emailTxtField.getText().matches("[^@]+[@][^@]+\\.[^@]+")){
                message += "Email is incorrect.\n";
                doIt = false;
            }
            if (loginNameTxtField.getText().isEmpty()){
                message += "Login name cannot be empty.\n";
                doIt = false;
            }
            else{
                if(!dB.checkLoginName(loginNameTxtField.getText())){
                    if(dB.checkConnection()) {//Connection must be alive
                        message += "Login name is taken.\n";
                    }
                    doIt = false;
                }
            }
            if (passwordTxtField.getText().length()<4){
                message += "Password needs to be at least 4 charachters.\n";
                doIt = false;
            }
            if (passwordTxtField.getText().contains(" ")){
                message += "Password cannot have whitespace.\n";
                doIt = false;
            }

            if (!phoneNumberTxtField.getText().matches("[0-9]+")){
                message += "Phone number can only contain numbers.\n";
                doIt = false;
            }
            if (addressTxtField.getText().isEmpty()){
                message += "Address cannot be empty.\n";
                doIt = false;
            }

            if (cityTxtField.getText().isEmpty()){
                message += "City cannot be empty.\n";
                doIt = false;
            }

            if (countryTxtField.getText().isEmpty()){
                message += "Country cannot be empty.\n";
                doIt = false;
            }

            if(doIt){
                if(dB.insertUser(
                        role,
                        firstNameTxtField.getText(),
                        loginNameTxtField.getText(),
                        emailTxtField.getText(),
                        loginNameTxtField.getText(),
                        passwordTxtField.getText(),
                        phoneNumberTxtField.getText(),
                        addressTxtField.getText(),
                        cityTxtField.getText(),
                        countryTxtField.getText())){
                    tS.getButtonAnimation(pane,writeBtn,dS.getOkImgView().getImage());
                    resetText();
                }
                else{
                    tS.getButtonAnimation(pane,writeBtn,dS.getNotOkImgView().getImage());
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING, message);
                alert.setTitle("Info");
                alert.show();
            }

        }

        private void updateUser(){
        Role role = roleComboBox.getSelectionModel().getSelectedItem();
        if(role!=null && !loginNameTxtField.getText().isEmpty() && !passwordTxtField.getText().isEmpty()) {
            User update = createNewUser(role.getValue());

            if(dB.updateUser(update)){
                user.setRole(role.getValue());
                user.setFirstName(firstNameTxtField.getText());
                user.setLastName(lastNameTxtField.getText());
                user.setEmail(emailTxtField.getText());
                user.setLoginName(loginNameTxtField.getText());
                user.setPassword(passwordTxtField.getText());
                user.setPhoneNumber(phoneNumberTxtField.getText());
                user.setAddress(addressTxtField.getText());
                user.setCity(cityTxtField.getText());
                user.setCountry(countryTxtField.getText());
                setPredicate.setPredicate();
                tS.getButtonAnimation(pane,writeBtn,dS.getOkImgView().getImage());
            }     
            else{
                tS.getButtonAnimation(pane,writeBtn,dS.getNotOkImgView().getImage());
            }
        }
        else {
            String message = "" ;
            if(loginNameTxtField.getText().isEmpty()){
                message = "Login name cannot be empty.\n";
            }
            if(passwordTxtField.getText().isEmpty()){
                message += "Password cannot be empty.\n";
            }
            Alert alert = new Alert(Alert.AlertType.WARNING, message);
            alert.setTitle("Info");
            alert.show();
            tS.getButtonAnimation(pane,writeBtn,dS.getNotOkImgView().getImage());
        }
        }

        private void setListener45(TextField textField){
            textField.textProperty().addListener((obs,oV,nV)->{
                if(nV.length()>45){
                    textField.setText(oV);
                }
            });
        }

        private User createNewUser(int roleId){
            return new User(
                    id,
                    roleId,
                    firstNameTxtField.getText(),
                    lastNameTxtField.getText(),
                    emailTxtField.getText(),
                    loginNameTxtField.getText(),
                    passwordTxtField.getText(),
                    phoneNumberTxtField.getText(),
                    addressTxtField.getText(),
                    cityTxtField.getText(),
                    countryTxtField.getText());
        }
        private void resetText(){
            firstNameTxtField.setText("");
            lastNameTxtField.setText("");
            loginNameTxtField.setText("");
            emailTxtField.setText("");
            loginNameTxtField.setText("");
            passwordTxtField.setText("");
            phoneNumberTxtField.setText("");
            addressTxtField.setText("");
            cityTxtField.setText("");
            countryTxtField.setText("");
        }
    }

