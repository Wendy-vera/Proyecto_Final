package com.nirami.servlet;

<<<<<<< HEAD
import com.nirami.dao.*;
import com.nirami.model.*;
=======
import com.nirami.dao.CategoriaDAO;
import com.nirami.dao.ProductoDAO;
import com.nirami.model.Categoria;
import com.nirami.model.Producto;
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
import com.nirami.utils.IUUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
<<<<<<< HEAD
import jakarta.servlet.http.*;
=======
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@MultipartConfig
@WebServlet(urlPatterns = {
<<<<<<< HEAD
    "/", "/index", "/login", "/registro", "/logout",
    "/perfil", "/categorias", "/carrito", "/producto", "/favoritos",
    "/perfilVendedor", "/crearProductos",
    "/vendor/producto/guardar", "/vendor/producto/eliminar",
    "/admin", "/admin/perfil",
    "/admin/productos", "/admin/categorias",
    "/admin/clientes", "/admin/vendedores", "/admin/ventas",
    "/admin/categoria/guardar", "/admin/categoria/eliminar",
    "/admin/usuario/estado", "/admin/venta/estado",
    "/auth/login", "/auth/registro"
=======
    "/",
    "/index",
    "/login",
    "/registro",
    "/perfil",
    "/categorias",
    "/carrito",
    "/producto",
    "/crearProductos",
    "/perfilVendedor",
    "/vendor/producto/guardar",
    "/vendor/producto/eliminar"
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
})
public class FrontController extends HttpServlet {

    private TemplateEngine templateEngine;

<<<<<<< HEAD
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProductoDAO  productoDAO  = new ProductoDAO();
    private final UsuarioDAO   usuarioDAO   = new UsuarioDAO();
    private final VentaDAO     ventaDAO     = new VentaDAO();

    // =========================================================
    //  INIT
=======
    // DAOs — sin estado, seguros para compartir entre peticiones
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProductoDAO  productoDAO  = new ProductoDAO();

    // =========================================================
    //  INIT — configurar Thymeleaf
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
    // =========================================================
    @Override
    public void init() throws ServletException {
        JakartaServletWebApplication app =
            JakartaServletWebApplication.buildApplication(getServletContext());
        WebApplicationTemplateResolver resolver =
            new WebApplicationTemplateResolver(app);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jbs");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    // =========================================================
    //  GET
    // =========================================================
    @Override
<<<<<<< HEAD
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html;charset=UTF-8");
        String uri = req.getServletPath();
        
        // CORRECCIÓN: Se cambia a 'true' para mantener el rastreo por URL activo
        HttpSession session = req.getSession(true);
=======
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String uri = request.getServletPath();
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
        String vista;

        switch (uri) {

<<<<<<< HEAD
            // ── AUTH ──────────────────────────────────────────
            case "/":
            case "/login":
                vista = "fragments/login";
                break;

=======
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
            case "/registro":
                vista = "fragments/registro";
                break;

<<<<<<< HEAD
            case "/logout":
                if (session != null) session.invalidate();
                res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/login"));
                return;

            // ── CLIENTE ───────────────────────────────────────
            case "/index": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                List<Producto> recientes = productoDAO.listarTodos(6);
                req.setAttribute("recientes", recientes);
                List<Categoria> categorias = categoriaDAO.listarActivas();
                req.setAttribute("categorias", categorias);
                vista = "Client/index";
                break;
            }

            case "/categorias": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                List<Categoria> cats = categoriaDAO.listarActivas();
                req.setAttribute("categorias", cats);
                String idCatStr = req.getParameter("idCategoria");
                if (idCatStr != null && !idCatStr.isEmpty()) {
                    try {
                        int idCat = Integer.parseInt(idCatStr);
                        req.setAttribute("idCatActiva", idCat);
                        req.setAttribute("productos",
                            productoDAO.listarPorCategoria(idCat));
                    } catch (NumberFormatException ignored) {}
                } else {
                    req.setAttribute("productos", productoDAO.listarTodos(0));
                }
                vista = "Client/categorias";
                break;
            }

