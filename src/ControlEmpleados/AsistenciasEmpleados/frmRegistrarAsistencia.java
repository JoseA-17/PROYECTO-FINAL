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
import java.sql.Time;
import java.util.Date;

/**
 *
 * @author anton
 */
public class frmRegistrarAsistencia extends javax.swing.JFrame {
 
    /**
     * Creates new form frmRegistrarAsistencia
     */
    public frmRegistrarAsistencia() {
        initComponents();
        this.setLocationRelativeTo(null);
        cargarTablaAsistencias();
        agregarListenerSeleccionFila();
    }
    
private String idEmpleadoSeleccionado = ""; // Variable para almacenar el ID del empleado seleccionado

private void agregarListenerSeleccionFila() {
    tblEmpleados.getSelectionModel().addListSelectionListener(e -> {
        int row = tblEmpleados.getSelectedRow();
        if (row != -1) {
            // Se ha seleccionado una fila, muestra el ID o realiza alguna acción
            idEmpleadoSeleccionado = tblEmpleados.getValueAt(row, 0).toString(); // Asigna el ID del empleado
            System.out.println("ID del empleado seleccionado: " + idEmpleadoSeleccionado);    
        }
    });
}

private void registrarAsistencia() 
{
    // Validar selección de empleado en la tabla
    int selectedRow = tblEmpleados.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Obtener datos de la tabla y los combos
    String idEmpleado = tblEmpleados.getValueAt(selectedRow, 0).toString(); // ID del empleado
    String nombreEmpleado = tblEmpleados.getValueAt(selectedRow, 1).toString(); // Nombre
    String cargoEmpleado = tblEmpleados.getValueAt(selectedRow, 2).toString(); // Cargo
    String diaSemana = cmbDiaSemana.getSelectedItem().toString().toUpperCase(); // Día en mayúsculas
    String estadoAsistencia = cmbAsistencia.getSelectedItem().toString().substring(0, 1); // P, A o R
    String horaEntrada = cmbEntrada.getSelectedItem().toString() + " " + cmbHorarioEntrada.getSelectedItem().toString(); // Hora + AM/PM
    String horaSalida = cmbSalida.getSelectedItem().toString() + " " + cmbHorarioSalida.getSelectedItem().toString(); // Hora + AM/PM
    String jornada = cmbJornada.getSelectedItem().toString(); // Jornada (por ejemplo, "Matutina")
    String observacion = txtAreaObservacion.getText(); // Observaciones

    // Mapear días de la semana
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
        JOptionPane.showMessageDialog(this, "Día seleccionado no válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Mostrar datos en consola para depuración
    System.out.println("ID Empleado: " + idEmpleado);
    System.out.println("Nombre Empleado: " + nombreEmpleado);
    System.out.println("Cargo Empleado: " + cargoEmpleado);
    System.out.println("Día Seleccionado: " + diaSemana);
    System.out.println("Columna Día: " + columnaDia);
    System.out.println("Asistencia: " + estadoAsistencia);
    System.out.println("Entrada: " + horaEntrada);
    System.out.println("Salida: " + horaSalida);
    System.out.println("Jornada: " + jornada);
    System.out.println("Observación: " + observacion);

    // Crear conexión
    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();
    if (c == null) 
    {
        JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Consultas SQL
    String checkQuery = "SELECT COUNT(*) FROM asistencias WHERE ID = ?";
    String insertQuery = "INSERT INTO asistencias (ID, Nombre, Cargo, Asistencia, Entrada, Salida, Jornada, Observacion, LUN, MAR, MIE, JUE, VIE, SAB, DOM) " +
                         "VALUES (?, ?, ?, '', '', '', '', '', '', '', '', '', '', '', '')";
    String updateQuery = "UPDATE asistencias SET Asistencia = ?, Entrada = ?, Salida = ?, Jornada = ?, Observacion = ?, " +
                         columnaDia + " = ? WHERE ID = ?";

    try 
    {
        // Verificar si el registro existe
        PreparedStatement psCheck = c.prepareStatement(checkQuery);
        psCheck.setString(1, idEmpleado);
        ResultSet rs = psCheck.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        if (count == 0) 
        {
            // Insertar registro inicial si no existe
            PreparedStatement psInsert = c.prepareStatement(insertQuery);
            psInsert.setString(1, idEmpleado);
            psInsert.setString(2, nombreEmpleado);
            psInsert.setString(3, cargoEmpleado);
            psInsert.executeUpdate();
            psInsert.close();

            System.out.println("Registro inicial creado para el empleado con ID: " + idEmpleado);
        }

        // Actualizar registro con datos de asistencia
        PreparedStatement psUpdate = c.prepareStatement(updateQuery);
        psUpdate.setString(1, estadoAsistencia); // Asistencia: P, A o R
        psUpdate.setString(2, horaEntrada); // Hora de entrada
        psUpdate.setString(3, horaSalida); // Hora de salida
        psUpdate.setString(4, jornada); // Jornada
        psUpdate.setString(5, observacion); // Observaciones
        psUpdate.setString(6, estadoAsistencia); // Estado para el día
        psUpdate.setString(7, idEmpleado); // ID del empleado

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


private void cargarTablaAsistencias() {
    Conexion conn = new Conexion("empleados"); // Conexión a la base de datos
    Connection c = conn.getConexion();
    
    if (c == null) 
    {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try 
    {
        // Consulta para combinar datos de empleados y asistencias
        String query = "SELECT e.ID, e.Nombre, e.Cargo, a.Asistencia, a.Entrada, a.Salida, a.Jornada, a.Observacion " +
                       "FROM empleados e " +
                       "LEFT JOIN asistencias a ON e.ID = a.ID";

        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Limpiar la tabla antes de agregar nuevos datos
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0); // Eliminar filas existentes

        while (rs.next()) 
        {
            // Obtener datos de cada columna
            Object[] row = new Object[3]; // Cambia 3 por el número de columnas a mostrar
            row[0] = rs.getInt("ID");        // ID del empleado
            row[1] = rs.getString("Nombre"); // Nombre
            row[2] = rs.getString("Cargo");  // Cargo

            // Agregar fila a la tabla
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


    // Método para buscar por ID de empleado (opcional)
    private void buscarEmpleado() 
    {
       String idEmpleado = txtID.getText().trim();
    if (idEmpleado.isEmpty()) 
    {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa un ID de empleado.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Consulta SQL para buscar al empleado
    String query = "SELECT * FROM empleados WHERE id = ?";

    try 
    {
        Conexion conn = new Conexion("empleados");
        Connection c = conn.getConexion();
        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(idEmpleado)); // Convertir el ID ingresado a entero

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            // Si se encuentra el empleado, muestra sus datos en los campos correspondientes
            String nombre = rs.getString("nombre");
            String cargo = rs.getString("cargo");

            // Agregar al comboBox o tabla si es necesario
            JOptionPane.showMessageDialog(this, "Empleado encontrado: " + nombre + " - " + cargo);

            // Aquí podrías hacer algo con la información obtenida, como llenar un formulario o actualizar la tabla de asistencia.
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
    
private Time convertirAHoraFormatoMySQL(String hora) throws ParseException 
{
    // Limpiar espacios extraños
    hora = hora.trim();
    
    // Definir el formato de entrada (12 horas con AM/PM)
    SimpleDateFormat inputFormat = new SimpleDateFormat("h a"); // Formato 12 horas con AM/PM
    // Definir el formato de salida (24 horas)
    SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm:ss"); // Formato 24 horas
    
    // Intentar parsear la hora de entrada
    Date date = inputFormat.parse(hora);
    // Convertir la hora al formato 24 horas
    String horaFormateada = outputFormat.format(date);
    
    // Convertir a Time para la base de datos
    return Time.valueOf(horaFormateada);
}


// Método para registrar las horas por día
private void registrarHorasPorDia() 
{
    // Validar selección de empleado en la tabla
    int selectedRow = tblEmpleados.getSelectedRow();
    if (selectedRow == -1) 
    {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Obtener datos de la tabla y los combos
    String idEmpleado = tblEmpleados.getValueAt(selectedRow, 0).toString(); // ID del empleado
    String nombreEmpleado = tblEmpleados.getValueAt(selectedRow, 1).toString(); // Nombre
    String cargoEmpleado = tblEmpleados.getValueAt(selectedRow, 2).toString(); // Cargo
    String diaSemana = cmbDiaSemana.getSelectedItem().toString().toUpperCase(); // Día en mayúsculas
    String horaEntrada = cmbEntrada.getSelectedItem().toString() + " " + cmbHorarioEntrada.getSelectedItem().toString(); // Hora + AM/PM
    String horaSalida = cmbSalida.getSelectedItem().toString() + " " + cmbHorarioSalida.getSelectedItem().toString(); // Hora + AM/PM

    // Mapear días de la semana a las columnas correspondientes en la base de datos
    Map<String, String> diasMap = new HashMap<>();
    diasMap.put("LUNES", "lunes");
    diasMap.put("MARTES", "martes");
    diasMap.put("MIERCOLES", "miercoles");
    diasMap.put("JUEVES", "jueves");
    diasMap.put("VIERNES", "viernes");
    diasMap.put("SABADO", "sabado");
    diasMap.put("DOMINGO", "domingo");

    String columnaDia = diasMap.get(diaSemana);
    if (columnaDia == null) {
        JOptionPane.showMessageDialog(this, "Día seleccionado no válido.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Crear conexión a la base de datos
    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();
    if (c == null) {
        JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Consultas SQL
    String checkQuery = "SELECT COUNT(*) FROM horariopordias WHERE id = ?";
    String insertQuery = "INSERT INTO horariopordias (id, nombre, cargo, lunes, martes, miercoles, jueves, viernes, sabado, domingo) " +
                         "VALUES (?, ?, ?, NULL, NULL, NULL, NULL, NULL, NULL, NULL)";
    String updateQuery = "UPDATE horariopordias SET " + columnaDia + " = ? WHERE id = ?";

    try 
    {
        // Verificar si el registro existe en la base de datos
        PreparedStatement psCheck = c.prepareStatement(checkQuery);
        psCheck.setString(1, idEmpleado);
        ResultSet rs = psCheck.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        if (count == 0) 
        {
            // Insertar registro inicial si no existe
            PreparedStatement psInsert = c.prepareStatement(insertQuery);
            psInsert.setString(1, idEmpleado);
            psInsert.setString(2, nombreEmpleado);
            psInsert.setString(3, cargoEmpleado);
            psInsert.executeUpdate();
            psInsert.close();
        }

        // Actualizar el día seleccionado con las horas de entrada y salida
        PreparedStatement psUpdate = c.prepareStatement(updateQuery);
        psUpdate.setString(1, horaEntrada + " - " + horaSalida); // Guardar el rango de horas
        psUpdate.setString(2, idEmpleado); // ID del empleado

        // Ejecutar la actualización
        int rowsAffected = psUpdate.executeUpdate();
        if (rowsAffected > 0) 
        {
            JOptionPane.showMessageDialog(this, "Horas registradas correctamente para el día: " + diaSemana);
        } 
        
        else 
        {
            JOptionPane.showMessageDialog(this, "Error al actualizar el registro de horas.");
        }

        psUpdate.close();
        rs.close();
        psCheck.close();
        } 
    
    catch (SQLException e) 
    {
        JOptionPane.showMessageDialog(this, "Error al registrar horas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        cmbHorarioEntrada = new javax.swing.JComboBox<>();
        cmbHorarioSalida = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaObservacion = new javax.swing.JTextArea();
        btnVolver = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        cmbDiaSemana = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

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
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 540, -1, 23));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar ");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 70, -1));
        jPanel1.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 76, -1));

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Cargo", "Asistencia", "Hora Entrada", "Hora Salida", "Jornada", "Observacion"
            }
        ));
        jScrollPane1.setViewportView(tblEmpleados);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 102, 585, 366));

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
        jPanel1.add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 480, 91, 70));

        btnBuscar.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscar.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscar.setBorder(null);
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 50, 30, 40));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Hora de entrada");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 150, 115, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Hora de Salida");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 200, 101, -1));

