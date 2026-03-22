# Sistema de Gestão de Navios e Cargas

## Descrição do Problema

Num ambiente portuário, é essencial gerir eficientemente a informação sobre os navios que atracam, as cargas que transportam e o estado de cada operação.  
Muitas vezes, esta gestão é feita de forma manual, o que pode levar a erros, duplicação de dados e dificuldade em visualizar em tempo real.

Este projeto surge para responder à necessidade de uma aplicação simples, intuitiva e funcional que permita:

- Cadastrar navios com identificador único e nome.
- Associar cargas a cada navio, com descrição e peso.
- Acompanhar o estado de cada navio (em espera, atracado, carregado).
- Pesquisar navios por ID ou nome.
- Visualizar rapidamente os detalhes de cada navio, incluindo a lista das suas cargas e o peso total transportado.

## Solução Proposta

A aplicação foi desenvolvida em **Java**, seguindo os princípios da **orientação a objetos**, o que garante um código organizado e de fácil manutenção.  
Para garantir a persistência dos dados, foi integrado um **banco de dados MySQL**, permitindo que as informações sejam guardadas mesmo após o encerramento do programa.

A arquitetura está dividida em quatro camadas principais:

1. **Modelo (Carga e Navio)** – classes que representam os objetos do domínio, encapsulando os seus atributos e comportamentos básicos.
2. **Acesso a Dados (ConexaoMySQL)** – classe responsável por estabelecer a ligação com o banco de dados.
3. **Serviço (PortoService)** – contém a lógica de negócio e a comunicação com o banco de dados, utilizando JDBC para executar operações SQL.
4. **Interface Gráfica (SistemaPortoGUI)** – construída com **Java Swing**, apresenta um conjunto de abas que organizam as diferentes funcionalidades de forma clara e acessível.

### Funcionalidades

- **Cadastrar navio** – informar ID e nome; verifica se o ID já existe na base de dados.
- **Cadastrar carga** – escolher o navio destinatário, indicar ID da carga, descrição e peso. Impede duplicação de IDs de carga no mesmo navio.
- **Listar navios** – apresenta uma tabela com todos os navios, seus IDs e estados, carregados diretamente do banco.
- **Pesquisar navio** – permite pesquisa por ID ou por parte do nome.
- **Alterar estado** – modifica o estado do navio entre “em espera”, “atracado” e “carregado”, atualizando o registo no banco.
- **Ver detalhes** – mostra informação completa do navio, incluindo a lista de cargas e o peso total.

Todas as operações fornecem feedback imediato ao utilizador, indicando sucesso ou erro de forma amigável.

## Tecnologias Usadas

- **Java** – linguagem de programação.
- **Java Swing** – biblioteca gráfica para construção da interface.
- **MySQL** – sistema de gestão de bases de dados relacional.
- **JDBC** – API Java para conexão e execução de comandos SQL.

Para o funcionamento, é necessário ter o **MySQL** instalado e em execução, bem como o **conector JDBC para MySQL**.
