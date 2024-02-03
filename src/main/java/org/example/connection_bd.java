package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection_bd {

    public static void main(String[] args){
        String host = "jdbc:postgresql://localhost:5432/FaturaCartaoCredito";
        String user = "Seu user aqui";
        String password = "Sua Senha aqui";

        //Testa conexão com o BD
        try {
            Connection connection = DriverManager.getConnection(host, user, password);
            System.out.println("Conexão Feita!\n");

            //BdFunctions.joinNaturalFoneClienteEmpresa(connection);
            //BdFunctions.diffFoneClienteEmpresa(connection);
            //BdFunctions.unionNome(connection);
            //BdFunctions.cartesianPlan(connection);
            BdFunctions.imprime_fatura(connection, "123456789123456");

            connection.close();
            System.out.println("\nConexão encerrada!");
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

}
