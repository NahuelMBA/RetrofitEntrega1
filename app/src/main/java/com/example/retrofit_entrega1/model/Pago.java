package com.example.retrofit_entrega1.model;

import java.io.Serializable;

public class Pago implements Serializable {
    private int idPago;
    private String fechaPago;
    private double monto;
    private String detalle;
    private boolean estado;
    private int idContrato;

    public int getIdPago() { return idPago; }
    public String getFechaPago() { return fechaPago; }
    public double getMonto() { return monto; }
    public String getDetalle() { return detalle; }
    public boolean isEstado() { return estado; }
    public int getIdContrato() { return idContrato; }
}
