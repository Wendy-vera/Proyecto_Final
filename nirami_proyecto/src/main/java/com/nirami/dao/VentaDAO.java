package com.nirami.dao;

import com.nirami.model.DetalleVenta;
import com.nirami.model.Venta;
import database_nirami.ClassConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para ventas y detalle_ventas.
 * Requiere que en la BD existan las tablas ventas y detalle_ventas
 * (incluidas en nirami_db_completo.sql).
 */
public class VentaDAO {

    // ─── CREAR VENTA ──────────────────────────────────────────

    /**
     * Inserta una venta con sus detalles en una transacción.
     * Descuenta stock de cada producto.
     * @return idVenta generado
     */
    public int crearVenta(int idCliente, List<DetalleVenta> detalles) {
        String sqlVenta   = "INSERT INTO ventas (id_cliente, total, estado) VALUES (?, ?, 'Pendiente')";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlStock   = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";

        Connection con = null;
        try {
            con = ClassConexion.MetodoConexion();
            con.setAutoCommit(false);

            double total = detalles.stream()
                                   .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                                   .sum();

            int idVenta;
            try (PreparedStatement ps = con.prepareStatement(sqlVenta,
                                             Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, idCliente);
                ps.setDouble(2, total);
                ps.executeUpdate();
                try (ResultSet ks = ps.getGeneratedKeys()) {
                    ks.next();
                    idVenta = ks.getInt(1);
                }
            }

            for (DetalleVenta d : detalles) {
                // descontar stock
                try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
                    ps.setInt(1, d.getCantidad());
                    ps.setInt(2, d.getIdProducto());
                    ps.setInt(3, d.getCantidad());
                    int rows = ps.executeUpdate();
                    if (rows == 0) throw new RuntimeException(
                        "Stock insuficiente para producto " + d.getIdProducto());
                }
                // insertar detalle
                try (PreparedStatement ps = con.prepareStatement(sqlDetalle)) {
                    ps.setInt(1, idVenta);
                    ps.setInt(2, d.getIdProducto());
                    ps.setInt(3, d.getCantidad());
                    ps.setDouble(4, d.getPrecioUnitario());
                    ps.executeUpdate();
                }
            }

            con.commit();
            return idVenta;

        } catch (Exception e) {
            if (con != null) try { con.rollback(); } catch (SQLException ig) {}
            throw new RuntimeException("Error al crear venta: " + e.getMessage(), e);
        } finally {
            if (con != null) try { con.close(); } catch (SQLException ig) {}
        }
    }

    // ─── LISTAR ───────────────────────────────────────────────

    public List<Venta> listarTodas() {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.id_cliente, v.fecha_venta, v.total, v.estado, " +
                     "       u.nombre_usuario " +
                     "FROM ventas v " +
                     "JOIN usuarios u ON v.id_cliente = u.id_usuario " +
                     "ORDER BY v.fecha_venta DESC";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearVenta(rs));
        } catch (SQLException e) {
            System.err.println("[VentaDAO] listarTodas: " + e.getMessage());
        }
        return lista;
    }

    public List<Venta> listarPorCliente(int idCliente) {
        List<Venta> lista = new ArrayList<>();
        String sql = "SELECT v.id_venta, v.id_cliente, v.fecha_venta, v.total, v.estado, " +
                     "       u.nombre_usuario " +
                     "FROM ventas v " +
                     "JOIN usuarios u ON v.id_cliente = u.id_usuario " +
                     "WHERE v.id_cliente = ? ORDER BY v.fecha_venta DESC";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearVenta(rs));
            }
        } catch (SQLException e) {
            System.err.println("[VentaDAO] listarPorCliente: " + e.getMessage());
        }
        return lista;
    }

    public List<DetalleVenta> listarDetallesPorVenta(int idVenta) {
        List<DetalleVenta> lista = new ArrayList<>();
        String sql = "SELECT dv.id_detalle, dv.id_venta, dv.id_producto, dv.cantidad, " +
                     "       dv.precio_unitario, p.nombre AS nombre_producto, p.imagen " +
                     "FROM detalle_ventas dv " +
                     "JOIN productos p ON dv.id_producto = p.id_producto " +
                     "WHERE dv.id_venta = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVenta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta d = new DetalleVenta();
                    d.setIdDetalle(rs.getInt("id_detalle"));
                    d.setIdVenta(rs.getInt("id_venta"));
                    d.setIdProducto(rs.getInt("id_producto"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    d.setNombreProducto(rs.getString("nombre_producto"));
                    d.setImagenProducto(rs.getString("imagen"));
                    lista.add(d);
                }
            }
        } catch (SQLException e) {
            System.err.println("[VentaDAO] listarDetalles: " + e.getMessage());
        }
        return lista;
    }

    // ─── ACTUALIZAR ESTADO ────────────────────────────────────

    public void actualizarEstado(int idVenta, String estado) {
        String sql = "UPDATE ventas SET estado = ? WHERE id_venta = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idVenta);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando estado de venta: " + e.getMessage(), e);
        }
    }

    // ─── HELPER ───────────────────────────────────────────────

    private Venta mapearVenta(ResultSet rs) throws SQLException {
        Venta v = new Venta();
        v.setIdVenta(rs.getInt("id_venta"));
        v.setIdCliente(rs.getInt("id_cliente"));
        Timestamp ts = rs.getTimestamp("fecha_venta");
        if (ts != null) v.setFechaVenta(ts.toLocalDateTime());
        v.setTotal(rs.getDouble("total"));
        v.setEstado(rs.getString("estado"));
        v.setNombreCliente(rs.getString("nombre_usuario"));
        return v;
    }
}
