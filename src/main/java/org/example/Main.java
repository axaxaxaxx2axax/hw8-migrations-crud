package org.example;


import org.example.database.Database;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = Database.getConnection();
            System.out.println("Database initialized and migrations applied successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to initialize database and apply migrations.");
        }
    }
}