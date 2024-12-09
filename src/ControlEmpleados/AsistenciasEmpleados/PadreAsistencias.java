/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados.AsistenciasEmpleados;

/**
 *
 * @author anton
 */
public class PadreAsistencias 
{
    private String nombre;
    private String cargo;
    private String estadoAsistencia; // Presente, Ausente, Permiso, etc.
    private String horaEntrada;
    private String horaSalida;
    private String observaciones;

    public PadreAsistencias(String nombre, String cargo, String estadoAsistencia, String horaEntrada, String horaSalida, String observaciones) {
        this.nombre = nombre;
        this.cargo = cargo;
        this.estadoAsistencia = estadoAsistencia;
        this.horaEntrada = horaEntrada;
        this.horaSalida = horaSalida;
        this.observaciones = observaciones;
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

    public String getEstadoAsistencia() {
        return estadoAsistencia;
    }

    public void setEstadoAsistencia(String estadoAsistencia) {
        this.estadoAsistencia = estadoAsistencia;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    
}
