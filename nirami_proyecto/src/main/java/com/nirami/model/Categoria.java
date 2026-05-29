package com.nirami.model;

public class Categoria {

    private int    idCategoria;
    private String nombreCategoria;
    private String descripcion;
<<<<<<< HEAD
    private boolean activa = true;

=======

    // ── Constructores ──────────────────────────────────────────
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
    public Categoria() {}

    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria     = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

<<<<<<< HEAD
=======
    // ── Getters / Setters ──────────────────────────────────────
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
    public int getIdCategoria()                  { return idCategoria; }
    public void setIdCategoria(int v)            { this.idCategoria = v; }

    public String getNombreCategoria()           { return nombreCategoria; }
    public void setNombreCategoria(String v)     { this.nombreCategoria = v; }

    public String getDescripcion()               { return descripcion; }
    public void setDescripcion(String v)         { this.descripcion = v; }

<<<<<<< HEAD
    public boolean isActiva()                    { return activa; }
    public void setActiva(boolean v)             { this.activa = v; }

=======
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
    @Override
    public String toString() {
        return "Categoria{id=" + idCategoria + ", nombre=" + nombreCategoria + "}";
    }
<<<<<<< HEAD

    public void setThemeDescription(String descripcion) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
=======
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
}
