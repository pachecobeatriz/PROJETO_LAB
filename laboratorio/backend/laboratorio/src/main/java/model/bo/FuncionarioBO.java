package model.bo;

import java.sql.Connection;

import javax.transaction.UserTransaction;

import model.dao.BancoJTA;
import model.dao.FuncionarioDAO;
import model.dao.UsuarioDAO;
import model.vo.FuncionarioVO;

public class FuncionarioBO {

	// PUT com VO
	public FuncionarioVO atualizar(FuncionarioVO funcionarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		FuncionarioVO funcionarioAtualizado = null;

		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA();

		try {
			userTransaction.begin();

			boolean usuarioAtualizado = usuarioDAO.atualizar(funcionarioVO, conn);
			boolean funcionarioAtualizadoDB = funcionarioDAO.atualizar(funcionarioVO, conn);

			if (usuarioAtualizado && funcionarioAtualizadoDB) {
				userTransaction.commit();
				funcionarioAtualizado = funcionarioVO;
				System.out.println("Funcionário atualizado com sucesso!");
			} else {
				userTransaction.rollback();
				System.out.println("Erro ao atualizar o Funcionário. Rollback realizado.");
			}

		} catch (Exception erro) {
			BancoJTA.rollbackJTA();
			System.out.println("Erro geral na transação de atualização do Funcionário.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			BancoJTA.closeConnectionJTA(conn);
		}

		return funcionarioAtualizado;
	}

//	// PUT com RESPONSE
//	public boolean atualizar(FuncionarioVO funcionarioVO) {
//		UsuarioDAO usuarioDAO = new UsuarioDAO();
//		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
//		boolean sucesso = false;
//
//		UserTransaction userTransaction = BancoJTA.getUserTransaction();
//		Connection conn = BancoJTA.getConnectionJTA();
//
//		try {
//			userTransaction.begin();
//
//			// Atualiza as colunas na tabela 'usuario'
//			boolean usuarioAtualizado = usuarioDAO.atualizar(funcionarioVO, conn);
//
//			// Atualiza as colunas na tabela 'funcionario'
//			boolean funcionarioAtualizado = funcionarioDAO.atualizar(funcionarioVO, conn);
//
//			if (usuarioAtualizado && funcionarioAtualizado) {
//				userTransaction.commit();
//				sucesso = true;
//				System.out.println("Funcionário atualizado com sucesso!");
//			} else {
//				userTransaction.rollback();
//				System.out.println("Erro ao atualizar o Funcionário. Rollback realizado.");
//			}
//
//		} catch (Exception erro) {
//			BancoJTA.rollbackJTA();
//			System.out.println("Erro geral na transação de atualização do Funcionário.");
//			System.out.println("Erro: " + erro.getMessage());
//		} finally {
//			BancoJTA.closeConnectionJTA(conn);
//		}
//
//		return sucesso;
//	}

}
