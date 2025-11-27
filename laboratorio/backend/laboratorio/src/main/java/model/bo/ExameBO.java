package model.bo;

import java.sql.Connection;
import java.util.List;

import javax.transaction.UserTransaction;

import model.dao.BancoJTA;
//import jakarta.validation.constraints.AssertFalse.List;
import model.dao.ExameDAO;
import model.dto.ExameDTO;
import model.dto.RequisicaoExamesDTO;
import model.enums.StatusExame;
import model.vo.ExameVO;
import model.vo.PacienteVO;

public class ExameBO {

	// CADASTRAR...

	// POST com VO
	public ExameVO cadastrar(ExameVO exameVO) {
		ExameDAO exameDAO = new ExameDAO();

		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA();

		try {
			userTransaction.begin();

			if (exameDAO.verificarExistenciaExamePorId(exameVO, conn)) {
				System.out.println("\nExame já cadastrado!");
			} else {
				exameVO = exameDAO.cadastrar(exameVO);
			}

			userTransaction.commit();

		} catch (Exception erro) {
			BancoJTA.rollbackJTA();

		} finally {
			BancoJTA.closeConnectionJTA(conn);
		}

		return exameVO;
	}

	// ATUALIZAR...

	public boolean atualizar(ExameVO exameVO) {
		ExameDAO exameDAO = new ExameDAO();

		if (exameVO.getIdExame() <= 0) {
			System.out.println("Erro BO: ID do Exame inválido para atualização.");
			return false;
		}

		return exameDAO.atualizar(exameVO);
	}

	// EXCLUIR...

	public String excluir(int idExame) {
		ExameDAO exameDAO = new ExameDAO();

		StatusExame statusAtual = exameDAO.consultarStatus(idExame);

		if (statusAtual == null) {
			return "NAO_ENCONTRADO";
		}
		if (statusAtual != StatusExame.PENDENTE) {
			return "LAUDO_EXISTENTE";
		}

		boolean sucesso = exameDAO.excluir(idExame);

		if (sucesso) {
			return "SUCESSO";
		} else {
			return "FALHA_DB";
		}

	}

	// 3 LISTAR...

	public List<RequisicaoExamesDTO> listarRequisicoesPorPaciente(int idPaciente) {
		ExameDAO exameDAO = new ExameDAO();

		PacienteVO pacienteVO = new PacienteVO();
		pacienteVO.setIdUsuario(idPaciente);

		return exameDAO.listarPorPaciente(pacienteVO);
	}

	public List<RequisicaoExamesDTO> listarRequisicoesPorMedico(int idMedico) {
		ExameDAO exameDAO = new ExameDAO();
		return exameDAO.listarPorMedico(idMedico);
	}

	public List<ExameDTO> listarPorRequisicao(int numeroPedido) {
		ExameDAO exameDAO = new ExameDAO();
		return exameDAO.listar(numeroPedido);
	}

//	// POST com VO mais simples
//	public ExameVO cadastrar(ExameVO exameVO) {
//		ExameDAO exameDAO = new ExameDAO();
//
//		if (exameVO.getStatus() == null) {
//			exameVO.setStatus(StatusExame.PENDENTE);
//		}
//
//		return exameDAO.cadastrar(exameVO);
//	}

}
