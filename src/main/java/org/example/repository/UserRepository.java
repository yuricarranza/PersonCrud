package org.example.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.model.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class UserRepository {
    private final String _urlUserEndpoint;
    private final String _dbConnectionUrl;

    public UserRepository(Properties properties) {
        _urlUserEndpoint = properties.getProperty("service.url");
        _dbConnectionUrl = properties.getProperty("database.url");
    }

    public ArrayList<User> GetUsers() throws IOException, InterruptedException {
        var users = new ArrayList<User>();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(_urlUserEndpoint))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<User>>() {}.getType();
            users = gson.fromJson(response.body(), listType);
            return users;
        }
    }

    public boolean UserExists(int id){
        boolean userExists = false;
        try (var connection = DriverManager.getConnection(_dbConnectionUrl)){
            var statement = connection.prepareStatement("select count(1) from dbo.[User] where id = ?");
            statement.setInt(1, id);
            var rs = statement.executeQuery();
            while (rs.next()){
                userExists = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userExists;
    }

    public void RegisterUser(User user){
        try (var connection = DriverManager.getConnection(_dbConnectionUrl)){
            var statement = connection.prepareStatement("insert into dbo.[User](id, name, email, phone) values (?, ?, ?, ?)");
            statement.setInt(1, user.getId());
            statement.setString(2, user.getName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
