package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.vo.MedicoVO;

public class MedicoDAO {

	public MedicoVO cadastrar(MedicoVO medicoVO, Connection conn) {
		String query = "INSERT INTO medico (idmedico, crm, especialidade) " + "VALUES(?, ?, ?)";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);

		try {
			pstmt.setInt(1, medicoVO.getIdUsuario());
			pstmt.setString(2, medicoVO.getCrm());
			pstmt.setString(3, medicoVO.getEspecialidade());
			pstmt.execute();
		} catch (Exception erro) {
			System.out.println("MedicoDAO - Erro ao executar a query do método de cadastrar o Médico.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return medicoVO;
	}

	// NOVO
	public boolean atualizar(MedicoVO medicoVO, Connection conn) {
		String query = "UPDATE medico SET crm=?, especialidade=? WHERE idmedico=?";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);
		boolean sucesso = false;

		try {
			pstmt.setString(1, medicoVO.getCrm());
			pstmt.setString(2, medicoVO.getEspecialidade());
			pstmt.setInt(3, medicoVO.getIdUsuario()); // idmedico é o id do usuário

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = (linhasAfetadas > 0);

		} catch (Exception erro) {
			System.out.println("MedicoDAO - Erro ao executar a query do método de atualizar o Médico.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return sucesso;
	}

}
