package model.dto;

import java.time.LocalDate;

public class RequisicaoExamesDTO {
	
	private int id;
	private int numeroPedido;
	private String nome;
	private LocalDate data;
	
	public RequisicaoExamesDTO(int id, int numeroPedido, String nome, LocalDate data) {
		super();
		this.id = id;
		this.numeroPedido = numeroPedido;
		this.nome = nome;
		this.data = data;
	}

	public RequisicaoExamesDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumeroPedido() {
		return numeroPedido;
	}

	public void setNumeroPedido(int numeroPedido) {
		this.numeroPedido = numeroPedido;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

}
