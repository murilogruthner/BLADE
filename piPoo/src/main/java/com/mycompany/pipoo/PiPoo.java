/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pipoo;

import telas.telaLogin1;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import model.Gastos;



/**
 *
 * @author muril
 */
public class PiPoo {
     public static void main(String[] args) {
        System.out.println("Iniciando o sistema...");

        try (Connection conn = ConnectionFactory.getConnection()) {
            if (conn != null) {
                System.out.println("Conexao com SQLite funcionando!");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
        new Gastos().createTable();

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new telaLogin1().setVisible(true);
            }
        });
    }
}
