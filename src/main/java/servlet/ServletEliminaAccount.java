package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.JsonObject;
import dao.SkillBuildersDao;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "eliminazione", value = "/eliminazione")
public class ServletEliminaAccount extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Credentials", "true");

        JsonObject responseJson = new JsonObject();
        try {
            // CREO LA SESSIONE
            HttpSession session = request.getSession();
            // RECUPER L'EMAIL DALLA SESSIONE
            String email = (String) session.getAttribute("email");

            // CREO ISTANZA DEL DAO E RICHIAMO LA FUNZIONE PER ELIMINARE
            SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
            skillBuildersDao.eliminaUtente(email);

            responseJson.addProperty("risultato", "sucesso!");
            responseJson.addProperty("contenuto", "Utente eliminato");

        } catch (SQLException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "Java Exception");
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseJson.toString());
        printWriter.flush();
    }

}