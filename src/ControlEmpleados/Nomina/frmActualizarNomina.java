/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ControlEmpleados.Nomina;

import DAO.Conexion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author anton
 */
public class frmActualizarNomina extends javax.swing.JFrame 
{

    /**
     * Creates new form frmActualizarNomina
     */
    public frmActualizarNomina() 
    {
        initComponents();
        llenarTabla();
        
        if (tblNominas != null) 
        {
        tblNominas.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
        {
            public void valueChanged(ListSelectionEvent e) 
            {
                if (!e.getValueIsAdjusting()) 
                {
                    llenarCamposConDatos();
                }
            }
        });
        }
    }
            
    private void llenarTabla() 
    {
    DefaultTableModel modelo = new DefaultTableModel(); 
    modelo.addColumn("ID");
    modelo.addColumn("Nombre");
    modelo.addColumn("Cargo");
    modelo.addColumn("Horas Trabajadas");
    modelo.addColumn("Salario Base");
    modelo.addColumn("Bonificaciones");
    modelo.addColumn("Deducciones");
    modelo.addColumn("Total");

    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    try 
    {
        Conexion conexion = new Conexion("empleados"); 
        conn = conexion.getConexion();

        if (conn == null) 
        {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos.");
            return;
        }

        String query = "SELECT ID, Nombre, Cargo, HorasTrabajadas, SalarioBase, Bonificaciones, Deducciones, Total FROM nominas";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(query);

        while (rs.next()) 
        {
            Object[] fila = new Object[8];
            fila[0] = rs.getInt("ID");
            fila[1] = rs.getString("Nombre");
            fila[2] = rs.getString("Cargo");
            fila[3] = rs.getInt("HorasTrabajadas");
            fila[4] = rs.getDouble("SalarioBase");
            fila[5] = rs.getDouble("Bonificaciones");
            fila[6] = rs.getDouble("Deducciones");
            fila[7] = rs.getDouble("Total");
            modelo.addRow(fila);
        }

        tblNominas.setModel(modelo);
    } 
    catch (SQLException e) 
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar los datos.");
    } 
    finally 
    {
        try 
        {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}

    private void llenarCamposConDatos() 
    {
    int selectedRow = tblNominas.getSelectedRow();

    if (selectedRow != -1) {
        // Obtener los datos de la fila seleccionada
        String nombre = tblNominas.getValueAt(selectedRow, 1).toString(); 
        String cargo = tblNominas.getValueAt(selectedRow, 2).toString();  
        int id = Integer.parseInt(tblNominas.getValueAt(selectedRow, 0).toString());
        int horasTrabajadas = Integer.parseInt(tblNominas.getValueAt(selectedRow, 3).toString());
        double bonificaciones = Double.parseDouble(tblNominas.getValueAt(selectedRow, 5).toString()); 
        double deducciones = Double.parseDouble(tblNominas.getValueAt(selectedRow, 6).toString()); 
        double salarioNeto = Double.parseDouble(tblNominas.getValueAt(selectedRow, 7).toString()); 
        double salarioBase = Double.parseDouble(tblNominas.getValueAt(selectedRow, 4).toString()); 

        // Asignar los valores a los campos de texto
        txtNombre1.setText(nombre);
        txtID2.setText(String.valueOf(id));
        txtHorasTrabajadas1.setText(String.valueOf(horasTrabajadas));
        txtBonificaciones1.setText(String.valueOf(bonificaciones));
        txtDeducciones1.setText(String.valueOf(deducciones));
        txtSalarioNeto1.setText(String.valueOf(salarioNeto));
        txtSalarioBase1.setText(String.valueOf(salarioBase));  
    }
}


   private void actualizarNomina() 
   {
    String nombre = txtNombre1.getText().trim();
    int id = obtenerValorInt(txtID2.getText().trim());  
    int horasTrabajadas = obtenerValorInt(txtHorasTrabajadas1.getText().trim());
    double bonificaciones = obtenerValorDouble(txtBonificaciones1.getText().trim());
    double deducciones = obtenerValorDouble(txtDeducciones1.getText().trim());
    double salarioBase = obtenerValorDouble(txtSalarioBase1.getText().trim());  

    // Validar que los campos no estén vacíos
    if (nombre.isEmpty() || id == 0 || horasTrabajadas == 0 || salarioBase == 0) 
    {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos correctamente.");
        return;
    }

    // Verificar si el empleado existe en la base de datos
    if (!empleadoExistePorID(id)) 
    {
        JOptionPane.showMessageDialog(this, "Empleado no encontrado.");
        return;
    }

    // Actualizar la nomina en la base de datos
    Connection conn = null;
    PreparedStatement pst = null;
    try 
    {
        Conexion conexion = new Conexion("empleados");
        conn = conexion.getConexion();

        // SQL para actualizar la nomina
        String query = "UPDATE nominas SET Nombre = ?, HorasTrabajadas = ?, Bonificaciones = ?, Deducciones = ?, SalarioBase = ? WHERE ID = ?";
        pst = conn.prepareStatement(query);
        pst.setString(1, nombre);
        pst.setInt(2, horasTrabajadas);
        pst.setDouble(3, bonificaciones);
        pst.setDouble(4, deducciones);
        pst.setDouble(5, salarioBase);  // Actualizamos el SalarioBase con el valor del nuevo campo
        pst.setInt(6, id);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Nómina actualizada correctamente.");
    } 
    
    catch (SQLException e) 
    {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al actualizar la nómina.");
    } 
    
    finally 
    {
        try 
        {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } 
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}


//convertir datos del los text field a numeros
private int obtenerValorInt(String valor) 
{
    try 
    {
        return Integer.parseInt(valor);
    }
    
    catch (NumberFormatException e) 
    {
        return 0; 
    }
}

private double obtenerValorDouble(String texto) 
{
    if (texto.isEmpty()) 
    {
        return 0.0; 
    }
    try 
    {
        return Double.parseDouble(texto);
    } 
    
    catch (NumberFormatException e) 
    {
        return 0.0; 
    }
}


    
public BigDecimal obtenerSalarioBase(String cargo) 
{
        BigDecimal salarioBase = BigDecimal.ZERO;
        String sql = "SELECT salario_base FROM cargos WHERE nombre = ?";
        
        try (Connection conn = new Conexion("empleados").getConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cargo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) 
            {
                salarioBase = rs.getBigDecimal("salario_base");
            } 
        } 
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        
        return salarioBase;
    }

// Método para verificar si un empleado existe por su ID
private boolean empleadoExistePorID(int idEmpleado) 
{
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    try 
    {
        Conexion conexion = new Conexion("empleados");
        conn = conexion.getConexion();

        String query = "SELECT * FROM empleados WHERE ID = ?";
        pst = conn.prepareStatement(query);
        pst.setInt(1, idEmpleado);

        rs = pst.executeQuery();
        return rs.next(); // Si encuentra un resultado, devuelve true
    } 
    
    catch (SQLException e) 
    {
        e.printStackTrace();
        return false;
    } 
    
    finally 
    {
        try 
        {
            if (rs != null) rs.close();
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        } 
        
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
}

    private void filtrarPorID() 
    {
    String idEmpleado = txtID.getText().trim();

    if (idEmpleado.isEmpty()) 
    {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();

    if (c == null) 
    {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try 
    {
        String query = "SELECT asistencias.id, empleados.nombre, empleados.cargo, " +
                       "asistencias.asistencia, asistencias.entrada, asistencias.salida, asistencias.observacion " +
                       "FROM asistencias " +
                       "INNER JOIN empleados ON asistencias.id = empleados.id " +
                       "WHERE empleados.id = ?";  // Filtrar por ID

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(idEmpleado)); 

        ResultSet rs = ps.executeQuery();
        
        //limpiar tabla
        DefaultTableModel model = (DefaultTableModel) tblNominas.getModel();
        model.setRowCount(0);

        //llenar despues de filtrar
        while (rs.next()) 
        {
            Object[] row = new Object[7];
            row[0] = rs.getInt("id");
            row[1] = rs.getString("nombre");
            row[2] = rs.getString("cargo");
            row[3] = rs.getString("asistencia");
            row[4] = rs.getString("entrada");
            row[5] = rs.getString("salida");
            row[6] = rs.getString("observacion");

            model.addRow(row);
        }

        if (model.getRowCount() == 0) 
        {
            JOptionPane.showMessageDialog(this, "No se encontraron asistencias para el ID proporcionado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }

        rs.close();
        ps.close();
    } 
    
    catch (SQLException e) 
    {
        JOptionPane.showMessageDialog(this, "Error al realizar la consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } 
    
    catch (NumberFormatException e) 
    {
        JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
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

private void filtrarPorNombre() 
{
    // Obtener el texto del campo txtNombre
    String nombreEmpleado = txtNombre.getText().trim();

    if (nombreEmpleado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un nombre para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();

    if (c == null) 
    {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try 
    {
        String query = "SELECT asistencias.id, empleados.nombre, empleados.cargo, " +
                       "asistencias.asistencia, asistencias.entrada, asistencias.salida, asistencias.observacion " +
                       "FROM asistencias " +
                       "INNER JOIN empleados ON asistencias.id = empleados.id " +
                       "WHERE empleados.nombre LIKE ?"; 

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, "%" + nombreEmpleado + "%"); 

        ResultSet rs = ps.executeQuery();

        DefaultTableModel model = (DefaultTableModel) tblNominas.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los resultados filtrados
        while (rs.next())
        {
            Object[] row = new Object[7];
            row[0] = rs.getInt("id");
            row[1] = rs.getString("nombre");
            row[2] = rs.getString("cargo");
            row[3] = rs.getString("asistencia");
            row[4] = rs.getString("entrada");
            row[5] = rs.getString("salida");
            row[6] = rs.getString("observacion");

            model.addRow(row);
        }

        if (model.getRowCount() == 0) 
        {
            JOptionPane.showMessageDialog(this, "No se encontraron asistencias para el nombre proporcionado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }

        rs.close();
        ps.close();
    } 
    
    catch (SQLException e) 
    {
        JOptionPane.showMessageDialog(this, "Error al realizar la consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        txtEmpleado = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtID1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtHorasTrabajadas = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtBonificaciones = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtDeducciones = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtSalarioBase = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNominas = new javax.swing.JTable();
        btnVolver1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        btnBuscarPorID = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnBuscarPorNombre = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNombre1 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtID2 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtHorasTrabajadas1 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtBonificaciones1 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtDeducciones1 = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtSalarioNeto1 = new javax.swing.JTextField();
        btnActualizar = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        txtSalarioBase1 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Nombre");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("ID");

        txtID1.setText(" ");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setText("Horas Trabajadas");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setText("Bonificaciones");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel12.setText("Deducciones");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel13.setText("Salario Neto");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Salario Base");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Volver");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(48, 514, -1, -1));

        tblNominas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Cargo", "Salario base", "Horas trabajadas", "Bonificaciones", "Deducciones", "Total"
            }
        ));
        jScrollPane1.setViewportView(tblNominas);
        if (tblNominas.getColumnModel().getColumnCount() > 0) {
            tblNominas.getColumnModel().getColumn(7).setResizable(false);
        }

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 151, 769, 295));

        btnVolver1.setBackground(new java.awt.Color(102, 153, 255));
        btnVolver1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/out50x50.png"))); // NOI18N
        btnVolver1.setBorder(null);
        btnVolver1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolver1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnVolver1, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 458, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Actualizacion de Nominas");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 0, -1, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText(" Filtrar por nombre:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 64, -1, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Filtrar por ID: ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(56, 104, -1, -1));
        jPanel1.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(186, 64, 199, -1));
        jPanel1.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(186, 104, 55, -1));

        btnBuscarPorID.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscarPorID.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscarPorID.setBorder(null);
        btnBuscarPorID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorIDActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarPorID, new org.netbeans.lib.awtextra.AbsoluteConstraints(246, 94, -1, -1));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Buscar");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(286, 104, 60, -1));

        btnBuscarPorNombre.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscarPorNombre.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscarPorNombre.setBorder(null);
        btnBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorNombreActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarPorNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(396, 54, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Buscar");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(436, 64, 60, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Datos Para Actualizar");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 130, -1, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nombre");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(781, 183, 81, -1));
        jPanel1.add(txtNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(868, 183, 209, -1));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("ID");
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1083, 183, -1, -1));

        txtID2.setText(" ");
        jPanel1.add(txtID2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 183, 31, -1));

        jLabel17.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Horas Trabajadas");
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(781, 217, -1, -1));
        jPanel1.add(txtHorasTrabajadas1, new org.netbeans.lib.awtextra.AbsoluteConstraints(939, 217, 51, -1));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Bonificaciones");
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(781, 251, -1, -1));
        jPanel1.add(txtBonificaciones1, new org.netbeans.lib.awtextra.AbsoluteConstraints(913, 251, 67, -1));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Deducciones");
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(781, 291, 126, -1));
        jPanel1.add(txtDeducciones1, new org.netbeans.lib.awtextra.AbsoluteConstraints(919, 291, 73, -1));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Salario Neto");
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(781, 366, 118, -1));
        jPanel1.add(txtSalarioNeto1, new org.netbeans.lib.awtextra.AbsoluteConstraints(911, 366, 81, -1));

        btnActualizar.setBackground(new java.awt.Color(102, 153, 255));
        btnActualizar.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\actualizado 50x50).png")); // NOI18N
        btnActualizar.setBorder(null);
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        jPanel1.add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 450, 67, -1));

        jLabel21.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Salario Base");
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(781, 331, -1, -1));
        jPanel1.add(txtSalarioBase1, new org.netbeans.lib.awtextra.AbsoluteConstraints(906, 331, 86, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Actualizar");
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 510, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1185, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVolver1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolver1ActionPerformed
         this.dispose(); 
        frmGestionSalarios frame = new frmGestionSalarios();
        frame.setVisible(true);
    }//GEN-LAST:event_btnVolver1ActionPerformed

    private void btnBuscarPorIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorIDActionPerformed
        // TODO add your handling code here:
        filtrarPorID();
    }//GEN-LAST:event_btnBuscarPorIDActionPerformed

    private void btnBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorNombreActionPerformed
        // TODO add your handling code here:
        filtrarPorNombre();
    }//GEN-LAST:event_btnBuscarPorNombreActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        actualizarNomina();
    }//GEN-LAST:event_btnActualizarActionPerformed

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
            java.util.logging.Logger.getLogger(frmActualizarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmActualizarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmActualizarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmActualizarNomina.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmActualizarNomina().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscarPorID;
    private javax.swing.JButton btnBuscarPorNombre;
    private javax.swing.JButton btnVolver1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNominas;
    private javax.swing.JTextField txtBonificaciones;
    private javax.swing.JTextField txtBonificaciones1;
    private javax.swing.JTextField txtDeducciones;
    private javax.swing.JTextField txtDeducciones1;
    private javax.swing.JTextField txtEmpleado;
    private javax.swing.JTextField txtHorasTrabajadas;
    private javax.swing.JTextField txtHorasTrabajadas1;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtID1;
    private javax.swing.JTextField txtID2;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombre1;
    private javax.swing.JTextField txtSalarioBase;
    private javax.swing.JTextField txtSalarioBase1;
    private javax.swing.JTextField txtSalarioNeto1;
    private javax.swing.JTextField txtTotal;
    // End of variables declaration//GEN-END:variables
}
