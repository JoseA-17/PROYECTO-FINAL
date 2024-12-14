/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iText;

/**
 *
 * @author anton
 */
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class GenerarReciboPDF 
{
public static void generarRecibo(String empresa, String nombre, int id, String correo, String cargo, String seguro, int edad,
                                  int horasTrabajadas, double tarifaPorHora, double bonificaciones, double deducciones,
                                  double horasDisponibles, double horasUsadas, double salarioBruto, double totalDeducciones, double salarioNeto) {
    PDDocument document = new PDDocument();

    try {
        // Evitar salario neto negativo
        if (salarioNeto < 0) {
            salarioNeto = 0;
        }

        // Redondear la tarifa por hora a 2 decimales
        tarifaPorHora = Math.round(tarifaPorHora * 100.0) / 100.0;

        // Calcular la tarifa por hora dividiendo el salario base por las horas trabajadas
        double salarioBase = salarioBruto - bonificaciones + deducciones;
        tarifaPorHora = Math.round((salarioBase / horasTrabajadas) * 100.0) / 100.0;

        // Crear página
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        // Crear flujo de contenido
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        // Encabezado: Nombre de la empresa
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
        contentStream.newLineAtOffset(220, 750);
        contentStream.showText("La Mundial S.B");  // Nombre de la empresa
        contentStream.endText();

        // Información del empleado
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(50, 700);
        contentStream.showText("Información del Empleado");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(50, 680);
        contentStream.showText("Nombre: " + nombre);
        contentStream.newLineAtOffset(0, -20);  // Más espacio entre campos
        contentStream.showText("ID: " + id);
        contentStream.newLineAtOffset(0, -20);  // Más espacio entre campos
        contentStream.showText("Correo: " + correo);
        contentStream.newLineAtOffset(0, -20);  // Más espacio entre campos
        contentStream.showText("Cargo: " + cargo);
        contentStream.newLineAtOffset(0, -20);  // Más espacio entre campos
        contentStream.showText("Seguro: " + seguro);
        contentStream.newLineAtOffset(0, -20);  // Más espacio entre campos
        contentStream.showText("Edad: " + edad);
        contentStream.endText();

        // Espacio adicional antes de la declaración de ingresos
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 580);  // Ajustado para mayor espacio
        contentStream.endText();

        // Dibuja el marco para la declaración de ingresos
        contentStream.setLineWidth(1);
        contentStream.addRect(50, 450, 500, 120); // Marco de la declaración de ingresos
        contentStream.stroke();

        // Declaración de ingresos
        contentStream.beginText(); 
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(55, 560); // Ajuste de posición dentro del marco
        contentStream.showText("Declaración de Ingresos");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(55, 540);
        contentStream.showText("Tipo de Pago: Regular");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Tarifa por Hora: " + String.format("%.2f", tarifaPorHora));  // Tarifa con 2 decimales
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Horas Trabajadas: " + horasTrabajadas);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Bonificaciones: " + bonificaciones);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Deducciones: " + deducciones);
        contentStream.endText();

        // Verificar y mostrar horas extras
        if (horasUsadas > 60) 
        {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            contentStream.newLineAtOffset(55, 480); // Ajusta la posición según sea necesario
            contentStream.showText("Horas Extras: " + (horasUsadas - 60));
            contentStream.endText();
        }

        // Dibuja el marco para la tabla 2 (fuera del bloque de texto)
        contentStream.setLineWidth(1);
        contentStream.addRect(50, 260, 500, 150); // Marco de la tabla
        contentStream.stroke();

        // Tabla 2: Resumen de horas y deducciones
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(55, 400);
        contentStream.showText("Resumen de Horas y Deducciones");
        contentStream.endText();

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(55, 380);
        contentStream.showText("Horas Disponibles: " + horasDisponibles);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Horas Usadas: " + horasUsadas);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Salario Bruto: " + salarioBruto);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Total Deducciones: " + totalDeducciones);
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Salario Neto: " + salarioNeto);
        contentStream.endText();

        // Espacio para las firmas
        contentStream.beginText();
        contentStream.newLineAtOffset(0, -30);
        contentStream.endText();

        // Línea para la firma del Gerente
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(50, 100);
        contentStream.showText("______________________________");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Firma del Gerente");
        contentStream.endText();

        // Línea para la firma del Empleado
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.newLineAtOffset(300, 100);
        contentStream.showText("______________________________");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Firma del Empleado");
        contentStream.endText();

        // Finalizar flujo de contenido
        contentStream.close();

        // Guardar el archivo
        document.save("Recibo_" + nombre + ".pdf");
        JOptionPane.showMessageDialog(null, "Recibo generado exitosamente.");

    } 
    catch (IOException e) 
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error al generar el recibo.");
    } 
    finally 
    {
        try 
        {
            document.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}


}

    