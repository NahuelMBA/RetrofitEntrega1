package com.example.retrofit_entrega1;

import java.io.Serializable;

public class Inquilino implements Serializable {
    private int idInquilino;
    private String dni;
    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;
    private String email;
    private String nombreGarante;
    private String telefonoGarante;

    public int getIdInquilino() { return idInquilino; }
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getNombreGarante() { return nombreGarante; }
    public String getTelefonoGarante() { return telefonoGarante; }
}