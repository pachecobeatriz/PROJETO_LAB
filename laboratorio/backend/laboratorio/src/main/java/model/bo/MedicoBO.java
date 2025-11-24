package model.bo;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import model.dao.BancoJTA;
import model.dao.MedicoDAO;
import model.dao.UsuarioDAO;
import model.vo.MedicoVO;
import model.vo.UsuarioVO;

public class MedicoBO {

	public MedicoVO cadastrar(MedicoVO medicoVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		MedicoDAO medicoDAO = new MedicoDAO();

		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA();

		try {
			userTransaction.begin();

			if (usuarioDAO.verificarExistenciaMedicoPorCpf(medicoVO, conn)) {
				System.out.println("\nMédico já cadastrado!");
			} else {
				UsuarioVO usuarioVO = usuarioDAO.cadastrar(medicoVO, conn);
				medicoVO.setIdUsuario(usuarioVO.getIdUsuario());
				medicoVO = medicoDAO.cadastrar(medicoVO, conn);
			}
			userTransaction.commit();

		} catch (Exception erro) {
			BancoJTA.rollbackJTA();
		} finally {
			BancoJTA.closeConnectionJTA(conn);
		}

		return medicoVO;
	}

	// NOVO
	public boolean atualizar(MedicoVO medicoVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		MedicoDAO medicoDAO = new MedicoDAO();
		boolean sucesso = false;

		// Controle de transação JTA
		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA();

		try {
			userTransaction.begin();

			// 1. Atualiza as colunas na tabela 'usuario' (reutiliza o método do usuário)
			boolean usuarioAtualizado = usuarioDAO.atualizar(medicoVO, conn);

			// 2. Atualiza as colunas na tabela 'medico'
			boolean medicoAtualizado = medicoDAO.atualizar(medicoVO, conn);

			if (usuarioAtualizado && medicoAtualizado) {
				userTransaction.commit();
				sucesso = true;
				System.out.println("Médico atualizado com sucesso!");
			} else {
				// Se uma das atualizações falhar, faz rollback
				userTransaction.rollback();
				System.out.println("Erro ao atualizar o Médico. Rollback realizado.");
			}

		} catch (Exception erro) {
			// Garante o rollback em caso de erro
			BancoJTA.rollbackJTA();
			System.out.println("Erro geral na transação de atualização do Médico.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			BancoJTA.closeConnectionJTA(conn);
		}

		return sucesso;
	}

}
