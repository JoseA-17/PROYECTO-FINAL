/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados.GestionEmpleados;

import ControlEmpleados.Empleado;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author axl
 */
public class EmpleadosManager 
{
    private static List<Empleado> empleados = new ArrayList<>();
    private static int contadorId = 1; // Variable estática para el contador de IDs

    // Método para agregar un empleado
    public static void agregarEmpleado(Empleado emp) {
        emp.setId(contadorId); // Asignar el ID al empleado
        empleados.add(emp);
        contadorId++; // Incrementar el contador para el siguiente ID
    }

    public static List<Empleado> obtenerEmpleados() {
    return empleados;
}

public static boolean eliminarEmpleado(int idEmpleado) {
    for (int i = 0; i < empleados.size(); i++) {
        Empleado emp = empleados.get(i);
        if (emp.getId() == idEmpleado) {
            empleados.remove(i); // Eliminar por índice
            return true; // Retornar true porque se eliminó exitosamente
        }
    }
    return false; // Retornar false si no se encontró al empleado
}

public static Empleado obtenerEmpleadoPorId(int idEmpleado) {
    for (Empleado emp : empleados) {
        if (emp.getId() == idEmpleado) {
            return emp;
        }
    }
    return null;
}
// Método para obtener todos los empleados
    public static List<Empleado> obtenerTodosEmpleados() {
        return empleados;
    }
    
 // Método para modificar un empleado
    public static boolean modificarEmpleado(int id, String nombre, String cargo, String telefono, String correo, int edad, boolean seguro) {
        Empleado empleado = obtenerEmpleadoPorId(id);
        if (empleado != null) {
            empleado.setNombre(nombre);
            empleado.setCargo(cargo);
            empleado.setTelefono(telefono);
            empleado.setCorreo(correo);
            empleado.setEdad(edad);
            empleado.setSeguro(seguro);
            return true;
        }
        return false;
    }


}
