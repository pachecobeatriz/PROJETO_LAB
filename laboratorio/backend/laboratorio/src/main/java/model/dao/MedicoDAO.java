package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.enums.Perfil;
import model.vo.MedicoVO;

public class MedicoDAO {

	// CADASTRAR...

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

	// ATUALIZAR...

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

	// LISTAR...

	public MedicoVO buscarPorId(int idUsuario) {
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultado = null;
		MedicoVO medico = null;

		String query = "SELECT u.idusuario, u.nome, u.cpf, u.email, u.login, u.senha, u.perfil, "
				+ "       m.crm, m.especialidade " + "FROM usuario u " + "JOIN medico m ON u.idusuario = m.idmedico "
				+ "WHERE u.idusuario = ?";

		try {
			pstmt = Banco.getPreparedStatement(conn, query);
			pstmt.setInt(1, idUsuario);

			resultado = pstmt.executeQuery();

			if (resultado.next()) {
				medico = new MedicoVO();
				medico.setIdUsuario(resultado.getInt("idusuario"));
				medico.setNome(resultado.getString("nome"));
				medico.setCpf(resultado.getString("cpf"));
				medico.setEmail(resultado.getString("email"));
				medico.setLogin(resultado.getString("login"));
				medico.setSenha(resultado.getString("senha"));
				medico.setPerfil(Perfil.valueOf(resultado.getString("perfil")));
				medico.setCrm(resultado.getString("crm"));
				medico.setEspecialidade(resultado.getString("especialidade"));
			}

		} catch (SQLException erro) {
			System.out.println("MedicoDAO - Erro ao executar a query do método buscarPorId.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return medico;
	}

}
