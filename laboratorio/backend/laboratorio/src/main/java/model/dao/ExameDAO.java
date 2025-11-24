package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.dto.ExameDTO;
import model.dto.RequisicaoExamesDTO;
import model.enums.StatusExame;
import model.vo.ExameVO;
import model.vo.MedicoVO;
import model.vo.PacienteVO;

public class ExameDAO {

	// logar como paciente, tela principal
	public List<RequisicaoExamesDTO> listarPorPaciente(PacienteVO pacienteVO) {
		Connection conn = Banco.getConnection();
		ResultSet resultado = null;

		List<RequisicaoExamesDTO> listaRequisicaoExames = new ArrayList<>();

		String query = "SELECT row_number() over (order by numeropedido) as id, " + "numeropedido, nome, data "
				+ "FROM (SELECT distinct e.numeropedido, u.nome as nome, e.dataexame as data "
				+ "FROM exame e, medico m, usuario u " + "WHERE e.idmedico = m.idmedico "
				+ "AND m.idmedico = u.idusuario " + "AND e.idpaciente = ? " + ") as dados " + "ORDER BY numeropedido";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);

		try {
			pstmt.setInt(1, pacienteVO.getIdUsuario());
			resultado = pstmt.executeQuery();

			while (resultado.next()) {
				RequisicaoExamesDTO requisicaoExames = new RequisicaoExamesDTO();
				requisicaoExames.setId(resultado.getInt("id"));
				requisicaoExames.setNumeroPedido(resultado.getInt("numeropedido"));
				requisicaoExames.setNome(resultado.getString("nome"));
				requisicaoExames.setData(resultado.getDate("data").toLocalDate());
				listaRequisicaoExames.add(requisicaoExames);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método realizarLogin");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return listaRequisicaoExames;

	}

	// logar como médico, tela principal
	public List<RequisicaoExamesDTO> listarPorMedico(MedicoVO medicoVO) {
		return null;
	}

	// logar como funcionário, tela principal
	public List<ExameDTO> listar(int numeroPedido) {
		return null;
	}

	/**
	 * Cadastra um novo exame na tabela 'exame'.
	 * 
	 * @param exameVO O objeto VO com os dados do exame.
	 * @return O objeto ExameVO preenchido com o ID gerado (idExame).
	 */
	public ExameVO cadastrar(ExameVO exameVO) {
		String query = "INSERT INTO exame (idpaciente, idmedico, idtipoexame, numeropedido, dataexame, observacoes, status) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

		// Usa o método que retorna a chave gerada (idExame)
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		ResultSet resultado = null;

		try {
			pstmt.setInt(1, exameVO.getIdPaciente());
			pstmt.setInt(2, exameVO.getIdMedico());
			pstmt.setInt(3, exameVO.getIdTipoExame());
			pstmt.setInt(4, exameVO.getNumeroPedido());
			pstmt.setObject(5, exameVO.getDataExame()); // LocalDate
			pstmt.setString(6, exameVO.getObservacoes());
			pstmt.setString(7, exameVO.getStatus().name());
			pstmt.execute();

			resultado = pstmt.getGeneratedKeys();

			if (resultado.next()) {
				exameVO.setIdExame(resultado.getInt(1)); // 1 é a coluna do ID gerado
			}
		} catch (SQLException erro) {
			System.out.println("ExameDAO - Erro ao executar a query do método de cadastrar o Exame.");
			System.out.println("Erro: " + erro.getMessage());
			exameVO = null; // Indica que o cadastro falhou
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return exameVO;
	}

	/**
	 * Atualiza um exame existente na tabela 'exame'.
	 * 
	 * @param exameVO O objeto VO com os novos dados e o ID do exame.
	 * @return true se o exame foi atualizado com sucesso, false caso contrário.
	 */
	public boolean atualizar(ExameVO exameVO) {
		String query = "UPDATE exame SET idpaciente=?, idmedico=?, idtipoexame=?, numeropedido=?, "
				+ "dataexame=?, observacoes=?, status=? WHERE idexame=?";

		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);
		boolean sucesso = false;

		try {
			pstmt.setInt(1, exameVO.getIdPaciente());
			pstmt.setInt(2, exameVO.getIdMedico());
			pstmt.setInt(3, exameVO.getIdTipoExame());
			pstmt.setInt(4, exameVO.getNumeroPedido());
			pstmt.setObject(5, exameVO.getDataExame());
			pstmt.setString(6, exameVO.getObservacoes());
			pstmt.setString(7, exameVO.getStatus().name());
			pstmt.setInt(8, exameVO.getIdExame()); // WHERE CLAUSE

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = (linhasAfetadas > 0);

		} catch (SQLException erro) {
			System.out.println("ExameDAO - Erro ao executar a query do método de atualizar o Exame.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return sucesso;
	}

	/**
	 * Consulta o status atual de um exame pelo ID.
	 * 
	 * @param idExame O ID do exame a ser consultado.
	 * @return O StatusExame atual, ou null se o exame não for encontrado.
	 */
	public StatusExame consultarStatus(int idExame) {
		Connection conn = Banco.getConnection();
		ResultSet resultado = null;
		StatusExame statusAtual = null;

		String query = "SELECT status FROM exame WHERE idexame = ?";
		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);

		try {
			pstmt.setInt(1, idExame);
			resultado = pstmt.executeQuery();

			if (resultado.next()) {
				// Converte a string do banco de dados para o Enum StatusExame
				statusAtual = StatusExame.valueOf(resultado.getString("status"));
			}
		} catch (SQLException erro) {
			System.out.println("ExameDAO - Erro ao consultar status do Exame.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return statusAtual;
	}

	/**
	 * Exclui um exame da tabela 'exame'.
	 * 
	 * @param idExame O ID do exame a ser excluído.
	 * @return true se a exclusão foi bem-sucedida, false caso contrário.
	 */
	public boolean excluir(int idExame) {
		String query = "DELETE FROM exame WHERE idexame = ?";

		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);
		boolean sucesso = false;

		try {
			pstmt.setInt(1, idExame);

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = (linhasAfetadas > 0);

		} catch (SQLException erro) {
			System.out.println("ExameDAO - Erro ao executar a query do método de excluir o Exame.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return sucesso;
	}

}
