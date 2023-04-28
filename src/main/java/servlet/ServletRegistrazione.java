package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
//import dao.UtenzeDao;
//import exception.EmailAlreadyTakenException;
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

		String nome = temp.get("nome").toString(); // TROVO IL NOME
		String password = temp.get("password").toString(); // TROVO LA PASSWORD
		String email = temp.get("email").toString(); // TROVO LA MAIL
		String anno = temp.get("anno").toString(); // TROVO IL NOME
		String indirizzo = temp.get("indirizzo").toString(); // TROVO L'INDIRIZZO
		String sezione = temp.get("sezione").toString(); // TROVO LA SEZIONE
		String quartiere = temp.get("quartiere").toString(); // TROVO LA POSIZIONE


		JsonObject responseJson = new JsonObject();
		responseJson.addProperty("risultato", "sucesso!");


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