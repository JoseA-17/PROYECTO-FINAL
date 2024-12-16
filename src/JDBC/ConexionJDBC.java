/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

/**
 *
 * @author anton
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionJDBC {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/empleados";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // 

    public static Connection getConexion() {
        Connection conn = null;
        try {
            // Cargar el controlador de MySQL
            Class.forName(DRIVER);
            System.out.println("Driver OK");

            // Conectar a la base de datos
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Base de Datos OK");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Error de conexión: " + e.getMessage());
        }
        return conn;
    }
}





