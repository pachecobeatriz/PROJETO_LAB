package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.enums.Perfil;
import model.vo.MedicoVO;
import model.vo.PacienteVO;
import model.vo.UsuarioVO;

public class UsuarioDAO {

	// LOGIN...

	public UsuarioVO realizarLogin(UsuarioVO usuarioVO) {

		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;

		UsuarioVO usuario = new UsuarioVO();
		String query = "SELECT idusuario, nome, cpf, email, login, senha, perfil " + "FROM usuario " + "WHERE login = '"
				+ usuarioVO.getLogin() + "' " + "AND senha = '" + usuarioVO.getSenha() + "' ";

		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) { // podia ser "if" no lugar do "while".
				usuario.setIdUsuario(resultado.getInt("idusuario"));
				usuario.setNome(resultado.getString("nome"));
				usuario.setCpf(resultado.getString("cpf"));
				usuario.setEmail(resultado.getString("email"));
				usuario.setLogin(resultado.getString("login"));
				usuario.setSenha(resultado.getString("senha"));
				usuario.setPerfil(Perfil.valueOf(resultado.getString("perfil")));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método realizarLogin");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}

		return usuario;
	}

	// CADASTRAR...

	public UsuarioVO cadastrar(UsuarioVO usuarioVO, Connection conn) {
		String query = "INSERT INTO usuario (nome, cpf, email, login, senha, perfil) " + "VALUES(?, ?, ?, ?, ?, ?)";

		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		ResultSet resultado = null;

		try {
			pstmt.setString(1, usuarioVO.getNome());
			pstmt.setString(2, usuarioVO.getCpf());
			pstmt.setString(3, usuarioVO.getEmail());
			pstmt.setString(4, usuarioVO.getLogin());
			pstmt.setString(5, usuarioVO.getSenha());
			pstmt.setString(6, usuarioVO.getPerfil().name());
			pstmt.execute();
			resultado = pstmt.getGeneratedKeys();

			if (resultado.next()) {
				usuarioVO.setIdUsuario(resultado.getInt(1)); // 1 é pq a única coluna gerada é a da chave.
			}
		} catch (Exception erro) {
			System.out.println("UsuarioDAO - Erro ao executar a query do método de cadastrar o Usuário.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return usuarioVO;
	}

	// ATUALIZAR...

	public boolean atualizar(UsuarioVO usuarioVO, Connection conn) {
		String query = "UPDATE usuario SET nome=?, cpf=?, email=?, login=?, senha=?, perfil=? WHERE idusuario=?";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);
		boolean sucesso = false;

		try {
			pstmt.setString(1, usuarioVO.getNome());
			pstmt.setString(2, usuarioVO.getCpf());
			pstmt.setString(3, usuarioVO.getEmail());
			pstmt.setString(4, usuarioVO.getLogin());
			pstmt.setString(5, usuarioVO.getSenha());
			pstmt.setString(6, usuarioVO.getPerfil().name());
			pstmt.setInt(7, usuarioVO.getIdUsuario());

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = (linhasAfetadas > 0);

		} catch (SQLException erro) {
			System.out.println("UsuarioDAO - Erro ao executar a query do método de atualizar o Usuário.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return sucesso;
	}

	// 2 OUTROS...

	public boolean verificarExistenciaPacientePorCpf(PacienteVO pacienteVO, Connection conn) {
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;

		String query = "SELECT cpf FROM usuario WHERE cpf = '" + pacienteVO.getCpf() + "' ";

		try {
			resultado = stmt.executeQuery(query);

			if (resultado.next()) {
				retorno = true;
			}
		} catch (Exception erro) {
			System.out.println(
					"Erro ao executar a query do método que verifica se o Usuário Paciente já existe na base de dados.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return retorno;
	}

	public boolean verificarExistenciaMedicoPorCpf(MedicoVO medicoVO, Connection conn) {
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;

		String query = "SELECT cpf FROM usuario WHERE cpf = '" + medicoVO.getCpf() + "' ";

		try {
			resultado = stmt.executeQuery(query);

			if (resultado.next()) {
				retorno = true;
			}
		} catch (Exception erro) {
			System.out.println(
					"Erro ao executar a query do método que verifica se o Usuário Médico já existe na base de dados.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return retorno;
	}

}