            case "/producto": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                String idStr = req.getParameter("id");
                if (idStr != null) {
                    try {
                        Producto p = productoDAO.buscarPorId(Integer.parseInt(idStr));
                        req.setAttribute("producto", p);
                    } catch (NumberFormatException ignored) {}
                }
                vista = "Client/producto";
                break;
            }

            case "/carrito": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                int idCliente = getIdUsuario(session);
                
                // Seguridad extra por si el ID falla
                if (idCliente == -1) { redirigirLogin(req, res); return; }
                
                req.setAttribute("compras", ventaDAO.listarPorCliente(idCliente));
                vista = "Client/carrito";
                break;
            }

            case "/favoritos": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                vista = "Client/categorias";
                break;
            }

            case "/perfil": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                int idUsuario = getIdUsuario(session);
                if (idUsuario == -1) { redirigirLogin(req, res); return; }
                
                Usuario u = usuarioDAO.buscarPorId(idUsuario);
                req.setAttribute("usuario", u);
                vista = "Client/perfil";
                break;
            }

            // ── VENDEDOR ──────────────────────────────────────
            case "/perfilVendedor": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                int idUsuario = getIdUsuario(session);
                if (idUsuario == -1) { redirigirLogin(req, res); return; }
                
                Usuario u = usuarioDAO.buscarPorId(idUsuario);
                req.setAttribute("usuario", u);
                vista = "Vendor/perfilVendedor";
                break;
            }

            case "/crearProductos": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                
                int idVendedor = getIdUsuario(session);
                // CORRECCIÓN: Si el ID es inválido, aborta de inmediato protegiendo la consulta
                if (idVendedor == -1) { 
                    redirigirLogin(req, res); 
                    return; 
                }
                
                List<Categoria> categorias = categoriaDAO.listarActivas();
                req.setAttribute("categorias", categorias);
                String editarId = req.getParameter("editar");
                if (editarId != null && !editarId.isEmpty()) {
                    try {
                        req.setAttribute("productoEditar",
                            productoDAO.buscarPorId(Integer.parseInt(editarId)));
                    } catch (NumberFormatException ignored) {}
                }
                String busqueda = req.getParameter("busqueda");
                req.setAttribute("busqueda", busqueda);
                List<Producto> productos = (busqueda != null && !busqueda.trim().isEmpty())
                    ? productoDAO.buscarPorNombreYVendedor(busqueda.trim(), idVendedor)
                    : productoDAO.listarPorVendedor(idVendedor);
                req.setAttribute("productos", productos);
                boolean mostrarFormulario = req.getParameter("crear") != null
                                         || req.getParameter("editar") != null;
                req.setAttribute("mostrarFormulario", mostrarFormulario);
                vista = "Vendor/crearProductos";
                break;
            }

            // ── ADMIN ─────────────────────────────────────────
            case "/admin": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                req.setAttribute("totalClientes",
                    usuarioDAO.listarPorRol(UsuarioDAO.ROL_CLIENTE).size());
                req.setAttribute("totalVendedores",
                    usuarioDAO.listarPorRol(UsuarioDAO.ROL_VENDEDOR).size());
                req.setAttribute("totalProductos",
                    productoDAO.listarTodos(0).size());
                req.setAttribute("totalVentas",
                    ventaDAO.listarTodas().size());
                vista = "Admin/gestionAdmin";
                break;
            }

            case "/admin/perfil": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                int idAdmin = getIdUsuario(session);
                if (idAdmin == -1) { redirigirLogin(req, res); return; }
                
                Usuario u = usuarioDAO.buscarPorId(idAdmin);
                req.setAttribute("usuario", u);
                vista = "Admin/perfilAdmin";
                break;
            }

            case "/admin/productos": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                req.setAttribute("productos", productoDAO.listarTodos(0));
                vista = "Admin/gestionProductos";
                break;
            }

            case "/admin/categorias": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                String editarId = req.getParameter("editar");
                if (editarId != null && !editarId.isEmpty()) {
                    try {
                        req.setAttribute("categoriaEditar",
                            categoriaDAO.buscarPorId(Integer.parseInt(editarId)));
                    } catch (NumberFormatException ignored) {}
                }
                req.setAttribute("mostrarFormulario",
                    req.getParameter("crear") != null || req.getParameter("editar") != null);
                req.setAttribute("categorias", categoriaDAO.listarTodas());
                vista = "Admin/gestionCategorias";
                break;
            }

            case "/admin/clientes": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                req.setAttribute("clientes",
                    usuarioDAO.listarPorRol(UsuarioDAO.ROL_CLIENTE));
                vista = "Admin/gestionClientes";
                break;
            }

            case "/admin/vendedores": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                req.setAttribute("vendedores",
                    usuarioDAO.listarPorRol(UsuarioDAO.ROL_VENDEDOR));
                vista = "Admin/gestionVendedores";
                break;
            }

            case "/admin/ventas": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                IUUtils.pasarMensajes(req);
                req.setAttribute("ventas", ventaDAO.listarTodas());
                vista = "Admin/gestionVentas";
                break;
            }

            default:
                vista = "fragments/login";
        }

        renderizar(vista, req, res);
