/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.dao;

import br.edu.utfpr.excecao.ConexaoNaoEstabelecidaException;
import br.edu.utfpr.excecao.ConexaoNaoFechadaException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author jean
 */
public class ConnectionFactory {
    private static final String bd = "jdbc:derby:memory:database;create=true";
    
    public static Connection abrirConexao() {
        try (Connection conn = DriverManager.getConnection("jdbc:derby:memory:database;create=true")) {
            return conn;
        }catch(SQLException e) {
            throw new RuntimeException("Erro ao abrir conexão:" + e);
        }
    }
    
    public static void fecharConexao(Connection conn, PreparedStatement pstm, ResultSet res) {
            try{
                if(res != null)
                    res.close();
                if(pstm != null)
                    pstm.close();
                if(conn != null)
                    conn.close();
            } catch(SQLException e) {
                System.out.println("Erro ao fechar conexão");
            }
    }
}
