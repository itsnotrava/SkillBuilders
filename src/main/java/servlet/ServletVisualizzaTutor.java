package servlet;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "VisualizzaTutor", value = "/visualizzaTutor")
public class ServletVisualizzaTutor extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");

        String body = getBody(request);
        // CREDO UN JSON PER IL RISULTATO
        JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);
        // fromJason => trasforma da stringa a Json, prende in input -stringa- -tipo destinazione-

        JsonObject responseJson = new JsonObject();
        try {
            int anno = jsBody.get("anno").getAsInt();
            String provincia = jsBody.get("provincia").getAsString();
            String indirizzo = jsBody.get("indirizzo").getAsString();
            // TODO
            responseJson.addProperty("risultato", "sucesso!");
            // CREO L'ARRAY CON LE EMAIL DEI TUTOR
            JsonArray vet = new JsonArray();
            vet.add("sorghi@gmail.com");
            vet.add("giorgio@gmail.com");
            // AGGIUNGO CONTENUTO A RISPOSTA.CONTENUTO
            responseJson.add("emails", vet);
        } catch (NullPointerException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "formato del body scorretta");
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