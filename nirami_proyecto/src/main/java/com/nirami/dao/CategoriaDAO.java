package com.nirami.dao;

import com.nirami.model.Categoria;
import database_nirami.ClassConexion;

<<<<<<< HEAD
import java.sql.*;
=======
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

<<<<<<< HEAD
    public List<Categoria> listarTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, activa FROM categorias ORDER BY nombre";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] listarTodas: " + e.getMessage());
        }
        return lista;
    }

    public List<Categoria> listarActivas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion, activa FROM categorias WHERE activa = TRUE ORDER BY nombre";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] listarActivas: " + e.getMessage());
=======
    /**
     * Devuelve todas las categorías activas para poblar el &lt;select&gt; del formulario.
     */
    public List<Categoria> listarTodas() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion " +
                     "FROM categorias " +
                     "WHERE activa = TRUE " +
                     "ORDER BY nombre";

        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setIdCategoria(rs.getInt("id_categoria"));
                c.setNombreCategoria(rs.getString("nombre"));
                c.setDescripcion(rs.getString("descripcion"));
                lista.add(c);
            }

        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al listar categorías: " + e.getMessage());
            e.printStackTrace();
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
        }
        return lista;
    }

<<<<<<< HEAD
    public Categoria buscarPorId(int idCategoria) {
        String sql = "SELECT id_categoria, nombre, descripcion, activa FROM categorias WHERE id_categoria = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] buscarPorId: " + e.getMessage());
        }
        return null;
    }

    public void crear(Categoria c) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear categoria: " + e.getMessage(), e);
        }
    }

    public void actualizar(Categoria c) {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ?, activa = ? WHERE id_categoria = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setBoolean(3, c.isActiva());
            ps.setInt(4, c.getIdCategoria());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar categoria: " + e.getMessage(), e);
        }
    }

    public void eliminar(int idCategoria) {
        String sql = "UPDATE categorias SET activa = FALSE WHERE id_categoria = ?";
        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCategoria);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar categoria: " + e.getMessage(), e);
        }
    }

    private Categoria mapear(ResultSet rs) throws SQLException {
        Categoria c = new Categoria();
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setNombreCategoria(rs.getString("nombre"));
        c.setDescripcion(rs.getString("descripcion"));
        c.setActiva(rs.getBoolean("activa"));
        return c;
    }
=======
    /**
     * Busca una categoría por su ID.
     */
    public Categoria buscarPorId(int idCategoria) {
        String sql = "SELECT id_categoria, nombre, descripcion " +
                     "FROM categorias WHERE id_categoria = ?";

        try (Connection con = ClassConexion.MetodoConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCategoria);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Categoria c = new Categoria();
                    c.setIdCategoria(rs.getInt("id_categoria"));
                    c.setNombreCategoria(rs.getString("nombre"));
                    c.setDescripcion(rs.getString("descripcion"));
                    return c;
                }
            }

        } catch (SQLException e) {
            System.err.println("[CategoriaDAO] Error al buscar categoría: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
}
