package database_nirami;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ClassConexion {

<<<<<<< HEAD
    // 1. CAMBIAMOS EL NOMBRE DE LA BASE DE DATOS EN LA URL (De 'nirami' a 'nirami_db_completo')
    private static final String URL      = "jdbc:mysql://localhost:3306/nirami_db_completo"
=======
    private static final String URL      = "jdbc:mysql://localhost:3306/nirami"
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
                                         + "?useSSL=false"
                                         + "&allowPublicKeyRetrieval=true"
                                         + "&serverTimezone=America/Bogota";
    private static final String USUARIO  = "root";
<<<<<<< HEAD
    private static final String PASSWORD = "#Aprendiz2024";
=======
    private static final String PASSWORD = "RosaFabio9014";
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5

    public static Connection MetodoConexion() {
        try {
            // Cargar el driver con el classloader del contexto web (obligatorio en Tomcat)
            Class.forName("com.mysql.cj.jdbc.Driver",
                          true,
                          Thread.currentThread().getContextClassLoader());

            Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD);
<<<<<<< HEAD
            
            // 2. ACTUALIZAMOS EL MENSAJE DE CONSOLA PARA EVITAR CONFUSIONES
            System.out.println("[ClassConexion] Conexion abierta a 'nirami_db_completo'.");
=======
            System.out.println("[ClassConexion] Conexion abierta a 'nirami'.");
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
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
<<<<<<< HEAD
}
=======
}
>>>>>>> 9bc7c03d3ca472ea15f2bbb1639b150f685a96b5
