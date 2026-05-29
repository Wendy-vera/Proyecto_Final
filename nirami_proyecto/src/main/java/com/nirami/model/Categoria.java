package com.nirami.model;

public class Categoria {

    private int    idCategoria;
    private String nombreCategoria;
    private String descripcion;
    private boolean activa = true;

    public Categoria() {}

    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria     = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdCategoria()                  { return idCategoria; }
    public void setIdCategoria(int v)            { this.idCategoria = v; }

    public String getNombreCategoria()           { return nombreCategoria; }
    public void setNombreCategoria(String v)     { this.nombreCategoria = v; }

    public String getDescripcion()               { return descripcion; }
    public void setDescripcion(String v)         { this.descripcion = v; }

    public boolean isActiva()                    { return activa; }
    public void setActiva(boolean v)             { this.activa = v; }

    @Override
    public String toString() {
        return "Categoria{id=" + idCategoria + ", nombre=" + nombreCategoria + "}";
    }

    public void setThemeDescription(String descripcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
