package com.nirami.model;

import java.time.LocalDateTime;

public class Usuario {

    private int           idUsuario;
    private int           idRol;
    private String        nombreUsuario;
    private String        contrasena;
    private LocalDateTime fechaRegistro;
    private boolean       activo;

    // campos adicionales cargados por JOIN
    private String        correo;
    private String        telefono;
    private String        nombreRol;

    public Usuario() {}

    public int getIdUsuario()                    { return idUsuario; }
    public void setIdUsuario(int v)              { this.idUsuario = v; }

    public int getIdRol()                        { return idRol; }
    public void setIdRol(int v)                  { this.idRol = v; }

    public String getNombreUsuario()             { return nombreUsuario; }
    public void setNombreUsuario(String v)       { this.nombreUsuario = v; }

    public String getContrasena()                { return contrasena; }
    public void setContrasena(String v)          { this.contrasena = v; }

    public LocalDateTime getFechaRegistro()      { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime v){ this.fechaRegistro = v; }

    public boolean isActivo()                    { return activo; }
    public void setActivo(boolean v)             { this.activo = v; }

    public String getCorreo()                    { return correo; }
    public void setCorreo(String v)              { this.correo = v; }

    public String getTelefono()                  { return telefono; }
    public void setTelefono(String v)            { this.telefono = v; }

    public String getNombreRol()                 { return nombreRol; }
    public void setNombreRol(String v)           { this.nombreRol = v; }

    @Override
    public String toString() {
        return "Usuario{id=" + idUsuario + ", nombre=" + nombreUsuario + ", rol=" + idRol + "}";
    }
}
