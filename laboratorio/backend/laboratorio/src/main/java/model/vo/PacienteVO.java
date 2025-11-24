package model.vo;

import java.time.LocalDate;

import model.enums.Perfil;

public class PacienteVO extends UsuarioVO {

	private LocalDate dataNascimento;

	public PacienteVO(int idUsuario, String nome, String cpf, String email, String login, String senha, Perfil perfil,
			LocalDate dataNascimento) {
		super(idUsuario, nome, cpf, email, login, senha, perfil);
		this.dataNascimento = dataNascimento;
	}

	public PacienteVO() {
		super();
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

}
