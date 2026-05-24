package com.example.retrofit_entrega1;

import java.io.Serializable;

public class Contrato implements Serializable {
    private int idContrato;
    private String fechaInicio;
    private String fechaFin;
    private double montoAlquiler;
    private Inquilino inquilino;
    private Inmueble inmueble;

    public int getIdContrato() { return idContrato; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public double getMontoAlquiler() { return montoAlquiler; }
    public Inquilino getInquilino() { return inquilino; }
    public Inmueble getInmueble() { return inmueble; }
}