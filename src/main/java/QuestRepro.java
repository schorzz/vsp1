import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dto.BroadcastInfo;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class QuestRepro {


    private List<Quest> questListe;
    private BroadcastInfo bci;
    private String questPath="/blackboard/quests";


    public QuestRepro(BroadcastInfo bci) {
        this.bci = bci;
        questListe=new ArrayList<>();
    }

    public void getQuests(){
        Log.logDebug("QuestRepro:getQuests--> enter");

    try {

        URL url = new URL("" + bci.getURL() + questPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("GET");


        if (conn.getResponseCode() != 200) {
            //throw new RuntimeException("Failed : HTTP error code : "
            //      + conn.getResponseCode());
            //System.out.println("Fehlercode: "+conn.getResponseCode()+"\nFehlertext: "+conn.getResponseMessage());
            Log.logDebug("QuestRepro:getQuests ->" + "Fehlercode: " + conn.getResponseCode() + "\nFehlertext: " + conn.getResponseMessage());


        }
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String obj="";
        String output="";

        while ((output = br.readLine()) != null){
            obj+=output;
        }


        JSONObject jasonObj = new JSONObject(obj);
        //jasonObj.get("objects").toString()

        try {
            Log.logDebug("QuestRepro:getQuests-->parsen des Jsonobjektes");
            //JSONArray jsonarray = new JSONArray(obj);
            JSONArray jsonarray = new JSONArray(jasonObj.get("objects").toString());
            Log.logDebug("QuestRepro:getQuests-->jsonarray laenge:"+jsonarray.length());
            Log.logDebug("QuestRepro:getQuests-->jsonarray:"+jsonarray);

            Log.log("quests");
            for (int i = 0; i < jsonarray.length(); i++) {

                Quest tmp = new Quest();
                Log.log(Log.LINE);
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Log.logDebug("QuestRepro:getQuests-->jsonarrayElement länge:"+jsonobject.length());
                Log.logDebug("QuestRepro:getQuests-->jsonarrayElement:"+jsonobject);
                Log.log("name: "+jsonobject.getString("name"));
                tmp.setName(jsonobject.getString("name"));
                Log.log("id: "+jsonobject.get("id"));
                tmp.setId(new Integer(jsonobject.get("id").toString()));

                //Log.log("reward: "+jsonobject.getString("reward").toString());
                tmp.setReward(new Integer(jsonobject.get("reward").toString()));
                Log.log("beschreibung: "+jsonobject.getString("description"));
                tmp.setBeschreibung(jsonobject.getString("description"));


                List<Task> tasklist = getTasks(jsonobject.getInt("id"));
                tmp.setTaskliste(tasklist);
                questListe.add(tmp);
                Log.log(Log.LINE);
            }
        } catch (JSONException e) {
            Log.logDebug("QuestRepro:getQuests-->parsen des Jsonobjektes fehlgeschlagen.");
            Log.logDebug("QuestRepro:getQuests-->"+e.toString());
            e.printStackTrace();
        }


    } catch (ProtocolException e) {
        e.printStackTrace();
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }


    public List<Task> getTasks(Integer id){

        Log.logDebug("QuestRepro:getTasks--> enter");
        ArrayList<Task> taskliste = new ArrayList<>();

        try {

            URL url = new URL("" + bci.getURL() + questPath+"/"+id+"/tasks");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");


            if (conn.getResponseCode() != 200) {
                //throw new RuntimeException("Failed : HTTP error code : "
                //      + conn.getResponseCode());
                //System.out.println("Fehlercode: "+conn.getResponseCode()+"\nFehlertext: "+conn.getResponseMessage());
                Log.logDebug("QuestRepro:getTasks ->" + "Fehlercode: " + conn.getResponseCode() + "\nFehlertext: " + conn.getResponseMessage());


            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String obj="";
            String output="";

            while ((output = br.readLine()) != null){
                obj+=output;
            }


            JSONObject jasonObj = new JSONObject(obj);
            //jasonObj.get("objects").toString()

            try {
                Log.logDebug("QuestRepro:getTasks-->parsen des Jsonobjektes");
                //JSONArray jsonarray = new JSONArray(obj);
                JSONArray jsonarray = new JSONArray(jasonObj.get("objects").toString());
                Log.logDebug("QuestRepro:getTasks-->jsonarray länge:"+jsonarray.length());
                Log.logDebug("QuestRepro:getTasks-->jsonarray:"+jsonarray);

                Log.log("tasks");

                for (int i = 0; i < jsonarray.length(); i++) {


                    Log.log(Log.LINE);
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    Log.logDebug("QuestRepro:getTasks-->jsonarrayElement länge:"+jsonobject.length());
                    Log.logDebug("QuestRepro:getTasks-->jsonarrayElement:"+jsonobject);
                    //Log.log("name: "+jsonobject.getString("name"));

                    //Log.log("id: "+jsonobject.get("id"));


                    //Log.log("reward: "+jsonobject.getString("reward").toString());

                    //Log.log("beschreibung: "+jsonobject.getString("description"));

                    String name = jsonobject.getString("name");
                    Log.log("name: "+name);

                    Integer taskId = jsonobject.getInt("id");
                    Log.log("id: "+taskId);

                    String ressource = jsonobject.getString("resource");
                    Log.log("ressource: "+ressource);

                    String location = jsonobject.getString("location");
                    Log.log("location: "+location);

                    Integer requiredPlayers = jsonobject.getInt("required_players");
                    Log.log("required_players: "+requiredPlayers);

                    Task task = new Task(name,taskId,ressource,location, requiredPlayers);



                   taskliste.add(task);
                    Log.log(Log.LINE);
                }


            } catch (JSONException e) {
                Log.logDebug("QuestRepro:getTasks-->parsen des Jsonobjektes fehlgeschlagen.");
                Log.logDebug("QuestRepro:getTasks-->"+e.toString());
                e.printStackTrace();
            }


        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return taskliste;

    }
    public Quest getQuest(Integer id){

        for(Quest e:questListe){
            Log.logDebug("QuestRepro:getQuests()--> questID: "+e.getId()+" uebergebene id: "+id);
            if(e.getId().equals(id)){
                return e;
            }
        }
        return null;

    }
    public Integer questlistSize(){
        if(questListe==null){return null;}
        return questListe.size();
    }
}
