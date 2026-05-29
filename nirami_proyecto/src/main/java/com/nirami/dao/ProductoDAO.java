package com.nirami.dao;

import com.nirami.model.Categoria;
import com.nirami.model.Producto;
import database_nirami.ClassConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private static final String SQL_SELECT =
        "SELECT p.id_producto, p.nombre, p.descripcion, p.precio, p.stock, " +
        "       p.imagen, p.id_categoria, p.id_vendedor, " +
        "       c.nombre AS nombre_categoria " +
        "FROM productos p " +
        "LEFT JOIN categorias c ON p.id_categoria = c.id_categoria ";

    private Producto mapear(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setIdProducto(rs.getInt("id_producto"));
        p.setNombreProducto(rs.getString("nombre"));
        p.setDescripcionProducto(rs.getString("descripcion"));
        p.setPrecioProducto(rs.getDouble("precio"));
        p.setCantidadProducto(rs.getInt("stock"));
        p.setImagenProducto(rs.getString("imagen"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setIdVendedor(rs.getInt("id_vendedor"));
        int idCat = rs.getInt("id_categoria");
        if (idCat > 0) {
            Categoria cat = new Categoria();
            cat.setIdCategoria(idCat);
            String nc = rs.getString("nombre_categoria");
            cat.setNombreCategoria(nc != null ? nc : "");
            p.setCategoria(cat);
        }
        return p;
    }

    /** Todos los productos disponibles. limit=0 trae todos. */
    public List<Producto> listarTodos(int limit) {
        List<Producto> lista = new ArrayList<>();
        String sql = SQL_SELECT + "WHERE p.disponible = TRUE ORDER BY p.nombre" +
                     (limit > 0 ? " LIMIT " + limit : "");
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] listarTodos: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> listarPorCategoria(int idCategoria) {
        List<Producto> lista = new ArrayList<>();
        String sql = SQL_SELECT + "WHERE p.disponible = TRUE AND p.id_categoria = ? ORDER BY p.nombre";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] listarPorCategoria: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> listarPorVendedor(int idVendedor) {
        List<Producto> lista = new ArrayList<>();
        String sql = SQL_SELECT + "WHERE p.id_vendedor = ? AND p.disponible = TRUE ORDER BY p.nombre";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] listarPorVendedor: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> buscarPorNombreYVendedor(String nombre, int idVendedor) {
        List<Producto> lista = new ArrayList<>();
        String sql = SQL_SELECT + "WHERE p.id_vendedor = ? AND p.disponible = TRUE AND p.nombre LIKE ? ORDER BY p.nombre";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idVendedor);
            ps.setString(2, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] buscarPorNombreYVendedor: " + e.getMessage());
        }
        return lista;
    }

    public Producto buscarPorId(int idProducto) {
        String sql = SQL_SELECT + "WHERE p.id_producto = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[ProductoDAO] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public void crear(Producto p) {
        String sql = "INSERT INTO productos (id_categoria, id_vendedor, nombre, precio, stock, descripcion, imagen) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getIdCategoria());
            ps.setInt(2, p.getIdVendedor());
            ps.setString(3, p.getNombreProducto());
            ps.setDouble(4, p.getPrecioProducto());
            ps.setInt(5, p.getCantidadProducto());
            ps.setString(6, p.getDescripcionProducto());
            ps.setString(7, p.getImagenProducto());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo crear el producto: " + e.getMessage(), e);
        }
    }

    public void actualizar(Producto p) {
        String sql;
        if (p.getImagenProducto() != null) {
            sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, id_categoria=?, imagen=? WHERE id_producto=?";
        } else {
            sql = "UPDATE productos SET nombre=?, descripcion=?, precio=?, stock=?, id_categoria=? WHERE id_producto=?";
        }
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombreProducto());
            ps.setString(2, p.getDescripcionProducto());
            ps.setDouble(3, p.getPrecioProducto());
            ps.setInt(4, p.getCantidadProducto());
            ps.setInt(5, p.getIdCategoria());
            if (p.getImagenProducto() != null) {
                ps.setString(6, p.getImagenProducto());
                ps.setInt(7, p.getIdProducto());
            } else {
                ps.setInt(6, p.getIdProducto());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar el producto: " + e.getMessage(), e);
        }
    }

    public void eliminar(int idProducto) {
        String sql = "UPDATE productos SET disponible = FALSE WHERE id_producto = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idProducto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo eliminar el producto: " + e.getMessage(), e);
        }
    }
}
