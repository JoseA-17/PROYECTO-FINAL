/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados.Nomina;

/**
 *
 * @author anton
 */

public class PadreNominasEmpleados 
{
    private int idEmpleado;
    private String nombreEmpleado;
    private String cargoEmpleado;
    private double horasTrabajadas;
    private double salarioBase;
    private double bonificaciones;
    private double deducciones;
    private double total;

    public PadreNominasEmpleados(int idEmpleado, String nombreEmpleado, String cargoEmpleado, double horasTrabajadas, double salarioBase, double bonificaciones, double deducciones) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.cargoEmpleado = cargoEmpleado;
        this.horasTrabajadas = horasTrabajadas;
        this.salarioBase = salarioBase;
        this.bonificaciones = bonificaciones;
        this.deducciones = deducciones;
        this.total = calcularTotal(); 
    }
    
    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getCargoEmpleado() {
        return cargoEmpleado;
    }

    public void setCargoEmpleado(String cargoEmpleado) {
        this.cargoEmpleado = cargoEmpleado;
    }

    public double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(double horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(double salarioBase) {
        this.salarioBase = salarioBase;
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

    private double calcularTotal() 
    {
        return salarioBase + bonificaciones - deducciones; 
    }
}

