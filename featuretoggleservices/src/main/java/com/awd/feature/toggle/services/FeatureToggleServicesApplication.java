package com.awd.feature.toggle.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SpringBootApplication
public class FeatureToggleServicesApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeatureToggleServicesApplication.class, args);
    }

    /*private final String url = "jdbc:postgresql://localhost/localDB";
    private final String user = "postgres";
    private final String password = "admin";

    *//**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     *//*
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    *//**
     * @param args the command line arguments
     *//*
    public static void main(String[] args) {
        FeatureToggleServicesApplication app = new FeatureToggleServicesApplication();
        app.connect();
    }*/
}
