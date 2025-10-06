package org.example;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties prop = new Properties();
        try (var input = new FileInputStream("config.properties")){
            prop.load(input);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        PersonDataAccess personDataAccess = new PersonDataAccess(prop);
        var person = personDataAccess.GetPersonById(1);
        if (person != null)
        {
            System.out.println(person.getId() + ", " + person.getFirstName() + ", " + person.getLastName());
        }
    }
}