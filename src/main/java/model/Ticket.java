package model;

public class Ticket {
	private int id;
	private String testo;
	private String materia;
	private String email_cliente;

	public Ticket(int id, String testo, String materia, String email_cliente) {
		this.id = id;
		this.testo = testo;
		this.materia = materia;
		this.email_cliente = email_cliente;
	}
}
