import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// classe responsavel pela conexao com o banco de dados mysql
public class ConexaoMySQL {
    // ajuste os dados conforme seu ambiente
    private static final String URL = "jdbc:mysql://localhost:3306/porto_db";
    private static final String USUARIO = "root";
    private static final String SENHA = "senha";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("driver jdbc nao encontrado");
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