=======
            case "/index":
                vista = "Client/index";
                break;

            case "/perfil":
                vista = "Client/perfil";
                break;

            case "/categorias":
                vista = "Client/categorias";
                break;

            case "/carrito":
                vista = "Client/carrito";
                break;

            case "/producto":
                vista = "Client/producto";
                break;

            case "/perfilVendedor":
                vista = "Vendor/perfilVendedor";
                break;

            // ── CREAR / GESTIONAR PRODUCTOS ───────────────────
            case "/crearProductos": {
                // 1. Trasladar mensajes flash de sesión al request
                IUUtils.pasarMensajes(request);
                // 2. Obtener el ID del vendedor desde la sesión
                HttpSession session = request.getSession(false);
                int idVendedor = 1;
                if (session != null && session.getAttribute("idUsuario") != null) {
                    idVendedor = (int) session.getAttribute("idUsuario");
                }
                // 3. Cargar categorías para el <select>
                List<Categoria> categorias = categoriaDAO.listarTodas();
                request.setAttribute("categorias", categorias);
                // 4. Si viene ?editar=ID → cargar producto en el formulario izquierdo
                String editarId = request.getParameter("editar");
                if (editarId != null && !editarId.isEmpty()) {
                    try {
                        Producto productoEditar = productoDAO.buscarPorId(
                                Integer.parseInt(editarId));
                        request.setAttribute("productoEditar", productoEditar);
                    } catch (NumberFormatException ignored) { }
                }
                // 5. Lista de productos (con filtro opcional ?busqueda=X)
                String busqueda = request.getParameter("busqueda");
                request.setAttribute("busqueda", busqueda);
                List<Producto> productos;
                if (busqueda != null && !busqueda.trim().isEmpty()) {
                    productos = productoDAO.buscarPorNombreYVendedor(
                            busqueda.trim(), idVendedor);
                } else {
                    productos = productoDAO.listarPorVendedor(idVendedor);
                }
                request.setAttribute("productos", productos);

                // 6. Mostrar formulario si viene ?crear=true o ?editar=ID  ← AQUÍ
                boolean mostrarFormulario = request.getParameter("crear") != null
                                         || request.getParameter("editar") != null;
                request.setAttribute("mostrarFormulario", mostrarFormulario);

                vista = "Vendor/crearProductos";
                break;
            }
            default:
                vista = "fragments/login";
                break;
        }

        renderizar(vista, request, response);
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
    }

    // =========================================================
    //  POST
    // =========================================================
    @Override
