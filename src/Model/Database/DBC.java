package Model.Database;

import Model.Person;

import java.sql.*;
import java.util.ArrayList;

public class DBC extends Config{

    public ArrayList<Person> getAllContacts(){
        String sql = "select * from contacts";
        ArrayList<Person> users = null;
        try {
            ResultSet rs = createStatement.executeQuery(sql);
            users = new ArrayList<>();

            while (rs.next()){
                Person actualPerson = new Person(rs.getInt("id"),rs.getString("lastname"),rs.getString("firstname"),rs.getString("email"));
                users.add(actualPerson);
            }
        } catch (SQLException ex) {
            System.out.println("Valami baj van a userek kiolvasásakor");
            System.out.println(""+ex);
        }
        return users;
    }

    public void addContact(Person person){
        try {
            String sql = "insert into contacts (lastname, firstname, email) values (?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, person.getLastName());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a contact hozzáadásakor");
            System.out.println(""+ex);
        }
    }

    public void updateContact(Person person){
        try {
            String sql = "update contacts set lastname = ?, firstname = ? , email = ? where id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, person.getLastName());
            preparedStatement.setString(2, person.getFirstName());
            preparedStatement.setString(3, person.getEmail());
            preparedStatement.setInt(4, Integer.parseInt(person.getId()));
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println("Valami baj van a contact hozzáadásakor");
            System.out.println(""+ex);
        }
    }

}
