/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ControlEmpleados.AsistenciasEmpleados;

import DAO.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author anton
 */
public class frmEliminarAsistencia extends javax.swing.JFrame {

    /**
     * Creates new form frmEliminarAsistencia
     */
    public frmEliminarAsistencia() {
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
            idEmpleadoSeleccionado = tblEmpleados.getValueAt(row, 0).toString(); // Asigna el ID del empleado desde la primera columna
            System.out.println("ID del empleado seleccionado: " + idEmpleadoSeleccionado);    
        }
    });
}


    private void eliminarAsistencia() {
    // Validar que se haya seleccionado un empleado
    if (idEmpleadoSeleccionado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un empleado de la tabla.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Crear conexión
    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();
    
    if (c == null) {
        JOptionPane.showMessageDialog(this, "No se pudo establecer conexión con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Consulta SQL para eliminar la asistencia
    String deleteQuery = "DELETE FROM asistencias WHERE ID = ?";

    try {
        PreparedStatement psDelete = c.prepareStatement(deleteQuery);
        psDelete.setString(1, idEmpleadoSeleccionado); // Usamos el ID del empleado seleccionado
        int rowsAffected = psDelete.executeUpdate();
        
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Asistencia eliminada correctamente para el empleado con ID: " + idEmpleadoSeleccionado);
            // Actualizar la tabla después de eliminar la asistencia
            cargarTablaAsistencias();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar la asistencia del empleado.");
        }

        psDelete.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar la asistencia: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
        try {
            if (c != null) c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    
    private void cargarTablaAsistencias() {
    // Obtener la conexión
    Conexion conn = new Conexion("empleados"); // Usa la base de datos correcta
    Connection c = conn.getConexion();

    if (c == null) {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Crear consulta para traer todos los datos
        String query = "SELECT id, nombre, cargo, asistencia, entrada, salida, observacion, LUN, MAR, MIE, JUE, VIE, SAB, DOM FROM asistencias";
        Statement stmt = c.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Limpiar la tabla antes de cargar nuevos datos
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los resultados
        while (rs.next()) {
            Object[] row = new Object[14]; // 14 columnas en la tabla
            row[0] = rs.getInt("id");
            row[1] = rs.getString("nombre");
            row[2] = rs.getString("cargo");
            row[3] = rs.getString("asistencia");
            row[4] = rs.getString("entrada");
            row[5] = rs.getString("salida");
            row[6] = rs.getString("observacion");
            row[7] = rs.getString("LUN");
            row[8] = rs.getString("MAR");
            row[9] = rs.getString("MIE");
            row[10] = rs.getString("JUE");
            row[11] = rs.getString("VIE");
            row[12] = rs.getString("SAB");
            row[13] = rs.getString("DOM");

            // Agregar la fila al modelo
            model.addRow(row);
        }

        rs.close();
        stmt.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private void filtrarPorID() {
    // Obtener el texto del campo txtID
    String idEmpleado = txtID.getText().trim();

    if (idEmpleado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obtener la conexión
    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();

    if (c == null) {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Crear la consulta SQL para filtrar por ID
        String query = "SELECT asistencias.id, empleados.nombre, empleados.cargo, " +
                       "asistencias.asistencia, asistencias.entrada, asistencias.salida, asistencias.observacion " +
                       "FROM asistencias " +
                       "INNER JOIN empleados ON asistencias.id = empleados.id " +
                       "WHERE empleados.id = ?";  // Filtrar por ID

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(idEmpleado));  // Convertir el ID a entero

        ResultSet rs = ps.executeQuery();

        // Limpiar la tabla antes de agregar nuevos datos
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los resultados filtrados
        while (rs.next()) {
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

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron asistencias para el ID proporcionado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al realizar la consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private void filtrarPorNombre() {
    // Obtener el texto del campo txtNombre
    String nombreEmpleado = txtNombre.getText().trim();

    if (nombreEmpleado.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese un nombre para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Obtener la conexión
    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();

    if (c == null) {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Crear la consulta SQL para filtrar por nombre
        String query = "SELECT asistencias.id, empleados.nombre, empleados.cargo, " +
                       "asistencias.asistencia, asistencias.entrada, asistencias.salida, asistencias.observacion " +
                       "FROM asistencias " +
                       "INNER JOIN empleados ON asistencias.id = empleados.id " +
                       "WHERE empleados.nombre LIKE ?";  // Filtrar por nombre (con LIKE)

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, "%" + nombreEmpleado + "%");  // Permitir coincidencias parciales

        ResultSet rs = ps.executeQuery();

        // Limpiar la tabla antes de agregar nuevos datos
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los resultados filtrados
        while (rs.next()) {
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

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron asistencias para el nombre proporcionado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }

        rs.close();
        ps.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al realizar la consulta: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private void eliminarAsistenciasSemanales() {
    int respuesta = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que deseas eliminar todas las asistencias semanales?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (respuesta != JOptionPane.YES_OPTION) {
        return;
    }

    // Obtener la conexión
    Conexion conn = new Conexion("empleados");
    Connection c = conn.getConexion();

    if (c == null) {
        JOptionPane.showMessageDialog(this, "Error: No se pudo conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    try {
        // Eliminar registros de la tabla asistencias
        String deleteAsistenciasQuery = "DELETE FROM asistencias";
        PreparedStatement psAsistencias = c.prepareStatement(deleteAsistenciasQuery);
        psAsistencias.executeUpdate();
        psAsistencias.close();

        // Eliminar registros de la tabla horapordias
        String deleteHorapordiasQuery = "DELETE FROM horariopordias";
        PreparedStatement psHorapordias = c.prepareStatement(deleteHorapordiasQuery);
        psHorapordias.executeUpdate();
        psHorapordias.close();

        // Limpiar la tabla local de NetBeans (asumiendo que tblEmpleados es la tabla local)
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0);

        JOptionPane.showMessageDialog(this, "Asistencias semanales eliminadas exitosamente.");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al eliminar asistencias: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        btnBuscarPorNombre = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        btnVolver = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        btnBuscarPorID = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnEliminarSemanales = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Eliminar Asistencias");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 10, 220, -1));

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Cargo", "Hora Entrada", "Hora Salida", " Jornada", "Observacion", "LUN", "MAR", "MIE", "JUE", "VIE", "SAB", "DOM"
            }
        ));
        jScrollPane1.setViewportView(tblEmpleados);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 830, 275));

        btnBuscarPorNombre.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscarPorNombre.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscarPorNombre.setBorder(null);
        btnBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorNombreActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarPorNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText(" Filtrar por nombre:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 176, -1));
        jPanel2.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 199, -1));

        btnVolver.setBackground(new java.awt.Color(102, 153, 255));
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/out50x50.png"))); // NOI18N
        btnVolver.setBorder(null);
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        jPanel2.add(btnVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 450, -1, 60));
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(785, 41, 37, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Filtrar por ID: ");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 140, -1));

        txtID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDActionPerformed(evt);
            }
        });
        jPanel2.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 55, -1));

        btnBuscarPorID.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscarPorID.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscarPorID.setBorder(null);
        btnBuscarPorID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorIDActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarPorID, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, -1, -1));

        jButton1.setBackground(new java.awt.Color(102, 153, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/delete user 50x50.png"))); // NOI18N
        jButton1.setBorder(null);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 450, -1, 50));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Eliminar");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 510, -1, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Buscar");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 110, -1, -1));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Volver ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 510, -1, -1));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Buscar");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 70, -1, -1));

        btnEliminarSemanales.setText("Eliminar las asistencias semanales");
        btnEliminarSemanales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarSemanalesActionPerformed(evt);
            }
        });
        jPanel2.add(btnEliminarSemanales, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 460, -1, 60));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 830, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorNombreActionPerformed
        // TODO add your handling code here:
        filtrarPorNombre();
    }//GEN-LAST:event_btnBuscarPorNombreActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        this.dispose();
        frmAsistenciasEmpleado formAsistencias = new frmAsistenciasEmpleado();

        formAsistencias.setVisible(true);

        formAsistencias.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnBuscarPorIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorIDActionPerformed
        // TODO add your handling code here:
        filtrarPorID();
    }//GEN-LAST:event_btnBuscarPorIDActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        eliminarAsistencia();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDActionPerformed

    private void btnEliminarSemanalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarSemanalesActionPerformed
        // TODO add your handling code here:
        eliminarAsistenciasSemanales();
    }//GEN-LAST:event_btnEliminarSemanalesActionPerformed

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
            java.util.logging.Logger.getLogger(frmEliminarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmEliminarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmEliminarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmEliminarAsistencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmEliminarAsistencia().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPorID;
    private javax.swing.JButton btnBuscarPorNombre;
    private javax.swing.JButton btnEliminarSemanales;
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
