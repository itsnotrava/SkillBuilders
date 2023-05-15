package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.UtenteGiàEsistente;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.EmailSender;
import model.OTPGenerator;

@WebServlet(name = "verificaEmail", value = "/verificaEmail")
public class ServletVerificaEmail extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");

		String body = getBody(request);
		JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);

		JsonObject responseJson = new JsonObject();
		try {
			String email = jsBody.get("email").getAsString();

			// Verifico che l'utente non esisti già
			SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
			skillBuildersDao.checkUtenteNonEsistente(email);

			// Genera OTP
			int otp = OTPGenerator.generateOTP();

			// Invia OTP per mail
			EmailSender sender = new EmailSender(email, otp);
			sender.send();

			HttpSession session = request.getSession(true);
			session.setAttribute("email", email);
			session.setAttribute("otp", otp);

			responseJson.addProperty("risultato", "successo");
			responseJson.addProperty("contenuto", "email inviata");
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto");
		} catch (UtenteGiàEsistente e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "utente già esistente");
		} catch (MessagingException | SQLException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "Java Exception: " + e.toString());
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