package com.nirami.servlet;

import com.nirami.dao.CategoriaDAO;
import com.nirami.dao.ProductoDAO;
import com.nirami.model.Categoria;
import com.nirami.model.Producto;
import com.nirami.utils.IUUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

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
})
public class FrontController extends HttpServlet {

    private TemplateEngine templateEngine;

    // DAOs — sin estado, seguros para compartir entre peticiones
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProductoDAO  productoDAO  = new ProductoDAO();

    // =========================================================
    //  INIT — configurar Thymeleaf
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String uri = request.getServletPath();
        String vista;

        switch (uri) {

            case "/registro":
                vista = "fragments/registro";
                break;

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
    }

    // =========================================================
    //  POST
    // =========================================================
    @Override
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
                    String nombreImagen = null;
                    if (imagenPart != null && imagenPart.getSize() > 0) {
                        nombreImagen = Paths.get(imagenPart.getSubmittedFileName())
                                           .getFileName().toString();
                        String rutaGuardado = request.getServletContext()
                                                     .getRealPath(
                                                       "/assets/imagenes/imgproductos/");
                        new File(rutaGuardado).mkdirs();
                        imagenPart.write(rutaGuardado + File.separator + nombreImagen);
                    }

                    if (idProductoStr == null || idProductoStr.isEmpty()) {
                        // ── CREAR ──────────────────────────────
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
                        // ── EDITAR ─────────────────────────────
                        Producto existente = productoDAO.buscarPorId(
                                Integer.parseInt(idProductoStr));
                        if (existente != null) {
                            existente.setNombreProducto(nombre);
                            existente.setDescripcionProducto(descripcion);
                            existente.setPrecioProducto(precio);
                            existente.setCantidadProducto(cantidad);
                            existente.setIdCategoria(idCategoria);
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
                return;
            }

            default:
                doGet(request, response);
        }
    }

    // =========================================================
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
    }
}
