/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados;

/**
 *
 * @author anton
 */
public class Empleado 
{
    private int id;
    private String nombre;
    private String cargo;
    private String telefono;
    private String correo;
    private int edad;
    private boolean seguro;

    public Empleado(String nombre, String cargo, String telefono, String correo, int edad, boolean seguro) 
    {
        this.nombre = nombre;
        this.cargo = cargo;
        this.telefono = telefono;
        this.correo = correo;
        this.edad = edad;
        this.seguro = seguro;
    }

    public int getId() 
    {
        return id;
    }

    public void setId(int id) 
    {
        this.id = id;
    }
    
    public String getNombre() 
    {
        return nombre;
    }

    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }

    public String getCargo() 
    {
        return cargo;
    }

    public void setCargo(String cargo) 
    {
        this.cargo = cargo;
    }

    public String getTelefono() 
    {
        return telefono;
    }

    public void setTelefono(String telefono) 
    {
        this.telefono = telefono;
    }

    public String getCorreo() 
    {
        return correo;
    }

    public void setCorreo(String correo) 
    {
        this.correo = correo;
    }

    public int getEdad() 
    {
        return edad;
    }

    public void setEdad(int edad) 
    {
        this.edad = edad;
    }

    public boolean isSeguro() 
    {
        return seguro;
    }

    public void setSeguro(boolean seguro) 
    {
        this.seguro = seguro;
    }
}
