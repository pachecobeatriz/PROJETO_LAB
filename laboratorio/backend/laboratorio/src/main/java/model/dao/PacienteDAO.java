package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.enums.Perfil;
import model.vo.PacienteVO;

public class PacienteDAO {

	// CADASTRAR...

	public PacienteVO cadastrar(PacienteVO pacienteVO, Connection conn) {
		String query = "INSERT INTO paciente (idpaciente, datanascimento) " + "VALUES(?, ?)";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);

		try {
			pstmt.setInt(1, pacienteVO.getIdUsuario());
			pstmt.setObject(2, pacienteVO.getDataNascimento());
			pstmt.execute();
		} catch (Exception erro) {
			System.out.println("PacienteDAO - Erro ao executar a query do método de cadastrar o Paciente.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return pacienteVO;
	}

	// ATUALIZAR...

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

	// 3 LISTAR...

	public PacienteVO buscarPorId(int idUsuario) {
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultado = null;
		PacienteVO paciente = null;

		String query = "SELECT u.idusuario, u.nome, u.cpf, u.email, u.login, u.senha, u.perfil, "
				+ "       p.datanascimento " + "FROM usuario u " + "JOIN paciente p ON u.idusuario = p.idpaciente "
				+ "WHERE u.idusuario = ?";

		try {
			pstmt = Banco.getPreparedStatement(conn, query);
			pstmt.setInt(1, idUsuario);

			resultado = pstmt.executeQuery();

			if (resultado.next()) {
				paciente = new PacienteVO();
				paciente.setIdUsuario(resultado.getInt("idusuario"));
				paciente.setNome(resultado.getString("nome"));
				paciente.setCpf(resultado.getString("cpf"));
				paciente.setEmail(resultado.getString("email"));
				paciente.setLogin(resultado.getString("login"));
				paciente.setSenha(resultado.getString("senha"));
				paciente.setPerfil(Perfil.valueOf(resultado.getString("perfil")));

				// datanascimento (DATE) -> LocalDate
				paciente.setDataNascimento(resultado.getDate("datanascimento").toLocalDate());
			}

		} catch (SQLException erro) {
			System.out.println("PacienteDAO - Erro ao executar a query do método buscarPorId.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return paciente;
	}

}
