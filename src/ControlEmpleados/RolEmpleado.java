/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados;

/**
 *
 * @author anton
 */
public enum RolEmpleado {
    SUPERVISOR(500.00),
    INGENIERO(450.00),
    TECNICO(350.00),
    CAJERO(300.00),
    SOPORTE(280.00);

    private final double salarioBase;

    RolEmpleado(double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public double getSalarioBase() {
        return salarioBase;
    }
}

