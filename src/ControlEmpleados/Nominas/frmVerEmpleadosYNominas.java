/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ControlEmpleados.Nominas;

import ControlEmpleados.GestionEmpleados.frmGestionEmpleados;
import DAO.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Kelly
 */
public class frmVerEmpleadosYNominas extends javax.swing.JFrame {

    /**
     * Creates new form frmVerEmpleadosYNominas
     */
    public frmVerEmpleadosYNominas() {
        initComponents();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnVolver1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        btnBuscarPorID = new javax.swing.JButton();
        btnBuscarPorNombre = new javax.swing.JButton();

        btnVolver1.setBackground(new java.awt.Color(102, 153, 255));
        btnVolver1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/out50x50.png"))); // NOI18N
        btnVolver1.setBorder(null);
        btnVolver1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolver1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Historial de Empleados y Nominas");

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Cargo", "Horas trabajadas", "Salario base", "Bonificaciones", "Deducciones", "Total"
            }
        ));
        jScrollPane1.setViewportView(tblEmpleados);

        jButton2.setBackground(new java.awt.Color(102, 153, 255));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Volver");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        btnVolver.setBackground(new java.awt.Color(102, 153, 255));
        btnVolver.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/out50x50.png"))); // NOI18N
        btnVolver.setBorder(null);
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        jLabel2.setText(" Filtrar por nombre:");

        jLabel4.setText("Filtrar por ID: ");

        btnBuscarPorID.setText("Buscar");
        btnBuscarPorID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorIDActionPerformed(evt);
            }
        });

        btnBuscarPorNombre.setText("Buscar");
        btnBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorNombreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscarPorID))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnBuscarPorNombre)))))
                .addContainerGap(915, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnVolver)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(jButton2)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarPorNombre)
                    .addComponent(jLabel2)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarPorID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)
                .addGap(40, 40, 40)
                .addComponent(btnVolver)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2)
                .addGap(25, 25, 25))
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

    private void btnVolver1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolver1ActionPerformed
        // TODO add your handling code here:
        this.dispose();  // Cierra el formulario actual
        frmGestionSalarios frame = new frmGestionSalarios();
        frame.setVisible(true);
    }//GEN-LAST:event_btnVolver1ActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
        this.dispose();  // Cierra el formulario actual
        frmGestionEmpleados frame = new frmGestionEmpleados();
        frame.setVisible(true);
    }//GEN-LAST:event_btnVolverActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBuscarPorIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorIDActionPerformed
        // TODO add your handling code here:
        filtrarPorID();
    }//GEN-LAST:event_btnBuscarPorIDActionPerformed

    private void btnBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorNombreActionPerformed
        // TODO add your handling code here:
        filtrarPorNombre();
    }//GEN-LAST:event_btnBuscarPorNombreActionPerformed

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
            java.util.logging.Logger.getLogger(frmVerEmpleadosYNominas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVerEmpleadosYNominas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVerEmpleadosYNominas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVerEmpleadosYNominas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmVerEmpleadosYNominas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPorID;
    private javax.swing.JButton btnBuscarPorNombre;
    private javax.swing.JButton btnVolver;
    private javax.swing.JButton btnVolver1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
