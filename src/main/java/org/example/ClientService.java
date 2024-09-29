package org.example;

import lombok.Data;
import lombok.SneakyThrows;
import org.example.database.Database;
import org.example.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    public static final String CREATE_CLIENT_SQL = "INSERT INTO CLIENT (NAME) VALUES (?)";
    public static final String GET_CLIENT_SQL = "SELECT NAME FROM CLIENT WHERE ID = ?";
    public static final String UPDATE_CLIENT_SQL = "UPDATE CLIENT SET NAME = ? WHERE ID = ?";
    public static final String DELETE_CLIENT_SQL = "DELETE FROM CLIENT WHERE ID = ?";
    public static final String LIST_ALL_CLIENTS_SQL = "SELECT * FROM CLIENT";

    private void validateName(String name) {
        if (name == null || name.length() < 3 || name.length() > 50) {
            throw new IllegalArgumentException("Ім'я має бути від 3 до 50 символів.");
        }
    }

    @SneakyThrows
    public long create(String name) {
        validateName(name);
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_CLIENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);

            statement.executeUpdate();
            try (ResultSet generatedKey = statement.getGeneratedKeys();) {
                if (generatedKey.next()) {
                    return generatedKey.getLong(1);
                } else {
                    throw new SQLException("Не вдалося отримати ID створеного клієнта.");
                }
            }
        }
    }

    @SneakyThrows
    public String getById(long id) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_CLIENT_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("NAME");
                } else {
                    throw new SQLException("Не вдалося отримати ім'я клієнта.");
                }
            }
        }
    }

    @SneakyThrows
    public void setName(long id, String name) {
        validateName(name);
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_CLIENT_SQL)) {
            statement.setString(1, name);
            statement.setLong(2, id);
            int i = statement.executeUpdate();
            if (i == 0) {
                throw new SQLException("Не вдалося оновити ім'я клієнта.");
            }
        }
    }

    @SneakyThrows
    public void deleteById(long id) {
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_CLIENT_SQL)) {
            statement.setLong(1, id);
            int i = statement.executeUpdate();
            if (i == 0) {
                throw new SQLException("Не вдалося оновити ім'я клієнта.");
            }
            System.out.println(i);
        }
    }

    @SneakyThrows
    public List<Client> listAll() {
        List<Client> result = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(LIST_ALL_CLIENTS_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.add(Client.builder()
                        .id(resultSet.getObject("ID", Long.class))
                        .name(resultSet.getString("NAME"))
                        .build());
            }
            return result;
        }
    }
}
