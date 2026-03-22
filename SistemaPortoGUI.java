import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// classe principal com a interface grafica swing
public class SistemaPortoGUI extends JFrame {
    private PortoService service;
    private JTabbedPane tabbedPane;

    // componentes dos paineis
    private JTextField txtIdNavio, txtNomeNavio;
    private JTextField txtIdNavioCarga, txtIdCarga, txtDescCarga, txtPesoCarga;
    private JTextField txtPesquisa;
    private JTable tableNavios;
    private DefaultTableModel tableModel;
    private JTextArea txtDetalhes;
    private JTextField txtAlterarId;   // apenas o campo id; status via combobox

    public SistemaPortoGUI() {
        service = new PortoService();
        setTitle("sistema de gestao de navios e cargas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("cadastrar navio", criarPainelCadastroNavio());
        tabbedPane.addTab("cadastrar carga", criarPainelCadastroCarga());
        tabbedPane.addTab("listar navios", criarPainelListarNavios());
        tabbedPane.addTab("pesquisar navio", criarPainelPesquisar());
        tabbedPane.addTab("alterar status", criarPainelAlterarStatus());
        tabbedPane.addTab("detalhes do navio", criarPainelDetalhes());

        add(tabbedPane);
        setVisible(true);
    }

    private JPanel criarPainelCadastroNavio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("id do navio:"), gbc);
        txtIdNavio = new JTextField(10);
        gbc.gridx = 1;
        panel.add(txtIdNavio, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("nome do navio:"), gbc);
        txtNomeNavio = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtNomeNavio, gbc);

        JButton btnCadastrar = new JButton("cadastrar");
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnCadastrar, gbc);

        JLabel lblMensagem = new JLabel(" ");
        gbc.gridy = 3;
        panel.add(lblMensagem, gbc);

        btnCadastrar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtIdNavio.getText().trim());
                String nome = txtNomeNavio.getText().trim();
                if (nome.isEmpty()) {
                    lblMensagem.setText("nome nao pode ser vazio.");
                    return;
                }
                if (service.cadastrarNavio(id, nome)) {
                    lblMensagem.setText("navio cadastrado com sucesso!");
                    txtIdNavio.setText("");
                    txtNomeNavio.setText("");
                    atualizarTabela();
                } else {
                    lblMensagem.setText("id ja existente. tente outro.");
                }
            } catch (NumberFormatException ex) {
                lblMensagem.setText("id deve ser um numero inteiro.");
            }
        });

        return panel;
    }

    private JPanel criarPainelCadastroCarga() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("id do navio:"), gbc);
        txtIdNavioCarga = new JTextField(10);
        gbc.gridx = 1;
        panel.add(txtIdNavioCarga, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("id da carga:"), gbc);
        txtIdCarga = new JTextField(10);
        gbc.gridx = 1;
        panel.add(txtIdCarga, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("descricao:"), gbc);
        txtDescCarga = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtDescCarga, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("peso (t):"), gbc);
        txtPesoCarga = new JTextField(10);
        gbc.gridx = 1;
        panel.add(txtPesoCarga, gbc);

        JButton btnCadastrar = new JButton("associar carga");
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(btnCadastrar, gbc);

        JLabel lblMensagem = new JLabel(" ");
        gbc.gridy = 5;
        panel.add(lblMensagem, gbc);

        btnCadastrar.addActionListener(e -> {
            try {
                int idNavio = Integer.parseInt(txtIdNavioCarga.getText().trim());
                int idCarga = Integer.parseInt(txtIdCarga.getText().trim());
                double peso = Double.parseDouble(txtPesoCarga.getText().trim());
                String descricao = txtDescCarga.getText().trim();
                if (descricao.isEmpty()) {
                    lblMensagem.setText("descricao nao pode ser vazia.");
                    return;
                }
                if (service.adicionarCarga(idNavio, idCarga, descricao, peso)) {
                    lblMensagem.setText("carga associada com sucesso!");
                    txtIdNavioCarga.setText("");
                    txtIdCarga.setText("");
                    txtDescCarga.setText("");
                    txtPesoCarga.setText("");
                    atualizarTabela();
                } else {
                    lblMensagem.setText("navio nao encontrado ou carga ja existente.");
                }
            } catch (NumberFormatException ex) {
                lblMensagem.setText("verifique os campos numericos.");
            }
        });

        return panel;
    }

    private JPanel criarPainelListarNavios() {
        JPanel panel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"id", "nome", "status"}, 0);
        tableNavios = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(tableNavios);
        panel.add(scroll, BorderLayout.CENTER);

        JButton btnAtualizar = new JButton("atualizar lista");
        panel.add(btnAtualizar, BorderLayout.SOUTH);
        btnAtualizar.addActionListener(e -> atualizarTabela());

        atualizarTabela();
        return panel;
    }

    private JPanel criarPainelPesquisar() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("pesquisar por nome ou id:"));
        txtPesquisa = new JTextField(20);
        top.add(txtPesquisa);
        JButton btnPesquisar = new JButton("pesquisar");
        top.add(btnPesquisar);
        panel.add(top, BorderLayout.NORTH);

        JTextArea areaResultado = new JTextArea(10, 40);
        areaResultado.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaResultado);
        panel.add(scroll, BorderLayout.CENTER);

        btnPesquisar.addActionListener(e -> {
            String termo = txtPesquisa.getText().trim();
            if (termo.isEmpty()) {
                areaResultado.setText("digite um termo para pesquisa.");
                return;
            }
            try {
                int id = Integer.parseInt(termo);
                Navio navio = service.buscarNavioPorId(id);
                if (navio != null) {
                    areaResultado.setText("navio encontrado:\nid: " + navio.getId() + "\nnome: " + navio.getNome() + "\nstatus: " + navio.getStatus());
                } else {
                    areaResultado.setText("nenhum navio com id " + id);
                }
            } catch (NumberFormatException ex) {
                List<Navio> resultados = service.buscarNaviosPorNome(termo);
                if (resultados.isEmpty()) {
                    areaResultado.setText("nenhum navio encontrado com o nome \"" + termo + "\"");
                } else {
                    StringBuilder sb = new StringBuilder("navios encontrados:\n");
                    for (Navio n : resultados) {
                        sb.append("id: ").append(n.getId()).append(" | nome: ").append(n.getNome()).append(" | status: ").append(n.getStatus()).append("\n");
                    }
                    areaResultado.setText(sb.toString());
                }
            }
        });

        return panel;
    }

    private JPanel criarPainelAlterarStatus() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("id do navio:"), gbc);
        txtAlterarId = new JTextField(10);
        gbc.gridx = 1;
        panel.add(txtAlterarId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("novo status:"), gbc);

        // combobox com os status permitidos
        String[] statusOptions = {"em espera", "atracado", "carregado"};
        JComboBox<String> cbStatus = new JComboBox<>(statusOptions);
        gbc.gridx = 1;
        panel.add(cbStatus, gbc);

        JButton btnAlterar = new JButton("alterar status");
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(btnAlterar, gbc);

        JLabel lblMensagem = new JLabel(" ");
        gbc.gridy = 3;
        panel.add(lblMensagem, gbc);

        btnAlterar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtAlterarId.getText().trim());
                String novoStatus = (String) cbStatus.getSelectedItem();
                if (service.alterarStatus(id, novoStatus)) {
                    lblMensagem.setText("status alterado com sucesso!");
                    txtAlterarId.setText("");
                    cbStatus.setSelectedIndex(0);
                    atualizarTabela();
                } else {
                    lblMensagem.setText("navio nao encontrado.");
                }
            } catch (NumberFormatException ex) {
                lblMensagem.setText("id deve ser um numero inteiro.");
            }
        });

        return panel;
    }

    private JPanel criarPainelDetalhes() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel top = new JPanel(new FlowLayout());
        top.add(new JLabel("id do navio:"));
        JTextField txtDetalhesId = new JTextField(10);
        top.add(txtDetalhesId);
        JButton btnExibir = new JButton("exibir detalhes");
        top.add(btnExibir);
        panel.add(top, BorderLayout.NORTH);

        txtDetalhes = new JTextArea(15, 40);
        txtDetalhes.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtDetalhes);
        panel.add(scroll, BorderLayout.CENTER);

        btnExibir.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtDetalhesId.getText().trim());
                String detalhes = service.obterDetalhesNavio(id);
                txtDetalhes.setText(detalhes);
            } catch (NumberFormatException ex) {
                txtDetalhes.setText("id invalido.");
            }
        });

        return panel;
    }

    private void atualizarTabela() {
        if (tableModel != null) {
            tableModel.setRowCount(0);
            for (Navio n : service.listarNavios()) {
                tableModel.addRow(new Object[]{n.getId(), n.getNome(), n.getStatus()});
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaPortoGUI());
    }
}
