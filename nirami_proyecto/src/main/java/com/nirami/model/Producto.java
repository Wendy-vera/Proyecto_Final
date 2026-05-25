package com.nirami.model;

public class Producto {

    private int       idProducto;
    private String    nombreProducto;
    private String    descripcionProducto;
    private double    precioProducto;
    private int       cantidadProducto;
    private String    imagenProducto;   // nombre del archivo guardado en imgproductos/
    private int       idCategoria;
    private int       idVendedor;
    private Categoria categoria;        // cargado por join en los DAOs

    // ── Constructores ──────────────────────────────────────────
    public Producto() {}

    // ── Getters / Setters ──────────────────────────────────────
    public int getIdProducto()                   { return idProducto; }
    public void setIdProducto(int v)             { this.idProducto = v; }

    public String getNombreProducto()            { return nombreProducto; }
    public void setNombreProducto(String v)      { this.nombreProducto = v; }

    public String getDescripcionProducto()       { return descripcionProducto; }
    public void setDescripcionProducto(String v) { this.descripcionProducto = v; }

    public double getPrecioProducto()            { return precioProducto; }
    public void setPrecioProducto(double v)      { this.precioProducto = v; }

    public int getCantidadProducto()             { return cantidadProducto; }
    public void setCantidadProducto(int v)       { this.cantidadProducto = v; }

    public String getImagenProducto()            { return imagenProducto; }
    public void setImagenProducto(String v)      { this.imagenProducto = v; }

    public int getIdCategoria()                  { return idCategoria; }
    public void setIdCategoria(int v)            { this.idCategoria = v; }

    public int getIdVendedor()                   { return idVendedor; }
    public void setIdVendedor(int v)             { this.idVendedor = v; }

    public Categoria getCategoria()              { return categoria; }
    public void setCategoria(Categoria v)        { this.categoria = v; }

    @Override
    public String toString() {
        return "Producto{id=" + idProducto + ", nombre=" + nombreProducto + "}";
    }
}
