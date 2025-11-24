package model.vo;

public class TipoExameVO {
	
	private int idTipoExame;
	private String nome;
	
	public TipoExameVO(int idTipoExame, String nome) {
		super();
		this.idTipoExame = idTipoExame;
		this.nome = nome;
	}

	public TipoExameVO() {
		super();
	}

	public int getIdTipoExame() {
		return idTipoExame;
	}

	public void setIdTipoExame(int idTipoExame) {
		this.idTipoExame = idTipoExame;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
