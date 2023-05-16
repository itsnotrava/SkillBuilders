package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.UtenteNonEsistente;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Utente;

@WebServlet(name = "VisualizzaProprio", value = "/visualizzaProprio")
public class ServletVisualizzaProprio extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        String body = getBody(request);
        Gson gson = new Gson();
        JsonObject jsBody = gson.fromJson(body, JsonObject.class);

        JsonObject responseJson = new JsonObject();
        try {
            // Prendi i dati dalla sessione
            HttpSession session = request.getSession(false);
            String email = (String) session.getAttribute("email");

            // Costruisco la risposta
            SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
            Utente utente = skillBuildersDao.getUtente(email);
            JsonObject jsUtente = gson.fromJson(gson.toJson(utente), JsonObject.class);
            responseJson.addProperty("risultato", "sucesso!");
            responseJson.add("contenuto", jsUtente);

        } catch (NullPointerException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "formato del body scorretto");
        } catch (SQLException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "Java Exception");
        } catch (UtenteNonEsistente e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "utente non esistente");
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