        cmbEntrada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "9", "10", "11", "12", "1", "2" }));
        jPanel1.add(cmbEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 150, 49, -1));

        cmbSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));
        jPanel1.add(cmbSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 200, 49, -1));

        cmbAsistencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Presente", "Faltante", "Retraso", "Permiso", "Excusa" }));
        jPanel1.add(cmbAsistencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 110, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Estado");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 65, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Jornada");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 230, 69, -1));

        cmbJornada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Matutina", "Vespertina" }));
        jPanel1.add(cmbJornada, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 230, -1, -1));

        cmbHorarioEntrada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));
        jPanel1.add(cmbHorarioEntrada, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 150, 56, -1));

        cmbHorarioSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PM", "AM" }));
        jPanel1.add(cmbHorarioSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 200, 56, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Observacion");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 330, 90, -1));

        txtAreaObservacion.setColumns(20);
        txtAreaObservacion.setRows(5);
        jScrollPane2.setViewportView(txtAreaObservacion);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 370, -1, -1));

        btnVolver.setBackground(new java.awt.Color(102, 153, 255));
        btnVolver.setForeground(new java.awt.Color(102, 153, 255));
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/out50x50.png"))); // NOI18N
        btnVolver.setBorder(null);
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        jPanel1.add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, -1, 60));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Dia:");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 270, 37, -1));

        cmbDiaSemana.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO" }));
        jPanel1.add(cmbDiaSemana, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 270, -1, -1));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Buscar por iD");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 125, -1));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Volver");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, -1, 23));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 595, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        // TODO add your handling code here:
        registrarHorasPorDia();
        registrarAsistencia();
        
        
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
    private javax.swing.JComboBox<String> cmbHorarioEntrada;
    private javax.swing.JComboBox<String> cmbHorarioSalida;
    private javax.swing.JComboBox<String> cmbJornada;
    private javax.swing.JComboBox<String> cmbSalida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
