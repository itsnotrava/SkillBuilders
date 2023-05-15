package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.EmailOPasswordErrati;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "accesso", value = "/accesso")
public class ServletAccesso extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String body = getBody(request);
		JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);

		JsonObject responseJson = new JsonObject();
		try {
			String email = jsBody.get("email").getAsString();
			String password = jsBody.get("password").getAsString();

			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			skillBuildersDao.checkUtenteConPassword(email, password);

			HttpSession session = request.getSession(true);
			session.setAttribute("email", email);

			responseJson.addProperty("risultato", "sucesso!");
			responseJson.addProperty("contenuto", "accesso avvenuto");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		} catch (EmailOPasswordErrati e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "email o password errati");
		} catch (SQLException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "Java Exception");
		}

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