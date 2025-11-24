package model.bo;

import java.util.List;

//import jakarta.validation.constraints.AssertFalse.List;
import model.dao.ExameDAO;
import model.dto.ExameDTO;
import model.dto.RequisicaoExamesDTO;
import model.enums.StatusExame;
import model.vo.ExameVO;
import model.vo.PacienteVO;

public class ExameBO {

	public ExameVO cadastrar(ExameVO exameVO) {
		ExameDAO exameDAO = new ExameDAO();

		if (exameVO.getStatus() == null) {
			exameVO.setStatus(StatusExame.PENDENTE);
		}

		return exameDAO.cadastrar(exameVO);
	}

	public boolean atualizar(ExameVO exameVO) {
		ExameDAO exameDAO = new ExameDAO();

		if (exameVO.getIdExame() <= 0) {
			System.out.println("Erro BO: ID do Exame inválido para atualização.");
			return false;
		}

		return exameDAO.atualizar(exameVO);
	}

	public String excluir(int idExame) {
		ExameDAO exameDAO = new ExameDAO();

		// Consulta o status atual do exame
		StatusExame statusAtual = exameDAO.consultarStatus(idExame);

		if (statusAtual == null) {
			return "NAO_ENCONTRADO";
		}

		// Aplica a Regra de Negócio: Só pode excluir se for PENDENTE
		if (statusAtual != StatusExame.PENDENTE) {
			return "LAUDO_EXISTENTE";
		}

		// Executa a exclusão
		boolean sucesso = exameDAO.excluir(idExame);

		if (sucesso) {
			return "SUCESSO";
		} else {
			return "FALHA_DB";
		}
	}

	// ~ NOVAS ADIÇÕES - Sandro ~

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

}
