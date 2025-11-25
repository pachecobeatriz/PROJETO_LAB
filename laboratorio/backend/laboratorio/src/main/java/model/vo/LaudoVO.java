package model.vo;

import java.time.LocalDate;

public class LaudoVO {

	private int idLaudo;
	private int idExame;
	private byte[] arquivo;
	private LocalDate dataLaudo;

	public LaudoVO(int idLaudo, int idExame, byte[] arquivo, LocalDate dataLaudo) {
		super();
		this.idLaudo = idLaudo;
		this.idExame = idExame;
		this.arquivo = arquivo;
		this.dataLaudo = dataLaudo;
	}

	public LaudoVO() {
		super();
	}

	public int getIdLaudo() {
		return idLaudo;
	}

	public void setIdLaudo(int idLaudo) {
		this.idLaudo = idLaudo;
	}

	public int getIdExame() {
		return idExame;
	}

	public void setIdExame(int idExame) {
		this.idExame = idExame;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public LocalDate getDataLaudo() {
		return dataLaudo;
	}

	public void setDataLaudo(LocalDate dataLaudo) {
		this.dataLaudo = dataLaudo;
	}

}
