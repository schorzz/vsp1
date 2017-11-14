import com.google.gson.Gson;
import dto.BroadcastInfo;
import dto.LoginSuccessDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;


public class User {


    private String username;
    private String password;
    private BroadcastInfo broadcastInfo;

    private String userPath = "/users";

    private String loginPath = "/login";
    private String authorisationToken=null;

    private boolean loggedIn=false;


    public User(BroadcastInfo broadcastInfo) {
        this.username = username;
        this.password = password;
        this.broadcastInfo = broadcastInfo;
    }

    public boolean createUser(String username, String password) {

        try {

            //es wid versucht eine verbindung herzustellen und ein user anzulegen

            URL url = new URL("" + broadcastInfo.getURL() + userPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String json = "{\"name\":\"" + username + "\",\"password\":\"" + password + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
                //throw new RuntimeException("Failed : HTTP error code : "
                //      + conn.getResponseCode());
                return false;
            }


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username = username;
        this.password = password;

        return true;

    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean login(){
        try {

            loggedIn=false;
            //es wid versucht eine verbindung herzustellen und ein user anzulegen

            URL url = new URL("" + broadcastInfo.getURL() + loginPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", generateAuthentication());


            if (conn.getResponseCode() != 200) {
                //throw new RuntimeException("Failed : HTTP error code : "
                //      + conn.getResponseCode());
                //System.out.println("Fehlercode: "+conn.getResponseCode()+"\nFehlertext: "+conn.getResponseMessage());
                Log.logDebug("User:login() ->"+"Fehlercode: "+conn.getResponseCode()+"\nFehlertext: "+conn.getResponseMessage());

                return false;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String obj="";
            String output="";

            while ((output = br.readLine()) != null){
                obj+=output;
            }
            conn.disconnect();

            Log.logDebug("User:login() ->"+"erhaltenes JSON ist:\n"+obj);

            Gson json = new Gson();

            LoginSuccessDTO ladto = json.fromJson(obj, LoginSuccessDTO.class);


            if(ladto==null){
                Log.logDebug("User:login() ->"+"aus JSON geparstes Objekt ist null");
                return false;
            }

            authorisationToken=ladto.getToken();
            //System.out.println("geparstes objet:\n"+ladto);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        loggedIn=true;
        return true;
    }

    public boolean login(String username, String password){

        this.username=username;
        this.password=password;
        return login();
    }

    private String generateAuthentication(){
        String authorization ="";

        if (username != null && password != null) {
            authorization = username + ":" + password;
        }

        if (authorization != null) {


            authorization = "Basic " + new String(Base64.getEncoder().encode(authorization.getBytes()));
            return authorization;
        }
        return null;
    }

    public String getAuthorisationToken() {
        return "Token "+authorisationToken;
    }
    /*
    public boolean whoAmI(){
        if (!loggedIn){
            return false;
        }
    }
    */
}