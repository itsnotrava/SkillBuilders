package model;

public class Utente {
	private String email;
	private String nome;
	private String password;
	private int anno;
	private String indirizzo;
	private String nome_foto;
	private String comune;
	private boolean flag_tutor;

	public Utente(String email, String nome, String password, int anno, String indirizzo, String nome_foto, String comune, boolean flag_tutor) {
		this.email = email;
		this.nome = nome;
		this.password = password;
		this.anno = anno;
		this.indirizzo = indirizzo;
		this.nome_foto = nome_foto;
		this.comune = comune;
		this.flag_tutor = flag_tutor;
	}
}
