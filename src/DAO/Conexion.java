/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author anton
 */
import java.sql.*;
public class Conexion 
{
    Connection c;
    public Conexion(String dbName)
    {
        
        try {
            /* Driver */
            String driver = "com.mysql.jdbc.Driver";
            System.out.println( "=> Cargando el Driver:" );
            Class.forName( driver );
            System.out.println("Driver OK");
            
            /* Definiendo Base de Datos */
            String url = "jdbc:mysql://localhost/"+dbName;
            String user = "root";
            String pass = "";
            
            /* Conectando */
            System.out.println( "=> conectando a la BD:" );
            c = DriverManager.getConnection( url , user , pass );
            System.out.println("Base de Datos OK");
            }
    catch( Exception x ) {
            System.out.println("Error al tratar de conectar a la BD!");
        } 

        
    }
     public Connection getConexion(){
        return c;
    }
     
}
