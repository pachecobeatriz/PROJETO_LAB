package model.dto;

import model.enums.StatusExame;

public class ExameDTO {

	private int idExame; // Exame
	private int idPaciente; // Exame
	private String paciente;
	private int idMedico; // Exame
	private String medico;
	private int numeropedido; // Exame
	private int idTipoExame; // TipoE
	private String nomeExame; // TipoE
	private String dataExame; // Exame
	private String observacoes; // Exame
	private StatusExame status; // Exame
	private int idLaudo; // Laudo
	private String dataLaudo; // Laudo

	public ExameDTO(int idExame, int idPaciente, String paciente, int idMedico, String medico, int numeropedido,
			int idTipoExame, String nomeExame, String dataExame, String observacoes, StatusExame status, int idLaudo,
			String dataLaudo) {
		super();
		this.idExame = idExame;
		this.idPaciente = idPaciente;
		this.paciente = paciente;
		this.idMedico = idMedico;
		this.medico = medico;
		this.numeropedido = numeropedido;
		this.idTipoExame = idTipoExame;
		this.nomeExame = nomeExame;
		this.dataExame = dataExame;
		this.observacoes = observacoes;
		this.status = status;
		this.idLaudo = idLaudo;
		this.dataLaudo = dataLaudo;
	}

	public ExameDTO() {
		super();
	}

	public int getIdExame() {
		return idExame;
	}

	public void setIdExame(int idExame) {
		this.idExame = idExame;
	}

	public int getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(int idPaciente) {
		this.idPaciente = idPaciente;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public int getIdMedico() {
		return idMedico;
	}

	public void setIdMedico(int idMedico) {
		this.idMedico = idMedico;
	}

	public String getMedico() {
		return medico;
	}

	public void setMedico(String medico) {
		this.medico = medico;
	}

	public int getNumeropedido() {
		return numeropedido;
	}

	public void setNumeropedido(int numeropedido) {
		this.numeropedido = numeropedido;
	}

	public int getIdTipoExame() {
		return idTipoExame;
	}

	public void setIdTipoExame(int idTipoExame) {
		this.idTipoExame = idTipoExame;
	}

	public String getNomeExame() {
		return nomeExame;
	}

	public void setNomeExame(String nomeExame) {
		this.nomeExame = nomeExame;
	}

	public String getDataExame() {
		return dataExame;
	}

	public void setDataExame(String dataExame) {
		this.dataExame = dataExame;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public StatusExame getStatus() {
		return status;
	}

	public void setStatus(StatusExame status) {
		this.status = status;
	}

	public int getIdLaudo() {
		return idLaudo;
	}

	public void setIdLaudo(int idLaudo) {
		this.idLaudo = idLaudo;
	}

	public String getDataLaudo() {
		return dataLaudo;
	}

	public void setDataLaudo(String dataLaudo) {
		this.dataLaudo = dataLaudo;
	}

}
