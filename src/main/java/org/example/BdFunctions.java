package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BdFunctions {

    public static void imprime_fatura(Connection connection, String nroContrato) throws SQLException {
        String query = "SELECT * FROM Fatura WHERE nroContratoCliente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nroContrato);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String Fatura = rs.getString("nroFatura");
                    java.sql.Date vencimento = rs.getDate("vencimento");
                    System.out.println("NroFatura" + Fatura);
                    System.out.println("Vencimento: " + vencimento);
                    System.out.println("nroContrato: " + nroContrato);

                    getCliente(connection, nroContrato);
                    getFaturaInfo(connection, nroContrato);
                    getTransacaoInfo(connection);

                    float valorTot = rs.getFloat("totalFatura");
                    float parcMin = rs.getFloat("valorMinParcela");
                    System.out.println("Total a pagar: R$" + valorTot);
                    System.out.println("Parcela Mínima: R$" + parcMin);

                }

            }


        }
    }

    public static void getFaturaInfo(Connection connection, String nroContratoCliente) throws SQLException {
        String query = "SELECT c.NroCartao, c.bandeiraCartao " +
                "FROM Fatura f " +
                "JOIN Cartao c ON f.idCartao = c.idCartao " +
                "WHERE f.nroContratoCliente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nroContratoCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nroCartao = rs.getString("nroCartao");
                    String bandeira = rs.getString("bandeiraCartao");

                    System.out.println("Número do Cartão: " + nroCartao);
                    System.out.println("Bandeira do Cartão: " + bandeira + "\n");
                }
            }
        }
    }

    public static void getTransacaoInfo(Connection connection) throws SQLException {
        String query = "SELECT " +
                "t.nroDespesa AS \"Nro Transação\", " +
                "t.dataDespesa AS \"Data Transação\", " +
                "e.nomeEmpresa AS \"Empresa\", " +
                "t.valorDespesa AS \"Valor\" " +
                "FROM " +
                "Transacao t " +
                "JOIN " +
                "Empresa e ON t.idEmpresa = e.idEmpresa";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int nroTransacao = rs.getInt("Nro Transação");
                String dataTransacao = rs.getString("Data Transação");
                String nomeEmpresa = rs.getString("Empresa");
                double valor = rs.getDouble("Valor");

                System.out.println("Número da Transação: " + nroTransacao);
                System.out.println("Data da Transação: " + dataTransacao);
                System.out.println("Nome da Empresa: " + nomeEmpresa);
                System.out.println("Valor: " + valor);
                System.out.println("--------------------");
            }
        }
    }

    // O restante do seu código


    public static void getCliente(Connection connection, String nroContratoCliente) throws SQLException {
        String query = "SELECT c.* " +
                "FROM Cliente c " +
                "JOIN FaturaCartao fc ON c.idCliente = fc.idCliente " +
                "JOIN Fatura f ON fc.nroFatura = f.nroFatura " +
                "WHERE f.nroContratoCliente = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nroContratoCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ;
                    String nome = rs.getString("nomeCliente");
                    String email = rs.getString("emailCliente");
                    String CPF = rs.getString("CPF");
                    int idEndereco = rs.getInt("idEndereco");
                    System.out.println("nome:" + nome);
                    System.out.println("email:" + email);
                    System.out.println("CPF:" + CPF);
                    getEndereco(connection, idEndereco);


                }

            }

        }

    }

    public static void getEndereco(Connection connection, int idEndereco) throws SQLException {
        String query = "SELECT * FROM Endereco WHERE idEndereco = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idEndereco);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int idCidade = rs.getInt("idCidade");
                    getCity(connection, idCidade);
                    int idBairro = rs.getInt("idBairro");
                    getBairro(connection, idBairro);
                    int idLogradouro = rs.getInt("idLogradouro");
                    getLogradouro(connection, idLogradouro);
                    int numCasa = rs.getInt("nroCasa");
                    System.out.println(numCasa);
                    String complemento = rs.getString("complemento");
                    System.out.println("complemento: " + complemento + "\n");

                }
            }
        }

    }

    //Condições: existir tabela Logradouro, possuir idLogradouro na tabela Logradouro
    //Usa o id da Logradouro para obter a sigla do Logradouro, usando uma query na tabela Logradouro
    public static void getLogradouro(Connection connection, int idLogradouro) throws SQLException {
        String query = "SELECT * FROM Logradouro WHERE idLogradouro = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idLogradouro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nomeLogradouro = rs.getString("nomeLogradouro");
                    String siglaUF = rs.getString("siglaLogradouro");
                    System.out.println(siglaUF + " " + nomeLogradouro);
                }
            }
        }

    }

    //Condições: existir tabela Bairro, possuir idBairro na tabela Bairro
    //Usa o id do bairro para obter o nome do bairro, usando uma query na tabela Bairro
    public static void getBairro(Connection connection, int idBairro) throws SQLException {
        String query = "SELECT nomeBairro FROM Bairro WHERE idBairro = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idBairro);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nomeBairro = rs.getString("nomeBairro");
                    System.out.println(nomeBairro);
                }
            }
        }
    }

    //Condições: existir tabela Cidade, possuir idCidade na tabela Cidade
    //Usa o id da cidade para obter o nome da cidade e a sigla do estado, usando uma query na tabela Cidade
    public static void getCity(Connection connection, int idCidade) throws SQLException {
        String query = "SELECT nomeCidade, siglaUF FROM Cidade WHERE idCidade = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, idCidade);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nomeCidade = rs.getString("nomeCidade");
                    String siglaUF = rs.getString("siglaUF");

                    System.out.println(siglaUF + " " + nomeCidade);
                }

            }

        }

    }

    public static void unionNome(Connection connection) throws SQLException {
        String query = "SELECT nomeCliente AS Nome, 'Cliente' AS Tipo FROM Cliente " +
                "UNION " +
                "SELECT nomeEmpresa AS Nome, 'Empresa' AS Tipo FROM Empresa";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("Union NomeEmpresa e NomeCliente");
            while (rs.next()) {
                String Nome = rs.getString("Nome");
                System.out.println(Nome);
            }
        }


    }

    public static void cartesianPlan(Connection connection) throws SQLException {
        String query = "SELECT * FROM Fatura, Cartao WHERE Fatura.idCartao = Cartao.idCartao";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            System.out.println("Produto Cartesiano Fatura e Cartao");
            while (rs.next()) {
                int idCartao = rs.getInt("idCartao");
                String nroCartao = rs.getString("nroCartao");
                String bandeiraCartao = rs.getString("bandeiraCartao");
                String validadeCartao = rs.getString("validadeCartao");
                String diaVencimentoCartao = rs.getString("diaVencimentoCartao");
                int cvvCartao = rs.getInt("cvvCartao");

                int nroFatura = rs.getInt("nroFatura");
                String vencimento = rs.getString("vencimento");
                String nroContratoCliente = rs.getString("nroContratoCliente");
                float totalFatura = rs.getFloat("totalFatura");
                float valorMinParcela = rs.getFloat("valorMinParcela");

                System.out.println("--------------------");

                System.out.println("ID Cartão: " + idCartao);
                System.out.println("Número Cartão: " + nroCartao);
                System.out.println("Bandeira Cartão: " + bandeiraCartao);
                System.out.println("Validade Cartão: " + validadeCartao);
                System.out.println("Dia Vencimento Cartão: " + diaVencimentoCartao);
                System.out.println("CVV Cartão: " + cvvCartao);

                System.out.println("Número Fatura: " + nroFatura);
                System.out.println("Vencimento: " + vencimento);
                System.out.println("Número Contrato Cliente: " + nroContratoCliente);
                System.out.println("Total Fatura: " + totalFatura);
                System.out.println("Valor Mínimo Parcela: " + valorMinParcela);

                System.out.println("--------------------");
            }
        }

    }
    public static void diffFoneClienteEmpresa(Connection connection) throws SQLException {
        String query = "(SELECT nroFone, idCliente, nroDDI, nroDDD FROM FoneCliente) " +
                "EXCEPT " +
                "(SELECT nroFone, idEmpresa AS idCliente, nroDDI, nroDDD FROM FoneEmpresa)";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Diferença na Tabela FoneCliente:");
            while (rs.next()) {
                String nroFone = rs.getString("nroFone");
                int idCliente = rs.getInt("idCliente");
                String nroDDI = rs.getString("nroDDI");
                String nroDDD = rs.getString("nroDDD");

                System.out.println("nroFone: " + nroFone);
                System.out.println("idCliente: " + idCliente);
                System.out.println("nroDDI: " + nroDDI);
                System.out.println("nroDDD: " + nroDDD);
                System.out.println("--------------------");
            }


        }
    }
    public static void joinNaturalFoneClienteEmpresa (Connection connection) throws SQLException {
        String query = "SELECT " +
                "fc.nroFone AS foneCliente, " +
                "fc.idCliente, " +
                "fc.nroDDI AS ddiCliente, " +
                "fc.nroDDD AS dddCliente, " +
                "fe.nroFone AS foneEmpresa, " +
                "fe.idEmpresa, " +
                "fe.nroDDI AS ddiEmpresa, " +
                "fe.nroDDD AS dddEmpresa " +
                "FROM " +
                "FoneCliente fc " +
                "LEFT JOIN " +
                "FoneEmpresa fe ON fc.nroFone = fe.nroFone";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Resultado da Junção Natural FoneCliente e FoneEmpresa:");
            while (rs.next()) {
                String foneCliente = rs.getString("foneCliente");
                int idCliente = rs.getInt("idCliente");
                String ddiCliente = rs.getString("ddiCliente");
                String dddCliente = rs.getString("dddCliente");

                String foneEmpresa = rs.getString("foneEmpresa");
                int idEmpresa = rs.getInt("idEmpresa");
                String ddiEmpresa = rs.getString("ddiEmpresa");
                String dddEmpresa = rs.getString("dddEmpresa");

                System.out.println("Fone Cliente: " + foneCliente);
                System.out.println("ID Cliente: " + idCliente);
                System.out.println("DDI Cliente: " + ddiCliente);
                System.out.println("DDD Cliente: " + dddCliente);

                System.out.println("Fone Empresa: " + foneEmpresa);
                System.out.println("ID Empresa: " + idEmpresa);
                System.out.println("DDI Empresa: " + ddiEmpresa);
                System.out.println("DDD Empresa: " + dddEmpresa);

                System.out.println("--------------------");
            }
        }
    }
}