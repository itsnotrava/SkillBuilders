package servlet;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "accesso", value = "/accesso")
public class ServletAccesso extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String body = getBody(request);
		// CREDO UN JSON PER IL RISULTATO
		JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);
		// fromJason => trasforma da stringa a Json, prende in input -stringa- -tipo destinazione-

		JsonObject responseJson = new JsonObject();
		try {
			String nome = jsBody.get("email").toString(); // TROVO IL NOME (email)
			String password = jsBody.get("password").toString(); // TROVO IL NOME (email)
			// TODO
			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "accesso avvenuto");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		}

		// Invio il risultato al client
		PrintWriter printWriter = response.getWriter();
		printWriter.println(responseJson.toString());
		printWriter.flush();
	}

	// PRESA DA INTERNET, SI OCCUPA DI FARE IL BODY DELLA RICHIESTAA
	public static String getBody(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

}