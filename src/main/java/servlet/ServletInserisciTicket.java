package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "inserisciTicket", value = "/inserisciTicket")
public class ServletInserisciTicket extends HttpServlet {

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
			String testo = jsBody.get("testo").getAsString();
			String materia = jsBody.get("materia").getAsString();

			// Inserisco il ticket
			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			skillBuildersDao.insertTicket(testo, materia, email_cliente);

			// Costruisco il risultato
			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "ticket inserito");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
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