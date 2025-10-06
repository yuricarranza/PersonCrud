package org.example;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class PersonDataAccess {
    private final String _dbConnectionUrl;
    public PersonDataAccess(Properties properties){
        _dbConnectionUrl = properties.getProperty("database.url");
    }
    public ArrayList<Person> GetPeople(String dbConnectionUrl){
        var people = new ArrayList<Person>();
        try (var connection = DriverManager.getConnection(_dbConnectionUrl)){
            var statement = connection.createStatement();
            var rs = statement.executeQuery("select p.BusinessEntityID, p.FirstName, p.LastName from Person.Person p");
            while (rs.next()){
                Person person = new Person();
                person.setId(rs.getInt("BusinessEntityID"));
                person.setFirstName(rs.getString("FirstName"));
                person.setLastName(rs.getString("LastName"));
                people.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return people;
    }

    public Person GetPersonById(int id){
        Person person = null;
        try (var connection = DriverManager.getConnection(_dbConnectionUrl)){
            var statement = connection.prepareStatement("select p.BusinessEntityID, p.FirstName, p.LastName from Person.Person p where p.BusinessEntityID = ?");
            statement.setInt(1, id);
            var rs = statement.executeQuery();
            while (rs.next()){
                person = new Person();
                person.setId(rs.getInt("BusinessEntityID"));
                person.setFirstName(rs.getString("FirstName"));
                person.setLastName(rs.getString("LastName"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return person;
    }

    public void RegisterPerson(Person person){
        try (var connection = DriverManager.getConnection(_dbConnectionUrl)){
            var statement = connection.prepareStatement("insert into Person.Person (BusinessEntityID, FirstName, LastName) values (?, ?, ?)");
            statement.setInt(1, person.getId());
            statement.setString(2, person.getFirstName());
            statement.setString(3, person.getLastName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
