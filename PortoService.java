import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PortoService {

    // cadastra um navio no banco
    public boolean cadastrarNavio(int id, String nome) {
        String sql = "INSERT INTO navio (id, nome, status) VALUES (?, ?, 'em espera')";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, nome);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // erro de chave duplicada
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    // lista todos os navios
    public List<Navio> listarNavios() {
        List<Navio> lista = new ArrayList<>();
        String sql = "SELECT id, nome, status FROM navio ORDER BY id";
        try (Connection conn = ConexaoMySQL.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Navio n = new Navio(rs.getInt("id"), rs.getString("nome"));
                n.setStatus(rs.getString("status"));
                lista.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // busca um navio pelo id, incluindo as cargas associadas
    public Navio buscarNavioPorId(int id) {
        Navio navio = null;
        String sqlNavio = "SELECT id, nome, status FROM navio WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmtNavio = conn.prepareStatement(sqlNavio)) {
            stmtNavio.setInt(1, id);
            ResultSet rsNavio = stmtNavio.executeQuery();
            if (rsNavio.next()) {
                navio = new Navio(rsNavio.getInt("id"), rsNavio.getString("nome"));
                navio.setStatus(rsNavio.getString("status"));

                // carregar as cargas
                String sqlCargas = "SELECT id, descricao, peso FROM carga WHERE navio_id = ?";
                try (PreparedStatement stmtCargas = conn.prepareStatement(sqlCargas)) {
                    stmtCargas.setInt(1, id);
                    ResultSet rsCargas = stmtCargas.executeQuery();
                    while (rsCargas.next()) {
                        Carga c = new Carga(rsCargas.getInt("id"),
                                rsCargas.getString("descricao"),
                                rsCargas.getDouble("peso"));
                        navio.adicionarCarga(c);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return navio;
    }

    // busca navios cujo nome contenha o texto
    public List<Navio> buscarNaviosPorNome(String nome) {
        List<Navio> lista = new ArrayList<>();
        String sql = "SELECT id, nome, status FROM navio WHERE nome LIKE ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nome + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Navio n = new Navio(rs.getInt("id"), rs.getString("nome"));
                n.setStatus(rs.getString("status"));
                lista.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // altera o status de um navio
    public boolean alterarStatus(int id, String novoStatus) {
        String sql = "UPDATE navio SET status = ? WHERE id = ?";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, novoStatus);
            stmt.setInt(2, id);
            int linhas = stmt.executeUpdate();
            return linhas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // adiciona uma carga a um navio
    public boolean adicionarCarga(int idNavio, int idCarga, String descricao, double peso) {
        String sql = "INSERT INTO carga (id, navio_id, descricao, peso) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexaoMySQL.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarga);
            stmt.setInt(2, idNavio);
            stmt.setString(3, descricao);
            stmt.setDouble(4, peso);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                return false;
            }
            e.printStackTrace();
            return false;
        }
    }

    // obtem um texto com todos os detalhes de um navio
    public String obterDetalhesNavio(int id) {
        Navio navio = buscarNavioPorId(id);
        if (navio == null) return "navio nao encontrado.";

        StringBuilder sb = new StringBuilder();
        sb.append("id: ").append(navio.getId()).append("\n");
        sb.append("nome: ").append(navio.getNome()).append("\n");
        sb.append("status: ").append(navio.getStatus()).append("\n");
        sb.append("cargas associadas:\n");
        if (navio.getCargas().isEmpty()) {
            sb.append("  nenhuma carga cadastrada.\n");
        } else {
            for (Carga c : navio.getCargas()) {
                sb.append("  - ").append(c.toString()).append("\n");
            }
            sb.append("peso total das cargas: ").append(navio.calcularPesoTotal()).append(" t\n");
        }
        return sb.toString();
    }
}