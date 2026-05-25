package com.nirami.dao;

import com.nirami.model.Categoria;
import database_nirami.ClassConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

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
        }
        return lista;
    }

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
}
