/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JDBC;

/**
 *
 * @author anton
 */

public class Nomina {
    private int id; // Cambiar a minúsculas
    private String nombre;
    private String cargo;
    private int horasTrabajadas;
    private double bonificaciones;
    private double deducciones;
    private double total;

    // Constructor
    public Nomina(int id, String nombre, String cargo, int horasTrabajadas, double bonificaciones, double deducciones, double total) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.horasTrabajadas = horasTrabajadas;
        this.bonificaciones = bonificaciones;
        this.deducciones = deducciones;
        this.total = total;
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public double getBonificaciones() {
        return bonificaciones;
    }

    public void setBonificaciones(double bonificaciones) {
        this.bonificaciones = bonificaciones;
    }

    public double getDeducciones() {
        return deducciones;
    }

    public void setDeducciones(double deducciones) {
        this.deducciones = deducciones;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}


