package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import model.vo.FuncionarioVO;

public class FuncionarioDAO {

	public boolean atualizar(FuncionarioVO funcionarioVO, Connection conn) {
		// idfuncionario (PK) é o mesmo que idUsuario
		String query = "UPDATE funcionario SET matricula=?, cargo=? WHERE idfuncionario=?";

		PreparedStatement pstmt = Banco.getPreparedStatement(conn, query);
		boolean sucesso = false;

		try {
			pstmt.setString(1, funcionarioVO.getMatricula());
			pstmt.setString(2, funcionarioVO.getCargo());
			pstmt.setInt(3, funcionarioVO.getIdUsuario());

			int linhasAfetadas = pstmt.executeUpdate();
			sucesso = (linhasAfetadas > 0);

		} catch (Exception erro) {
			System.out.println("FuncionarioDAO - Erro ao executar a query do método de atualizar o Funcionário.");
			System.out.println("Erro: " + erro.getMessage());
		}

		return sucesso;
	}

}
