package servlet;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "controllaOtpEmail", value = "/controllaOtpEmail")
public class ServletControllaOtpEmail extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.addHeader("Access-Control-Allow-Credentials", "true");

		String body = getBody(request);
		JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);

		JsonObject responseJson = new JsonObject();
		try {
			HttpSession session = request.getSession(false);
			System.out.println(session.getId());
			int otpServer = (int) session.getAttribute("email");
			int otpClient = jsBody.get("otp").getAsInt();

			if (otpServer == otpClient) {
				responseJson.addProperty("risultato", "sucesso!");
				responseJson.addProperty("contenuto", "otp corretto");
			} else {
				responseJson.addProperty("risultato", "boia errore!");
				responseJson.addProperty("contenuto", "otp errato");
				session.invalidate();
			}
		} catch (NullPointerException e) {
			responseJson.addProperty("risultato", "boia errore!");
			responseJson.addProperty("contenuto", "formato del body scorretto: " + e.toString());
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