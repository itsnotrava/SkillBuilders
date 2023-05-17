package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.UtenteNonEsistente;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "modificaRecensione", value = "/modificaRecensione")
public class ServletModificaRecensione extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");

		String body = getBody(request);
		JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);

		JsonObject responseJson = new JsonObject();
		try {
			// Prendo i dati dalla sessione
			HttpSession session = request.getSession(false);
			String email_cliente = (String) session.getAttribute("email");

			// Prendo i dati dal body
			int id = jsBody.get("id").getAsInt();
			int voto = jsBody.get("voto").getAsInt();
			String descrizione = jsBody.get("descrizione").getAsString();
			String materia = jsBody.get("materia").getAsString();
			String email_tutor = jsBody.get("email_tutor").getAsString();

			// Inserisco il ticket
			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			skillBuildersDao.checkUtenteEsistente(email_tutor);
			skillBuildersDao.updateRecensione(id, voto, descrizione, materia, email_tutor, email_cliente);

			// Costruisco il risultato
			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "recensione aggiornata");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		} catch (UtenteNonEsistente e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "email tutor non trovata");
		} catch (SQLException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "Java Exception");
		}

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