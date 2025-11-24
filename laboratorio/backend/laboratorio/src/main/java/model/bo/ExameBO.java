package model.bo;

import java.util.List;

//import jakarta.validation.constraints.AssertFalse.List;
import model.dao.ExameDAO;
import model.dto.RequisicaoExamesDTO;
import model.enums.StatusExame;
import model.vo.ExameVO;
import model.vo.PacienteVO;

public class ExameBO {

	/**
	 * Realiza a validação e o cadastro de um novo exame.
	 * 
	 * @param exameVO O objeto VO com os dados do exame.
	 * @return O ExameVO cadastrado com o ID gerado, ou null em caso de falha.
	 */
	public ExameVO cadastrar(ExameVO exameVO) {
		ExameDAO exameDAO = new ExameDAO();

		// 1. Regra de Negócio: Garante que o status inicial do exame é PENDENTE
		if (exameVO.getStatus() == null) {
			exameVO.setStatus(StatusExame.PENDENTE);
		}

		// ** (Adicionar validações de existência de idPaciente, idMedico, idTipoExame
		// aqui) **

		// 2. Chama o DAO para persistir
		return exameDAO.cadastrar(exameVO);
	}

	// ... outros métodos (como os que chamam listarPorPaciente, etc.)

	/**
	 * Realiza validações e chama o DAO para atualizar o exame.
	 * 
	 * @param exameVO O objeto VO com os dados atualizados.
	 * @return true se a atualização foi bem-sucedida, false caso contrário.
	 */
	public boolean atualizar(ExameVO exameVO) {
		ExameDAO exameDAO = new ExameDAO();

		// 1. Regras de Negócio:
		// Ex: Você poderia adicionar aqui uma validação para garantir que o idExame foi
		// fornecido (idExame > 0)
		if (exameVO.getIdExame() <= 0) {
			System.out.println("Erro BO: ID do Exame inválido para atualização.");
			return false;
		}

		// 2. Chama o DAO para persistir
		return exameDAO.atualizar(exameVO);
	}

	/**
	 * Exclui um exame, aplicando a regra de que só pode ser PENDENTE.
	 * 
	 * @param idExame O ID do exame a ser excluído.
	 * @return String com o resultado da operação (SUCESSO, NAO_ENCONTRADO ou
	 *         LAUDO_EXISTENTE).
	 */
	public String excluir(int idExame) {
		ExameDAO exameDAO = new ExameDAO();

		// 1. Consulta o status atual do exame
		StatusExame statusAtual = exameDAO.consultarStatus(idExame);

		if (statusAtual == null) {
			return "NAO_ENCONTRADO";
		}

		// 2. Aplica a Regra de Negócio: Só pode excluir se for PENDENTE
		if (statusAtual != StatusExame.PENDENTE) {
			// O status é PRONTO ou outro que impede a exclusão
			return "LAUDO_EXISTENTE";
		}

		// 3. Se for PENDENTE, executa a exclusão
		boolean sucesso = exameDAO.excluir(idExame);

		if (sucesso) {
			return "SUCESSO";
		} else {
			// Falha genérica do DB (apesar de termos consultado antes)
			return "FALHA_DB";
		}
	}

	/**
	 * Busca todas as requisições de exames associadas ao paciente logado.
	 * 
	 * @param pacienteVO O objeto PacienteVO do usuário logado.
	 * @return Lista de RequisicaoExamesDTO.
	 */
	public List<RequisicaoExamesDTO> listarRequisicoesPorPaciente(PacienteVO pacienteVO) {
		ExameDAO exameDAO = new ExameDAO();
		return exameDAO.listarPorPaciente(pacienteVO);
	}

}
