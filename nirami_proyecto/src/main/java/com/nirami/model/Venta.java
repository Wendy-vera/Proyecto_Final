package com.nirami.model;

import java.time.LocalDateTime;
import java.util.List;

public class Venta {

    private int           idVenta;
    private int           idCliente;
    private LocalDateTime fechaVenta;
    private double        total;
    private String        estado;   // Pendiente, Completada, Cancelada

    // campos por JOIN
    private String        nombreCliente;
    private List<DetalleVenta> detalles;

    public Venta() {}

    public int getIdVenta()                      { return idVenta; }
    public void setIdVenta(int v)                { this.idVenta = v; }

    public int getIdCliente()                    { return idCliente; }
    public void setIdCliente(int v)              { this.idCliente = v; }

    public LocalDateTime getFechaVenta()         { return fechaVenta; }
    public void setFechaVenta(LocalDateTime v)   { this.fechaVenta = v; }

    public double getTotal()                     { return total; }
    public void setTotal(double v)               { this.total = v; }

    public String getEstado()                    { return estado; }
    public void setEstado(String v)              { this.estado = v; }

    public String getNombreCliente()             { return nombreCliente; }
    public void setNombreCliente(String v)       { this.nombreCliente = v; }

    public List<DetalleVenta> getDetalles()      { return detalles; }
    public void setDetalles(List<DetalleVenta> v){ this.detalles = v; }
}
