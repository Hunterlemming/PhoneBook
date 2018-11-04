package Model;

import javafx.beans.property.SimpleStringProperty;

public class Person {

    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty email;
    private final SimpleStringProperty id;

    public  Person(int _id, String lName, String fName, String email){
        this.id = new SimpleStringProperty(Integer.toString(_id));
        this.firstName = new SimpleStringProperty(fName);
        this.lastName = new SimpleStringProperty(lName);
        this.email = new SimpleStringProperty(email);
    }

    public  Person(){
        this.id = new SimpleStringProperty("");
        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
    }

    public String getLastName(){
        return lastName.get();
    }

    public String getFirstName(){
        return firstName.get();
    }

    public String getEmail(){
        return email.get();
    }

    public String getId(){
        return id.get();
    }

    public void setFirstName(String fName){
        firstName.set(fName);
    }

    public void setLastName(String lName){
        lastName.set(lName);
    }

    public void setEmail(String mail){
        email.set(mail);
    }

    public void setId(String mail){
        id.set(mail);
    }

}
