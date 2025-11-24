package model.dto;

import java.time.LocalDate;

import model.enums.StatusExame;

public class ExameDTO {
	
	private int idExame;
	private int idPaciente;
	private String paciente; // nome do paciente | ñ tem no VO
	private int idMedico;
	private String medico; // nome do médico | ñ tem no VO
	private int numeropedido; // tem no VO, mas a escrita está diferente
	private int idTipoExame;
	private String nomeExame; // ñ tem no VO
	private LocalDate dataExame;
	private String observacoes;
	private StatusExame status;
	private int idLaudo; // ñ tem no VO
	private LocalDate dataLaudo; // ñ tem no VO
	
	public ExameDTO(int idExame, int idPaciente, String paciente, int idMedico, String medico, int numeropedido,
			int idTipoExame, String nomeExame, LocalDate dataExame, String observacoes, StatusExame status, int idLaudo,
			LocalDate dataLaudo) {
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

	public LocalDate getDataExame() {
		return dataExame;
	}

	public void setDataExame(LocalDate dataExame) {
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

	public LocalDate getDataLaudo() {
		return dataLaudo;
	}

	public void setDataLaudo(LocalDate dataLaudo) {
		this.dataLaudo = dataLaudo;
	}
	
}
