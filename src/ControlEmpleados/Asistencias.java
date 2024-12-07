/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados;

/**
 *
 * @author anton xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 */

import java.time.LocalDate;

public class Asistencias {
    private int empleadoId;
    private LocalDate fecha;
    private boolean diaCompleto;

    public Asistencias(int empleadoId, LocalDate fecha, boolean diaCompleto) {
        this.empleadoId = empleadoId;
        this.fecha = fecha;
        this.diaCompleto = diaCompleto;
    }

    // Métodos getter y setter

    public int getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(int empleadoId) {
        this.empleadoId = empleadoId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public boolean isDiaCompleto() {
        return diaCompleto;
    }

    public void setDiaCompleto(boolean diaCompleto) {
        this.diaCompleto = diaCompleto;
    }
    
}
