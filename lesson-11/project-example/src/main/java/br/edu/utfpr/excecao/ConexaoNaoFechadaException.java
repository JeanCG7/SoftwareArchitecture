/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.excecao;

/**
 *
 * @author jean
 */
public class ConexaoNaoFechadaException extends Exception {
    public ConexaoNaoFechadaException(String erro) {
        super(erro);
    }
}