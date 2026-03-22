import java.util.ArrayList;
import java.util.List;

public class Navio {
    private int id;
    private String nome;
    private String status; // "em espera", "atracado", "carregado"
    private List<Carga> cargas;

    public Navio(int id, String nome) {
        this.id = id;
        this.nome = nome;
        this.status = "em espera";
        this.cargas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status.equals("em espera") || status.equals("atracado") || status.equals("carregado")) {
            this.status = status;
        }
    }

    public List<Carga> getCargas() {
        return cargas;
    }

    public void adicionarCarga(Carga carga) {
        cargas.add(carga);
    }

    public double calcularPesoTotal() {
        double total = 0;
        for (Carga c : cargas) {
            total += c.getPeso();
        }
        return total;
    }
}