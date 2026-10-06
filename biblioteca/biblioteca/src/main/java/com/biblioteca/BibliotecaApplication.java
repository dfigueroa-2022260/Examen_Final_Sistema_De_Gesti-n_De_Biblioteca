package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class BibliotecaApplication {

    // Deben coincidir con application.properties
    private static final String SERVER_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";
    private static final String DB_NAME = "biblioteca";

    public static void main(String[] args) {
        crearBaseDeDatosSiNoExiste();
        SpringApplication.run(BibliotecaApplication.class, args);
    }

    private static void crearBaseDeDatosSiNoExiste() {
        try (Connection c = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + DB_NAME + "'")) {
            if (!rs.next()) {
                st.executeUpdate("CREATE DATABASE " + DB_NAME);
                System.out.println(">>> Base de datos '" + DB_NAME + "' creada");
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo conectar/crear la base de datos en PostgreSQL", e);
        }
    }
}
