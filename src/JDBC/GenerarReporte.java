/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

/**
 *
 * @author anton
 */

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenerarReporte {
    public static void main(String[] args) {
        try {
            // Conectar a la base de datos
            Connection conn = ConexionJDBC.getConexion();

            if (conn != null) {
                String sql = "SELECT ID, Nombre, Cargo, HorasTrabajadas, Bonificaciones, Deducciones, Total FROM nominas";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                List<Nomina> nominas = new ArrayList<>();

                // Rellenar la lista con datos del ResultSet
                while (rs.next()) {
                    nominas.add(new Nomina(
                        rs.getInt("ID"),
                        rs.getString("Nombre"),
                        rs.getString("Cargo"),
                        rs.getInt("HorasTrabajadas"),
                        rs.getDouble("Bonificaciones"),
                        rs.getDouble("Deducciones"),
                        rs.getDouble("Total")
                    ));
                }

                if (nominas.isEmpty()) {
                    System.out.println("No hay datos para generar el reporte.");
                    return;
                }

                // Fuente de datos para el reporte
                JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(nominas);

                // Compilar y llenar el reporte
                JasperReport reporte = JasperCompileManager.compileReport("src/main/resources/MyReport.jrxml");
                JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, dataSource);

                // Visualizar el reporte
                JasperViewer.viewReport(jasperPrint, false);

                // Exportar a PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, "reporte_nomina.pdf");

                conn.close();
            } else {
                System.out.println("No se pudo conectar a la base de datos.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}




