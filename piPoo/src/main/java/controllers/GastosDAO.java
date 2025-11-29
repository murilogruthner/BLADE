package controllers;

import com.mycompany.pipoo.ConnectionFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Gastos;

public class GastosDAO {

    public void inserir(Gastos g) {
        String sql = "INSERT INTO gastos (descricao, valor, data, categoria) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, g.getDescricao());
            ps.setDouble(2, g.getValor());
            ps.setString(3, g.getData().toString());
            ps.setString(4, g.getCategoria());

            ps.executeUpdate();
            System.out.println("Gasto inserido com sucesso!");

        } catch (SQLException e) {
            System.out.println("Erro ao inserir gasto: " + e.getMessage());
        }
    }

    public List<Gastos> listarTodos() {
        List<Gastos> lista = new ArrayList<>();
        String sql = "SELECT * FROM gastos";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Gastos g = new Gastos(
                        rs.getInt("id"),
                        rs.getString("descricao"),
                        rs.getDouble("valor"),
                        LocalDate.parse(rs.getString("data")),
                        rs.getString("categoria")
                );
                lista.add(g);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar gastos: " + e.getMessage());
        }
        return lista;
    }
        
    public Map<String, Double> getGastosPorCategoria() {
        Map<String, Double> mapa = new HashMap<>();
        String sql = "SELECT categoria, SUM(valor) AS total FROM gastos GROUP BY categoria";

        try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                mapa.put(rs.getString("categoria"), rs.getDouble("total"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar gastos por categoria: " + e.getMessage());
        }

        return mapa;
    }
    
    public List<Gastos> getTodos() {
    List<Gastos> lista = new ArrayList<>();
    String sql = "SELECT descricao, valor, data, categoria FROM gastos";

    try (Connection conn = ConnectionFactory.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            String descricao = rs.getString("descricao");
            double valor = rs.getDouble("valor");

            LocalDate data = null;
            try {
                String textoData = rs.getString("data");
                if (textoData != null && !textoData.isEmpty()) {
                    data = LocalDate.parse(textoData);
                }
            } catch (Exception e) {
                data = null;
            }

            String categoria = rs.getString("categoria");

            Gastos g = new Gastos(descricao, valor, data, categoria);
            lista.add(g);
        }

    } catch (SQLException e) {
        System.out.println("Erro ao buscar gastos: " + e.getMessage());
    }

    return lista;
}
    
    public void gerarHTML() {
    GastosDAO dao = new GastosDAO();
    List<Gastos> lista = dao.getTodos();
    File arquivo = new File("gastos.html");
    
    Map<String, List<Gastos>> mapaCategoria = new LinkedHashMap<>();
    for (Gastos g : lista) {
        mapaCategoria.computeIfAbsent(g.getCategoria(), k -> new ArrayList<>()).add(g);
    }

    try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivo), StandardCharsets.UTF_8))) {

        pw.println("<html><head>");
        pw.println("<meta charset='UTF-8'>");
        pw.println("<title>Relatório de Gastos</title>");
        pw.println("<style>");
        pw.println("table { width: 100%; border-collapse: collapse; font-family: Arial; }");
        pw.println("th, td { border: 1px solid #888; padding: 8px; text-align: left; }");
        pw.println("th { background-color: #f2f2f2; }");
        pw.println(".categoria { background-color: #d9edf7; font-weight: bold; }");
        pw.println("</style>");
        pw.println("</head><body>");

        pw.println("<h2>Relatório de Gastos</h2>");

        for (String categoria : mapaCategoria.keySet()) {

            double total = mapaCategoria.get(categoria)
                    .stream()
                    .mapToDouble(Gastos::getValor)
                    .sum();

            pw.println("<table>");
            pw.println("<tr class='categoria'><td colspan='3'>" + categoria
                    + " — Total: R$ " + String.format("%.2f", total) + "</td></tr>");
            pw.println("<tr><th>Descrição</th><th>Data</th><th>Valor (R$)</th></tr>");

            for (Gastos g : mapaCategoria.get(categoria)) {
                pw.println("<tr>");
                pw.println("<td>" + g.getDescricao() + "</td>");
                pw.println("<td>" + g.getData() + "</td>");
                pw.println("<td>R$ " + String.format("%.2f", g.getValor()) + "</td>");
                pw.println("</tr>");
            }

            pw.println("</table><br>");
        }

        pw.println("</body></html>");
        pw.flush();

    } catch (Exception e) {
        System.out.println("Erro ao gerar HTML: " + e.getMessage());
    }
}

  }
