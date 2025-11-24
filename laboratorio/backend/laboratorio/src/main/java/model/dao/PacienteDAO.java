package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.vo.PacienteVO;

public class PacienteDAO {

	public PacienteVO cadastrar(PacienteVO pacienteVO, Connection conn) {
		String query = "INSERT INTO paciente (idpaciente, datanascimento) " + "VALUES(?, ?)";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);

		try {
			pstmt.setInt(1, pacienteVO.getIdUsuario());
			System.out.println("Sucesso em pstmt.setInt");
			pstmt.setObject(2, pacienteVO.getDataNascimento());
			System.out.println("Sucesso em pstmt.setObject");
			pstmt.execute(); // parava aqui antes
			System.out.println("Sucesso em pstmt.execute");

		} catch (Exception erro) {
			System.out.println("PacienteDAO - Erro ao executar a query do método de cadastrar o Paciente.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return pacienteVO;
	}

	// NOVO
	public boolean atualizar(PacienteVO pacienteVO, Connection conn) {
		String query = "UPDATE paciente SET datanascimento=? WHERE idpaciente=?";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);
		boolean sucesso = false;

		try {
			pstmt.setObject(1, pacienteVO.getDataNascimento());
			pstmt.setInt(2, pacienteVO.getIdUsuario()); // idpaciente é igual ao idUsuario

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = (linhasAfetadas > 0);

		} catch (Exception erro) {
			System.out.println("PacienteDAO - Erro ao executar a query do método de atualizar o Paciente.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return sucesso;
	}

}
