package model.vo;

import model.enums.Perfil;

public class MedicoVO extends UsuarioVO {
	
	private String crm;
	private String especialidade;
	
	public MedicoVO(int idUsuario, String nome, String cpf, String email, String login, String senha, Perfil perfil,
			String crm, String especialidade) {
		super(idUsuario, nome, cpf, email, login, senha, perfil);
		this.crm = crm;
		this.especialidade = especialidade;
	}

	public MedicoVO() {
		super();
	}

	public String getCrm() {
		return crm;
	}

	public void setCrm(String crm) {
		this.crm = crm;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}
	
}
