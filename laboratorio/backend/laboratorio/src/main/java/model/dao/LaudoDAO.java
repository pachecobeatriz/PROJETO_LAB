package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import model.vo.LaudoVO;

public class LaudoDAO {

	public boolean cadastrar(LaudoVO laudo) {
		String sql = "INSERT INTO laudo (idexame, arquivo, datalaudo) VALUES (?, ?, ?)";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean sucesso = false;

		try {
			conn = Banco.getConnection();
			// pede as chaves geradas para preencher idLaudo
			pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			pstmt.setInt(1, laudo.getIdExame());
			pstmt.setBytes(2, laudo.getArquivo());

			if (laudo.getDataLaudo() != null) {
				pstmt.setDate(3, Date.valueOf(laudo.getDataLaudo()));
			} else {
				pstmt.setNull(3, Types.DATE);
			}

			int linhasAfetadas = pstmt.executeUpdate();
			if (linhasAfetadas > 0) {
				sucesso = true;
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int idGerado = rs.getInt(1);
					laudo.setIdLaudo(idGerado);
				}
			}

		} catch (SQLException e) {
			System.out.println("LaudoDAO - Erro ao cadastrar laudo.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("LaudoDAO - Erro ao fechar ResultSet.");
			}
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return sucesso;
	}

	public boolean atualizar(LaudoVO laudo) {
		String sql = "UPDATE laudo SET idexame = ?, arquivo = ?, datalaudo = ? WHERE idlaudo = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean sucesso = false;

		try {
			conn = Banco.getConnection();
			pstmt = Banco.getPreparedStatement(conn, sql);

			pstmt.setInt(1, laudo.getIdExame());
			pstmt.setBytes(2, laudo.getArquivo());

			if (laudo.getDataLaudo() != null) {
				pstmt.setDate(3, Date.valueOf(laudo.getDataLaudo()));
			} else {
				pstmt.setNull(3, Types.DATE);
			}

			pstmt.setInt(4, laudo.getIdLaudo());

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = linhasAfetadas > 0;

		} catch (SQLException e) {
			System.out.println("LaudoDAO - Erro ao atualizar laudo.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return sucesso;
	}

	// Busca o laudo pelo idExame (útil para download do PDF).
	public LaudoVO buscarPorIdExame(int idExame) {
		String sql = "SELECT idlaudo, idexame, arquivo, datalaudo " + "FROM laudo " + "WHERE idexame = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		LaudoVO laudo = null;

		try {
			conn = Banco.getConnection();
			pstmt = Banco.getPreparedStatement(conn, sql);
			pstmt.setInt(1, idExame);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				laudo = new LaudoVO();
				laudo.setIdLaudo(rs.getInt("idlaudo"));
				laudo.setIdExame(rs.getInt("idexame"));
				laudo.setArquivo(rs.getBytes("arquivo"));

				Date data = rs.getDate("datalaudo");
				if (data != null) {
					laudo.setDataLaudo(data.toLocalDate());
				}
			}

		} catch (SQLException e) {
			System.out.println("LaudoDAO - Erro ao buscar laudo por idExame.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			try {
				if (rs != null && !rs.isClosed()) {
					rs.close();
				}
			} catch (SQLException e) {
				System.out.println("LaudoDAO - Erro ao fechar ResultSet.");
			}
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return laudo;
	}
}
