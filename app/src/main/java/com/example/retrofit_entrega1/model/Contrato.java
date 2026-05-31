package com.example.retrofit_entrega1.model;

import java.io.Serializable;

public class Contrato implements Serializable {
    private int idContrato;
    private String fechaInicio;
    private String fechaFinalizacion;
    private double montoAlquiler;
    private boolean estado;
    private int idInquilino;
    private int idInmueble;
    private Inquilino inquilino;
    private Inmueble inmueble;

    public int getIdContrato() { return idContrato; }
    public String getFechaInicio() { return fechaInicio; }
    public String getFechaFinalizacion() { return fechaFinalizacion; }
    public double getMontoAlquiler() { return montoAlquiler; }
    public boolean isEstado() { return estado; }
    public int getIdInquilino() { return idInquilino; }
    public int getIdInmueble() { return idInmueble; }
    public Inquilino getInquilino() { return inquilino; }
    public Inmueble getInmueble() { return inmueble; }
}
