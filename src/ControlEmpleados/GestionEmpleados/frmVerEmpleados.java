/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ControlEmpleados.GestionEmpleados;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import DAO.Conexion;

/**
 *
 * @author anton
 */
public class frmVerEmpleados extends javax.swing.JFrame {

    /**
     * Creates new form frmVerEmpleados
     */
    public frmVerEmpleados() 
    {
        initComponents();
        this.setLocationRelativeTo(null);
        cargarEmpleados();
    }
    
   private void cargarEmpleados() {
        // Modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tblEmpleados.getModel();
        modelo.setRowCount(0); // Limpiar filas anteriores

        String query = "SELECT * FROM empleados"; // Consulta SQL para obtener todos los empleados

        try {
            Conexion conn = new Conexion("empleados"); // Conexión con la base de datos
            Connection c = conn.getConexion();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                // Obtiene los datos de cada columna
                String id = String.valueOf(rs.getInt("id"));
                String nombre = rs.getString("nombre");
                String telefono = rs.getString("telefono");
                String correo = rs.getString("correo");
                int edad = rs.getInt("edad");
                String cargo = rs.getString("cargo");
                String seguro = rs.getString("seguro");

                // Agrega la fila a la tabla
                modelo.addRow(new Object[]{id, nombre, telefono, correo, edad, cargo, seguro});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los empleados: " + e.getMessage());
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
        String query = "SELECT empleados.ID, empleados.Nombre, empleados.Telefono, empleados.Correo, " +
                       "empleados.Edad, empleados.Cargo, empleados.Seguro " +
                       "FROM empleados " +
                       "WHERE empleados.ID = ?";  // Filtrar por ID

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, Integer.parseInt(idEmpleado));  // Convertir el ID a entero

        ResultSet rs = ps.executeQuery();

        // Limpiar la tabla antes de agregar nuevos datos
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los resultados filtrados
        while (rs.next()) {
            Object[] row = new Object[7];
            row[0] = rs.getInt("ID");
            row[1] = rs.getString("Nombre");
            row[2] = rs.getString("Telefono");
            row[3] = rs.getString("Correo");
            row[4] = rs.getInt("Edad");
            row[5] = rs.getString("Cargo");
            row[6] = rs.getString("Seguro");

            model.addRow(row);
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron empleados para el ID proporcionado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
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
        String query = "SELECT empleados.ID, empleados.Nombre, empleados.Telefono, empleados.Correo, " +
                       "empleados.Edad, empleados.Cargo, empleados.Seguro " +
                       "FROM empleados " +
                       "WHERE empleados.Nombre LIKE ?";  // Filtrar por nombre (con LIKE)

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, "%" + nombreEmpleado + "%");  // Permitir coincidencias parciales

        ResultSet rs = ps.executeQuery();

        // Limpiar la tabla antes de agregar nuevos datos
        DefaultTableModel model = (DefaultTableModel) tblEmpleados.getModel();
        model.setRowCount(0);

        // Llenar la tabla con los resultados filtrados
        while (rs.next()) {
            Object[] row = new Object[7];
            row[0] = rs.getInt("ID");
            row[1] = rs.getString("Nombre");
            row[2] = rs.getString("Telefono");
            row[3] = rs.getString("Correo");
            row[4] = rs.getInt("Edad");
            row[5] = rs.getString("Cargo");
            row[6] = rs.getString("Seguro");

            model.addRow(row);
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No se encontraron empleados para el nombre proporcionado.", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmpleados = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnVolver2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        btnBuscarPorNombre = new javax.swing.JButton();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnBuscarPorID = new javax.swing.JButton();
        txtID = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        tblEmpleados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre", "Telefono", "Correo", "Edad", "Cargo", "Seguro"
            }
        ));
        jScrollPane1.setViewportView(tblEmpleados);

        jPanel1.setBackground(new java.awt.Color(102, 153, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Volver");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        btnVolver2.setBackground(new java.awt.Color(102, 153, 255));
        btnVolver2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/out50x50.png"))); // NOI18N
        btnVolver2.setBorder(null);
        btnVolver2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolver2ActionPerformed(evt);
            }
        });
        jPanel1.add(btnVolver2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, -1));

        jPanel2.setBackground(new java.awt.Color(102, 153, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Buscar");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 20, 70, -1));

        btnBuscarPorNombre.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscarPorNombre.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscarPorNombre.setBorder(null);
        btnBuscarPorNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorNombreActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarPorNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, -1, -1));
        jPanel2.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 20, 199, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Buscar");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 70, -1));

        btnBuscarPorID.setBackground(new java.awt.Color(102, 153, 255));
        btnBuscarPorID.setIcon(new javax.swing.ImageIcon("C:\\Users\\anton\\Downloads\\buscar 2 30x30.png")); // NOI18N
        btnBuscarPorID.setBorder(null);
        btnBuscarPorID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPorIDActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarPorID, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, -1, -1));
        jPanel2.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 55, -1));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText(" Filtrar por nombre:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 170, -1));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Filtrar por ID: ");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 130, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 876, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        cargarEmpleados();
        
    }//GEN-LAST:event_formWindowOpened

    private void btnBuscarPorIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorIDActionPerformed
        // TODO add your handling code here:
        filtrarPorID();
    }//GEN-LAST:event_btnBuscarPorIDActionPerformed

    private void btnBuscarPorNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPorNombreActionPerformed
        // TODO add your handling code here:
        filtrarPorNombre();
    }//GEN-LAST:event_btnBuscarPorNombreActionPerformed

    private void btnVolver2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolver2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
        frmGestionEmpleados frmgestionEmpleados = new frmGestionEmpleados();
   
        frmgestionEmpleados.setVisible(true);
        frmgestionEmpleados.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnVolver2ActionPerformed
    
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
            java.util.logging.Logger.getLogger(frmVerEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmVerEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmVerEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmVerEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            frmVerEmpleados frame = new frmVerEmpleados();
            frame.setVisible(true); 
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarPorID;
    private javax.swing.JButton btnBuscarPorNombre;
    private javax.swing.JButton btnVolver2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblEmpleados;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
