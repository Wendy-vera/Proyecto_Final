package com.nirami.model;

public class DetalleVenta {

    private int    idDetalle;
    private int    idVenta;
    private int    idProducto;
    private int    cantidad;
    private double precioUnitario;

    // por JOIN
    private String nombreProducto;
    private String imagenProducto;

    public DetalleVenta() {}

    public int getIdDetalle()                    { return idDetalle; }
    public void setIdDetalle(int v)              { this.idDetalle = v; }

    public int getIdVenta()                      { return idVenta; }
    public void setIdVenta(int v)                { this.idVenta = v; }

    public int getIdProducto()                   { return idProducto; }
    public void setIdProducto(int v)             { this.idProducto = v; }

    public int getCantidad()                     { return cantidad; }
    public void setCantidad(int v)               { this.cantidad = v; }

    public double getPrecioUnitario()            { return precioUnitario; }
    public void setPrecioUnitario(double v)      { this.precioUnitario = v; }

    public String getNombreProducto()            { return nombreProducto; }
    public void setNombreProducto(String v)      { this.nombreProducto = v; }

    public String getImagenProducto()            { return imagenProducto; }
    public void setImagenProducto(String v)      { this.imagenProducto = v; }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}
