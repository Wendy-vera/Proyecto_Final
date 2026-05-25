package com.nirami.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Utilidades de interfaz de usuario.
 * - pasarMensajes: transfiere mensajes flash de sesión al request
 *   para que Thymeleaf pueda leerlos y luego los limpia de la sesión.
 */
public class IUUtils {

    private IUUtils() { /* utilidad estática, no instanciar */ }

    /**
     * Mueve los atributos "mensajeExito" y "mensajeError" de la sesión
     * al request, y los elimina de la sesión para que solo se muestren
     * una vez (patrón Post-Redirect-Get flash messages).
     *
     * @param request el HttpServletRequest actual
     */
    public static void pasarMensajes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;

        transferir(session, request, "mensajeExito");
        transferir(session, request, "mensajeError");
    }

    private static void transferir(HttpSession session,
                                   HttpServletRequest request,
                                   String atributo) {
        Object valor = session.getAttribute(atributo);
        if (valor != null) {
            request.setAttribute(atributo, valor);
            session.removeAttribute(atributo);
        }
    }
}
