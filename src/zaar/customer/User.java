package zaar.customer;

public class User {
    private int customerID;
    private int role;
    private String firstName;
    private String lastName;
    private String email;
    private String loginName;
    private String password;
    private String phoneNumber;
    private String address;
    private String city;
    private String country;

    public User() {
        this(0,0, null, null, null, null, null, null, null, null, null);
    }

    public User(int customerID, int role, String firstName, String lastName, String email, String loginName, String password, String phoneNumber, String address, String city, String country) {
        this.customerID = customerID;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.loginName = loginName;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.city = city;
        this.country = country;
    }


    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
