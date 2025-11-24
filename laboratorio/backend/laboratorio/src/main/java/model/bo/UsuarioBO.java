package model.bo;

import model.dao.UsuarioDAO;
import model.vo.UsuarioVO;

public class UsuarioBO {

	public UsuarioVO realizarLogin(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.realizarLogin(usuarioVO);  // esses "returns" estão sempre devolvendo pra quem tá chamando o método.
	}

}
