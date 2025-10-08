package org.example;

import org.example.repository.UserRepository;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Properties prop = new Properties();
        try (var input = new FileInputStream("config.properties")){
            prop.load(input);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        UserRepository userRepository = new UserRepository(prop);
        var users = userRepository.GetUsers();
        for(var user: users){
            if (!userRepository.UserExists(user.getId())) {
                userRepository.RegisterUser(user);
                System.out.println("User registered successfully!");
            }
            else {
                System.out.println("User already exists");
            }
        }
    }
}