public class Customer {
    private String customerName;
    private String customerID;
    private String password;
    private int age;
    private String email;
    private String phoneNumber;
    private String gender;

    public Customer(String customerName, String customerID, String password, int age, String email, String phoneNumber,
            String gender) {
        this.customerName = customerName;
        this.customerID = customerID;
        this.password = password;
        this.age = age;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public boolean validatePassword(String userPasswordInput) {
        return this.password.equals(userPasswordInput);
    }

}