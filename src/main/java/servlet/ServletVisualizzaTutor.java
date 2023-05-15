package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "VisualizzaTutor", value = "/visualizzaTutor")
public class ServletVisualizzaTutor extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        String body = getBody(request);
        Gson gson = new Gson();
        JsonObject jsBody = gson.fromJson(body, JsonObject.class);

        JsonObject responseJson = new JsonObject();
        try {
            // Prendi i dati dalla sessione
            HttpSession session = request.getSession();
            String email = (String) session.getAttribute("email");

            // Prendi i dati dal body
            int anno = jsBody.get("anno").getAsInt();
            String provincia = jsBody.get("provincia").getAsString();
            String indirizzo = jsBody.get("indirizzo").getAsString();

            // Costruisco la risposta
            SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
            ArrayList<String> tutors = skillBuildersDao.getTutors(anno, provincia, indirizzo);
            JsonArray emails = new JsonArray();
            for (String string : tutors) {
                emails.add(string);
            }
            responseJson.addProperty("risultato", "sucesso!");
            responseJson.add("emails", emails);
        } catch (NullPointerException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "formato del body scorretta");
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