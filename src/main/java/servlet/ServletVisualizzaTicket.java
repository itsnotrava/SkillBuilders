package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.TicketNonEsistente;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Ticket;

@WebServlet(name = "VisualizzaTicket", value = "/visualizzaTicket")
public class ServletVisualizzaTicket extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");

		String body = getBody(request);
		Gson gson = new Gson();
		JsonObject jsBody = gson.fromJson(body, JsonObject.class);

		JsonObject responseJson = new JsonObject();
		try {
			// Prendo i dati dalla sessione
			HttpSession session = request.getSession(false);
			String email = (String) session.getAttribute("email");

			// Prendo i dati dal body
			int id = jsBody.get("id").getAsInt();

			// Ricavo i dati dal DB
			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			Ticket ticket = skillBuildersDao.getTicket(id);

			// Costruisco la risposta
			responseJson.addProperty("risultato", "sucesso!");
			JsonObject jsTicket = new JsonObject();
			jsTicket.addProperty("testo", ticket.testo);
			jsTicket.addProperty("materia", ticket.materia);
			jsTicket.addProperty("email_cliente", ticket.utente.email);
			jsTicket.addProperty("username", ticket.utente.nome);
			responseJson.add("contenuto", jsTicket);

		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		} catch (TicketNonEsistente e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "ticket non esistente");
		} catch (SQLException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "Java Exception");
		}

		// Invio il risultato al client
		PrintWriter printWriter = response.getWriter();
		printWriter.println(responseJson.toString());
		printWriter.flush();
	}

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