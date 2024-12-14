/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ControlEmpleados.AsistenciasEmpleados;

import DAO.Conexion;
import javax.swing.JOptionPane;
import java.sql.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author anton
 */
public class frmRegistrarAsistencia extends javax.swing.JFrame {
 
    /**
     * Creates new form frmRegistrarAsistencia
     */
    public frmRegistrarAsistencia() 
    {
         initComponents();
    this.setLocationRelativeTo(null);
    cargarTablaAsistencias();
    agregarListenerSeleccionFila();
    }
    
private String idEmpleadoSeleccionado = ""; //variable para almacenar el ID del empleado seleccionado

private void agregarListenerSeleccionFila() 
{
    tblEmpleados.getSelectionModel().addListSelectionListener(e -> 
    {
        int row = tblEmpleados.getSelectedRow();
        if (row != -1) 
        {
            idEmpleadoSeleccionado = tblEmpleados.getValueAt(row, 0).toString();   
        }
    });
}

private void registrarAsistencia() 
{
    int selectedRow = tblEmpleados.getSelectedRow();
    if (selectedRow == -1) 
    {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    //obtener datos de la tabla y los combos
    String idEmpleado = tblEmpleados.getValueAt(selectedRow, 0).toString();
    String nombreEmpleado = tblEmpleados.getValueAt(selectedRow, 1).toString();
    String cargoEmpleado = tblEmpleados.getValueAt(selectedRow, 2).toString(); 
    String diaSemana = cmbDiaSemana.getSelectedItem().toString().toUpperCase();
    String estadoAsistencia = cmbAsistencia.getSelectedItem().toString().substring(0, 1);
    String horaEntrada = cmbEntrada.getSelectedItem().toString();
    String horaSalida = cmbSalida.getSelectedItem().toString();
    String jornada = cmbJornada.getSelectedItem().toString();
    String observacion = txtAreaObservacion.getText(); 

    Map<String, String> diasMap = new HashMap<>();
    diasMap.put("LUNES", "LUN");
    diasMap.put("MARTES", "MAR");
    diasMap.put("MIERCOLES", "MIE");
    diasMap.put("JUEVES", "JUE");
    diasMap.put("VIERNES", "VIE");
    diasMap.put("SABADO", "SAB");
    diasMap.put("DOMINGO", "DOM");

    String columnaDia = diasMap.get(diaSemana);
    if (columnaDia == null) 
    {
        JOptionPane.showMessageDialog(this, "Dia seleccionado no válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();
    if (c == null) 
    {
        JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    //Consulta SQL
    String checkQuery = "SELECT COUNT(*) FROM asistencias WHERE ID = ?";
    String insertQuery = "INSERT INTO asistencias (ID, Nombre, Cargo, Asistencia, Entrada, Salida, Jornada, Observacion, LUN, MAR, MIE, JUE, VIE, SAB, DOM) " +
                         "VALUES (?, ?, ?, '', '', '', '', '', '', '', '', '', '', '', '')";
    String updateQuery = "UPDATE asistencias SET Asistencia = ?, Entrada = ?, Salida = ?, Jornada = ?, Observacion = ?, " +
                         columnaDia + " = ? WHERE ID = ?";

    try 
    {
        //Verificar si el registro existe
        PreparedStatement psCheck = c.prepareStatement(checkQuery);
        psCheck.setString(1, idEmpleado);
        ResultSet rs = psCheck.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        //si el empleado no tiene registros crea uno nuevo
        if (count == 0) 
        {
            PreparedStatement psInsert = c.prepareStatement(insertQuery);
            psInsert.setString(1, idEmpleado);
            psInsert.setString(2, nombreEmpleado);
            psInsert.setString(3, cargoEmpleado);
            psInsert.executeUpdate();
            psInsert.close();
        }

        //actualiiza los datos para la asistencia
        PreparedStatement psUpdate = c.prepareStatement(updateQuery);
        psUpdate.setString(1, estadoAsistencia); 
        psUpdate.setString(2, horaEntrada);
        psUpdate.setString(3, horaSalida);
        psUpdate.setString(4, jornada);
        psUpdate.setString(5, observacion);
        psUpdate.setString(6, estadoAsistencia);
        psUpdate.setString(7, idEmpleado);

        int rowsAffected = psUpdate.executeUpdate();
        if (rowsAffected > 0) 
        {
            JOptionPane.showMessageDialog(this, "Asistencia registrada correctamente para el día: " + diaSemana);
        } 
        
        else 
        {
            JOptionPane.showMessageDialog(this, "Error al actualizar el registro de asistencia.");
        }

        psUpdate.close();
        rs.close();
        psCheck.close();
    } 
    
    catch (SQLException e) 
    {
        JOptionPane.showMessageDialog(this, "Error al registrar asistencia: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } 
    
    finally 
    {
        try 
        {
            if (c != null) c.close();
        } 
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}

private void cargarTablaAsistencias() 
{
    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();
    
    if (c == null) 
    {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try 
    {
        //Consulta para combinar datos de empleados y asistencias
        String query = "SELECT e.ID, e.Nombre, e.Cargo, a.Asistencia, a.Entrada, a.Salida, a.Jornada, a.Observacion " +
                       "FROM empleados e " +
                       "LEFT JOIN asistencias a ON e.ID = a.ID";

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        //Limpiar la tabla
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0);//Elimina filas de la tabla para cargarla completa despues de registrar asistencia

        while (rs.next()) 
        {
            Object[] row = new Object[3]; 
            row[0] = rs.getInt("ID");    
            row[1] = rs.getString("Nombre"); 
            row[2] = rs.getString("Cargo"); 

            //agrega filas a la tabla
            model.addRow(row);
        }

        rs.close();
        stmt.close();
    } 
    
    catch (SQLException e) 
    {
        JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } 
    
    finally 
    {
        try 
        {
            if (c != null) 
            {
                c.close();
            }
        } 
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}


    //mtodo para buscar por ID (se reutiliza en todos los formularios donde exista una tabla
    private void buscarEmpleado() 
    {
       String idEmpleado = txtID.getText().trim();//obtiene el id del textfield y elimina espacios para evitar errores
    if (idEmpleado.isEmpty()) 
    {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa un ID de empleado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    //Consulta SQL
    String query = "SELECT * FROM empleados WHERE id = ?";

    try 
    {
        Conexion conn = new Conexion("empleados");
        Connection c = conn.getConexion();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(idEmpleado)); //convertir a int el id

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            String nombre = rs.getString("nombre");
            String cargo = rs.getString("cargo");

            JOptionPane.showMessageDialog(this, "Empleado encontrado: " + nombre + " - " + cargo);
        } 
        
        else 
        {
            JOptionPane.showMessageDialog(this, "Empleado no encontrado con el ID proporcionado.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        ps.close();
        c.close();
    } 
    
    catch (SQLException e) 
    {
        JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
   public class Utils 
   {
    public static int parsearHoras(String horaEntrada, String horaSalida) throws ParseException 
    {
        SimpleDateFormat formato = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

        //parsear las horas de entrada y salida
        Date entrada = formato.parse(horaEntrada);
        Date salida = formato.parse(horaSalida);

        //Calcular la diferencia en milisegundos entre las dos horas
        long diferencia = salida.getTime() - entrada.getTime();

        //Convertir la diferencia a horas (milisegundos a horas)
        return (int) (diferencia / (1000 * 60 * 60)); // Convertir milisegundos a horas
    }
}
    
   
private void registrarHorasPorDia() {
    int selectedRow = tblEmpleados.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Obtener los datos de la tabla y los combos
    String idEmpleado = tblEmpleados.getValueAt(selectedRow, 0).toString();
    String nombreEmpleado = tblEmpleados.getValueAt(selectedRow, 1).toString();
    String cargoEmpleado = tblEmpleados.getValueAt(selectedRow, 2).toString();
    String diaSemana = cmbDiaSemana.getSelectedItem().toString().toUpperCase();
    String horaEntrada = cmbEntrada.getSelectedItem().toString();
    String horaSalida = cmbSalida.getSelectedItem().toString();
    String estadoAsistencia = cmbAsistencia.getSelectedItem().toString();

    int horasTrabajadas = 0;

    // Validar y calcular horas trabajadas
    try {
        if (estadoAsistencia.equalsIgnoreCase("Faltante") ||
            estadoAsistencia.equalsIgnoreCase("Permiso") ||
            estadoAsistencia.equalsIgnoreCase("Excusa")) {
            horasTrabajadas = 0;
        } else {
            horasTrabajadas = Utils.parsearHoras(horaEntrada, horaSalida);

            if (horasTrabajadas < 0) {
                JOptionPane.showMessageDialog(this, "La hora de salida no puede ser antes que la hora de entrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    } catch (ParseException e) {
        JOptionPane.showMessageDialog(this, "Formato de hora inválido. Usa el formato HH:mm AM/PM.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Mapear días de la semana a las columnas correspondientes en la base de datos
    Map<String, String> diasMap = Map.of(
        "LUNES", "lunes",
        "MARTES", "martes",
        "MIERCOLES", "miercoles",
        "JUEVES", "jueves",
        "VIERNES", "viernes",
        "SABADO", "sabado",
        "DOMINGO", "domingo"
    );

    String columnaDia = diasMap.get(diaSemana);
    if (columnaDia == null) {
        JOptionPane.showMessageDialog(this, "Día seleccionado no válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Conexion conn = new Conexion("empleados");
    try (Connection c = conn.getConexion()) {

        String checkQuery = "SELECT COUNT(*) FROM horariopordias WHERE id = ?";
        String selectHorasQuery = "SELECT horastrabajadas, COALESCE(" + columnaDia + ", 0) AS " + columnaDia + " FROM horariopordias WHERE id = ?";
        String insertQuery = "INSERT INTO horariopordias (id, nombre, cargo, lunes, martes, miercoles, jueves, viernes, sabado, domingo, horastrabajadas) " +
                             "VALUES (?, ?, ?, 0, 0, 0, 0, 0, 0, 0, ?)";
        String updateQuery = "UPDATE horariopordias SET " + columnaDia + " = ?, horastrabajadas = ? WHERE id = ?";

        try (PreparedStatement psCheck = c.prepareStatement(checkQuery)) {
            psCheck.setString(1, idEmpleado);
            ResultSet rs = psCheck.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                // Insertar nuevo registro si no existe
                try (PreparedStatement psInsert = c.prepareStatement(insertQuery)) {
                    psInsert.setString(1, idEmpleado);
                    psInsert.setString(2, nombreEmpleado);
                    psInsert.setString(3, cargoEmpleado);
                    psInsert.setInt(4, horasTrabajadas);
                    psInsert.executeUpdate();
                }
            } else {
                // Actualizar el registro existente
                try (PreparedStatement psSelect = c.prepareStatement(selectHorasQuery)) {
                    psSelect.setString(1, idEmpleado);
                    ResultSet rsHoras = psSelect.executeQuery();

                    int horasPrevias = 0;
                    int horasDiaActual = 0;
                    if (rsHoras.next()) {
                        horasPrevias = rsHoras.getInt("horastrabajadas");
                        horasDiaActual = rsHoras.getInt(columnaDia);
                    }

                    int totalHorasTrabajadas = horasPrevias - horasDiaActual + horasTrabajadas;

                    try (PreparedStatement psUpdate = c.prepareStatement(updateQuery)) {
                        psUpdate.setInt(1, horasTrabajadas);
                        psUpdate.setInt(2, totalHorasTrabajadas);
                        psUpdate.setString(3, idEmpleado);
                        psUpdate.executeUpdate();
                    }
                }
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al registrar horas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}




    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JButton();
        btnBuscar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        cmbEntrada = new javax.swing.JComboBox<>();
        cmbSalida = new javax.swing.JComboBox<>();
        cmbAsistencia = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cmbJornada = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaObservacion = new javax.swing.JTextArea();
        btnVolver = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmbDiaSemana = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Registrar");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 590, -1, 23));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 70, -1));
        jPanel1.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 76, -1));

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Cargo", "Asistencia"
            }
        ));
        jScrollPane1.setViewportView(tblEmpleados);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 102, 585, 400));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Registro de Asistencias");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(307, 15, 270, -1));

        btnRegistrar.setBackground(new java.awt.Color(102, 153, 255));
        btnRegistrar.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\carpeta. registro 50x50.png")); // NOI18N
        btnRegistrar.setBorder(null);
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });
        jPanel1.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 520, 91, 70));

        btnBuscar.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscar.setBorder(null);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 30, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Hora de entrada");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 160, 130, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Hora de Salida");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 200, 130, -1));

        cmbEntrada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM" }));
        jPanel1.add(cmbEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 170, 80, -1));

        cmbSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM", "5:00 PM", "6:00 PM", "7:00 PM" }));
        jPanel1.add(cmbSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 200, 90, -1));

        cmbAsistencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Presente", "Faltante", "Permiso", "Excusa" }));
        jPanel1.add(cmbAsistencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 120, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Estado");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 120, 65, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Jornada");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 250, 69, -1));

        cmbJornada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Matutina", "Vespertina" }));
        jPanel1.add(cmbJornada, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 250, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Observacion");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 340, 100, -1));

        txtAreaObservacion.setColumns(20);
        txtAreaObservacion.setRows(5);
        jScrollPane2.setViewportView(txtAreaObservacion);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 380, -1, 120));

        btnVolver.setBackground(new java.awt.Color(102, 153, 255));
        btnVolver.setForeground(new java.awt.Color(102, 153, 255));
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/out50x50.png"))); // NOI18N
        btnVolver.setBorder(null);
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        jPanel1.add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 520, -1, 60));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 2, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Dia:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 300, 37, -1));

        cmbDiaSemana.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO" }));
        jPanel1.add(cmbDiaSemana, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 300, -1, -1));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Buscar por iD");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 125, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Volver");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 580, -1, 23));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Datos Asistencia");
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 70, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 858, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        registrarAsistencia();
        registrarHorasPorDia();
        
        
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        cargarTablaAsistencias();
    }//GEN-LAST:event_formWindowOpened

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        this.dispose();
        frmAsistenciasEmpleado formAsistencias = new frmAsistenciasEmpleado();
    
    formAsistencias.setVisible(true);
    
    formAsistencias.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        buscarEmpleado();
    }//GEN-LAST:event_btnBuscarActionPerformed
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmRegistrarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRegistrarAsistencia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cmbAsistencia;
    private javax.swing.JComboBox<String> cmbDiaSemana;
    private javax.swing.JComboBox<String> cmbEntrada;
    private javax.swing.JComboBox<String> cmbJornada;
    private javax.swing.JComboBox<String> cmbSalida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextArea txtAreaObservacion;
    private javax.swing.JTextField txtID;
    // End of variables declaration//GEN-END:variables
}
