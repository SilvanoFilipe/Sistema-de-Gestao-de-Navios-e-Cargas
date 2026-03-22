CREATE DATABASE porto_db;
USE porto_db;

-- tabela de navios
CREATE TABLE IF NOT EXISTS navio (
    id INT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'em espera'
);

-- tabela de cargas
CREATE TABLE IF NOT EXISTS carga (
    id INT NOT NULL,
    navio_id INT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    peso DOUBLE NOT NULL,
    PRIMARY KEY (id, navio_id),
    FOREIGN KEY (navio_id) REFERENCES navio(id) ON DELETE CASCADE
);
