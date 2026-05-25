package com.nirami.model;

public class Categoria {

    private int    idCategoria;
    private String nombreCategoria;
    private String descripcion;

    // ── Constructores ──────────────────────────────────────────
    public Categoria() {}

    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria     = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    // ── Getters / Setters ──────────────────────────────────────
    public int getIdCategoria()                  { return idCategoria; }
    public void setIdCategoria(int v)            { this.idCategoria = v; }

    public String getNombreCategoria()           { return nombreCategoria; }
    public void setNombreCategoria(String v)     { this.nombreCategoria = v; }

    public String getDescripcion()               { return descripcion; }
    public void setDescripcion(String v)         { this.descripcion = v; }

    @Override
    public String toString() {
        return "Categoria{id=" + idCategoria + ", nombre=" + nombreCategoria + "}";
    }
}
