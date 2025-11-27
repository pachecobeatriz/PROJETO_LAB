package model.bo;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import model.dao.BancoJTA;
import model.dao.PacienteDAO;
import model.dao.UsuarioDAO;
import model.vo.PacienteVO;
import model.vo.UsuarioVO;

public class PacienteBO {
	
	// CADASTRAR...
	
	public PacienteVO cadastrar(PacienteVO pacienteVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		PacienteDAO pacienteDAO = new PacienteDAO();

		// Controle de transação!
		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA();

		try {
			userTransaction.begin();
			// A partir deste ponto, todas as operações de banco de dados na Connection só serão permanentes se o commit for chamado.

			if (usuarioDAO.verificarExistenciaPacientePorCpf(pacienteVO, conn)) { // Conferindo se o user já existe.
				System.out.println("\nPaciente já cadastrado!");
			} else {
				UsuarioVO usuarioVO = usuarioDAO.cadastrar(pacienteVO, conn);
				pacienteVO.setIdUsuario(usuarioVO.getIdUsuario());
				pacienteVO = pacienteDAO.cadastrar(pacienteVO, conn);
			}
			userTransaction.commit();
			// Se tudo acima correu sem erros, confirma as alterações no banco de dados (grava permanentemente).

		} catch (Exception erro) {
			BancoJTA.rollbackJTA();
			// Desfaz todas as alterações feitas desde o begin()
		} finally {
			BancoJTA.closeConnectionJTA(conn);
		}

		return pacienteVO;
	}

	// ATUALIZAR...
	
	// PUT com VO
		public PacienteVO atualizar(PacienteVO pacienteVO) {
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			PacienteDAO pacienteDAO = new PacienteDAO();
			PacienteVO pacienteAtualizado = null;

			UserTransaction userTransaction = BancoJTA.getUserTransaction();
			Connection conn = BancoJTA.getConnectionJTA();

			try {
				userTransaction.begin();

				boolean usuarioAtualizado = usuarioDAO.atualizar(pacienteVO, conn);
				boolean pacienteAtualizadoDB = pacienteDAO.atualizar(pacienteVO, conn);

				if (usuarioAtualizado && pacienteAtualizadoDB) {
					userTransaction.commit();
					pacienteAtualizado = pacienteVO;
					System.out.println("Paciente atualizado com sucesso!");
				} else {
					userTransaction.rollback();
					System.out.println("Erro ao atualizar o Paciente. Rollback realizado.");
				}

			} catch (Exception erro) {
				BancoJTA.rollbackJTA();
				System.out.println("Erro geral na transação de atualização do Paciente.");
				System.out.println("Erro: " + erro.getMessage());
			} finally {
				BancoJTA.closeConnectionJTA(conn);
			}

			return pacienteAtualizado;
		}
		
		// LISTAR...
		
		public PacienteVO buscarPorId(int idUsuario) {
			PacienteDAO pacienteDAO = new PacienteDAO();
			return pacienteDAO.buscarPorId(idUsuario);
		}

//		// PUT com RESPONSE
//		public boolean atualizar(PacienteVO pacienteVO) {
//			UsuarioDAO usuarioDAO = new UsuarioDAO();
//			PacienteDAO pacienteDAO = new PacienteDAO();
//			boolean sucesso = false;
//
//			UserTransaction userTransaction = BancoJTA.getUserTransaction();
//			Connection conn = BancoJTA.getConnectionJTA();
//
//			try {
//				userTransaction.begin();
//
//				// Atualiza as colunas na tabela 'usuario'
//				boolean usuarioAtualizado = usuarioDAO.atualizar(pacienteVO, conn);
//
//				// Atualiza as colunas na tabela 'paciente'
//				boolean pacienteAtualizado = pacienteDAO.atualizar(pacienteVO, conn);
//
//				if (usuarioAtualizado && pacienteAtualizado) {
//					userTransaction.commit();
//					sucesso = true;
//					System.out.println("Paciente atualizado com sucesso!");
//				} else {
//					userTransaction.rollback();
//					System.out.println("Erro ao atualizar o Paciente. Rollback realizado.");
//				}
//
//			} catch (Exception erro) {
//				BancoJTA.rollbackJTA();
//				System.out.println("Erro geral na transação de atualização do Paciente.");
//				System.out.println("Erro: " + erro.getMessage());
//			} finally {
//				BancoJTA.closeConnectionJTA(conn);
//			}
//
//			return sucesso;
//		}

}
