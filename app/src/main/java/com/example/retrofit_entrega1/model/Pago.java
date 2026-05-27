package com.example.retrofit_entrega1.model;

import java.io.Serializable;

public class Pago implements Serializable {
    private int idPago;
    private int nroPago;
    private int idAlquiler;
    private String fecha;
    private double importe;

    public int getIdPago() { return idPago; }
    public int getNroPago() { return nroPago; }
    public int getIdAlquiler() { return idAlquiler; }
    public String getFecha() { return fecha; }
    public double getImporte() { return importe; }
}