/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados;

/**
 *
 * @author anton
 */
public class Nominas {
    private int empleadoId;
    private double salarioFinal;

    public Nominas(int empleadoId, double salarioFinal) {
        this.empleadoId = empleadoId;
        this.salarioFinal = salarioFinal;
    }

    public static double calcularSalario(double salarioBase, int horasTrabajadas) {
        return salarioBase * (horasTrabajadas / 160.0); // Basado en un mes de 160 horas
    }

    // Métodos getter y setter

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public double getSalarioFinal() {
        return salarioFinal;
    }

    public void setSalarioFinal(double salarioFinal) {
        this.salarioFinal = salarioFinal;
    }
    
}

