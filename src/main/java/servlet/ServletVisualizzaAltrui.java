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

@WebServlet(name = "VisualizzaAltrui", value = "/visualizzaAltrui")
public class ServletVisualizzaAltrui extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Credentials", "true");

        String body = getBody(request);
        // CREDO UN JSON PER IL RISULTATO
        JsonObject jsBody = new Gson().fromJson(body, JsonObject.class);
        // fromJason => trasforma da stringa a Json, prende in input -stringa- -tipo destinazione-

        JsonObject responseJson = new JsonObject();
        JsonObject contenuto = new JsonObject();
        try {
            //HttpSession session = request.getSession();
            //String email = (String) session.getAttribute("email");

            // Prendo i dati dal body
            String email = jsBody.get("email").getAsString();
            SkillBuildersDao skillBuildersDao = new SkillBuildersDao();
            // CONTROLLO CHE L'UTENTE ESISTE
            skillBuildersDao.checkUtenteEsistente(email);
            // PRENDO DATI DAL DB
            Utente utente = skillBuildersDao.getUtente(email);
            // COSTRUISCO IL RISULTATO
            contenuto.addProperty("nome", utente.nome);
            contenuto.addProperty("email", utente.email);
            contenuto.addProperty("anno", utente.anno);
            contenuto.addProperty("indirizzo", utente.indirizzo);
            contenuto.addProperty("foto", utente.nome_foto);
            contenuto.addProperty("comune", utente.comune);

            responseJson.addProperty("risultato", "sucesso!");
            responseJson.add("contenuto", contenuto);

            // COSTRUISCO RISULTATO
            /*
            JsonArray arrayUtenti = new JsonArray();
            for(int i=0; i<10; i++){ //
                JsonObject altrui = new JsonObject();
                altrui.addProperty("nome", "Francesco");
                altrui.addProperty("email", "sorghi@gmail.com");
                altrui.addProperty("anno", 3);
                altrui.addProperty("indirizzo", "informatico");
                altrui.addProperty("foto", "1110001100101001");
                altrui.addProperty("quartiere", "Navile");

                arrayUtenti.add(altrui);
            }
            responseJson.addProperty("risultato", "sucesso!");
            responseJson.add("contenuto", arrayUtenti);
            */

        } catch (NullPointerException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "formato del body scorretto");
        } catch (SQLException e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "java exception");
        } catch (UtenteNonEsistente e) {
            responseJson.addProperty("risultato", "boia errore!");
            responseJson.addProperty("contenuto", "email non trovata");
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