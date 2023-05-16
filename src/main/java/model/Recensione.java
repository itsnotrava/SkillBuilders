package model;

public class Recensione {
	public int id;
	public int voto;
	public String descrizione;
	public String materia;
	public String email_tutor;
	public String email_cliente;

	public Recensione(int id, int voto, String descrizione, String materia, String email_tutor, String email_cliente) {
		this.id = id;
		this.voto = voto;
		this.descrizione = descrizione;
		this.materia = materia;
		this.email_tutor = email_tutor;
		this.email_cliente = email_cliente;
	}
}
