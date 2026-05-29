package database_nirami;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClassConexion {

    // 1. CAMBIAMOS EL NOMBRE DE LA BASE DE DATOS EN LA URL (De 'nirami' a 'nirami_db_completo')
    private static final String URL      = "jdbc:mysql://localhost:3306/nirami_db_completo"
                                         + "?useSSL=false"
                                         + "&allowPublicKeyRetrieval=true"
                                         + "&serverTimezone=America/Bogota";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "#Aprendiz2024";

    public static Connection MetodoConexion() {
        try {
            // Cargar el driver con el classloader del contexto web (obligatorio en Tomcat)
            Class.forName("com.mysql.cj.jdbc.Driver",
                          true,
                          Thread.currentThread().getContextClassLoader());

            Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            
            // 2. ACTUALIZAMOS EL MENSAJE DE CONSOLA PARA EVITAR CONFUSIONES
            System.out.println("[ClassConexion] Conexion abierta a 'nirami_db_completo'.");
            return con;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                "Driver MySQL no encontrado. Verifica que mysql-connector-j este en WEB-INF/lib. "
                + e.getMessage(), e);
        } catch (SQLException e) {
            throw new RuntimeException(
                "No se pudo conectar a la BD. Causa: " + e.getMessage(), e);
        }
    }
}