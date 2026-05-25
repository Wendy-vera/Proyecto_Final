package database_nirami;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClassConexion {

    private static final String URL      = "jdbc:mysql://localhost:3306/nirami"
                                         + "?useSSL=false"
                                         + "&allowPublicKeyRetrieval=true"
                                         + "&serverTimezone=America/Bogota";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "RosaFabio9014";

    public static Connection MetodoConexion() {
        try {
            // Cargar el driver con el classloader del contexto web (obligatorio en Tomcat)
            Class.forName("com.mysql.cj.jdbc.Driver",
                          true,
                          Thread.currentThread().getContextClassLoader());

            Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            System.out.println("[ClassConexion] Conexion abierta a 'nirami'.");
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
