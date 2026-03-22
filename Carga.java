public class Carga {
    private int id;
    private String descricao;
    private double peso; // peso em toneladas

    public Carga(int id, String descricao, double peso) {
        this.id = id;
        this.descricao = descricao;
        this.peso = peso;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "carga id: " + id + ", descricao: " + descricao + ", peso: " + peso + " t";
    }
}