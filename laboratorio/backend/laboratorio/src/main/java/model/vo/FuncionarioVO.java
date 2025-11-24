package model.vo;

import model.enums.Perfil;

public class FuncionarioVO extends UsuarioVO {
	
	private String matricula;
	private String cargo;
	
	public FuncionarioVO(int idUsuario, String nome, String cpf, String email, String login, String senha,
			Perfil perfil, String matricula, String cargo) {
		super(idUsuario, nome, cpf, email, login, senha, perfil);
		this.matricula = matricula;
		this.cargo = cargo;
	}

	public FuncionarioVO() {
		super();
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

}
