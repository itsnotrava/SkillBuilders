package servlet;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "CambiaFlag", value = "/cambiaFlag")
public class ServletCambiaFlag extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Credentials", "true");

        String body = getBody(request);
        JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);

        JsonObject responseJson = new JsonObject();
        try {
            HttpSession session = request.getSession();

            String email = (String) session.getAttribute("email");
            boolean flagTutor = jsBody.get("flagTutor").getAsBoolean();
            // TODO
            responseJson.addProperty("risultato", "sucesso!");
            JsonObject contenutoJson = new JsonObject();
            contenutoJson.addProperty("email", "sorghi@gmail.com");
            contenutoJson.addProperty("flagTutor", true);
            responseJson.add("contenuto", contenutoJson);
        } catch (NullPointerException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "formato del body scorretto");
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