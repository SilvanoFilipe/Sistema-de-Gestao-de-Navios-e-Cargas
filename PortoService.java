import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PortoService {
    private List<Navio> navios;

    public PortoService() {
        navios = new ArrayList<>();
    }

    // cadastra um navio
    public boolean cadastrarNavio(int id, String nome) {
        if (buscarNavioPorId(id) != null) {
            return false; // id ja existe
        }
        navios.add(new Navio(id, nome));
        return true;
    }

    // lista todos os navios
    public List<Navio> listarNavios() {
        return new ArrayList<>(navios);
    }

    // busca navio por id
    public Navio buscarNavioPorId(int id) {
        for (Navio n : navios) {
            if (n.getId() == id) {
                return n;
            }
        }
        return null;
    }

    // busca navios por nome
    public List<Navio> buscarNaviosPorNome(String nome) {
        return navios.stream()
                .filter(n -> n.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    // altera o status de um navio
    public boolean alterarStatus(int id, String novoStatus) {
        Navio navio = buscarNavioPorId(id);
        if (navio == null) return false;
        navio.setStatus(novoStatus);
        return true;
    }

    // adiciona uma carga a um navio
    public boolean adicionarCarga(int idNavio, int idCarga, String descricao, double peso) {
        Navio navio = buscarNavioPorId(idNavio);
        if (navio == null) return false;

        // verifica se ja existe carga com este id no navio
        for (Carga c : navio.getCargas()) {
            if (c.getId() == idCarga) return false;
        }

        Carga carga = new Carga(idCarga, descricao, peso);
        navio.adicionarCarga(carga);
        return true;
    }

    // obtem detalhes de um navio
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