<<<<<<< HEAD
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("text/html;charset=UTF-8");
        req.setCharacterEncoding("UTF-8");
        String uri = req.getServletPath();
        HttpSession session = req.getSession(true);

        switch (uri) {

            // ── LOGIN ─────────────────────────────────────────
            case "/auth/login": {
                String correo    = req.getParameter("correo");
                String contrasena = req.getParameter("contrasena");
                Usuario u = usuarioDAO.autenticar(correo, contrasena);
                if (u == null) {
                    session.setAttribute("mensajeError",
                        "Correo o contraseña incorrectos.");
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/login"));
                    return;
                }
                session.setAttribute("idUsuario",  u.getIdUsuario());
                session.setAttribute("idRol",      u.getIdRol());
                session.setAttribute("nombreUsuario", u.getNombreUsuario());
                
                // Redirecciones codificadas según el rol asignado
                if (u.getIdRol() == UsuarioDAO.ROL_ADMIN) {
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/admin"));
                } else if (u.getIdRol() == UsuarioDAO.ROL_VENDEDOR) {
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/crearProductos"));
                } else {
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/index"));
                }
                return;
            }

            // ── REGISTRO ──────────────────────────────────────
            case "/auth/registro": {
                String nombre    = req.getParameter("nombreUsuario");
                String correo    = req.getParameter("correo");
                String contrasena = req.getParameter("contrasena");
                String confirmar  = req.getParameter("confirmarContrasena");
                String rolStr     = req.getParameter("idRol");
                int idRol = (rolStr != null && rolStr.equals("2"))
                            ? UsuarioDAO.ROL_VENDEDOR : UsuarioDAO.ROL_CLIENTE;

                if (!contrasena.equals(confirmar)) {
                    session.setAttribute("mensajeError", "Las contraseñas no coinciden.");
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/registro"));
                    return;
                }
                if (usuarioDAO.existeCorreo(correo)) {
                    session.setAttribute("mensajeError", "El correo ya está registrado.");
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/registro"));
                    return;
                }
                if (usuarioDAO.existeNombreUsuario(nombre)) {
                    session.setAttribute("mensajeError", "El nombre de usuario ya existe.");
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/registro"));
                    return;
                }
                try {
                    usuarioDAO.registrar(nombre, correo, contrasena, idRol);
                    session.setAttribute("mensajeExito", "Registro exitoso. Inicia sesión.");
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/login"));
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error al registrar: " + e.getMessage());
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/registro"));
                }
                return;
            }

            // ── GUARDAR PRODUCTO (vendedor) ───────────────────
            case "/vendor/producto/guardar": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                try {
                    String idProductoStr = req.getParameter("idProducto");
                    String nombre        = req.getParameter("nombreProducto");
                    String descripcion   = req.getParameter("descripcionProducto");
                    double precio        = Double.parseDouble(req.getParameter("precioProducto"));
                    int cantidad         = Integer.parseInt(req.getParameter("cantidadProducto"));
                    int idCategoria      = Integer.parseInt(req.getParameter("idCategoria"));
                    int idVendedor       = getIdUsuario(session);

                    Part imagenPart = req.getPart("imagenProducto");
=======
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String uri = request.getServletPath();
        HttpSession session = request.getSession(true);

        switch (uri) {

            // ── GUARDAR producto (crear o editar) ─────────────
            case "/vendor/producto/guardar": {
                try {
                    String idProductoStr = request.getParameter("idProducto");
                    String nombre        = request.getParameter("nombreProducto");
                    String descripcion   = request.getParameter("descripcionProducto");
                    double precio        = Double.parseDouble(
                                              request.getParameter("precioProducto"));
                    int cantidad         = Integer.parseInt(
                                              request.getParameter("cantidadProducto"));
                    int idCategoria      = Integer.parseInt(
                                              request.getParameter("idCategoria"));

                    int idVendedor = 1; // respaldo para pruebas sin login
                    if (session.getAttribute("idUsuario") != null) {
                        idVendedor = (int) session.getAttribute("idUsuario");
                    }

                    // Manejo de imagen subida
                    Part imagenPart = request.getPart("imagenProducto");
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
                    String nombreImagen = null;
                    if (imagenPart != null && imagenPart.getSize() > 0) {
                        nombreImagen = Paths.get(imagenPart.getSubmittedFileName())
                                           .getFileName().toString();
<<<<<<< HEAD
                        String ruta = req.getServletContext()
                                        .getRealPath("/assets/imagenes/imgproductos/");
                        new File(ruta).mkdirs();
                        imagenPart.write(ruta + File.separator + nombreImagen);
                    }

                    if (idProductoStr == null || idProductoStr.isEmpty()) {
=======
                        String rutaGuardado = request.getServletContext()
                                                     .getRealPath(
                                                       "/assets/imagenes/imgproductos/");
                        new File(rutaGuardado).mkdirs();
                        imagenPart.write(rutaGuardado + File.separator + nombreImagen);
                    }

                    if (idProductoStr == null || idProductoStr.isEmpty()) {
                        // ── CREAR ──────────────────────────────
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
                        Producto nuevo = new Producto();
                        nuevo.setNombreProducto(nombre);
                        nuevo.setDescripcionProducto(descripcion);
                        nuevo.setPrecioProducto(precio);
                        nuevo.setCantidadProducto(cantidad);
                        nuevo.setIdCategoria(idCategoria);
                        nuevo.setIdVendedor(idVendedor);
                        if (nombreImagen != null) nuevo.setImagenProducto(nombreImagen);
                        productoDAO.crear(nuevo);
                        session.setAttribute("mensajeExito", "Producto creado correctamente.");
                    } else {
<<<<<<< HEAD
                        Producto existente = productoDAO.buscarPorId(Integer.parseInt(idProductoStr));
=======
                        // ── EDITAR ─────────────────────────────
                        Producto existente = productoDAO.buscarPorId(
                                Integer.parseInt(idProductoStr));
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
                        if (existente != null) {
                            existente.setNombreProducto(nombre);
                            existente.setDescripcionProducto(descripcion);
                            existente.setPrecioProducto(precio);
                            existente.setCantidadProducto(cantidad);
                            existente.setIdCategoria(idCategoria);
<<<<<<< HEAD
                            existente.setImagenProducto(nombreImagen);
                            productoDAO.actualizar(existente);
                        }
                        session.setAttribute("mensajeExito", "Producto actualizado correctamente.");
                    }
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error al guardar: " + e.getMessage());
                }
                res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/crearProductos"));
                return;
            }

            // ── ELIMINAR PRODUCTO (vendedor) ──────────────────
            case "/vendor/producto/eliminar": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                try {
                    productoDAO.eliminar(Integer.parseInt(req.getParameter("idProducto")));
                    session.setAttribute("mensajeExito", "Producto eliminado correctamente.");
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error al eliminar: " + e.getMessage());
                }
                res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/crearProductos"));
                return;
            }

            // ── GUARDAR CATEGORÍA (admin) ─────────────────────
            case "/admin/categoria/guardar": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                try {
                    String idStr      = req.getParameter("idCategoria");
                    String nombre     = req.getParameter("nombreCategoria");
                    String descripcion = req.getParameter("descripcionCategoria");

                    Categoria c = new Categoria();
                    c.setNombreCategoria(nombre);
                    c.setDescripcion(descripcion);
                    c.setActiva(true);

                    if (idStr == null || idStr.isEmpty()) {
                        categoriaDAO.crear(c);
                        session.setAttribute("mensajeExito", "Categoría creada.");
                    } else {
                        c.setIdCategoria(Integer.parseInt(idStr));
                        categoriaDAO.actualizar(c);
                        session.setAttribute("mensajeExito", "Categoría actualizada.");
                    }
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error: " + e.getMessage());
                }
                res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/admin/categorias"));
                return;
            }

            // ── ELIMINAR CATEGORÍA (admin) ────────────────────
            case "/admin/categoria/eliminar": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                try {
                    categoriaDAO.eliminar(Integer.parseInt(req.getParameter("idCategoria")));
                    session.setAttribute("mensajeExito", "Categoría desactivada.");
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error: " + e.getMessage());
                }
                res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/admin/categorias"));
                return;
            }

            // ── CAMBIAR ESTADO USUARIO (admin) ────────────────
            case "/admin/usuario/estado": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                try {
                    int idUsuario = Integer.parseInt(req.getParameter("idUsuario"));
                    boolean activo = "true".equals(req.getParameter("activo"));
                    String destino = req.getParameter("destino");
                    usuarioDAO.cambiarEstado(idUsuario, activo);
                    session.setAttribute("mensajeExito",
                        activo ? "Usuario activado." : "Usuario desactivado.");
                    
                    String pathDestino = "vendedor".equals(destino) ? "/admin/vendedores" : "/admin/clientes";
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + pathDestino));
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error: " + e.getMessage());
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/admin/clientes"));
                }
                return;
            }

            // ── CAMBIAR ESTADO VENTA (admin) ──────────────────
            case "/admin/venta/estado": {
                if (!esAdmin(session)) { redirigirLogin(req, res); return; }
                try {
                    int idVenta    = Integer.parseInt(req.getParameter("idVenta"));
                    String estado  = req.getParameter("estado");
                    ventaDAO.actualizarEstado(idVenta, estado);
                    session.setAttribute("mensajeExito", "Estado de venta actualizado.");
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error: " + e.getMessage());
                }
                res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/admin/ventas"));
                return;
            }

            // ── ACTUALIZAR PERFIL ─────────────────────────────
            case "/perfil": {
                if (!autenticado(session)) { redirigirLogin(req, res); return; }
                try {
                    int idU    = getIdUsuario(session);
                    String nom = req.getParameter("nombreUsuario");
                    String cor = req.getParameter("correo");
                    String tel = req.getParameter("telefono");
                    usuarioDAO.actualizarPerfil(idU, nom, cor, tel);
                    session.setAttribute("nombreUsuario", nom);
                    session.setAttribute("mensajeExito", "Perfil actualizado correctamente.");
                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error: " + e.getMessage());
                }
                
                int rol = getRol(session);
                if (rol == UsuarioDAO.ROL_ADMIN) {
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/admin/perfil"));
                } else if (rol == UsuarioDAO.ROL_VENDEDOR) {
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/perfilVendedor"));
                } else {
                    res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/perfil"));
                }
=======
                            // Solo cambiar imagen si se subió una nueva
                            if (nombreImagen != null) {
                                existente.setImagenProducto(nombreImagen);
                            } else {
                                existente.setImagenProducto(null); // conservar la anterior
                            }
                            productoDAO.actualizar(existente);
                        }
                        session.setAttribute("mensajeExito",
                                             "Producto actualizado correctamente.");
                    }

                } catch (Exception e) {
                    session.setAttribute("mensajeError", "Error al guardar: " + e.getMessage());
                }
                response.sendRedirect(request.getContextPath() + "/crearProductos");
                return;
            }

            // ── ELIMINAR producto ──────────────────────────────
            case "/vendor/producto/eliminar": {
                try {
                    int idProducto = Integer.parseInt(
                            request.getParameter("idProducto"));
                    productoDAO.eliminar(idProducto);
                    session.setAttribute("mensajeExito", "Producto eliminado correctamente.");
                } catch (Exception e) {
                    session.setAttribute("mensajeError",
                                         "Error al eliminar: " + e.getMessage());
                }
                response.sendRedirect(request.getContextPath() + "/crearProductos");
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
                return;
            }

            default:
