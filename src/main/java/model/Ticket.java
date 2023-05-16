package model;

public class Ticket {
	public int id;
	public String testo;
	public String materia;
	public Utente utente;

	public Ticket(int id, String testo, String materia, Utente utente) {
		this.id = id;
		this.testo = testo;
		this.materia = materia;
		this.utente = utente;
	}
}
