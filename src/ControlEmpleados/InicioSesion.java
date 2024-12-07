/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControlEmpleados;

/**
 *
 * @author anton
 */
public class InicioSesion {
    private String username;
    private String password; // Podrías agregar cifrado para seguridad.

    public InicioSesion(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean validarCredenciales(String usernameInput, String passwordInput) {
        return this.username.equals(usernameInput) && this.password.equals(passwordInput);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}

   
