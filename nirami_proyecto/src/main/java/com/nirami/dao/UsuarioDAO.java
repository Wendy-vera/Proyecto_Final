package com.nirami.dao;

import com.nirami.model.Usuario;
import database_nirami.ClassConexion;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO JDBC para usuarios, correos y teléfonos.
 * Roles: 1=Administrador, 2=Vendedor, 3=Cliente
 */
public class UsuarioDAO {

    // ─── CONSTANTES DE ROL ────────────────────────────────────
    public static final int ROL_ADMIN    = 1;
    public static final int ROL_VENDEDOR = 2;
    public static final int ROL_CLIENTE  = 3;

    // ─── SQL BASE ─────────────────────────────────────────────
    private static final String SQL_SELECT =
        "SELECT u.id_usuario, u.id_rol, u.nombre_usuario, u.contrasena, " +
        "       u.fecha_registro, u.activo, r.nombre_rol, " +
        "       c.correo, t.telefono " +
        "FROM usuarios u " +
        "JOIN roles r ON u.id_rol = r.id_rol " +
        "LEFT JOIN correos c ON c.id_usuario = u.id_usuario " +
        "LEFT JOIN telefonos t ON t.id_usuario = u.id_usuario ";

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setIdRol(rs.getInt("id_rol"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setContrasena(rs.getString("contrasena"));
        Timestamp ts = rs.getTimestamp("fecha_registro");
        if (ts != null) u.setFechaRegistro(ts.toLocalDateTime());
        u.setActivo(rs.getBoolean("activo"));
        u.setNombreRol(rs.getString("nombre_rol"));
        u.setCorreo(rs.getString("correo"));
        u.setTelefono(rs.getString("telefono"));
        return u;
    }

    // ─── LOGIN ────────────────────────────────────────────────

    /**
     * Autentica por correo + contraseña (texto plano por ahora).
     * Devuelve el usuario si existe y está activo, null si no.
     */
    public Usuario autenticar(String correo, String contrasena) {
        String sql = SQL_SELECT +
                     "WHERE c.correo = ? AND u.contrasena = ? AND u.activo = TRUE";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] autenticar: " + e.getMessage());
        }
        return null;
    }

    // ─── REGISTRAR ────────────────────────────────────────────

    /**
     * Registra un nuevo usuario (cliente por defecto idRol=3).
     * También inserta el correo en tabla correos.
     * Devuelve el id generado o -1 si falla.
     */
    public int registrar(String nombreUsuario, String correo,
                         String contrasena, int idRol) {
        String sqlUsuario = "INSERT INTO usuarios (id_rol, nombre_usuario, contrasena) " +
                            "VALUES (?, ?, ?)";
        String sqlCorreo  = "INSERT INTO correos (correo, id_usuario) VALUES (?, ?)";

        Connection con = null;
        try {
            con = ClassConexion.MetodoConexion();
            con.setAutoCommit(false);

            int idGenerado;
            try (PreparedStatement ps = con.prepareStatement(sqlUsuario,
                                             Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idRol);
                ps.setString(2, nombreUsuario);
                ps.setString(3, contrasena);
                ps.executeUpdate();
                try (ResultSet ks = ps.getGeneratedKeys()) {
                    if (!ks.next()) throw new SQLException("No se obtuvo ID generado.");
                    idGenerado = ks.getInt(1);
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sqlCorreo)) {
                ps.setString(1, correo);
                ps.setInt(2, idGenerado);
                ps.executeUpdate();
            }

            con.commit();
            return idGenerado;

        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ignored) {}
            System.err.println("[UsuarioDAO] registrar: " + e.getMessage());
            throw new RuntimeException("No se pudo registrar: " + e.getMessage(), e);
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignored) {}
        }
    }

    // ─── BUSCAR POR ID ────────────────────────────────────────

    public Usuario buscarPorId(int idUsuario) {
        String sql = SQL_SELECT + "WHERE u.id_usuario = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    // ─── LISTAR POR ROL ───────────────────────────────────────

    public List<Usuario> listarPorRol(int idRol) {
        List<Usuario> lista = new ArrayList<>();
        String sql = SQL_SELECT + "WHERE u.id_rol = ? ORDER BY u.nombre_usuario";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] listarPorRol: " + e.getMessage());
        }
        return lista;
    }

    /** Todos los usuarios (para panel admin). */
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = SQL_SELECT + "ORDER BY u.id_rol, u.nombre_usuario";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] listarTodos: " + e.getMessage());
        }
        return lista;
    }

    // ─── ACTUALIZAR PERFIL ────────────────────────────────────

    /** Actualiza nombre, correo y teléfono del usuario. */
    public void actualizarPerfil(int idUsuario, String nombreUsuario,
                                  String correo, String telefono) {
        String sqlU = "UPDATE usuarios SET nombre_usuario = ? WHERE id_usuario = ?";
        String sqlC = "UPDATE correos SET correo = ? WHERE id_usuario = ?";
        String sqlT = "INSERT INTO telefonos (telefono, id_usuario) VALUES (?, ?) " +
                      "ON DUPLICATE KEY UPDATE telefono = VALUES(telefono)";

        Connection con = null;
        try {
            con = ClassConexion.MetodoConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sqlU)) {
                ps.setString(1, nombreUsuario);
                ps.setInt(2, idUsuario);
                ps.executeUpdate();
            }
            if (correo != null && !correo.isBlank()) {
                try (PreparedStatement ps = con.prepareStatement(sqlC)) {
                    ps.setString(1, correo);
                    ps.setInt(2, idUsuario);
                    ps.executeUpdate();
                }
            }
            if (telefono != null && !telefono.isBlank()) {
                try (PreparedStatement ps = con.prepareStatement(sqlT)) {
                    ps.setString(1, telefono);
                    ps.setInt(2, idUsuario);
                    ps.executeUpdate();
                }
            }
            con.commit();
        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ignored) {}
            throw new RuntimeException("Error al actualizar perfil: " + e.getMessage(), e);
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ignored) {}
        }
    }

    /** Cambia la contraseña del usuario. */
    public void cambiarContrasena(int idUsuario, String nuevaContrasena) {
        String sql = "UPDATE usuarios SET contrasena = ? WHERE id_usuario = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevaContrasena);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar contraseña: " + e.getMessage(), e);
        }
    }

    // ─── ACTIVAR / DESACTIVAR ─────────────────────────────────

    public void cambiarEstado(int idUsuario, boolean activo) {
        String sql = "UPDATE usuarios SET activo = ? WHERE id_usuario = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, activo);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cambiar estado: " + e.getMessage(), e);
        }
    }

    /** Verifica si un correo ya está registrado. */
    public boolean existeCorreo(String correo) {
        String sql = "SELECT 1 FROM correos WHERE correo = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] existeCorreo: " + e.getMessage());
        }
        return false;
    }

    /** Verifica si un nombre de usuario ya está registrado. */
    public boolean existeNombreUsuario(String nombreUsuario) {
        String sql = "SELECT 1 FROM usuarios WHERE nombre_usuario = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] existeNombreUsuario: " + e.getMessage());
        }
        return false;
    }
}
