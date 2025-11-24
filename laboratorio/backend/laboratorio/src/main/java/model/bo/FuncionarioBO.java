package model.bo;

import java.sql.Connection;
import javax.transaction.UserTransaction;

import model.dao.BancoJTA;
import model.dao.FuncionarioDAO;
import model.dao.UsuarioDAO;
import model.vo.FuncionarioVO;

public class FuncionarioBO {

	public boolean atualizar(FuncionarioVO funcionarioVO) {
		// Reutiliza o DAO de Usuário para a parte da tabela 'usuario'
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		boolean sucesso = false;

		// Controle de transação JTA
		UserTransaction userTransaction = BancoJTA.getUserTransaction();
		Connection conn = BancoJTA.getConnectionJTA();

		try {
			userTransaction.begin();

			// 1. Atualiza as colunas na tabela 'usuario' (reutiliza o método do usuário)
			boolean usuarioAtualizado = usuarioDAO.atualizar(funcionarioVO, conn);

			// 2. Atualiza as colunas na tabela 'funcionario'
			boolean funcionarioAtualizado = funcionarioDAO.atualizar(funcionarioVO, conn);

			if (usuarioAtualizado && funcionarioAtualizado) {
				userTransaction.commit();
				sucesso = true;
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

		return sucesso;
	}

}
