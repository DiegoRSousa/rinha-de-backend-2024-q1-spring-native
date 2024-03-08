package com.rinha.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
public class AppController {

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<TransacaoResponse> novaTransacao(@PathVariable int id,
            @RequestBody TransacaoRequest transacao) {

        if (!transacao.validar())
            return ResponseEntity.unprocessableEntity().build();

        Cliente cliente;
        try {
            try (Connection con = HikariCPDataSource.getConnection()) {
                con.setAutoCommit(false);

                try (PreparedStatement ps = con
                        .prepareStatement("select * from cliente where id = ? for no key update")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next())
                        cliente = new Cliente(rs.getInt("id"), rs.getInt("limite"), rs.getInt("saldo"));
                    else
                        return ResponseEntity.notFound().build();
                }


                if ('c' == transacao.tipo()) {
                    cliente.creditar((int) transacao.valor());
                } else {
                    if (!cliente.debitar((int) transacao.valor())) {
                        return ResponseEntity.unprocessableEntity().build();
                    }
                }

                try (PreparedStatement ps = con
                        .prepareStatement("update cliente set limite = ?, saldo = ? where id = ?")) {
                    ps.setInt(1, cliente.getLimite());
                    ps.setInt(2, cliente.getSaldo());
                    ps.setInt(3, cliente.getId());
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "insert into transacao (descricao, tipo, valor, cliente_id, realizada_em) values (?,?,?,?,?)")) {
                    ps.setString(1, transacao.descricao());
                    ps.setString(2, String.valueOf(transacao.tipo()));
                    ps.setInt(3, (int) transacao.valor());
                    ps.setInt(4, id);
                    ps.setObject(5, LocalDateTime.now());
                    ps.executeUpdate();
                }

                con.commit();

            }

        } catch (SQLException e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(new TransacaoResponse(cliente));

    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<Extrato> getExtrato(@PathVariable int id) {
        Cliente cliente;
        List<Transacao> ultimasTransacoes = new ArrayList<>();
        try {
            try (Connection con = HikariCPDataSource.getConnection()) {
                try (PreparedStatement ps = con
                        .prepareStatement("select id, limite, saldo from cliente where id = ?")) {
                    ps.setInt(1, id);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next())
                        cliente = new Cliente(rs.getInt("id"), rs.getInt("limite"), rs.getInt("saldo"));
                    else
                        return ResponseEntity.notFound().build();
                }

                try (PreparedStatement ps = con.prepareStatement("""
                        select t.id, t.valor, t.tipo, t.descricao, t.realizada_em
                        from Transacao t
                        where t.cliente_id = ?
                        order by realizada_em desc limit 10
                        """)) {
                    ps.setInt(1, id);
                    var rs = ps.executeQuery();
                    while (rs.next()) {
                        ultimasTransacoes.add(new Transacao(
                                rs.getInt("id"),
                                rs.getInt("valor"),
                                rs.getString("tipo").charAt(0),
                                rs.getString("descricao"),
                                rs.getObject("realizada_em", LocalDateTime.class)));
                    }

                }

            }
        } catch (SQLException e) {
            System.out.println(e);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(new Extrato(cliente, ultimasTransacoes));
    }

}
