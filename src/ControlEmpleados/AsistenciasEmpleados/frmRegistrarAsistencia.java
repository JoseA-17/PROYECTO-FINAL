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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Volver");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Buscar por iD");

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

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setText("Registro de Asistencias");

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Hora de entrada");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Hora de Salida");

        cmbEntrada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "8", "9", "10", "11", "12", "1", "2" }));

        cmbSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "12", "1", "2", "3", "4", "5", "6", "7", "8", "9" }));

        cmbAsistencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Presente", "Faltante", "Retraso", "Permiso", "Excusa" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Estado");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Jornada");

        cmbJornada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Matutina", "Vespertina" }));

        cmbHorarioEntrada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AM", "PM" }));

        cmbHorarioSalida.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "PM", "AM" }));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Observacion");

        txtAreaObservacion.setColumns(20);
        txtAreaObservacion.setRows(5);
        jScrollPane2.setViewportView(txtAreaObservacion);

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("Dia:");

        cmbDiaSemana.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(289, 289, 289)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 688, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(144, 144, 144))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbDiaSemana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(cmbJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(22, 22, 22)
                                    .addComponent(cmbSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(cmbHorarioSalida, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(cmbAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cmbEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cmbHorarioEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnVolver)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(cmbAsistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cmbEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbHorarioEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addGap(19, 19, 19)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(cmbSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbHorarioSalida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(cmbJornada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addComponent(jLabel4))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(cmbDiaSemana, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(48, 48, 48)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnVolver)
                    .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
