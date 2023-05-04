package servlet;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "registrazione", value = "/registrazione")
public class ServletRegistrazione extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String body = getBody(request);
		// CREDO UN JSON PER IL RISULTATO
		JsonObject temp = new Gson().fromJson(body, JsonObject.class);
		// fromJason => trasforma da stringa a Json, prende in input -stringa- -tipo destinazione-

		JsonObject responseJson = new JsonObject();
		try {
			String nome = temp.get("nome").getAsString(); // TROVO IL NOME
			String password = temp.get("password").getAsString(); // TROVO LA PASSWORD
			String email = temp.get("email").getAsString(); // TROVO LA MAIL
			String anno = temp.get("anno").getAsString(); // TROVO IL NOME
			String indirizzo = temp.get("indirizzo").getAsString(); // TROVO L'INDIRIZZO
			String sezione = temp.get("sezione").getAsString(); // TROVO LA SEZIONE
			String quartiere = temp.get("quartiere").getAsString(); // TROVO LA POSIZIONE
			boolean flagTutor = temp.get("flagTutor").getAsBoolean();
			// TODO
			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "registrazione avvenuta");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		}

		// Invio il risultato al client
		PrintWriter printWriter = response.getWriter();
		printWriter.println(responseJson.toString());
		printWriter.flush();
	}

	private static String getBody(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
}