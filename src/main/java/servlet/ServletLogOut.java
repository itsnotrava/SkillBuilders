package servlet;

import java.io.*;
import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dao.SkillBuildersDao;
import exceptions.EmailOPasswordErrati;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "LogOut", value = "/logout")
public class ServletLogOut extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        JsonObject responseJson = new JsonObject();
        try {
            HttpSession session = request.getSession(false);
            session.invalidate();
            responseJson.addProperty("risultato", "successo");
            responseJson.addProperty("contenuto", "logout avvenuto");
        } catch (NullPointerException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "sessione inesistente");
        }

        PrintWriter printWriter = response.getWriter();
        printWriter.println(responseJson.toString());
        printWriter.flush();
    }
}