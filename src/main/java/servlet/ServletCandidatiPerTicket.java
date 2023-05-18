package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.UtenteNonTutor;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "candidatiPerTicket", value = "/candidatiPerTicket")
public class ServletCandidatiPerTicket extends HttpServlet {

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
			int id_ticket = jsBody.get("id_ticket").getAsInt();
			String testo = jsBody.get("testo").getAsString();

			// Inserisco il ticket
			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			skillBuildersDao.creaNotifica(email_cliente, id_ticket, testo);

			// Costruisco il risultato
			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "candidatura avvenuta");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		} catch (UtenteNonTutor e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "utente non tutor");
		} catch (SQLException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "Java Exception: " + e.toString());
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