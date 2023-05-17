package servlet;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import model.Ticket;

@WebServlet(name = "VisualizzaTickets", value = "/visualizzaTickets")
public class ServletVisualizzaTickets extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Credentials", "true");

        String body = getBody(request);
        // CREDO UN JSON PER IL RISULTATO
        JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);
        // fromJason => trasforma da stringa a Json, prende in input -stringa- -tipo destinazione-

        JsonObject responseJson = new JsonObject();
        try {
            // Prendo i dati dalla sessione
            HttpSession session = request.getSession(false);
            String email = (String) session.getAttribute("email");

            // Prendo i dati dal body
            int anno = jsBody.get("anno").getAsInt();
            String comune = jsBody.get("comune").getAsString();
            String materia = jsBody.get("materia").getAsString();

            // Costruisco il risultato
            SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
            ArrayList<Ticket> tickets = skillBuildersDao.getTickets(anno, comune, materia);
            JsonArray jsTickets = new JsonArray();
            for (Ticket ticket : tickets) {
                JsonObject jsTicket = new JsonObject();
                jsTicket.addProperty("testo", ticket.testo);
                jsTicket.addProperty("materia", ticket.materia);
                jsTicket.addProperty("email_cliente", ticket.utente.email);
                jsTicket.addProperty("username", ticket.utente.nome);
                jsTickets.add(jsTicket);
            }
            responseJson.addProperty("risultato", "sucesso!");
            responseJson.add("contenuto", jsTickets);

        } catch (NullPointerException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "formato del body scorretto");
        } catch (SQLException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "Java Exception");
        }

        // Invio il risultato al client
        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseJson.toString());
        printWriter.flush();
    }

    // PRESA DA INTERNET, SI OCCUPA DI FARE IL BODY DELLA RICHIESTA
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