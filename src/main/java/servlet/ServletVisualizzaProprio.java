package servlet;

import java.io.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "VisualizzaProprio", value = "/visualizzaProprio")
public class ServletVisualizzaProprio extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        String body = getBody(request);
        // CREDO UN JSON PER IL RISULTATO
        JsonObject temp = new Gson().fromJson(body, JsonObject.class);
        // fromJason => trasforma da stringa a Json, prende in input -stringa- -tipo destinazione-

        //String nome = temp.get("email").toString(); // TROVO IL NOME (email)
        //String password = temp.get("password").toString(); // TROVO IL NOME (email)
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("risultato", "sucesso!");
        JsonObject contenutoJson = new JsonObject();
        contenutoJson.addProperty("nome", "Francesco");
        contenutoJson.addProperty("email", "sorghi@gmail.com");
        contenutoJson.addProperty("emailRecupero", "si@gmail.com");
        contenutoJson.addProperty("anno", 3);
        contenutoJson.addProperty("indirizzo", "informatico");
        contenutoJson.addProperty("foto", "1110001100101001");
        contenutoJson.addProperty("quartiere", "Navile");

        responseJson.addProperty("contenuto", "contenutoJson");

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