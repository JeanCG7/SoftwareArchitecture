package br.edu.utfpr.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.java.Log;

@Log
public class ClienteDAO {

    private Connection conn;

    // Responsável por criar a tabela Cliente no banco.

    public ClienteDAO() {

        conn = ConnectionFactory.abrirConexao();
        log.info("Criando tabela cliente ...");
        try {
            conn.createStatement().executeUpdate(
                    "CREATE TABLE cliente ("
                    + "id int NOT NULL GENERATED ALWAYS AS IDENTITY CONSTRAINT id_cliente_pk PRIMARY KEY,"
                    + "nome varchar(255),"
                    + "telefone varchar(30),"
                    + "idade int,"
                    + "limiteCredito double,"
                    + "id_pais int)");
        } catch (SQLException e) {

        }
    }

    public boolean salvar(ClienteDTO dto) {
        PreparedStatement pstm = null;
        String sql;
        try {
            if (Optional.ofNullable(dto.getId()).orElse(0) != 0) {
                sql = "UPDATE cliente set nome = ?, telefone = ?, idade = ?, limiteCredito = ?, id_pais = ? WHERE id = ?";
                pstm.setInt(6, dto.getId());
            } else {
                sql = "INSERT INTO cliente (nome, telefone, idade, limiteCredito, id_pais) values (?,?,?,?,?)";
            }

            conn.prepareStatement(sql);
            pstm.setString(1, dto.getNome());
            pstm.setString(2, dto.getTelefone());
            pstm.setInt(3, dto.getIdade());
            pstm.setDouble(4, dto.getLimiteCredito());
            pstm.setInt(5, dto.getPais().getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao salvar usuário: " + e);
            return false;
        } finally {
            ConnectionFactory.fecharConexao(conn, pstm, null);
        }
        return true;
    }

    public boolean deletar(ClienteDTO dto) {
        PreparedStatement pstm = null;
        String sql;

        if (Optional.ofNullable(dto.getId()).orElse(0) == 0) {
            return false;
        }
        try {
            sql = "DELETE FROM clientes WHERE id = ?";
            conn.prepareStatement(sql);
            pstm.setInt(1, dto.getId());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar usuário: " + e);
            return false;
        } finally {
            ConnectionFactory.fecharConexao(conn, pstm, null);
        }
        return true;
    }

    public List<ClienteDTO> buscar(Integer id, String nome) {
        PreparedStatement pstm = null;
        ResultSet res = null;
        String sql;
        String kindOfSelect = "";

        if (Optional.ofNullable(id).orElse(0) == 0 && nome == "") {
            return null;
        }

        try {
            if (Optional.ofNullable(id).orElse(0) == 0) {
                kindOfSelect = "WHERE name LIKE '%?%'";
                pstm.setString(1, nome);
            } else {
                kindOfSelect = "WHERE id = ?";
                pstm.setInt(1, id);
            }
            sql = "SELECT id, nome, telefone, idade, limiteCredito, id_pais FROM clientes " + kindOfSelect;
            
            res = pstm.executeQuery();
            List<ClienteDTO> clientes = new ArrayList<ClienteDTO>();
            ClienteDTO cliente;
            while(res.next()){
                cliente = ClienteDTO.builder().id(res.getInt(1)).nome(res.getString(2)).telefone(res.getString(3)).idade(res.getInt(4)).limiteCredito(res.getDouble(5))
                        .pais(PaisDTO.builder().id(res.getInt(6)).build()).build();
                clientes.add(cliente);
            }
            return clientes;
        } catch (SQLException e) {
            System.out.println("Erro ao buscar usuários:" + e);
            return null;
        } finally {
            ConnectionFactory.fecharConexao(conn, pstm, res);
        }
    }
}
