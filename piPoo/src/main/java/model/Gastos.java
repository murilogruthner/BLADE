/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.mycompany.pipoo.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 *
 * @author muril
 */
public class Gastos {
    public void createTable(){
        String query = "CREATE TABLE IF NOT EXISTS gastos("
                +"id INTEGER PRIMARY KEY AUTOINCREMENT, "
                +"descricao TEXT NOT NULL, "
                +"valor REAL NOT NULL, "
                +"data TEXT NOT NULL, "
                +"categoria TEXT NOT NULL)";
                
        try(Connection conn = ConnectionFactory.getConnection();
                Statement stmt = conn.createStatement()){
                    stmt.execute(query);
        } catch(SQLException e){
            e.printStackTrace();
        }
                
    }
	private int id;
	private String descricao;
	private double valor;
        private LocalDate data;
        private String categoria;
        
	public Gastos() {
	}
	public Gastos(int id, String descricao, double valor, LocalDate data, String categoria) {
		this.id = id;
		this.descricao = descricao;
		this.valor = valor;
                this.data = data;
                this.categoria = categoria;
	}
        
        public Gastos(String descricao, double valor, LocalDate data, String categoria) {
            this.descricao = descricao;
            this.valor = valor;
            this.data = data;
            this.categoria = categoria;
}

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public double getValor() {
            return valor;
        }

        public void setValor(double valor) {
            this.valor = valor;
        }

        public LocalDate getData() {
            return data;
        }

        public void setData(LocalDate data) {
            this.data = data;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }
}
