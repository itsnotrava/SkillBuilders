package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.UtenteGiàEsistente;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@WebServlet(name = "registrazione", value = "/registrazione")
public class ServletRegistrazione extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");

		String body = getBody(request);
		JsonObject temp = new Gson().fromJson(body, JsonObject.class);


		JsonObject responseJson = new JsonObject();
		// PRENDO I PARAMETRI DAL BODY
		try {
			String nome = temp.get("nome").getAsString();
			String password = temp.get("password").getAsString();
			String email = temp.get("email").getAsString();
			int anno = temp.get("anno").getAsInt();
			String indirizzo = temp.get("indirizzo").getAsString();
			String foto = temp.get("foto").getAsString();
			String comune = temp.get("comune").getAsString();
			boolean flagTutor = temp.get("flagTutor").getAsBoolean();

			// OTTENGO HAS DELLA PASSWORD (BYcript)
			//password = model.Hash.creoHash(password); NON FUNZIONA
			password = BCrypt.hashpw(password, BCrypt.gensalt(12));

			// INSERISCO NEL DB
			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			skillBuildersDao.insertUtente(nome, password, email, anno, indirizzo, foto, comune, flagTutor);

			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "registrazione avvenuta");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		} catch (SQLException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "Java Exception");
		}

		String header = response.getHeader("Set-Cookie");
		response.setHeader("Set-Cookie", header+"; SameSite=None");

		PrintWriter printWriter = response.getWriter();
		printWriter.println(responseJson.toString());
		printWriter.flush();
	}

	// PER PRENDERE IL BODY
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