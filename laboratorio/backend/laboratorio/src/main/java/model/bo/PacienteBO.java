package model.bo;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import model.dao.BancoJTA;
import model.dao.PacienteDAO;
import model.dao.UsuarioDAO;
import model.vo.PacienteVO;
import model.vo.UsuarioVO;

public class PacienteBO {

	public PacienteVO cadastrar(PacienteVO pacienteVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		PacienteDAO pacienteDAO = new PacienteDAO();

		// Controle de transação!
		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA(); // Geralmente faz no DAO, estamos fazendo aqui pq é um controle
														// de transação.

		try {
			userTransaction.begin();

			if (usuarioDAO.verificarExistenciaUsuarioPorCpf(pacienteVO, conn)) { // Conferindo se o user já existe.
				System.out.println("\nPaciente já cadastrado!");
			} else {
				UsuarioVO usuarioVO = usuarioDAO.cadastrar(pacienteVO, conn);
				pacienteVO.setIdUsuario(usuarioVO.getIdUsuario());
				pacienteVO = pacienteDAO.cadastrar(pacienteVO, conn);
			}
			userTransaction.commit();

		} catch (Exception erro) {
			BancoJTA.rollbackJTA();
		} finally {
			BancoJTA.closeConnectionJTA(conn);
		}

		return pacienteVO;
	}

	// NOVO
	public boolean atualizar(PacienteVO pacienteVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		PacienteDAO pacienteDAO = new PacienteDAO();
		boolean sucesso = false;

		// Controle de transação!
		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA();

		try {
			userTransaction.begin();

			// 1. Atualiza as colunas na tabela 'usuario'
			boolean usuarioAtualizado = usuarioDAO.atualizar(pacienteVO, conn);

			// 2. Atualiza as colunas na tabela 'paciente'
			boolean pacienteAtualizado = pacienteDAO.atualizar(pacienteVO, conn);

			if (usuarioAtualizado && pacienteAtualizado) {
				userTransaction.commit();
				sucesso = true;
				System.out.println("Paciente atualizado com sucesso!");
			} else {
				// Se uma das atualizações falhar, faz rollback
				userTransaction.rollback();
				System.out.println("Erro ao atualizar o Paciente. Rollback realizado.");
			}

		} catch (Exception erro) {
			// Se houver qualquer exceção, garante o rollback
			BancoJTA.rollbackJTA();
			System.out.println("Erro geral na transação de atualização do Paciente.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			BancoJTA.closeConnectionJTA(conn);
		}

		return sucesso;
	}

}
