package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.UtenteGiàEsistente;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "registrazione", value = "/registrazione")
public class ServletRegistrazione extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String body = getBody(request);
		JsonObject temp = new Gson().fromJson(body, JsonObject.class);

		JsonObject responseJson = new JsonObject();
		try {
			String nome = temp.get("nome").getAsString();
			String password = temp.get("password").getAsString();
			String email = temp.get("email").getAsString();
			String anno = temp.get("anno").getAsString();
			String indirizzo = temp.get("indirizzo").getAsString();
			String foto = temp.get("foto").getAsString();
			String comune = temp.get("comune").getAsString();
			boolean flagTutor = temp.get("flagTutor").getAsBoolean();

			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			skillBuildersDao.insertUtente(nome, password, email, anno, indirizzo, foto, comune, flagTutor);

			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "registrazione avvenuta");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		} catch (UtenteGiàEsistente e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "utente già esistente");
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