/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iText;

/**
 *
 * @author anton
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class GenerarReciboPDF {
    public static void generarRecibo(String nombre, int id, int horasTrabajadas, double bonificaciones, double deducciones, double salarioBase, double salarioNeto) {
    PDDocument document = new PDDocument();

    try {
        // Evitar salario neto negativo
        if (salarioNeto < 0) {
            salarioNeto = 0;
        }

        // Crear página
        PDPage page = new PDPage();
        document.addPage(page);

        // Crear flujo de contenido
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);  // Usamos la fuente Helvetica
        contentStream.newLineAtOffset(100, 700);  // Posición inicial

        // Añadir el contenido con saltos de línea
        contentStream.showText("Recibo de Nómina");
        contentStream.newLineAtOffset(0, -20);  // Ajusta la posición en Y para la siguiente línea
        contentStream.showText("Empleado: " + nombre);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("ID: " + id);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Horas Trabajadas: " + horasTrabajadas);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Salario Base: " + salarioBase);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Bonificaciones: " + bonificaciones);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Deducciones: " + deducciones);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Salario Neto: " + salarioNeto);

        // Termina la sección de texto
        contentStream.endText();
        contentStream.close();

        // Guardar el archivo
        document.save(new File("Recibo_" + nombre + ".pdf"));
        document.close();

        JOptionPane.showMessageDialog(null, "Recibo generado exitosamente.");
    } catch (IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al generar el recibo.");
    }
}
}
    
   







