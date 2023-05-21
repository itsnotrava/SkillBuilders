package model;

public class Utente {
	public String email;
	public String nome;
	public String password;
	public int anno;
	public String indirizzo;
	public String nome_foto;
	public String comune;
	public boolean flag_tutor;
	public String biografia;

	public Utente(String email, String nome, String password, int anno, String indirizzo, String nome_foto, String comune, boolean flag_tutor, String biografia) {
		this.email = email;
		this.nome = nome;
		this.password = password;
		this.anno = anno;
		this.indirizzo = indirizzo;
		this.nome_foto = nome_foto;
		this.comune = comune;
		this.flag_tutor = flag_tutor;
		this.biografia = biografia;
	}
}
