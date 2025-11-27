package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.dto.ExameDTO;
import model.dto.RequisicaoExamesDTO;
import model.enums.StatusExame;
import model.vo.ExameVO;
import model.vo.PacienteVO;

public class ExameDAO {

	// CADASTRAR...

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

	// ATUALIZAR...

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
			pstmt.setInt(8, exameVO.getIdExame()); // where

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

	// EXCLUIR...

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

	// 3 LISTAR...

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

				requisicaoExames.setData(resultado.getDate("data").toLocalDate().toString());
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
	public List<RequisicaoExamesDTO> listarPorMedico(int idMedico) {
		Connection conn = Banco.getConnection();
		ResultSet resultado = null;

		List<RequisicaoExamesDTO> listaRequisicaoExames = new ArrayList<>();

		String query = "SELECT row_number() over (order by numeropedido) as id, " + "       numeropedido, nome, data "
				+ "FROM (SELECT DISTINCT e.numeropedido, u.nome AS nome, e.dataexame AS data " + "      FROM exame e "
				+ "      JOIN paciente p ON e.idpaciente = p.idpaciente "
				+ "      JOIN usuario u ON p.idpaciente = u.idusuario " + "      WHERE e.idmedico = ?) AS dados "
				+ "ORDER BY numeropedido";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);

		try {
			pstmt.setInt(1, idMedico);
			resultado = pstmt.executeQuery();

			while (resultado.next()) {
				RequisicaoExamesDTO requisicao = new RequisicaoExamesDTO();
				requisicao.setId(resultado.getInt("id"));
				requisicao.setNumeroPedido(resultado.getInt("numeropedido"));
				requisicao.setNome(resultado.getString("nome"));

				// se o DTO estiver com String data:
				requisicao.setData(resultado.getDate("data").toLocalDate().toString());

				listaRequisicaoExames.add(requisicao);
			}
		} catch (SQLException erro) {
			System.out.println("ExameDAO - Erro ao listar requisições por médico.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return listaRequisicaoExames;
	}

	// logar como funcionário, tela principal
	// logar como funcionário / paciente, tela principal da requisição
	public List<ExameDTO> listar(int numeroPedido) {
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		ResultSet resultado = null;

		List<ExameDTO> listaExames = new ArrayList<>();

		String query = "SELECT e.idexame, " + "       e.idpaciente, " + "       uPac.nome      AS nomePaciente, "
				+ "       e.idmedico, " + "       uMed.nome      AS nomeMedico, " + "       e.idtipoexame, "
				+ "       t.nome         AS nomeExame, " + "       e.numeropedido, " + "       e.dataexame, "
				+ "       e.observacoes, " + "       e.status, " + "       l.idlaudo, " + "       l.datalaudo "
				+ "FROM exame e " + "JOIN paciente p    ON e.idpaciente = p.idpaciente "
				+ "JOIN usuario uPac  ON p.idpaciente = uPac.idusuario "
				+ "JOIN medico m      ON e.idmedico = m.idmedico "
				+ "JOIN usuario uMed  ON m.idmedico = uMed.idusuario "
				+ "JOIN tipo_exame t  ON e.idtipoexame = t.idtipoexame "
				+ "LEFT JOIN laudo l  ON e.idexame = l.idexame " + "WHERE e.numeropedido = ? " + "ORDER BY e.idexame";

		try {
			pstmt = Banco.getPreparedStatement(conn, query);
			pstmt.setInt(1, numeroPedido);
			resultado = pstmt.executeQuery();

			while (resultado.next()) {
				ExameDTO dto = new ExameDTO();
				dto.setIdExame(resultado.getInt("idexame"));
				dto.setIdPaciente(resultado.getInt("idpaciente"));
				dto.setPaciente(resultado.getString("nomePaciente"));
				dto.setIdMedico(resultado.getInt("idmedico"));
				dto.setMedico(resultado.getString("nomeMedico"));
				dto.setIdTipoExame(resultado.getInt("idtipoexame"));
				dto.setNomeExame(resultado.getString("nomeExame"));
				dto.setNumeropedido(resultado.getInt("numeropedido"));
				dto.setDataExame(resultado.getDate("dataexame").toLocalDate().toString());
				dto.setObservacoes(resultado.getString("observacoes"));
				dto.setStatus(StatusExame.valueOf(resultado.getString("status")));
				dto.setIdLaudo(resultado.getInt("idlaudo"));
				if (resultado.wasNull()) {
					dto.setIdLaudo(0); // se quiser tratar 0 como "sem laudo"
				}
				java.sql.Date dataLaudoSql = resultado.getDate("datalaudo");
				if (dataLaudoSql != null) {
					dto.setDataLaudo(dataLaudoSql.toLocalDate().toString());
				}

				listaExames.add(dto);
			}
		} catch (SQLException erro) {
			System.out.println("ExameDAO - Erro ao listar exames por número de requisição.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return listaExames;
	}

	// 2 OUTROS...

	public boolean verificarExistenciaExamePorId(ExameVO exameVO, Connection conn) {
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;

		String query = "SELECT idexame FROM exame WHERE idexame = '" + exameVO.getIdExame() + "' ";

		try {
			resultado = stmt.executeQuery(query);

			if (resultado.next()) {
				retorno = true;
			}
		} catch (Exception erro) {
			System.out
					.println("Erro ao executar a query do método que verifica se o Exame já existe na base de dados.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return retorno;
	}

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

	public boolean atualizarStatusExame(int idExame, StatusExame novoStatus) {
		String sql = "UPDATE exame SET status = ? WHERE idexame = ?";

		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean sucesso = false;

		try {
			conn = Banco.getConnection();
			pstmt = Banco.getPreparedStatement(conn, sql);

			pstmt.setString(1, novoStatus.name()); // "PENDENTE" ou "PRONTO"
			pstmt.setInt(2, idExame);

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = (linhasAfetadas > 0);

		} catch (SQLException e) {
			System.out.println("ExameDAO - Erro ao atualizar status do exame.");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}

		return sucesso;
	}
}
