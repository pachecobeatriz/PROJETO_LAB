package model.vo;

import java.time.LocalDate;

import model.enums.StatusExame;

public class ExameVO {

	private int idExame;
	private int idPaciente;
	private int idMedico;
	private int idTipoExame;
	private int numeroPedido; // número da requisição
	private LocalDate dataExame;
	private String observacoes;
	private StatusExame status;
	
	public ExameVO(int idExame, int idPaciente, int idMedico, int idTipoExame, int numeroPedido, LocalDate dataExame,
			String observacoes, StatusExame status) {
		super();
		this.idExame = idExame;
		this.idPaciente = idPaciente;
		this.idMedico = idMedico;
		this.idTipoExame = idTipoExame;
		this.numeroPedido = numeroPedido;
		this.dataExame = dataExame;
		this.observacoes = observacoes;
		this.status = status;
	}

	public ExameVO() {
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

	public int getIdMedico() {
		return idMedico;
	}

	public void setIdMedico(int idMedico) {
		this.idMedico = idMedico;
	}

	public int getIdTipoExame() {
		return idTipoExame;
	}

	public void setIdTipoExame(int idTipoExame) {
		this.idTipoExame = idTipoExame;
	}

	public int getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
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
	
}