<<<<<<< HEAD
                doGet(req, res);
=======
                doGet(request, response);
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
        }
    }

    // =========================================================
<<<<<<< HEAD
    //  HELPERS
    // =========================================================
    private boolean autenticado(HttpSession s) {
        return s != null && s.getAttribute("idUsuario") != null;
    }
    private boolean esAdmin(HttpSession s) {
        return autenticado(s) && Integer.valueOf(UsuarioDAO.ROL_ADMIN)
                                        .equals(s.getAttribute("idRol"));
    }
    private int getIdUsuario(HttpSession s) {
        if (s == null || s.getAttribute("idUsuario") == null) return -1;
        return (int) s.getAttribute("idUsuario");
    }
    private int getRol(HttpSession s) {
        if (s == null || s.getAttribute("idRol") == null) return UsuarioDAO.ROL_CLIENTE;
        return (int) s.getAttribute("idRol");
    }
    
    private void redirigirLogin(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        res.sendRedirect(res.encodeRedirectURL(req.getContextPath() + "/login"));
    }

    private void renderizar(String vista, HttpServletRequest req,
                             HttpServletResponse res) throws IOException {
        JakartaServletWebApplication app =
            JakartaServletWebApplication.buildApplication(getServletContext());
        WebContext ctx = new WebContext(
            app.buildExchange(req, res), req.getLocale());
        req.getAttributeNames().asIterator().forEachRemaining(attr ->
            ctx.setVariable(attr, req.getAttribute(attr)));
        
        HttpSession s = req.getSession(false);
        if (s != null) {
            ctx.setVariable("sessionNombre", s.getAttribute("nombreUsuario"));
            ctx.setVariable("sessionRol",    s.getAttribute("idRol"));
        }
        templateEngine.process(vista, ctx, res.getWriter());
=======
    //  HELPER — renderizar con Thymeleaf
    // =========================================================
    private void renderizar(String vista, HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        JakartaServletWebApplication app =
            JakartaServletWebApplication.buildApplication(getServletContext());
        WebContext ctx = new WebContext(
            app.buildExchange(request, response),
            request.getLocale()
        );

        // Pasar todos los atributos del request al contexto Thymeleaf
        request.getAttributeNames().asIterator().forEachRemaining(attr ->
            ctx.setVariable(attr, request.getAttribute(attr))
        );

        templateEngine.process(vista, ctx, response.getWriter());
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
    }
}
